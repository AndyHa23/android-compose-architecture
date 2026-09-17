package com.andyha.feature.weather.ui.daily

import android.content.Intent
import android.provider.Settings
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.andyha.weatherdomain.model.DailyForecast
import com.andyha.weatherdomain.model.LocationState
import com.andyha.feature.weather.R
import com.andyha.feature.weather.compose.PermissionStateCard
import com.andyha.feature.weather.compose.WeatherImage
import com.andyha.feature.weather.compose.formatDate
import com.andyha.feature.weather.compose.formatShortWeekDay
import com.andyha.feature.weather.compose.isToday
import androidx.core.net.toUri

@Composable
fun DailyScreen(
    modifier: Modifier = Modifier,
    viewModel: DailyViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val items by viewModel.dailyForecast.collectAsStateWithLifecycle()
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
            val temperatureRange = remember(items) {
                val maxTemps = items.mapNotNull { it.maxTemp }
                val minTemps = items.mapNotNull { it.minTemp }
                if (maxTemps.isEmpty() || minTemps.isEmpty()) null
                else minTemps.min() to maxTemps.max()
            }

            // Show five columns per screen like the reference layout.
            val screenWidth = LocalConfiguration.current.screenWidthDp.dp
            val columnWidth = (screenWidth / 5).coerceIn(64.dp, 140.dp)

            LazyRow(
                modifier = modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background),
                contentPadding = PaddingValues(vertical = 16.dp),
            ) {
                itemsIndexed(items, key = { _, item -> item.timeStamp }) { index, item ->
                    Row(modifier = Modifier.fillMaxHeight()) {
                        if (index > 0) {
                            Box(
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .width(1.dp)
                                    .background(
                                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
                                    )
                            )
                        }
                        DailyForecastColumn(
                            item = item,
                            width = columnWidth,
                            scaleMin = temperatureRange?.first,
                            scaleMax = temperatureRange?.second,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun DailyForecastColumn(
    item: DailyForecast,
    width: Dp,
    scaleMin: Int?,
    scaleMax: Int?,
) {
    val context = LocalContext.current
    val timeStamp = item.timeStamp
    val weekDay = remember(timeStamp) { formatShortWeekDay(timeStamp) }
    val dayOfMonth = remember(context, timeStamp) { context.formatDate(timeStamp) }
    val today = remember(timeStamp) { isToday(timeStamp) }

    Column(
        modifier = Modifier
            .width(width)
            .fillMaxHeight()
            .padding(horizontal = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(
            text = weekDay,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        DayOfMonth(day = dayOfMonth, highlighted = today)
        WeatherImage(
            iconUrl = item.icon,
            contentDescription = item.condition,
            size = 36.dp,
        )
        ChanceOfRain(chance = item.chanceOfRain)
        TemperatureBar(
            maxTemp = item.maxTemp,
            minTemp = item.minTemp,
            scaleMin = scaleMin,
            scaleMax = scaleMax,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
        )
        Text(
            text = stringResource(R.string.precipitation_millimeters, item.precipitation ?: 0.0),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            maxLines = 1,
        )
    }
}

@Composable
private fun DayOfMonth(day: String, highlighted: Boolean) {
    Box(
        modifier = Modifier
            .size(28.dp)
            .then(
                if (highlighted) {
                    Modifier
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary)
                } else Modifier
            ),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = day,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = if (highlighted) {
                MaterialTheme.colorScheme.onPrimary
            } else {
                MaterialTheme.colorScheme.onSurface
            },
            maxLines = 1,
        )
    }
}

@Composable
private fun ChanceOfRain(chance: Int?) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(2.dp),
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_rainy_16),
            contentDescription = null,
            modifier = Modifier.size(14.dp),
            tint = MaterialTheme.colorScheme.primary,
        )
        Text(
            text = "${chance ?: 0}%",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 1,
        )
    }
}

private const val MinBarFraction = 0.06f

@Composable
private fun TemperatureBar(
    maxTemp: Int?,
    minTemp: Int?,
    scaleMin: Int?,
    scaleMax: Int?,
    modifier: Modifier = Modifier,
) {
    val high = maxTemp ?: minTemp ?: 0
    val low = minTemp ?: maxTemp ?: 0

    // Bars share one scale across the whole list so their heights are comparable.
    // Proportions are expressed as weights, so the bar tracks whatever height the
    // column is given without needing a measured track.
    val top: Float
    val bar: Float
    val bottom: Float
    val span = ((scaleMax ?: high) - (scaleMin ?: low)).toFloat()
    if (span <= 0f) {
        top = 0f
        bar = 1f
        bottom = 0f
    } else {
        val rawBar = (high - low) / span
        bar = rawBar.coerceIn(MinBarFraction, 1f)
        val rawTop = ((scaleMax ?: high) - high) / span
        val rawBottom = (low - (scaleMin ?: low)) / span
        val rawRest = rawTop + rawBottom
        val rest = 1f - bar
        if (rawRest <= 0f) {
            top = rest / 2f
            bottom = rest / 2f
        } else {
            top = rest * (rawTop / rawRest)
            bottom = rest * (rawBottom / rawRest)
        }
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if (top > 0f) {
            Spacer(modifier = Modifier.weight(top))
        }
        Text(
            text = stringResource(R.string.current_temperature, high),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 1,
        )
        Box(
            modifier = Modifier
                .padding(vertical = 2.dp)
                .width(12.dp)
                .weight(bar)
                .clip(RoundedCornerShape(percent = 50))
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primary,
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.35f),
                        )
                    )
                )
        )
        Text(
            text = stringResource(R.string.current_temperature, low),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 1,
        )
        if (bottom > 0f) {
            Spacer(modifier = Modifier.weight(bottom))
        }
    }
}
