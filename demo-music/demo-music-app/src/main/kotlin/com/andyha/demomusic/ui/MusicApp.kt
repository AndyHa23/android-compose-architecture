package com.andyha.demomusic.ui

import androidx.annotation.OptIn
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.andyha.coreui.base.context.BaseContext
import com.andyha.featuremusiclistui.details.MusicDetailsScreen
import com.andyha.feature.musiclist.pager.MediaListPagerScreen
import com.andyha.feature.musicplayer.ui.nowPlaying.NowPlayingBar
import com.andyha.feature.musicplayer.ui.nowPlaying.NowPlayingScreen
import com.andyha.feature.musicplayer.ui.nowPlaying.NowPlayingViewModel
import com.andyha.featureSettings.ui.settings.SettingsScreen
import com.andyha.musiccommonresource.utils.Constants.bottomNavigationBarHeight
import com.andyha.musiccommonresource.utils.Route

@OptIn(UnstableApi::class)
@Composable
fun MusicApp(
    context: BaseContext,
    nowPlayingViewModel: NowPlayingViewModel = hiltViewModel(),
) {
    val currentItem by nowPlayingViewModel.currentItem.collectAsStateWithLifecycle()
    val navController = context.navController
    val route = remember { mutableStateOf<String?>(navController.currentDestination?.route ?: "") }

    LaunchedEffect(true) {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            route.value = destination.route
        }
    }
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Scaffold(
            bottomBar = {
                //Only show NowPlayingBar if the current screen is not NowPlayingScreen
                if (currentItem != null && route.value != Route.NowPlaying.route) {
                    NowPlayingBar(
                        modifier = Modifier
                            .wrapContentHeight()
                            .fillMaxWidth()
                            .padding(
                                bottom = if (route.value == Route.Main.route) bottomNavigationBarHeight
                                else 0.dp
                            ),
                        onClick = { navController.navigate(Route.NowPlaying.route) })

                }
            },
        ) { _ ->
            NavHost(
                navController = navController,
                startDestination = Route.Main.route
            ) {
                composable(Route.Main.route) {
                    MediaListPagerScreen(
                        context = context,
                        settingsPage = { SettingsScreen(context) },
                    )
                }
                composable(Route.MediaDetails.route) {
                    MusicDetailsScreen(
                        context,
                        MediaItem.fromBundle(it.arguments!!)
                    )
                }
                composable(Route.NowPlaying.route) {
                    NowPlayingScreen(onClickBack = { navController.popBackStack() })
                }
            }
        }
    }
}
