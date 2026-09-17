package com.andyha.featureSettings.ui.settings

import android.os.Build
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Colorize
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.TextFormat
import androidx.compose.material.icons.filled.TextIncrease
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.andyha.coreextension.updateLanguageResource
import com.andyha.coreui.base.context.BaseContext
import com.andyha.coreui.base.manager.DefaultConfiguration
import com.andyha.coreui.base.theme.BaseTypography
import com.andyha.coreui.base.theme.PrimaryColors
import com.andyha.coreui.base.theme.ThemeColors
import com.andyha.coreui.base.theme.ThemeMode
import com.andyha.coreui.base.theme.label
import com.andyha.featureSettings.R
import com.andyha.musiccommonresource.utils.Constants.appHorizontalPadding

@Composable
fun SettingsScreen(context: BaseContext) {
    val scrollState = rememberScrollState()
    val theme by context.app.configurations.theme.collectAsState()
    val language by context.app.configurations.language.collectAsState()
    val fontFamily by context.app.configurations.fontFamily.collectAsState()
    val fontScale by context.app.configurations.fontScale.collectAsState()
    val primaryColor by context.app.configurations.primaryColor.collectAsState()
    val useMaterialYou by context.app.configurations.isUsingMaterialYou.collectAsState()
    val materialYouSupported = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
    val materialYouActive = useMaterialYou && materialYouSupported

    Scaffold(modifier = Modifier.fillMaxSize(), content = { contentPadding ->
        Box(
            modifier = Modifier
                .padding(contentPadding)
                .fillMaxSize()
        ) {
            Column(modifier = Modifier.verticalScroll(scrollState)) {
                SettingsOptionTile(
                    icon = { Icon(Icons.Filled.Language, null) },
                    title = { Text(stringResource(R.string.setting_language_title)) },
                    value = language,
                    values = run { context.app.configurations.languageDisplayNames },
                    captions = run { context.app.configurations.languageNativeNames },
                    onChange = { value ->
                        context.activity.updateLanguageResource(value)
                        context.app.configurations.setLanguage(value)
                    })

                HorizontalDivider(Modifier.padding(horizontal = appHorizontalPadding))

                SettingsOptionTile(
                    icon = { Icon(Icons.Filled.TextFormat, null) },
                    title = { Text(stringResource(R.string.setting_font_title)) },
                    value = BaseTypography.resolveFont(fontFamily).fontName,
                    values = BaseTypography.all.keys.associateWith { it },
                    onChange = { value -> context.app.configurations.setFontFamily(value) }
                )

                HorizontalDivider(Modifier.padding(horizontal = appHorizontalPadding))

                SettingsFloatInputTile(
                    context,
                    icon = { Icon(Icons.Filled.TextIncrease, null) },
                    title = { Text(stringResource(R.string.setting_font_title)) },
                    value = fontScale,
                    presets = scalingPresets,
                    labelText = { "x$it" },
                    onReset = { context.app.configurations.setFontScale(DefaultConfiguration.fontScale) },
                    onChange = { value -> context.app.configurations.setFontScale(value) })

                HorizontalDivider(Modifier.padding(horizontal = appHorizontalPadding))

                SettingsOptionTile(
                    icon = { Icon(Icons.Filled.Palette, null) },
                    title = { Text(stringResource(R.string.setting_theme_title)) },
                    value = theme,
                    values = mapOf(
                        ThemeMode.SYSTEM to stringResource(R.string.system_theme),
                        ThemeMode.LIGHT to stringResource(R.string.light_theme),
                        ThemeMode.DARK to stringResource(R.string.dark_theme),
                    ),
                    onChange = { value -> context.app.configurations.setThemeMode(value) }
                )

                HorizontalDivider(Modifier.padding(horizontal = appHorizontalPadding))

                SettingsSwitchTile(
                    icon = { Icon(Icons.Filled.Face, null) },
                    title = { Text(stringResource(R.string.use_material_you)) },
                    value = materialYouActive,
                    enabled = materialYouSupported,
                    onChange = { value -> context.app.configurations.setUseMaterialYou(value) }
                )

                HorizontalDivider(Modifier.padding(horizontal = appHorizontalPadding))

                SettingsOptionTile(
                    icon = { Icon(Icons.Filled.Colorize, null) },
                    title = { Text(stringResource(R.string.setting_primary_color)) },
                    value = ThemeColors.resolvePrimaryColorKey(primaryColor),
                    values = PrimaryColors.entries.associateWith { it.label() },
                    enabled = !materialYouActive,
                    disabledHint = stringResource(R.string.setting_primary_color_material_you_hint),
                    onChange = { value -> context.app.configurations.setPrimaryColor(value.name) })
            }
        }
    })
}

private val scalingPresets = listOf(
    0.25f, 0.5f, 0.75f, 1f, 1.25f, 1.5f, 1.75f, 2f, 2.25f, 2.5f, 2.75f, 3f,
)