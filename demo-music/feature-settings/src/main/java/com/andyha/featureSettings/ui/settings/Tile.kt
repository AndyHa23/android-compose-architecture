package com.andyha.featureSettings.ui.settings

import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

object SettingsTileDefaults {
    @Composable
    fun cardColors() = CardDefaults.cardColors(
        containerColor = Color.Transparent,
        disabledContainerColor = Color.Transparent,
    )

    @Composable
    fun listItemColors(enabled: Boolean = true) = when {
        enabled -> ListItemDefaults.colors(containerColor = Color.Transparent)
        else -> ListItemDefaults.colors(
            containerColor = Color.Transparent,
            leadingIconColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
            trailingIconColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
            headlineColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
            supportingColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
        )
    }
}
