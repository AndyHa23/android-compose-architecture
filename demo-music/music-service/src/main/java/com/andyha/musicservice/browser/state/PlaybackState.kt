package com.andyha.musicservice.browser.state

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

sealed class PlaybackState {
    open val icon: PlaybackIcon? = PlaybackIcon(
        Icons.Filled.PlayArrow,
        48.dp,
        36.dp,
        "pause",
    )

    data object Initial : PlaybackState()

    data object Buffering : PlaybackState()

    data object Ready : PlaybackState() {
        override val icon = PlaybackIcon(
            Icons.Filled.Pause,
            48.dp,
            36.dp,
            "playing",
        )
    }

    data object Playing : PlaybackState() {
        override val icon = PlaybackIcon(
            Icons.Filled.Pause,
            48.dp,
            36.dp,
            "playing",
        )
    }

    data object Pause : PlaybackState()
}

data class PlaybackIcon(
    val icon: ImageVector,
    val fullSize: Dp,
    val miniSize: Dp,
    val contentDescription: String
)
