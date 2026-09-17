package com.andyha.feature.musicplayer.ui.nowPlaying

import com.andyha.musicservice.browser.BrowserState
import com.andyha.musicservice.browser.MediaPlayerBrowser
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import com.andyha.musicservice.browser.state.PlaybackState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class NowPlayingViewModel @Inject constructor(
    private val mediaPlayerBrowser: MediaPlayerBrowser,
) : ViewModel() {

    private var _currentItem = MutableStateFlow<MediaItem?>(null)
    val currentItem: StateFlow<MediaItem?> = _currentItem.asStateFlow()

    private val _nowPlayingUiState = MutableStateFlow<NowPlayingUiState>(NowPlayingUiState.Initial)
    val nowPlayingUiState = _nowPlayingUiState.asStateFlow()

    val playerState = mediaPlayerBrowser.playerState

    init {
        loadMediaItems()
        collectCurrentMediaItem()
    }

    fun onUiEvent(mediaUiEvent: PlaybackUiEvent) {
        viewModelScope.launch {
            when (mediaUiEvent) {
                PlaybackUiEvent.PlayPause -> {
                    if (playerState.value.playbackState == PlaybackState.Playing) {
                        mediaPlayerBrowser.pause()
                    } else if (playerState.value.canPlay()) {
                        mediaPlayerBrowser.play()
                    }
                }

                PlaybackUiEvent.Forward -> mediaPlayerBrowser.skipToNext()
                PlaybackUiEvent.Backward -> mediaPlayerBrowser.skipToPrevious()

                PlaybackUiEvent.FastForward -> mediaPlayerBrowser.fastForward()
                PlaybackUiEvent.Rewind -> mediaPlayerBrowser.rewind()

                PlaybackUiEvent.SeekStart -> mediaPlayerBrowser.startDragging()
                is PlaybackUiEvent.SeekEnd -> mediaPlayerBrowser.stopDragging(mediaUiEvent.position)

                PlaybackUiEvent.ChangeRepeatMode -> mediaPlayerBrowser.changeRepeatMode()
                PlaybackUiEvent.ChangeShuffleMode -> mediaPlayerBrowser.changeShuffleMode()
            }
        }
    }

    private fun loadMediaItems() {
        viewModelScope.launch {
            mediaPlayerBrowser.browserState.collect { browserState ->
                when (browserState) {
                    is BrowserState.Ready -> _nowPlayingUiState.value = NowPlayingUiState.Ready
                    else -> _nowPlayingUiState.value = NowPlayingUiState.Initial
                }
            }
        }
    }

    private fun collectCurrentMediaItem() {
        viewModelScope.launch {
            mediaPlayerBrowser.currentMediaItem.collect {
                _currentItem.value = it
            }
        }
    }
}

sealed class NowPlayingUiState {
    data object Initial : NowPlayingUiState()
    data object Ready : NowPlayingUiState()
}

sealed class PlaybackUiEvent {
    data object PlayPause : PlaybackUiEvent()
    data object Backward : PlaybackUiEvent()
    data object Forward : PlaybackUiEvent()
    data object FastForward : PlaybackUiEvent()
    data object Rewind : PlaybackUiEvent()
    data object SeekStart : PlaybackUiEvent()
    data object ChangeRepeatMode : PlaybackUiEvent()
    data object ChangeShuffleMode: PlaybackUiEvent()
    data class SeekEnd(val position: Long) : PlaybackUiEvent()
}
