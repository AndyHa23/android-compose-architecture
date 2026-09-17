package com.andyha.coreextension

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import android.provider.Settings
import timber.log.Timber
import java.util.*

fun Int.toPx(): Int {
    val density = Resources.getSystem().displayMetrics.density
    return (this * density + .5F).toInt()
}

fun Context.updateLanguageResource(langTag: String): Configuration? {
    val configuration = resources.configuration
    try {
        val locale = langTag.convertToLocale()
        Timber.d("langTag = $langTag - current: $locale")
        Locale.setDefault(locale)
        configuration.setLocale(locale)
        resources.updateConfiguration(configuration, resources.displayMetrics)
        applicationContext.resources.updateConfiguration(configuration, resources.displayMetrics)
    } catch (e: Exception) {
        Timber.e(e)
    }
    return configuration
}

fun Activity?.openFingerprintScreen() {
    try {
        val action = when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.P -> Settings.ACTION_FINGERPRINT_ENROLL
            else -> Settings.ACTION_SECURITY_SETTINGS
        }
        this?.startActivity(Intent(action))
    } catch (e: Exception) {
        Timber.e("openFingerprintScreen error: ${e.message}")
        this?.startActivity(Intent(Settings.ACTION_SETTINGS))
    }
}

