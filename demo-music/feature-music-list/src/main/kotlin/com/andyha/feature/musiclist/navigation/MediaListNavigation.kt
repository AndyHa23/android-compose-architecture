package com.andyha.feature.musiclist.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

const val ROUTE_MEDIALIST_SCREEN = "route_medialist_screen"

fun NavController.navigateToMediaListScreen(navOptions: NavOptions? = null) {
    this.navigate(ROUTE_MEDIALIST_SCREEN, navOptions)
}

fun NavGraphBuilder.mediaListScreen() {
    composable(route = ROUTE_MEDIALIST_SCREEN) {
//        MediaListScreen()
    }
}
