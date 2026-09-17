package com.andyha.coreui.base.permission

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import com.andyha.coreui.base.theme.CoreApp
import com.andyha.coreui.util.Eventer

enum class PermissionEvents {
    MEDIA_PERMISSION_GRANTED,
}

data class PermissionsState(
    val required: List<String>,
    val granted: List<String>,
    val denied: List<String>,
) {
    fun hasAll() = denied.isEmpty()
}

class PermissionsManager(private val coreApp: CoreApp) {
    val onUpdate = Eventer<PermissionEvents>()

    fun handle(activity: ComponentActivity) {
        val state = getState(activity)
        if (state.hasAll()) return
        activity.registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            if (permissions.count { it.value } > 0) {
                onUpdate.dispatch(PermissionEvents.MEDIA_PERMISSION_GRANTED)
            }
        }.launch(state.denied.toTypedArray())
    }

    private fun getRequiredPermissions(): List<String> {
        val required = mutableListOf<String>()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            required.add(Manifest.permission.POST_NOTIFICATIONS)
        }
        return required
    }

    private fun getState(activity: ComponentActivity): PermissionsState {
        val required = getRequiredPermissions()
        val granted = mutableListOf<String>()
        val denied = mutableListOf<String>()
        required.forEach {
            if (activity.checkSelfPermission(it) == PackageManager.PERMISSION_GRANTED) {
                granted.add(it)
            } else {
                denied.add(it)
            }
        }
        return PermissionsState(
            required = required,
            granted = granted,
            denied = denied,
        )
    }
}
