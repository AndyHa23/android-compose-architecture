package com.andyha.musicservice.browser

import android.content.ComponentName
import android.content.Context
import android.os.SystemClock
import androidx.media3.common.MediaItem
import androidx.media3.session.LibraryResult
import androidx.media3.session.MediaBrowser
import androidx.media3.session.SessionToken
import com.andyha.corenetwork.di.IoDispatcher
import com.andyha.corenetwork.di.MainDispatcher
import com.andyha.musicdomain.model.SavedPlayList
import com.andyha.musicdomain.repository.SavedPlayListRepository
import com.andyha.musicservice.browser.BrowserState.Initial
import com.andyha.musicservice.browser.state.ControllerState
import com.andyha.musicservice.browser.state.FastForwardRewindController
import com.andyha.musicservice.browser.state.PlayerState
import com.andyha.musicservice.browser.state.RepeatMode
import com.andyha.musicservice.browser.state.RepeatMode.Companion.nextRepeatMode
import com.andyha.musicservice.browser.state.ShuffleMode
import com.andyha.musicservice.browser.state.ShuffleMode.Companion.nextShuffleMode
import com.andyha.musicservice.session.MediaPlaybackService
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.MoreExecutors
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.updateAndGet
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.time.Duration.Companion.milliseconds

class MediaPlayerBrowser @Inject constructor(
    @ApplicationContext context: Context,
    @MainDispatcher private val mainDispatcher: CoroutineDispatcher,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val savedPlayListRepository: SavedPlayListRepository,
) {
    private val _browserState = MutableStateFlow<BrowserState>(Initial)
    val browserState = _browserState.asStateFlow()

    private val _playerState = MutableStateFlow(PlayerState())
    val playerState = _playerState.asStateFlow()

    private val coroutineScope = CoroutineScope(mainDispatcher)

    private var browserFuture: ListenableFuture<MediaBrowser>

    internal lateinit var browser: MediaBrowser

    private val _currentMediaItem = MutableStateFlow<MediaItem?>(null)
    val currentMediaItem = _currentMediaItem.asStateFlow()

    private val _currentPlayList = MutableStateFlow<List<MediaItem>>(listOf())
    val currentPlayList = _currentPlayList.asStateFlow()

    private lateinit var fastForwardRewindController: FastForwardRewindController

    private var lastPersistedAt = 0L
    private var lastPersistedSignature: PersistSignature? = null

    init {
        val sessionToken = SessionToken(
            context,
            ComponentName(context, MediaPlaybackService::class.java),
        )

        browserFuture = MediaBrowser.Builder(context, sessionToken).buildAsync()
        browserFuture.addListener(
            {
                runCatching { browserFuture.get() }
                    .onSuccess { connectedBrowser ->
                        browser = connectedBrowser
                        _browserState.value = BrowserState.Ready
                        initController()
                        collectSavedPlaylist()
                    }
                    .onFailure { Timber.e(it, "Failed to connect MediaBrowser") }
            },
            MoreExecutors.directExecutor(),
        )
    }

    /**
     * Single entry point for player state mutation. [PlayerState] is immutable, so the
     * [MutableStateFlow] only re-emits when something actually changed.
     */
    internal fun updatePlayerState(transform: (PlayerState) -> PlayerState) {
        val previous = _playerState.value
        val updated = _playerState.updateAndGet(transform)
        if (previous.controllerState != updated.controllerState) {
            applyMuteForControllerState(updated.controllerState)
        }
    }

    private fun applyMuteForControllerState(controllerState: ControllerState) {
        if (browserState.value !is BrowserState.Ready) return
        if (controllerState.soundMuted && browser.volume > 0) browser.volume = 0F
        else if (!controllerState.soundMuted && browser.volume == 0F) browser.volume = 1F
    }

    private fun initController() {
        val listener = MediaPlayerListener(
            browser = this.browser,
            currentMediaItem = _currentMediaItem,
            currentPlayList = _currentPlayList,
            updatePlayerState = ::updatePlayerState,
            onPersistRequested = ::savePlayListToPersistence,
        )

        browser.addListener(listener)

        fastForwardRewindController = FastForwardRewindController(this, _currentMediaItem)
    }

    private fun collectSavedPlaylist() {
        coroutineScope.launch {
            savedPlayListRepository.savedPlayList
                .catch { it.printStackTrace() }
                .filter { it != null && it.playList.isNotEmpty() }
                .collect {
                    Timber.d("collect saved playlist: $it")
                    delay(1000.milliseconds)
                    restorePlayListAndSettings(it!!)
                }
        }
    }

    private fun restorePlayListAndSettings(savedPlayList: SavedPlayList) {
        val list = savedPlayList.playList.map { it.toAndroidMediaItem() }

        val playingItem =
            list.firstOrNull { it.mediaId == savedPlayList.playingItemId } ?: list[0]

        updatePlayerState {
            it.copy(
                repeatMode = RepeatMode.getRepeatMode(savedPlayList.repeatMode),
                shuffleMode = if (savedPlayList.shuffleMode) ShuffleMode.On else ShuffleMode.Off,
            )
        }

        coroutineScope.launch {
            preparePlayListAndPlay(
                list,
                playingItem,
                savedPlayList.playingPosition,
            )
        }
    }

    suspend fun getChildren(mediaItem: MediaItem): List<MediaItem> =
        getChildren(mediaItem.mediaId)

    suspend fun getChildren(mediaItemId: String): List<MediaItem> {
        if (browserState.value !is BrowserState.Ready) return emptyList()

        // MediaBrowser must be accessed from the thread it was built on.
        return withContext(mainDispatcher) {
            val childrenFuture = browser.getChildren(mediaItemId, 0, Int.MAX_VALUE, null)

            suspendCancellableCoroutine { continuation ->
                childrenFuture.addListener(
                    {
                        val children = runCatching {
                            val result = childrenFuture.get()
                            if (result?.resultCode == LibraryResult.RESULT_SUCCESS) {
                                result.value.orEmpty()
                            } else {
                                Timber.w("getChildren($mediaItemId) failed: ${result?.resultCode}")
                                emptyList()
                            }
                        }.getOrElse {
                            Timber.e(it, "getChildren($mediaItemId) threw")
                            emptyList()
                        }
                        continuation.resume(children)
                    },
                    MoreExecutors.directExecutor(),
                )

                continuation.invokeOnCancellation { childrenFuture.cancel(false) }
            }
        }
    }

    suspend fun preparePlayListAndPlay(
        playList: List<MediaItem>,
        itemToPlay: MediaItem,
        startingPosition: Long = 0,
    ) {
        Timber.d("preparePlayList: $playList")
        browser.apply {
            clearMediaItems()
            addMediaItems(playList)
        }
        var index = playList.indexOfFirst { it.mediaId == itemToPlay.mediaId }
        if (index < 0) index = 0

        browser.prepare()
        browser.repeatMode = _playerState.value.repeatMode.value
        browser.shuffleModeEnabled = _playerState.value.shuffleMode == ShuffleMode.On

        seekToMediaItem(index)
        if (startingPosition > 0) {
            // usually if startingPosition > 0, that means it's the last played media item
            // from previous session, so let's wait for a moment for the player to be ready
            // then perform the seeking to the startingPosition
            delay(500.milliseconds)
            seekTo(startingPosition)
        }
    }

    fun skipToPrevious() {
        browser.seekToPreviousMediaItem()
        browser.play()
    }

    fun skipToNext() {
        browser.seekToNextMediaItem()
        browser.play()
    }

    fun seekToMediaItem(index: Int) {
        if (index < 0 || index >= browser.mediaItemCount) {
            return
        }
        browser.seekToDefaultPosition(index)
        browser.play()
    }

    fun fastForward() {
        fastForwardRewindController.fastForward()
    }

    fun rewind() {
        fastForwardRewindController.rewind()
    }

    // event when user start dragging the duration seekbar
    fun startDragging() {
        updatePlayerState { it.copy(controllerState = ControllerState.SeekbarDragging) }
    }

    // event when user stop dragging the duration seekbar
    fun stopDragging(position: Long) {
        updatePlayerState { it.copy(controllerState = ControllerState.None) }
        seekTo(position)
    }

    // seek to a specific position of the current playing media (milliseconds)
    fun seekTo(position: Long) {
        Timber.d("seekTo: $position")
        Timber.d("current browser duration: ${browser.duration}")
        browser.seekTo(position)
    }

    fun play() {
        browser.play()
    }

    fun pause() {
        browser.pause()
    }

    fun changeRepeatMode() {
        updatePlayerState { it.copy(repeatMode = it.repeatMode.nextRepeatMode()) }
        browser.repeatMode = _playerState.value.repeatMode.value
        savePlayListToPersistence(force = true)
    }

    fun changeShuffleMode() {
        updatePlayerState { it.copy(shuffleMode = it.shuffleMode.nextShuffleMode()) }
        browser.shuffleModeEnabled = _playerState.value.shuffleMode == ShuffleMode.On
        savePlayListToPersistence(force = true)
    }

    private fun resetRepeatAndShuffleMode() {
        updatePlayerState { it.copy(repeatMode = RepeatMode.Off, shuffleMode = ShuffleMode.Off) }
        browser.repeatMode = RepeatMode.Off.value
        browser.shuffleModeEnabled = false
    }

    /**
     * Persisting the playlist serialises every item to JSON and rewrites the whole Room row, so it
     * must not run on every playback-position tick. Position-driven saves are throttled to
     * [PERSIST_THROTTLE_MS]; meaningful events (track change, play/pause, mode change) pass
     * [force] to save immediately.
     */
    private fun savePlayListToPersistence(force: Boolean = false) {
        if (browserState.value !is BrowserState.Ready) return
        val playingItem = _currentMediaItem.value ?: return
        val playList = _currentPlayList.value
        if (playList.isEmpty()) return

        val state = _playerState.value
        val signature = PersistSignature(
            playingItemId = playingItem.mediaId,
            playListSize = playList.size,
            repeatMode = state.repeatMode,
            shuffleMode = state.shuffleMode,
        )
        val now = SystemClock.elapsedRealtime()
        val throttled = !force &&
            signature == lastPersistedSignature &&
            now - lastPersistedAt < PERSIST_THROTTLE_MS
        if (throttled) return

        lastPersistedSignature = signature
        lastPersistedAt = now

        val currentPosition = browser.currentPosition
        coroutineScope.launch(ioDispatcher) {
            runCatching {
                savedPlayListRepository.savePlayList(
                    playList,
                    playingItem,
                    currentPosition,
                    state.repeatMode.value,
                    state.shuffleMode == ShuffleMode.On,
                )
            }.onFailure { Timber.e(it, "Failed to persist playlist") }
        }
    }

    private data class PersistSignature(
        val playingItemId: String,
        val playListSize: Int,
        val repeatMode: RepeatMode,
        val shuffleMode: ShuffleMode,
    )

    companion object {
        private const val PERSIST_THROTTLE_MS = 15_000L
    }
}

sealed class BrowserState {
    data object Initial : BrowserState()
    data object Ready : BrowserState()
}
