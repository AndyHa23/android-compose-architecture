package com.andyha.feature.musiclist.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import com.andyha.corenetwork.di.DefaultDispatcher
import com.andyha.feature.musiclist.list.MediaListUiState.Failed
import com.andyha.feature.musiclist.list.MediaListUiState.NotReady
import com.andyha.feature.musiclist.list.MediaListUiState.Ready
import com.andyha.musicdomain.model.DataState
import com.andyha.musicdomain.usecase.GetMusicDataStateUseCase
import com.andyha.musicdomain.usecase.RefreshMusicDataUseCase
import com.andyha.musiccommonresource.model.MediaRowUi
import com.andyha.musiccommonresource.model.toMediaRowUi
import com.andyha.musicservice.browser.BrowserState
import com.andyha.musicservice.browser.MediaPlayerBrowser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MediaListViewModel @Inject constructor(
    private val mediaPlayerBrowser: MediaPlayerBrowser,
    private val getMusicDataStateUseCase: GetMusicDataStateUseCase,
    private val refreshMusicDataUseCase: RefreshMusicDataUseCase,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher,
) : ViewModel() {

    private var _mediaItems = MutableStateFlow<List<MediaItem>>(emptyList())

    private val _mediaRows = MutableStateFlow<List<MediaRowUi>>(emptyList())
    val mediaRows: StateFlow<List<MediaRowUi>> = _mediaRows.asStateFlow()

    private var _currentMediaId = MutableStateFlow<String?>(null)
    val currentMediaId: StateFlow<String?> = _currentMediaId.asStateFlow()

    private val _mediaListUiState = MutableStateFlow<MediaListUiState>(NotReady)
    val mediaListUiState = _mediaListUiState.asStateFlow()

    init {
        collectBrowserAndDataState()
        collectCurrentMediaItem()
    }

    private fun collectBrowserAndDataState() {
        viewModelScope.launch {
            mediaPlayerBrowser.browserState.combine(getMusicDataStateUseCase.invoke()) { browserState, dataState ->
                Timber.d("Combine: browserState: $browserState, dataState: $dataState")
                when {
                    // A data failure is actionable regardless of the browser connection,
                    // so surface it rather than waiting on the browser.
                    dataState is DataState.Failed -> Failed

                    // wait for both MediaBrowser and items tree builder to be ready
                    // then go for the query
                    browserState is BrowserState.Ready && dataState == DataState.Ready -> Ready

                    else -> NotReady
                }
            }
                .catch { Timber.e(it, "Failed to observe media list state") }
                .collect { state ->
                    if (state !is Ready) {
                        _mediaItems.value = emptyList()
                        _mediaRows.value = emptyList()
                    }
                    _mediaListUiState.value = state
                }
        }
    }

    fun retry() {
        refreshMusicDataUseCase.invoke()
    }

    fun seekAndPlay(mediaId: String) {
        val playList = _mediaItems.value
        val itemToPlay = playList.firstOrNull { it.mediaId == mediaId } ?: return
        viewModelScope.launch {
            mediaPlayerBrowser.preparePlayListAndPlay(playList, itemToPlay)
        }
    }

    /** The rows only carry a media id, so navigation resolves the real item here. */
    fun mediaItemFor(mediaId: String): MediaItem? =
        _mediaItems.value.firstOrNull { it.mediaId == mediaId }

    fun loadInitialMediaItems(parentId: String, mediaType: Int) {
        viewModelScope.launch {
            // getChildren hops to the main thread internally (MediaBrowser is thread
            // confined), so only the filtering/sorting is moved off it here.
            val children = mediaPlayerBrowser.getChildren(parentId)
            val (items, rows) = withContext(defaultDispatcher) {
                val filtered = children
                    .filter {
                        // filter out item not to be desired
                        // for example an artist contains both albums and songs as children
                        // but in Album screen we only let album items in and vice versa
                        if (mediaType != MediaMetadata.MEDIA_TYPE_FOLDER_MIXED)
                            return@filter it.mediaMetadata.mediaType == mediaType
                        else true
                    }
                    .sortedByDescending { it.mediaMetadata.mediaType }
                filtered to filtered.map { it.toMediaRowUi() }
            }
            _mediaItems.value = items
            _mediaRows.value = rows
        }
    }

    private fun collectCurrentMediaItem() {
        viewModelScope.launch {
            mediaPlayerBrowser.currentMediaItem.collect {
                _currentMediaId.value = it?.mediaId
            }
        }
    }
}

sealed class MediaListUiState {
    data object NotReady : MediaListUiState()
    data object Ready : MediaListUiState()
    data object Failed : MediaListUiState()
}