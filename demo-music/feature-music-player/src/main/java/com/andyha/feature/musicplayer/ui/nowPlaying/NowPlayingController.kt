package com.andyha.feature.musicplayer.ui.nowPlaying

import android.view.MotionEvent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FastForward
import androidx.compose.material.icons.filled.FastRewind
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.andyha.musiccommonresource.utils.Constants.appHorizontalPadding
import com.andyha.musicservice.browser.state.ControllerState
import com.andyha.musicservice.browser.state.PlaybackIcon
import com.andyha.musicservice.browser.state.PlaybackState
import timber.log.Timber


@Composable
fun NowPlayingController(
    viewModel: NowPlayingViewModel = hiltViewModel(),
) {
    val playerState by viewModel.playerState.collectAsStateWithLifecycle()

    // fast forward and rewind
    val seekFactor = playerState.controllerState.description

    Column(horizontalAlignment = Alignment.CenterHorizontally) {

        Text(
            text = seekFactor,
            color = MaterialTheme.colorScheme.primary,
            style = TextStyle(fontSize = 16.sp),
            modifier = Modifier.padding(bottom = 32.dp),
        )

        Row(
            modifier = Modifier
                .padding(start = appHorizontalPadding, end = appHorizontalPadding, bottom = 48.dp)
                .fillMaxWidth()
                .wrapContentHeight(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            val playbackState = playerState.playbackState
            val controllerState = playerState.controllerState

            SkipButton(
                icon = Icons.Filled.SkipPrevious,
                contentDesc = "Go to previous item",
            ) {
                viewModel.onUiEvent(PlaybackUiEvent.Backward)
            }
            FastForwardRewindButton(
                Icons.Filled.FastRewind,
                "Rewind",
                onStart = { viewModel.onUiEvent(PlaybackUiEvent.Rewind) },
                onStop = { viewModel.onUiEvent(PlaybackUiEvent.Rewind) },
            )
            PlayPauseButton(
                playbackIcon = if (controllerState !is ControllerState.None) {
                    PlaybackState.Pause.icon
                } else {
                    playbackState.icon
                },
                miniPlayer = false,
            ) {
                viewModel.onUiEvent(PlaybackUiEvent.PlayPause)
            }
            FastForwardRewindButton(
                Icons.Filled.FastForward,
                "Fast Forward",
                onStart = { viewModel.onUiEvent(PlaybackUiEvent.FastForward) },
                onStop = { viewModel.onUiEvent(PlaybackUiEvent.FastForward) },
            )
            SkipButton(
                icon = Icons.Filled.SkipNext,
                contentDesc = "Go to next item",
            ) {
                viewModel.onUiEvent(PlaybackUiEvent.Forward)
            }
        }
    }
}

@Composable
fun PlayPauseButton(
    playbackIcon: PlaybackIcon?,
    modifier: Modifier = Modifier,
    miniPlayer: Boolean = true,
    onClick: () -> Unit
) {
    playbackIcon?.let {
        val size = if (miniPlayer) playbackIcon.miniSize else playbackIcon.fullSize
        IconButton(onClick = onClick, modifier = modifier.size(size)) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Icon(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(4.dp),
                    imageVector = playbackIcon.icon,
                    contentDescription = "play pause",
                    tint = MaterialTheme.colorScheme.onSurface,
                )
            }
        }
    }
}

@Composable
fun SkipButton(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    contentDesc: String,
    onClick: () -> Unit,
) {
    IconButton(onClick = onClick, modifier = modifier.size(48.dp)) {
        Icon(
            modifier = modifier
                .fillMaxSize()
                .padding(4.dp),
            imageVector = icon,
            contentDescription = contentDesc,
            tint = MaterialTheme.colorScheme.onSurface,
        )
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun FastForwardRewindButton(
    icon: ImageVector,
    contentDesc: String,
    onStart: () -> Unit,
    onStop: () -> Unit,
) {
    var isControlling by remember { mutableStateOf(false) }
    IconButton(onClick = { }, modifier = Modifier.size(48.dp)) {
        Icon(
            imageVector = icon,
            contentDescription = contentDesc,
            tint = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier
                .fillMaxSize()
                .padding(4.dp)
                .pointerInteropFilter { event ->
                    when (event.action) {
                        MotionEvent.ACTION_DOWN -> {
                            if (!isControlling) {
                                Timber.d("FastForwardRewindButton: startControlling")
                                onStart.invoke()
                                isControlling = true
                                true
                            } else {
                                false
                            }
                        }

                        MotionEvent.ACTION_UP -> {
                            if (isControlling) {
                                Timber.d("FastForwardRewindButton: stopControlling")
                                onStop.invoke()
                                isControlling = false
                                true
                            } else {
                                false
                            }
                        }

                        else -> false
                    }
                },
        )
    }
}

@Composable
@Preview
fun NowPlayingControllerPreview() {
    NowPlayingController()
}
