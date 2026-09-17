package com.andyha.feature.weather.ui.now

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import android.content.Intent
import android.provider.Settings
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.lazy.grid.GridItemSpan
import com.andyha.weathercommon.model.CurrentWeatherData
import com.andyha.weathercommon.model.WeatherMetric
import com.andyha.weatherdomain.model.LocationState
import com.andyha.feature.weather.R
import com.andyha.feature.weather.compose.PermissionStateCard
import com.andyha.feature.weather.compose.WeatherImage
import com.andyha.feature.weather.compose.airQualityDescription
import com.andyha.feature.weather.compose.airQualityDrawable
import com.andyha.feature.weather.compose.formatLastUpdated
import androidx.core.net.toUri

@Composable
fun NowScreen(
    modifier: Modifier = Modifier,
    viewModel: NowViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val items by viewModel.currentWeather.collectAsStateWithLifecycle()
    val locationState by viewModel.currentLocationState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.getCurrentLocationState()
    }

    when (locationState) {
        LocationState.PermissionDenied -> PermissionStateCard(
            message = stringResource(R.string.grant_location_permission_message, "Weather"),
            actionLabel = stringResource(R.string.allow),
            onAction = viewModel::requestLocationUpdate,
            modifier = modifier,
        )

        LocationState.PermissionDeniedForever -> PermissionStateCard(
            message = stringResource(R.string.location_permission_denied_forever_message),
            actionLabel = stringResource(R.string.settings),
            onAction = { context.startActivity(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                data = "package:${context.packageName}".toUri()
            }) },
            modifier = modifier,
        )

        else -> {
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                items(
                    items = items,
                    key = { it.key },
                    span = { item -> GridItemSpan(if (item is CurrentWeatherData.Metric) 1 else 3) },
                    contentType = { it::class },
                ) { item ->
                    when (item) {
                        is CurrentWeatherData.Now -> CurrentWeatherCard(item)
                        is CurrentWeatherData.AirQuality -> AirQualityCard(item)
                        is CurrentWeatherData.Metric -> WeatherMetricCard(item)
                    }
                }
            }
        }
    }
}

@Composable
private fun CurrentWeatherCard(
    item: CurrentWeatherData.Now,
) {
    val context = LocalContext.current
    val weather = item.weather
    Card {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stringResource(R.string.current_temperature, weather.temperature ?: 0),
                    style = MaterialTheme.typography.headlineMedium,
                )
                Text(
                    text = stringResource(R.string.feels_like, weather.feelLike ?: 0),
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(top = 4.dp),
                )
                Text(
                    text = context.formatLastUpdated(weather.lastUpdated),
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 4.dp),
                )
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = weather.condition.orEmpty(),
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.End,
                )
                WeatherImage(
                    iconUrl = weather.icon,
                    contentDescription = weather.condition,
                    size = 64.dp,
                    modifier = Modifier.padding(top = 8.dp),
                )
            }
        }
    }
}

@get:StringRes
private val WeatherMetric.titleRes: Int
    get() = when (this) {
        WeatherMetric.Humidity -> R.string.humidity
        WeatherMetric.Wind -> R.string.wind
        WeatherMetric.Pressure -> R.string.pressure
        WeatherMetric.Cloud -> R.string.cloud
        WeatherMetric.Visibility -> R.string.visibility
        WeatherMetric.Uv -> R.string.uv
    }

@get:DrawableRes
private val WeatherMetric.iconRes: Int
    get() = when (this) {
        WeatherMetric.Humidity -> R.drawable.ic_humidity
        WeatherMetric.Wind -> R.drawable.ic_wind
        WeatherMetric.Pressure -> R.drawable.ic_pressure
        WeatherMetric.Cloud -> R.drawable.ic_cloud
        WeatherMetric.Visibility -> R.drawable.ic_visibility
        WeatherMetric.Uv -> R.drawable.ic_uv
    }

@Composable
private fun WeatherMetricCard(item: CurrentWeatherData.Metric) {
    val weather = item.weather
    Card {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp, horizontal = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Image(
                painter = painterResource(item.type.iconRes),
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface),
            )
            Text(
                text = when (item.type) {
                    WeatherMetric.Humidity -> "${weather.humidity ?: 0}%"
                    WeatherMetric.Wind -> "${weather.windSpeed ?: 0.0} km/h"
                    WeatherMetric.Pressure -> "${weather.pressure ?: 0.0} mb"
                    WeatherMetric.Cloud -> "${weather.cloudCover ?: 0}%"
                    WeatherMetric.Visibility -> "${weather.visibility ?: 0.0} km"
                    WeatherMetric.Uv -> "${weather.uv ?: 0.0}"
                },
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(vertical = 8.dp),
            )
            Text(
                text = stringResource(item.type.titleRes),
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Composable
private fun AirQualityCard(
    item: CurrentWeatherData.AirQuality,
) {
    val context = LocalContext.current
    val weather = item.weather
    Card {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = stringResource(R.string.air_quality),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.weight(1f),
            )
            weather.airQuality?.let { quality ->
                context.airQualityDrawable(quality)?.let { drawable ->
                    Image(
                        painter = painterResource(drawable),
                        contentDescription = null,
                        modifier = Modifier.size(60.dp),
                    )
                }
            }
            Text(
                text = context.airQualityDescription(weather.airQuality),
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.End,
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp),
            )
        }
    }
}
