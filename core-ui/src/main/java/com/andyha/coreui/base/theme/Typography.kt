package com.andyha.coreui.base.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDirection
import com.andyha.coreresource.R

class BaseFont(
    val fontName: String,
    val fontFamily: () -> FontFamily,
) {
    companion object {
        fun fromValue(fontName: String, fontFamily: FontFamily) = BaseFont(
            fontName = fontName,
            fontFamily = { fontFamily }
        )
    }
}

object BuiltinFonts {
    val Roboto = BaseFont.fromValue(
        fontName = "Roboto",
        fontFamily = FontFamily(
            Font(R.font.roboto_regular, FontWeight.Normal),
            Font(R.font.roboto_bold, FontWeight.Bold)
        ),
    )

    val Helvetica = BaseFont.fromValue(
        fontName = "Helvetica",
        fontFamily = FontFamily(
            Font(R.font.helvetica, FontWeight.Normal),
            Font(R.font.helvetica_bold, FontWeight.Bold),
        ),
    )

    val Montserrat = BaseFont.fromValue(
        fontName = "Montserrat",
        fontFamily = FontFamily(
            Font(R.font.montserrat_regular, FontWeight.Normal),
            Font(R.font.montserrat_bold, FontWeight.Bold)
        ),
    )
}

object BaseTypography {
    val defaultFont = BuiltinFonts.Roboto

    val all = mapOf(
        BuiltinFonts.Roboto.fontName to BuiltinFonts.Roboto,
        BuiltinFonts.Helvetica.fontName to BuiltinFonts.Helvetica,
        BuiltinFonts.Montserrat.fontName to BuiltinFonts.Montserrat,
    )

    fun resolveFont(name: String?) = all[name] ?: defaultFont

    fun toTypography(font: BaseFont, textDirection: TextDirection = TextDirection.Ltr): Typography {
        val fontFamily = font.fontFamily()
        return Typography().run {
            copy(
                displayLarge = displayLarge.copy(
                    fontFamily = fontFamily,
                    textDirection = textDirection,
                ),
                displayMedium = displayMedium.copy(
                    fontFamily = fontFamily,
                    textDirection = textDirection,
                ),
                displaySmall = displaySmall.copy(
                    fontFamily = fontFamily,
                    textDirection = textDirection,
                ),
                headlineLarge = headlineLarge.copy(
                    fontFamily = fontFamily,
                    textDirection = textDirection,
                ),
                headlineMedium = headlineMedium.copy(
                    fontFamily = fontFamily,
                    textDirection = textDirection,
                ),
                headlineSmall = headlineSmall.copy(
                    fontFamily = fontFamily,
                    textDirection = textDirection,
                ),
                titleLarge = titleLarge.copy(
                    fontFamily = fontFamily,
                    textDirection = textDirection,
                ),
                titleMedium = titleMedium.copy(
                    fontFamily = fontFamily,
                    textDirection = textDirection,
                ),
                titleSmall = titleSmall.copy(
                    fontFamily = fontFamily,
                    textDirection = textDirection,
                ),
                bodyLarge = bodyLarge.copy(
                    fontFamily = fontFamily,
                    textDirection = textDirection,
                ),
                bodyMedium = bodyMedium.copy(
                    fontFamily = fontFamily,
                    textDirection = textDirection,
                ),
                bodySmall = bodySmall.copy(
                    fontFamily = fontFamily,
                    textDirection = textDirection,
                ),
                labelLarge = labelLarge.copy(
                    fontFamily = fontFamily,
                    textDirection = textDirection,
                ),
                labelMedium = labelMedium.copy(
                    fontFamily = fontFamily,
                    textDirection = textDirection,
                ),
                labelSmall = labelSmall.copy(
                    fontFamily = fontFamily,
                    textDirection = textDirection,
                ),
            )
        }
    }
}
