package com.andyha.feature.weather.ui.locationHistory

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.andyha.weatherdomain.model.LocationState.LocationDetected
import com.andyha.feature.weather.compose.WeatherImage

@Composable
fun LocationHistorySheet(
    onLocationSelected: (LocationDetected) -> Unit,
    viewModel: LocationHistoryViewModel = hiltViewModel(),
) {
    val sections by viewModel.locations.collectAsStateWithLifecycle()

    LazyColumn(
        modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp)
    ) {
        sections.forEach { section ->
            item(key = "header-${section.id}") {
                Text(
                    text = stringResource(section.id),
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp),
                )
            }
            items(section.data, key = { "${it.address}-${it.region}-${it.country}" }) { item ->
                LocationRow(item, onLocationSelected)
            }
        }
    }
}

@Composable
private fun LocationRow(
    item: LocationDetected,
    onLocationSelected: (LocationDetected) -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .clickable { onLocationSelected(item) }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = item.address, style = MaterialTheme.typography.titleMedium)
                Text(
                    text = item.region.ifEmpty { item.country },
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = 4.dp),
                )
            }
            WeatherImage(
                iconUrl = item.icon,
                contentDescription = null,
                size = 24.dp,
            )
            Text(
                text = "${item.temperature ?: 0}°C",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(start = 16.dp),
            )
            if (item.isSelected) {
                Box(
                    modifier = Modifier
                        .padding(start = 12.dp)
                        .size(width = 8.dp, height = 48.dp)
                        .clip(MaterialTheme.shapes.small)
                        .background(MaterialTheme.colorScheme.primary)
                )
            }
        }
    }
}
