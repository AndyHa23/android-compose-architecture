package com.andyha.feature.weather.ui

import androidx.lifecycle.viewModelScope
import com.andyha.coredata.storage.preference.AppSharedPreference
import com.andyha.coredata.storage.preference.currentUsername
import com.andyha.corenetwork.di.IoDispatcher
import com.andyha.coreui.base.manager.ConfigurationManager
import com.andyha.coreui.base.theme.Language
import com.andyha.coreui.base.theme.ThemeMode
import com.andyha.coreui.base.viewModel.BaseViewModel
import com.andyha.coreui.manager.ApiErrorHandler
import com.andyha.weatherdomain.model.LocationState
import com.andyha.weatherdomain.usecase.getCurrentLocationState.GetLocationStateUseCase
import com.andyha.weatherdomain.usecase.requestLocationUpdate.RequestLocationUpdateUseCase
import com.andyha.weatherdomain.usecase.setSelectedLocationUseCase.SetSelectedLocationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val requestLocationUpdateUseCase: RequestLocationUpdateUseCase,
    private val getLocationStateUseCase: GetLocationStateUseCase,
    private val setSelectedLocationUseCase: SetSelectedLocationUseCase,
    private val configurationManager: ConfigurationManager,
    private val prefs: AppSharedPreference,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val errorHandler: ApiErrorHandler,
) : BaseViewModel(ioDispatcher, errorHandler) {

    private val _currentLocationState = MutableStateFlow<LocationState>(LocationState.Undefined)
    val currentLocationState = _currentLocationState.asStateFlow()

    private val _logoutSuccessful = MutableStateFlow(false)
    val logoutSucessful = _logoutSuccessful.asStateFlow()

    init {
        requestLocationUpdate()

        viewModelScope.launch {
            getLocationStateUseCase()
                .catch { Timber.e(it) }
                .collect {
                    Timber.d("WeatherViewModel: currentLocationState: $it")
                    _currentLocationState.emit(it)
                }
        }
    }

    fun requestLocationUpdate() {
        requestLocationUpdateUseCase()
    }

    fun selectLocation(location: LocationState.LocationDetected) {
        viewModelScope.launch {
            setSelectedLocationUseCase(location.address)
                .catch { Timber.e(it) }
                .collectLatest { }
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

    fun isLoggedIn(): Boolean = prefs.currentUsername?.isNotEmpty() == true

    fun logout(){
        prefs.currentUsername = ""
        _logoutSuccessful.tryEmit(true)
    }
}