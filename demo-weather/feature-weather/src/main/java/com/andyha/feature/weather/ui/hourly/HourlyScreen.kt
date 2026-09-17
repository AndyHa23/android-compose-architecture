package com.andyha.feature.weather.ui.hourly

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.andyha.weatherdomain.model.HourlyForecast
import com.andyha.weatherdomain.model.LocationState
import com.andyha.feature.weather.R
import com.andyha.feature.weather.compose.PermissionStateCard
import com.andyha.feature.weather.compose.WeatherImage
import com.andyha.feature.weather.compose.formatHour
import com.andyha.feature.weather.compose.formatWeekDay

@Composable
fun HourlyScreen(
    modifier: Modifier = Modifier,
    viewModel: HourlyViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val sections by viewModel.hourlyForecast.collectAsStateWithLifecycle()
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
                data = Uri.parse("package:${context.packageName}")
            }) },
            modifier = modifier,
        )

        else -> {
            LazyColumn(
                modifier = modifier.fillMaxSize(),
            ) {
                sections.forEach { section ->
                    val rows = section.data
                    item(key = "header-${section.id}", contentType = "header") {
                        Text(
                            text = context.formatWeekDay(section.id),
                            style = MaterialTheme.typography.titleSmall,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp),
                        )
                    }
                    items(
                        items = rows,
                        key = { it.timeStamp },
                        contentType = { "forecast" },
                    ) { forecast ->
                        HourlyForecastRow(forecast)
                    }
                }
            }
        }
    }
}

@Composable
private fun HourlyForecastRow(forecast: HourlyForecast) {
    val context = LocalContext.current
    val hour = remember(context, forecast.timeStamp) { context.formatHour(forecast.timeStamp) }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = hour,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(end = 10.dp),
            )
            WeatherImage(
                iconUrl = forecast.icon,
                contentDescription = forecast.condition,
                size = 24.dp,
            )
            Text(
                text = stringResource(R.string.current_temperature, forecast.temperature ?: 0),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(start = 8.dp, end = 8.dp),
            )
            Text(
                text = stringResource(R.string.feels_like_hourly, forecast.feelsLike ?: 0),
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1f),
            )
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "${forecast.chanceOfRain ?: 0}%",
                    style = MaterialTheme.typography.bodySmall,
                )
                if ((forecast.precipitation ?: 0.0) > 0.0) {
                    Text(
                        text = "${forecast.precipitation}mm",
                        style = MaterialTheme.typography.bodySmall,
                    )
                }
            }
        }
    }
}
