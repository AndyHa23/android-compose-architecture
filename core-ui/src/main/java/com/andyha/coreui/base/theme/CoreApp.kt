package com.andyha.coreui.base.theme

import androidx.lifecycle.ViewModel
import com.andyha.coreui.base.manager.ConfigurationManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CoreApp @Inject constructor(
    val configurations: ConfigurationManager
): ViewModel(){


}