package com.andyha.demoweather.config

import com.andyha.corenetwork.config.NetworkConfig
import com.andyha.demoweather.BuildConfig
import javax.inject.Inject


class NetworkConfigImpl @Inject constructor() : NetworkConfig {
    override var weatherBaseUrl = BuildConfig.weatherBaseUrl
    override var weatherApiKey = BuildConfig.weatherApiKey
    override var musicBaseUrl = ""
}