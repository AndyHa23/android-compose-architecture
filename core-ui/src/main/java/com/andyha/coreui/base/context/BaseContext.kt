package com.andyha.coreui.base.context

import androidx.activity.ComponentActivity
import androidx.navigation.NavHostController
import com.andyha.coreui.base.theme.CoreApp

data class BaseContext(
    val app: CoreApp,
    val activity: ComponentActivity,
    val navController: NavHostController
)