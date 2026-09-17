package com.andyha.coreutils.permission

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts

/**
 * Invisible activity used to ask for a runtime permission when only a non-activity [Context]
 * is available. The result is forwarded back through [PermissionRequester].
 */
class PermissionRequestActivity : ComponentActivity() {

    private val permission: String
        get() = intent?.getStringExtra(ARG_PERMISSION).orEmpty()

    private val permissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            val result = when {
                isGranted -> PermissionResult.GRANTED
                // The system only offers a rationale while the permission can still be asked for.
                shouldShowRequestPermissionRationale(permission) -> PermissionResult.DENIED
                else -> PermissionResult.DENIED_FOREVER
            }
            PermissionRequester.onPermissionResult(result)
            finish()
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (permission.isEmpty()) {
            PermissionRequester.onPermissionResult(PermissionResult.DENIED)
            finish()
            return
        }

        if (savedInstanceState == null) {
            permissionLauncher.launch(permission)
        }
    }

    override fun finish() {
        super.finish()
        // Reset the animation to avoid flickering.
        overridePendingTransition(0, 0)
    }

    companion object {
        private const val ARG_PERMISSION = "permission"

        fun start(context: Context, permission: String) {
            val intent = Intent(context, PermissionRequestActivity::class.java)
                .putExtra(ARG_PERMISSION, permission)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

            context.startActivity(intent)
        }
    }
}
