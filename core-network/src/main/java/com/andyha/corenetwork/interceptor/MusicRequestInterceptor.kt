package com.andyha.corenetwork.interceptor

import com.andyha.corenetwork.config.NetworkConfig
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject


class MusicRequestInterceptor@Inject constructor(
    private val networkConfig: NetworkConfig
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val url = chain
            .request()
            .url
            .newBuilder()
            .build()

        return chain.proceed(chain.request().newBuilder().url(url).build())
    }
}