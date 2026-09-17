package com.andyha.coreui.base.manager

import android.content.Context
import android.content.res.Configuration
import com.andyha.coredata.storage.preference.AppSharedPreference
import com.andyha.coredata.storage.preference.fontFamily
import com.andyha.coredata.storage.preference.fontScale
import com.andyha.coredata.storage.preference.language
import com.andyha.coredata.storage.preference.materialYou
import com.andyha.coredata.storage.preference.primaryColor
import com.andyha.coredata.storage.preference.theme
import com.andyha.coreextension.getValueOrNull
import com.andyha.coreextension.localehelper.currentLocale
import com.andyha.coreui.base.theme.BuiltinFonts
import com.andyha.coreui.base.theme.Language
import com.andyha.coreui.base.theme.PrimaryColors
import com.andyha.coreui.base.theme.ThemeMode
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject


object DefaultConfiguration {
    val language = "en-US"
    val themeMode: ThemeMode = ThemeMode.SYSTEM
    val useMaterialYou = false
    val fontFamily = BuiltinFonts.Roboto.fontName
    val fontScale = 1.0f
    val primaryColor = PrimaryColors.Purple.name
}


class ConfigurationManagerImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val prefs: AppSharedPreference
) : ConfigurationManager {

    private val _theme = MutableStateFlow(getThemeMode())
    override val theme = _theme.asStateFlow()

    private val _language = MutableStateFlow(getLanguage())
    override val language = _language.asStateFlow()

    private val _orientation = MutableStateFlow(getOrientation())
    override val orientation = _orientation.asStateFlow()

    private val _fontFamily = MutableStateFlow(getFontFamily())
    override val fontFamily = _fontFamily.asStateFlow()

    private val _fontScale = MutableStateFlow(getFontScale())
    override val fontScale = _fontScale.asStateFlow()

    private val _isUsingMaterialYou = MutableStateFlow(isUsingMaterialYou())
    override val isUsingMaterialYou = _isUsingMaterialYou.asStateFlow()

    private val _primaryColor = MutableStateFlow(getPrimaryColor())
    override val primaryColor = _primaryColor.asStateFlow()

    override val languageDisplayNames: Map<String, String>
        get() = languageHelper.getLanguageDisplayNames()

    override val languageNativeNames: Map<String, String>
        get() = languageHelper.getLanguageNativeNames()

    private val languageHelper = Language(context)

    init {
        prefs.language =
            if (prefs.language?.isNotEmpty() == true) prefs.language
            else Language.getDeviceLocale()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        if (newConfig.orientation != orientation.getValueOrNull()) {
            _orientation.tryEmit(newConfig.orientation)
        }

        val newLanguage = newConfig.currentLocale.toString()

        if (newLanguage != language.getValueOrNull() || newLanguage != prefs.language) {
            _language.tryEmit(newLanguage)
            prefs.language = newLanguage
        }

        if (newConfig.fontScale != fontScale.getValueOrNull()) {
            _fontScale.tryEmit(newConfig.fontScale)
        }
    }

    private fun getThemeMode() = prefs.theme
        ?.let { ThemeMode.valueOf(it) }
        ?: DefaultConfiguration.themeMode

    override fun setThemeMode(themeMode: ThemeMode) {
        prefs.theme = themeMode.name
        _theme.update { getThemeMode() }
    }

    private fun getLanguage() = prefs.language ?: DefaultConfiguration.language

    override fun setLanguage(language: String) {
        prefs.language = language
        _language.update { getLanguage() }
    }

    private fun getFontFamily() = prefs.fontFamily ?: DefaultConfiguration.fontFamily

    override fun setFontFamily(value: String) {
        prefs.fontFamily = value
        _fontFamily.update { getFontFamily() }
    }

    private fun getFontScale() =
        prefs.fontScale.let { if (it == 0f) DefaultConfiguration.fontScale else it }

    override fun setFontScale(value: Float) {
        prefs.fontScale = value
        _fontScale.update { getFontScale() }
    }

    private fun isUsingMaterialYou() = prefs.materialYou

    override fun setUseMaterialYou(value: Boolean) {
        prefs.materialYou = value
        _isUsingMaterialYou.update { isUsingMaterialYou() }
    }

    private fun getPrimaryColor() = prefs.primaryColor ?: DefaultConfiguration.primaryColor

    override fun setPrimaryColor(value: String) {
        prefs.primaryColor = value
        _primaryColor.update { getPrimaryColor() }
    }

    private fun getOrientation() = context.resources.configuration.orientation
}

interface ConfigurationManager {
    val theme: StateFlow<ThemeMode>
    val language: StateFlow<String>
    val orientation: StateFlow<Int>
    val fontFamily: StateFlow<String>
    val fontScale: StateFlow<Float>
    val isUsingMaterialYou: StateFlow<Boolean>
    val primaryColor: StateFlow<String>
    val languageDisplayNames: Map<String, String>
    val languageNativeNames: Map<String, String>

    fun onConfigurationChanged(newConfig: Configuration)
    fun setThemeMode(themeMode: ThemeMode)
    fun setLanguage(language: String)
    fun setFontFamily(value: String)
    fun setFontScale(value: Float)
    fun setUseMaterialYou(value: Boolean)
    fun setPrimaryColor(value: String)
}