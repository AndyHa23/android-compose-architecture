package com.andyha.feature.login.ui

import androidx.lifecycle.viewModelScope
import com.andyha.coredata.storage.preference.AppSharedPreference
import com.andyha.coredata.storage.preference.currentUsername
import com.andyha.corenetwork.di.IoDispatcher
import com.andyha.coreui.base.manager.ConfigurationManager
import com.andyha.coreui.base.theme.Language
import com.andyha.coreui.base.theme.ThemeMode
import com.andyha.coreui.base.viewModel.BaseViewModel
import com.andyha.coreui.manager.ApiErrorHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val sharedPreference: AppSharedPreference,
    private val configurationManager: ConfigurationManager,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val errorHandler: ApiErrorHandler,
) : BaseViewModel(ioDispatcher, errorHandler) {

    private val _isUserAuthorized = MutableStateFlow(false)
    val isUserAuthorized = _isUserAuthorized.asStateFlow()

    val language = configurationManager.language

    init {
        checkLoginSession()
    }

    private fun checkLoginSession() {
        if (sharedPreference.currentUsername?.isNotEmpty() == true) {
            _isUserAuthorized.tryEmit(true)
        }
    }

    fun login(username: String) {
        viewModelScope.launch {
            sharedPreference.currentUsername = username
            delay(1000L.milliseconds)
            _isUserAuthorized.tryEmit(true)
        }
    }

    fun nextLanguage(): String {
        val currentLanguage = configurationManager.language.value
            .ifEmpty { Language.getDeviceLocale() }
        return when (currentLanguage) {
            Language.VIETNAMESE_LOCALE -> Language.ENGLISH_LOCALE
            else -> Language.VIETNAMESE_LOCALE
        }
    }

    fun setLanguage(language: String) {
        configurationManager.setLanguage(language)
    }

    fun toggleTheme() {
        val nextTheme = when (configurationManager.theme.value) {
            ThemeMode.DARK -> ThemeMode.LIGHT
            else -> ThemeMode.DARK
        }
        configurationManager.setThemeMode(nextTheme)
    }
}