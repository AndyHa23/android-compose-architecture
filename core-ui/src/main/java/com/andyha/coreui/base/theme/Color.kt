package com.andyha.coreui.base.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.stringResource
import androidx.core.graphics.ColorUtils
import com.andyha.coreresource.R

enum class PrimaryColors {
    Red,
    Orange,
    Amber,
    Yellow,
    Lime,
    Green,
    Emerald,
    Teal,
    Cyan,
    Sky,
    Blue,
    Indigo,
    Violet,
    Purple,
    Fuchsia,
    Pink,
    Rose;
}

object ThemeColors {
    val Red = Color(0xFFEF4444)
    val Orange = Color(0xFFF97316)
    val Amber = Color(0xFFF59E0B)
    val Yellow = Color(0xFFEAB308)
    val Lime = Color(0xFF84CC16)
    val Green = Color(0xFF22C55E)
    val Emerald = Color(0xFF10B981)
    val Teal = Color(0xFF14B8A6)
    val Cyan = Color(0xFF06B6D4)
    val Sky = Color(0xFF0EA5E9)
    val Blue = Color(0xFF3B82F6)
    val Indigo = Color(0xFF6366f1)
    val Violet = Color(0xFF8B5CF6)
    val Purple = Color(0xFFA855F7)
    val Fuchsia = Color(0xFFD946EF)
    val Pink = Color(0xFFEC4899)
    val Rose = Color(0xFFF43f5E)

    val Neutral50 = Color(0xFFFAFAFA)
    val Neutral100 = Color(0xFFF5F5F5)
    val Neutral200 = Color(0xFFE5E5E5)
    val Neutral800 = Color(0xFF262626)
    val Neutral900 = Color(0xFF171717)

    val DefaultPrimaryColor = PrimaryColors.Purple
    val PrimaryColorsMap = mapOf(
        PrimaryColors.Red to Red,
        PrimaryColors.Orange to Orange,
        PrimaryColors.Amber to Amber,
        PrimaryColors.Yellow to Yellow,
        PrimaryColors.Lime to Lime,
        PrimaryColors.Green to Green,
        PrimaryColors.Emerald to Emerald,
        PrimaryColors.Teal to Teal,
        PrimaryColors.Cyan to Cyan,
        PrimaryColors.Sky to Sky,
        PrimaryColors.Blue to Blue,
        PrimaryColors.Indigo to Indigo,
        PrimaryColors.Violet to Violet,
        PrimaryColors.Purple to Purple,
        PrimaryColors.Fuchsia to Fuchsia,
        PrimaryColors.Pink to Pink,
        PrimaryColors.Rose to Rose,
    )

    fun resolvePrimaryColorKey(value: String?) =
        PrimaryColors.entries.find { it.name == value } ?: DefaultPrimaryColor

    fun resolvePrimaryColor(value: PrimaryColors) = PrimaryColorsMap[value]!!
    fun resolvePrimaryColor(value: String?) = resolvePrimaryColor(resolvePrimaryColorKey(value))
}

@Composable
fun PrimaryColors.label() = when (this) {
    PrimaryColors.Red -> stringResource(R.string.common_color_red)
    PrimaryColors.Orange -> stringResource(R.string.common_color_orange)
    PrimaryColors.Amber -> stringResource(R.string.common_color_amber)
    PrimaryColors.Yellow -> stringResource(R.string.common_color_yellow)
    PrimaryColors.Lime -> stringResource(R.string.common_color_lime)
    PrimaryColors.Green -> stringResource(R.string.common_color_green)
    PrimaryColors.Emerald -> stringResource(R.string.common_color_emerald)
    PrimaryColors.Teal -> stringResource(R.string.common_color_teal)
    PrimaryColors.Cyan -> stringResource(R.string.common_color_cyan)
    PrimaryColors.Sky -> stringResource(R.string.common_color_sky)
    PrimaryColors.Blue -> stringResource(R.string.common_color_blue)
    PrimaryColors.Indigo -> stringResource(R.string.common_color_indigo)
    PrimaryColors.Violet -> stringResource(R.string.common_color_violet)
    PrimaryColors.Purple -> stringResource(R.string.common_color_purple)
    PrimaryColors.Fuchsia -> stringResource(R.string.common_color_fuchsia)
    PrimaryColors.Pink -> stringResource(R.string.common_color_pink)
    PrimaryColors.Rose -> stringResource(R.string.common_color_rose)
}

enum class ColorSchemeMode {
    LIGHT,
    DARK,
}

