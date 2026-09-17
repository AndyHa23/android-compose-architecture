package com.andyha.coreutils.permission

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

enum class PermissionResult {
    GRANTED,
    DENIED,
    DENIED_FOREVER
}

/**
 * Requests runtime permissions from any [Context] (including the application context) by
 * delegating the request to a transparent [PermissionRequestActivity].
 */
object PermissionRequester {

    private var callback: ((PermissionResult) -> Unit)? = null

    fun isPermissionGranted(context: Context, permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun request(context: Context, permission: String, onResult: (PermissionResult) -> Unit) {
        if (isPermissionGranted(context, permission)) {
            onResult(PermissionResult.GRANTED)
            return
        }

        callback = onResult
        PermissionRequestActivity.start(context, permission)
    }

    internal fun onPermissionResult(result: PermissionResult) {
        val onResult = callback
        callback = null
        onResult?.invoke(result)
    }
}
