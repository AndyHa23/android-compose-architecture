package com.andyha.musicservice.browser.state

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.RepeatOn
import androidx.compose.material.icons.filled.RepeatOneOn
import androidx.compose.ui.graphics.vector.ImageVector

enum class RepeatMode(val icon: ImageVector, val value: Int, val description: String) {
    Off(Icons.Filled.Repeat, 0, "Repeat off"),
    One(Icons.Filled.RepeatOneOn, 1, "Repeat one"),
    All(Icons.Filled.RepeatOn, 2, "Repeat all");

    companion object {
        fun RepeatMode.nextRepeatMode(): RepeatMode {
            val values = RepeatMode.entries
            val currentIndex = values.indexOf(this)
            val nextIndex = (currentIndex + 1).let {
                if (it >= values.size) 0 else it
            }
            return values[nextIndex]
        }

        private val map = entries.associateBy(RepeatMode::value)
        fun getRepeatMode(value: Int): RepeatMode = map[value] ?: Off
    }
}
