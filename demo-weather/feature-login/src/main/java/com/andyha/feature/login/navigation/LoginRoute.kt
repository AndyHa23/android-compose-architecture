package com.andyha.feature.login.navigation

import androidx.compose.runtime.Composable
import com.andyha.feature.login.ui.LoginScreen


@Composable
fun LoginRoute(
    onSignedIn: () -> Unit
) {
    LoginScreen(onSignin = onSignedIn)
}