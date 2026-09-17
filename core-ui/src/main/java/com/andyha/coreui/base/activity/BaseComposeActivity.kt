package com.andyha.coreui.base.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.compose.rememberNavController
import com.andyha.coreextension.collectSafely
import com.andyha.coreextension.updateLanguageResource
import com.andyha.coreui.base.context.BaseContext
import com.andyha.coreui.base.theme.BaseTheme
import com.andyha.coreui.base.theme.CoreApp
import com.andyha.coreui.base.theme.Language
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * Hosts the shared Compose entry point: edge-to-edge, the [CoreApp] view model, locale
 * propagation and the [BaseTheme] wrapper. Apps only provide their root composable.
 *
 * Subclasses must still be annotated with `@AndroidEntryPoint`.
 */
abstract class BaseComposeActivity : ComponentActivity() {

    @Composable
    protected abstract fun Content(context: BaseContext)

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        val viewModel: CoreApp by viewModels()

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.configurations.language.collectSafely {
                    Timber.d("collect language: $it")
                    updateLanguageResource(it.ifEmpty { Language.getDeviceLocale() })
                }
            }
        }

        setContent {
            val context = BaseContext(viewModel, this, rememberNavController())
            BaseTheme(context) {
                Content(context)
            }
        }
    }
}
