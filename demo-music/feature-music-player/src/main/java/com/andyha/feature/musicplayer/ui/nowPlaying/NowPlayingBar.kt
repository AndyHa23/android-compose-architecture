package com.andyha.feature.musicplayer.ui.nowPlaying

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.media3.common.MediaItem
import com.andyha.musiccommonresource.utils.Constants.appHorizontalPadding
import com.andyha.musiccommonresource.utils.MediaThumbnail
import com.andyha.musiccommonresource.widgets.LoadingIndicator
import com.andyha.musicservice.browser.state.PlayerState

@Composable
fun NowPlayingBar(
    modifier: Modifier,
    viewModel: NowPlayingViewModel = hiltViewModel(),
    onClick: () -> Unit,
) {
    val currentItem by viewModel.currentItem.collectAsStateWithLifecycle()
    val uiState by viewModel.nowPlayingUiState.collectAsStateWithLifecycle()
    val playerState by viewModel.playerState.collectAsStateWithLifecycle()

    if (currentItem != null) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .windowInsetsPadding(WindowInsets.safeDrawing)
                .wrapContentHeight()
                .background(color = MaterialTheme.colorScheme.primary)
                .clickable { onClick() },
            verticalAlignment = Alignment.CenterVertically,
        ) {
            NowPlayingBarMediaItem(
                currentItem,
                Modifier
                    .wrapContentHeight()
                    .weight(1f)
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            )
            PlaybackController(
                uiState,
                viewModel,
                playerState,
                Modifier.padding(end = appHorizontalPadding)
            )
        }
    }
}

@Composable
fun PlaybackController(
    mediaUiState: NowPlayingUiState,
    viewModel: NowPlayingViewModel,
    playerState: PlayerState,
    modifier: Modifier
) {
    when (mediaUiState) {
        is NowPlayingUiState.Ready -> {
            PlayPauseButton(playerState.playbackState.icon, modifier, true) {
                viewModel.onUiEvent(PlaybackUiEvent.PlayPause)
            }
        }

        else -> {
            LoadingIndicator(modifier)
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NowPlayingBarMediaItem(mediaItem: MediaItem?, modifier: Modifier) {
    Column(modifier = modifier) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            mediaItem?.let { MediaThumbnail(it, 40.dp) }

            Column(modifier = Modifier.padding(start = appHorizontalPadding)) {
                Text(
                    style = MaterialTheme.typography.titleMedium,
                    text = mediaItem?.mediaMetadata?.title.toString(),
                    maxLines = 1,
                    modifier = Modifier.basicMarquee(),
                )
                Text(
                    style = MaterialTheme.typography.titleSmall,
                    text = mediaItem?.mediaMetadata?.artist.toString(),
                    maxLines = 1,
                    modifier = Modifier.basicMarquee(),
                )
            }
        }
    }
}
