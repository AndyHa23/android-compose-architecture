package com.andyha.demoweather.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.andyha.coreui.base.context.BaseContext
import com.andyha.feature.login.navigation.LoginRoute
import com.andyha.feature.weather.model.WeatherRoute
import com.andyha.feature.weather.ui.WeatherMainRoute

@Composable
fun WeatherApp(
    context: BaseContext,
    isLoggedIn: Boolean,
) {
    val navController = context.navController
    val startDestination = if (isLoggedIn) WeatherRoute.Main.route else WeatherRoute.Login.route

    NavHost(
        navController = navController,
        startDestination = startDestination,
    ) {
        composable(WeatherRoute.Login.route) {
            LoginRoute(
                onSignedIn = {
                    navController.navigate(WeatherRoute.Main.route) {
                        popUpTo(WeatherRoute.Login.route) { inclusive = true }
                    }
                }
            )
        }
        composable(WeatherRoute.Main.route) {
            WeatherMainRoute(
                context = context,
                onLogout = {
                    navController.navigate(WeatherRoute.Login.route) {
                        popUpTo(WeatherRoute.Main.route) { inclusive = true }
                    }
                }
            )
        }
    }
}
