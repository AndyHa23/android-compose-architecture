package com.andyha.feature.musicplayer.ui.playlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import com.andyha.musicdomain.model.DataState
import com.andyha.musicdomain.usecase.GetMusicDataStateUseCase
import com.andyha.musiccommonresource.model.MediaRowUi
import com.andyha.musiccommonresource.model.toMediaRowUi
import com.andyha.musicservice.browser.BrowserState
import com.andyha.musicservice.browser.MediaPlayerBrowser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject


@HiltViewModel
class PlayListViewModel @Inject constructor(
    private val mediaPlayerBrowser: MediaPlayerBrowser,
    private val getMusicDataStateUseCase: GetMusicDataStateUseCase
) : ViewModel() {
    private var _playList = MutableStateFlow<List<MediaItem>>(emptyList())

    private val _playListRows = MutableStateFlow<List<MediaRowUi>>(emptyList())
    val playListRows: StateFlow<List<MediaRowUi>> = _playListRows.asStateFlow()

    private var _currentMediaId = MutableStateFlow<String?>(null)
    val currentMediaId: StateFlow<String?> = _currentMediaId.asStateFlow()

    private val _mediaListUiState = MutableStateFlow<PlayListUiState>(PlayListUiState.NotReady)
    val mediaListUiState = _mediaListUiState.asStateFlow()

    init {
        collectBrowserAndDataState()
        collectCurrentMediaItem()
    }

    private fun collectBrowserAndDataState() {
        viewModelScope.launch {
            mediaPlayerBrowser.browserState.combine(getMusicDataStateUseCase()) { browserState, dataState ->
                Timber.d("Combine: browserState: $browserState, dataState: $dataState")
                if (browserState is BrowserState.Ready && dataState == DataState.Ready) {
                    _mediaListUiState.value = PlayListUiState.Ready
                } else {
                    _mediaListUiState.value = PlayListUiState.NotReady
                    _playList.value = listOf()
                    _playListRows.value = listOf()
                }
            }.catch { it.printStackTrace() }.collect {}
        }
    }

    fun seekAndPlay(mediaId: String) {
        viewModelScope.launch {
            val index = _playList.value.indexOfFirst { it.mediaId == mediaId }
            if (index < 0) return@launch
            mediaPlayerBrowser.seekToMediaItem(index)
            mediaPlayerBrowser.play()
        }
    }

    private fun collectCurrentMediaItem() {
        viewModelScope.launch {
            // subscribe to current being played item
            mediaPlayerBrowser.currentMediaItem.collect {
                _currentMediaId.value = it?.mediaId
            }
        }

        viewModelScope.launch {
            // subscribe to current being played playlist
            mediaPlayerBrowser.currentPlayList.collect {
                _playList.value = it
                _playListRows.value = it.map { item -> item.toMediaRowUi() }
            }
        }
    }
}

sealed class PlayListUiState {
    data object NotReady : PlayListUiState()
    data object Ready : PlayListUiState()
}
