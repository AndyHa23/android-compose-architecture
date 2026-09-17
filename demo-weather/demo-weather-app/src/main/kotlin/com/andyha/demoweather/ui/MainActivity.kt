package com.andyha.demoweather.ui

import androidx.compose.runtime.Composable
import com.andyha.coreui.base.activity.BaseComposeActivity
import com.andyha.coreui.base.context.BaseContext
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : BaseComposeActivity() {

    @Composable
    override fun Content(context: BaseContext) {
        WeatherApp(context, false)
    }
}
