package com.andyha.coreui.base.theme

import android.content.Context
import android.content.res.Resources
import androidx.core.os.ConfigurationCompat
import com.andyha.coreresource.R
import timber.log.Timber

class Language(val context: Context) {

    private val presetLocaleDisplay = mapOf(
        ENGLISH_LOCALE to R.string.english_display_name,
        VIETNAMESE_LOCALE to R.string.vietnamese_display_name,
        CHINESE_LOCALE to R.string.chinese_display_name
    )
    private val presetLocaleNative = mapOf(
        ENGLISH_LOCALE to R.string.english_native_name,
        VIETNAMESE_LOCALE to R.string.vietnamese_native_name,
        CHINESE_LOCALE to R.string.chinese_native_name
    )

    fun getLanguageDisplayNames(): Map<String, String> {
        val deviceSystemLocale = getDeviceLocale()
        Timber.d("deviceSystemLocale=$deviceSystemLocale")
        return mapOf(
            SYSTEM_DEFAULT_LOCALE to context.getString(R.string.system_display_name)
                    + " (${resolveLocaleDisplayName(deviceSystemLocale)})"
        ) + presetLocaleDisplay.mapValues { context.getString(it.value) }
    }

    fun getLanguageNativeNames(): Map<String, String> {
        val deviceSystemLocale = getDeviceLocale()
        return mutableMapOf(
            SYSTEM_DEFAULT_LOCALE to context.getString(R.string.system_native_name)
                    + " (${resolveLocaleNativeName(deviceSystemLocale)})"
        ) + presetLocaleNative.mapValues { context.getString(it.value) }
    }

    fun resolveLocaleDisplayName(locale: String) = context.getString(
        presetLocaleDisplay[locale] ?: R.string.english_display_name
    )

    fun resolveLocaleNativeName(locale: String) = context.getString(
        presetLocaleNative[locale] ?: R.string.english_display_name
    )

    companion object {
        const val SYSTEM_DEFAULT_LOCALE = ""
        const val ENGLISH_LOCALE = "en-US"
        const val VIETNAMESE_LOCALE = "vi-VN"
        const val CHINESE_LOCALE = "zh-CN"

        fun getDeviceLocale(): String {
            Timber.d("getDeviceLocale=${ConfigurationCompat.getLocales(Resources.getSystem().configuration)}")
            return ConfigurationCompat.getLocales(Resources.getSystem().configuration)[0]?.let {
                "${it.language}-${it.country}"
            } ?: ENGLISH_LOCALE
        }
    }
}
