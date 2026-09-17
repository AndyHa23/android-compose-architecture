package com.andyha.coreui.base.theme

import android.app.Activity
import android.os.Build
import androidx.activity.ComponentActivity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.Density
import androidx.core.view.WindowCompat
import androidx.navigation.NavHostController
import com.andyha.coreui.base.context.BaseContext


enum class ThemeMode {
    SYSTEM,
    LIGHT,
    DARK,
}

@Composable
fun BaseTheme(
    context: BaseContext,
    content: @Composable () -> Unit,
) {
    val themeMode by context.app.configurations.theme.collectAsState()
    val isUsingMaterialYou by context.app.configurations.isUsingMaterialYou.collectAsState()
    val primaryColor by context.app.configurations.primaryColor.collectAsState()
    val fontFamily by context.app.configurations.fontFamily.collectAsState()
    val fontScale by context.app.configurations.fontScale.collectAsState()

    val colorSchemeMode = themeMode.toColorSchemeMode(isSystemInDarkTheme())
    val colorScheme = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && isUsingMaterialYou) {
        val currentContext = LocalContext.current
        when (colorSchemeMode) {
            ColorSchemeMode.LIGHT -> dynamicLightColorScheme(currentContext)
            else -> dynamicDarkColorScheme(currentContext)
        }
    } else {
        val primColor = ThemeColors.resolvePrimaryColor(primaryColor)
        when (colorSchemeMode) {
            ColorSchemeMode.LIGHT -> ThemeColorSchemes.createLightColorScheme(primColor)
            else -> ThemeColorSchemes.createDarkColorScheme(primColor)
        }
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        val activity = view.context as Activity
        SideEffect {
            WindowCompat.getInsetsController(activity.window, view).isAppearanceLightStatusBars =
                colorSchemeMode == ColorSchemeMode.LIGHT
        }
    }

    val typography = BaseTypography.toTypography(BaseTypography.resolveFont(fontFamily))


    MaterialTheme(
        colorScheme = colorScheme,
        typography = typography,
        content = {
            CompositionLocalProvider(
                LocalDensity provides Density(
                    density = LocalDensity.current.density,
                    fontScale = LocalDensity.current.fontScale * fontScale,
                ),
            ) {
                content()
            }
        }
    )
}

fun ThemeMode.toColorSchemeMode(isSystemInDarkTheme: Boolean) = when (this) {
    ThemeMode.SYSTEM -> if (isSystemInDarkTheme) ColorSchemeMode.DARK else ColorSchemeMode.LIGHT
    ThemeMode.LIGHT -> ColorSchemeMode.LIGHT
    ThemeMode.DARK -> ColorSchemeMode.DARK
}