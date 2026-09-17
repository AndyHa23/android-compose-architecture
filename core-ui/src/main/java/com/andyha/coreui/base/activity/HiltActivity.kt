package com.andyha.coreui.base.activity

import androidx.activity.ComponentActivity
import com.andyha.coredata.manager.SessionManager
import com.andyha.coredata.storage.preference.AppSharedPreference
import dagger.Lazy
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
open class HiltActivity : ComponentActivity() {
    @Inject
    lateinit var preference: AppSharedPreference

    @Inject
    lateinit var sessionManager: Lazy<SessionManager>
}