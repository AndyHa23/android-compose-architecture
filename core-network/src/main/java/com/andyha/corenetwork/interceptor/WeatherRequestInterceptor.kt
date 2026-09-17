package com.andyha.corenetwork.interceptor

import com.andyha.corenetwork.config.NetworkConfig
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject


class WeatherRequestInterceptor @Inject constructor(
    private val networkConfig: NetworkConfig
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val url = chain
            .request()
            .url
            .newBuilder()
            .addQueryParameter(KEY, networkConfig.weatherApiKey)
            .build()

        return chain.proceed(chain.request().newBuilder().url(url).build())
    }

    companion object {
        private const val KEY = "key"
    }
}