object ThemeColorSchemes {
    private val lightBackgroundColor = ThemeColors.Neutral50
    private val lightSurfaceColor = ThemeColors.Neutral100
    private val lightSurfaceVariantColor = ThemeColors.Neutral200
    private val lightContrastColor = Color.White

    private val darkBackgroundColor = ThemeColors.Neutral900
    private val darkSurfaceColor = ThemeColors.Neutral900
    private val darkSurfaceVariantColor = ThemeColors.Neutral800

    private const val BACKGROUND_BLEND_RATIO = 0.03f
    private const val SURFACE_BLEND_RATIO = 0.02f
    private const val SURFACE_VARIANT_BLEND_RATIO = 0.01f
    private const val DARK_ON_PRIMARY_LIGHTNESS = -0.3f
    private const val DARK_ON_SECONDARY_LIGHTNESS = -0.4f
    private const val DARK_ON_TERTIARY_LIGHTNESS = -0.5f
    private const val LIGHT_ON_BACKGROUND_LIGHTNESS = -0.5f
    private const val LIGHT_ON_SURFACE_LIGHTNESS = -0.5f
    private const val LIGHT_ON_SURFACE_VARIANT_LIGHTNESS = -0.45f

    fun createLightColorScheme(primaryColor: Color) = lightColorScheme(
        primary = primaryColor,
        onPrimary = lightContrastColor,
        primaryContainer = primaryColor,
        onPrimaryContainer = lightContrastColor,
        secondary = primaryColor,
        onSecondary = lightContrastColor,
        secondaryContainer = primaryColor,
        onSecondaryContainer = lightContrastColor,
        tertiary = primaryColor,
        onTertiary = lightContrastColor,
        tertiaryContainer = primaryColor,
        onTertiaryContainer = lightContrastColor,
        background = blendColors(lightBackgroundColor, primaryColor, BACKGROUND_BLEND_RATIO),
        onBackground = adjustLightness(primaryColor, LIGHT_ON_BACKGROUND_LIGHTNESS),
        surface = blendColors(lightSurfaceColor, primaryColor, SURFACE_BLEND_RATIO),
        onSurface = adjustLightness(primaryColor, LIGHT_ON_SURFACE_LIGHTNESS),
        surfaceVariant = blendColors(lightSurfaceVariantColor, primaryColor, SURFACE_BLEND_RATIO),
        onSurfaceVariant = adjustLightness(primaryColor, LIGHT_ON_SURFACE_VARIANT_LIGHTNESS),
    )

    fun createDarkColorScheme(primaryColor: Color) = darkColorScheme(
        primary = primaryColor,
        onPrimary = adjustLightness(primaryColor, DARK_ON_PRIMARY_LIGHTNESS),
        primaryContainer = primaryColor,
        onPrimaryContainer = lightContrastColor,
        secondary = primaryColor,
        onSecondary = adjustLightness(primaryColor, DARK_ON_SECONDARY_LIGHTNESS),
        secondaryContainer = primaryColor,
        onSecondaryContainer = lightContrastColor,
        tertiary = primaryColor,
        onTertiary = adjustLightness(primaryColor, DARK_ON_TERTIARY_LIGHTNESS),
        tertiaryContainer = primaryColor,
        onTertiaryContainer = lightContrastColor,
        background = blendColors(darkBackgroundColor, primaryColor, BACKGROUND_BLEND_RATIO),
        onBackground = lightContrastColor,
        surface = blendColors(darkSurfaceColor, primaryColor, SURFACE_BLEND_RATIO),
        onSurface = lightContrastColor,
        surfaceVariant = blendColors(
            darkSurfaceVariantColor,
            primaryColor,
            SURFACE_VARIANT_BLEND_RATIO
        ),
        onSurfaceVariant = lightContrastColor,
    )

    private fun blendColors(color1: Color, color2: Color, ratio: Float) =
        Color(ColorUtils.blendARGB(color1.toArgb(), color2.toArgb(), ratio))

    private fun adjustLightness(color: Color, threshold: Float): Color {
        val hsl = convertColorToHSL(color)
        hsl[2] = hsl[2] + threshold
        return convertHSLToColor(hsl)
    }

    private fun convertColorToHSL(color: Color): FloatArray {
        val out = FloatArray(3)
        ColorUtils.colorToHSL(color.toArgb(), out)
        return out
    }

    private fun convertHSLToColor(hsl: FloatArray) =
        Color(ColorUtils.HSLToColor(hsl))
}

