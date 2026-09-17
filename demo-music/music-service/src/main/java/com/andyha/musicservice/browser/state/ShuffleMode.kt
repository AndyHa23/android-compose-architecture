package com.andyha.musicservice.browser.state

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material.icons.filled.ShuffleOn
import androidx.compose.ui.graphics.vector.ImageVector


enum class ShuffleMode(val icon: ImageVector, val description: String) {
    Off(Icons.Filled.Shuffle, "Shuffle off"),
    On(Icons.Filled.ShuffleOn, "Shuffle on");

    companion object {
        fun ShuffleMode.nextShuffleMode(): ShuffleMode {
            val values = ShuffleMode.entries
            val currentIndex = values.indexOf(this)
            val nextIndex = (currentIndex + 1).let {
                if (it >= values.size) 0 else it
            }
            return values[nextIndex]
        }
    }
}