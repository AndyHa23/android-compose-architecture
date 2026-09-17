package com.andyha.musicservice.browser

import android.annotation.SuppressLint
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.common.Timeline
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaBrowser
import com.andyha.musicservice.browser.state.PlaybackPosition
import com.andyha.musicservice.browser.state.PlaybackState
import com.andyha.musicservice.browser.state.PlayerState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import timber.log.Timber
import kotlin.time.Duration.Companion.milliseconds

class MediaPlayerListener(
    private val browser: MediaBrowser,
    private val currentMediaItem: MutableStateFlow<MediaItem?>,
    private val currentPlayList: MutableStateFlow<List<MediaItem>>,
    private val updatePlayerState: ((PlayerState) -> PlayerState) -> Unit,
    private val onPersistRequested: (force: Boolean) -> Unit,
) : Player.Listener {

    private var playbackPositionUpdateJob: Job? = null

    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    private val playbackPositionInternal: PlaybackPosition?
        get() = browser.let {
            try {
                PlaybackPosition(
                    played = if (it.currentPosition < 0) 0 else it.currentPosition,
                    total = if (it.duration < 0) 0 else it.duration,
                )
            } catch (e: IllegalStateException) {
                e.printStackTrace()
                null
            }
        }

    @SuppressLint("SwitchIntDef")
    override fun onPlaybackStateChanged(playbackState: Int) {
        Timber.d("current Exo state: $playbackState")

        when (playbackState) {
            ExoPlayer.STATE_IDLE -> {
                updatePlayerState { it.copy(playbackState = PlaybackState.Ready) }
                browser.prepare()
            }

            ExoPlayer.STATE_BUFFERING -> {
                updatePlayerState { it.copy(playbackState = PlaybackState.Buffering) }
            }

            ExoPlayer.STATE_READY -> {
                updatePlayerState { it.copy(playbackState = PlaybackState.Ready) }
                refreshPlayList()
                refreshCurrentMediaItem()
                createDurationTimer()
            }

            ExoPlayer.STATE_ENDED -> {
                stopDurationTimer()
                onPersistRequested(true)
            }

            else -> {
                // Do Something
            }
        }
    }

    override fun onMediaMetadataChanged(mediaMetadata: MediaMetadata) {
        super.onMediaMetadataChanged(mediaMetadata)
        refreshCurrentMediaItem()
        onPersistRequested(true)
    }

    override fun onTimelineChanged(timeline: Timeline, reason: Int) {
        super.onTimelineChanged(timeline, reason)
        refreshPlayList()
        onPersistRequested(true)
    }

    private fun refreshCurrentMediaItem() {
        currentMediaItem.value = browser.currentMediaItem
    }

    // Rebuilding the playlist is O(n) and allocates, so only do it when the timeline really changed.
    private fun refreshPlayList() {
        currentPlayList.value = (0 until browser.mediaItemCount).map { browser.getMediaItemAt(it) }
    }

    override fun onIsPlayingChanged(isPlaying: Boolean) {
        updatePlayerState {
            it.copy(
                playbackState = if (isPlaying) PlaybackState.Playing else PlaybackState.Pause,
            )
        }
        onPersistRequested(true)
    }

    private fun createDurationTimer() {
        playbackPositionUpdateJob?.cancel()

        playbackPositionUpdateJob = flow {
            while (true) {
                playbackPositionInternal?.let { emit(it) }
                delay(POSITION_UPDATE_INTERVAL.milliseconds)
            }
        }.distinctUntilChanged().onEach { position ->
            updatePlayerState { it.copy(playbackPosition = position) }
            onPersistRequested(false)
        }.launchIn(coroutineScope)
    }


    private fun stopDurationTimer() {
        playbackPositionUpdateJob?.cancel()
    }

    companion object {
        const val POSITION_UPDATE_INTERVAL = 1000L
    }
}
