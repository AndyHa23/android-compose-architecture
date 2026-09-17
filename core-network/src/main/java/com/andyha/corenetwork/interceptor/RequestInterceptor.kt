package com.andyha.corenetwork.interceptor

import android.content.Context
import android.os.Build
import com.andyha.coredata.storage.preference.AppSharedPreference
import com.andyha.coredata.storage.preference.imei
import com.andyha.coredata.storage.preference.language
import com.andyha.coredata.storage.preference.token
import com.andyha.coreextension.getSettingDeviceId
import com.andyha.coreextension.versionName
import com.andyha.corenetwork.config.NetworkConfigConstants
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class RequestInterceptor @Inject constructor(
    private val context: Context,
    private val prefs: AppSharedPreference,
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder = chain.request().newBuilder()

        if (chain.request().header("unAuthorized") == null) {
            val accessToken = prefs.token
            if (accessToken.isNotEmpty()) {
                builder.header(
                    NetworkConfigConstants.AUTHORIZATION,
                    "${NetworkConfigConstants.BEARER} $accessToken"
                )
            }
        } else {
            builder.removeHeader("unAuthorized")
        }

        context.versionName()?.let { builder.header("X-APP-VERSION", it) }
        prefs.imei?.let { builder.header("X-IMEI", it) }
        context.getSettingDeviceId()?.let {
            builder.header("X-Device-Identifier", it)
            builder.header("User-Agent", it)
        }
        builder.header("X-Device-Family", Build.MODEL)
        prefs.language?.let { builder.header("X-Device-Locale", it) }
        builder.header("X-Device-Platform", "android")
        builder.header("X-Device-OS-Version", "android ${Build.VERSION.RELEASE}")
        builder.header("Content-Type", "application/json")
        return chain.proceed(builder.build())
    }
}