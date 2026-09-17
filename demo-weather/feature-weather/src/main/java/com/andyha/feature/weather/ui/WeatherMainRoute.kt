package com.andyha.feature.weather.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.andyha.coreextension.updateLanguageResource
import com.andyha.coreresource.R as CoreR
import com.andyha.coreui.base.context.BaseContext
import com.andyha.coreui.base.theme.Language
import com.andyha.weatherdomain.model.LocationState
import com.andyha.feature.weather.R
import com.andyha.feature.weather.model.WeatherTab
import com.andyha.feature.weather.ui.daily.DailyScreen
import com.andyha.feature.weather.ui.hourly.HourlyScreen
import com.andyha.feature.weather.ui.locationHistory.LocationHistorySheet
import com.andyha.feature.weather.ui.now.NowScreen
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherMainRoute(
    context: BaseContext,
    onLogout: () -> Unit,
    viewModel: WeatherViewModel = hiltViewModel(),
) {
    val activity = context.activity
    val coroutineScope = rememberCoroutineScope()
    val currentLocationState by viewModel.currentLocationState.collectAsStateWithLifecycle()
    val logoutSuccessful by viewModel.logoutSucessful.collectAsStateWithLifecycle()
    val language by context.app.configurations.language.collectAsStateWithLifecycle()
    val isVietnamese = language.ifEmpty { Language.getDeviceLocale() }.startsWith("vi")
    var selectedTab by rememberSaveable { mutableStateOf(WeatherTab.Now) }
    var showLocationSheet by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.requestLocationUpdate()
    }

    LaunchedEffect(logoutSuccessful) {
        if (logoutSuccessful) {
            onLogout()
        }
    }

    BackHandler(enabled = showLocationSheet) {
        showLocationSheet = false
    }

    key(language) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background,
        ) {
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                topBar = {
                    WeatherTopBar(
                        locationState = currentLocationState,
                        isVietnamese = isVietnamese,
                        onLocationClick = { showLocationSheet = true },
                        onLanguageClick = {
                            val next = viewModel.nextLanguage()
                            activity.updateLanguageResource(next)
                            viewModel.setLanguage(next)
                        },
                        onThemeClick = { viewModel.toggleTheme() },
                        onLogoutClick = { viewModel.logout() },
                    )
                },
                bottomBar = {
                    WeatherTabBar(
                        selectedTab = selectedTab,
                        onTabSelected = { selectedTab = it },
                    )
                },
            ) { padding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                ) {
                    when (selectedTab) {
                        WeatherTab.Now -> NowScreen(modifier = Modifier.fillMaxSize())
                        WeatherTab.Hourly -> HourlyScreen(modifier = Modifier.fillMaxSize())
                        WeatherTab.Daily -> DailyScreen(modifier = Modifier.fillMaxSize())
                    }
                }
            }
        }

        if (showLocationSheet) {
            ModalBottomSheet(
                onDismissRequest = { showLocationSheet = false },
            ) {
                LocationHistorySheet(
                    onLocationSelected = {
                        viewModel.selectLocation(it)
                        coroutineScope.launch { showLocationSheet = false }
                    }
                )
            }
        }
    }
}

@Composable
private fun WeatherTopBar(
    locationState: LocationState,
    isVietnamese: Boolean,
    onLocationClick: () -> Unit,
    onLanguageClick: () -> Unit,
    onThemeClick: () -> Unit,
    onLogoutClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .clickable(onClick = onLocationClick)
                .padding(horizontal = 8.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = when (locationState) {
                    is LocationState.LocationDetected -> locationState.address
                    else -> stringResource(R.string.detecting_location)
                },
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            if (locationState is LocationState.LocationDetected) {
                Text(
                    text = locationState.region.ifEmpty { locationState.country },
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
        WeatherToolbarButton(
            painter = painterResource(
                if (isVietnamese) CoreR.drawable.ic_flag_vn else CoreR.drawable.ic_flag_us
            ),
            contentDescription = stringResource(CoreR.string.change_language),
            tint = Color.Unspecified,
            onClick = onLanguageClick,
        )
        WeatherToolbarButton(
            painter = painterResource(CoreR.drawable.ic_theme_light),
            contentDescription = stringResource(CoreR.string.change_theme),
            onClick = onThemeClick,
        )
        WeatherToolbarButton(
            painter = painterResource(CoreR.drawable.ic_logout),
            contentDescription = stringResource(R.string.logout),
            onClick = onLogoutClick,
        )
    }
}

@Composable
private fun WeatherToolbarButton(
    painter: Painter,
    contentDescription: String,
    onClick: () -> Unit,
    tint: Color = MaterialTheme.colorScheme.onBackground,
) {
    IconButton(onClick = onClick) {
        Icon(
            painter = painter,
            contentDescription = contentDescription,
            modifier = Modifier.size(24.dp),
            tint = tint,
        )
    }
}

@Composable
private fun WeatherTabBar(
    selectedTab: WeatherTab,
    onTabSelected: (WeatherTab) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .navigationBarsPadding()
    ) {
        HorizontalDivider()
        TabRow(
            selectedTabIndex = selectedTab.ordinal,
            containerColor = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.onBackground,
            divider = {},
        ) {
            WeatherTab.entries.forEach { item ->
                val selected = item == selectedTab
                Tab(
                    selected = selected,
                    onClick = { onTabSelected(item) },
                    text = {
                        Text(
                            text = stringResource(item.title),
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                        )
                    },
                    icon = {
                        Icon(
                            painter = painterResource(item.icon),
                            contentDescription = null,
                            modifier = Modifier.wrapContentHeight(),
                        )
                    },
                )
            }
        }
    }
}
