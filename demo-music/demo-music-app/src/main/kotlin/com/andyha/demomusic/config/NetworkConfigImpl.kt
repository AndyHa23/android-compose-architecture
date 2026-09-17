package com.andyha.demomusic.config

import com.andyha.corenetwork.config.NetworkConfig
import com.andyha.demomusic.BuildConfig
import javax.inject.Inject


class NetworkConfigImpl @Inject constructor() : NetworkConfig {
    override var weatherBaseUrl = ""
    override var weatherApiKey = ""
    override var musicBaseUrl = BuildConfig.baseUrl
}