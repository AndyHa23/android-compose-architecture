package com.andyha.feature.musicplayer.ui.nowPlaying

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.andyha.feature.musicplayer.utils.DurationFormatter
import timber.log.Timber


@Composable
fun NowPlayingSeekBar(viewModel: NowPlayingViewModel = hiltViewModel()) {

    val playerState by viewModel.playerState.collectAsStateWithLifecycle()
    val playbackPosition = playerState.playbackPosition
    val repeatMode = playerState.repeatMode
    val shuffleMode = playerState.shuffleMode

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(24.dp, Alignment.End),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            SkipButton(
                icon = repeatMode.icon,
                contentDesc = repeatMode.description,
            ) {
                viewModel.onUiEvent(PlaybackUiEvent.ChangeRepeatMode)
            }

            SkipButton(
                icon = shuffleMode.icon,
                contentDesc = shuffleMode.description
            ) {
                viewModel.onUiEvent(PlaybackUiEvent.ChangeShuffleMode)
            }
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            var userDragSeekRatio by remember { mutableStateOf<Float?>(null) }

            Timber.d("playbackPosition: ${playbackPosition.played}")
            Timber.d("seekRatio: $userDragSeekRatio")

            PlaybackPositionText(
                userDragSeekRatio?.let { it * playbackPosition.total }?.toLong()
                    ?: playbackPosition.played,
                Alignment.CenterStart,
            )
            Box(modifier = Modifier.weight(1f)) {
                NowPlayingSeekBar(
                    ratio = playbackPosition.ratio,
                    onSeekStart = {
                        Timber.d("onSeekStart")
                        userDragSeekRatio = 0f
                        viewModel.onUiEvent(PlaybackUiEvent.SeekStart)
                    },
                    onSeek = {
                        Timber.d("onSeeking")
                        userDragSeekRatio = it
                    },
                    onSeekEnd = {
                        Timber.d("onSeekEnd: $it")
                        viewModel.onUiEvent(
                            PlaybackUiEvent.SeekEnd((it * playerState.playbackPosition.total).toLong()),
                        )
                        userDragSeekRatio = null
                    },
                    onSeekCancel = {
                        Timber.d("onSeekCancel")
                        userDragSeekRatio = null
                    },
                )
            }
            PlaybackPositionText(
                playbackPosition.total,
                Alignment.CenterEnd,
            )
        }
    }
}

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
private fun NowPlayingSeekBar(
    ratio: Float,
    onSeekStart: () -> Unit,
    onSeek: (Float) -> Unit,
    onSeekEnd: (Float) -> Unit,
    onSeekCancel: () -> Unit,
) {
    val sliderHeight = 18.dp
    val thumbSize = 18.dp
    val thumbSizeHalf = thumbSize.div(2)
    val trackHeight = 6.dp

    var dragging by remember { mutableStateOf(false) }
    var dragRatio by remember { mutableFloatStateOf(0f) }

    Timber.d("dragging: $dragging")

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxWidth()
            .height(sliderHeight),
        contentAlignment = Alignment.Center,
    ) {
        val sliderWidth = maxWidth

        Box(
            modifier = Modifier
                .height(sliderHeight)
                .fillMaxWidth()
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = { offset ->
                            val tapRatio = (offset.x / sliderWidth.toPx()).coerceIn(0f..1f)
                            onSeekEnd(tapRatio)
                        },
                    )
                }
                .pointerInput(Unit) {
                    var offsetX = 0f
                    detectHorizontalDragGestures(
                        onDragStart = { offset ->
                            offsetX = offset.x
                            dragging = true
                            onSeekStart()
                        },
                        onDragEnd = {
                            onSeekEnd(dragRatio)
                            offsetX = 0f
                            dragging = false
                            dragRatio = 0f
                        },
                        onDragCancel = {
                            onSeekCancel()
                            offsetX = 0f
                            dragging = false
                            dragRatio = 0f
                        },
                        onHorizontalDrag = { pointer, dragAmount ->
                            pointer.consume()
                            offsetX += dragAmount
                            dragRatio = (offsetX / sliderWidth.toPx()).coerceIn(0f..1f)
                            onSeek(dragRatio)
                        },
                    )
                },
        )
        Box(
            modifier = Modifier
                .padding(thumbSizeHalf, 0.dp)
                .height(trackHeight)
                .fillMaxWidth()
                .background(
                    MaterialTheme.colorScheme.surfaceVariant,
                    RoundedCornerShape(thumbSizeHalf),
                ),
        ) {
            Box(
                modifier = Modifier
                    .height(trackHeight)
                    .fillMaxWidth(if (dragging) dragRatio else ratio)
                    .background(
                        MaterialTheme.colorScheme.primary,
                        RoundedCornerShape(thumbSizeHalf),
                    ),
            )
        }
        Box(modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier
                    .size(thumbSize)
                    .offset(
                        sliderWidth
                            .minus(thumbSizeHalf.times(2))
                            .times(if (dragging) dragRatio else ratio),
                        0.dp,
                    )
                    .background(MaterialTheme.colorScheme.primary, CircleShape),
            )
        }
    }
}

@Composable
private fun PlaybackPositionText(
    duration: Long,
    alignment: Alignment,
) {
    val textStyle = MaterialTheme.typography.labelMedium
    val durationFormatted = DurationFormatter.formatMs(duration)

    Box(contentAlignment = alignment, modifier = Modifier.width(48.dp)) {
        Text(
            text = durationFormatted,
            style = textStyle
        )
    }
}

@Composable
@Preview
fun NowPlayingSeekbarPreview() {
    NowPlayingSeekBar()
}
