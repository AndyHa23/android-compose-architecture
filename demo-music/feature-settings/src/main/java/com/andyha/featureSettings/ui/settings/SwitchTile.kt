package com.andyha.featureSettings.ui.settings

import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable

@Composable
fun SettingsSwitchTile(
    icon: @Composable () -> Unit,
    title: @Composable () -> Unit,
    value: Boolean,
    enabled: Boolean = true,
    onChange: (Boolean) -> Unit,
) {
    Card(
        enabled = enabled,
        colors = SettingsTileDefaults.cardColors(),
        onClick = {
            onChange(!value)
        }
    ) {
        ListItem(
            colors = SettingsTileDefaults.listItemColors(enabled = enabled),
            leadingContent = { icon() },
            headlineContent = { title() },
            trailingContent = {
                Switch(
                    checked = value,
                    enabled = enabled,
                    onCheckedChange = {
                        onChange(!value)
                    }
                )
            }
        )
    }
}
