package br.app.saidabet.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val SdGreen = Color(0xFF2ECC71)
val SdGreenDark = Color(0xFF1B9E4B)
val SdBg = Color(0xFF0B1F14)
val SdSurface = Color(0xFF132A1C)
val SdSurface2 = Color(0xFF1A3A28)
val SdDanger = Color(0xFFE74C3C)
val SdText = Color(0xFFF0FFF4)
val SdMuted = Color(0xFF9BB5A4)

private val ColorScheme = darkColorScheme(
    primary = SdGreen,
    onPrimary = Color(0xFF003216),
    secondary = SdGreenDark,
    background = SdBg,
    surface = SdSurface,
    onBackground = SdText,
    onSurface = SdText,
    error = SdDanger
)

private val Typo = Typography(
    displayLarge = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Bold,
        fontSize = 48.sp,
        color = SdText
    ),
    headlineMedium = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.SemiBold,
        fontSize = 24.sp,
        color = SdText
    ),
    titleLarge = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.SemiBold,
        fontSize = 20.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        color = SdText
    ),
    bodyMedium = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 14.sp,
        color = SdMuted
    ),
    labelLarge = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp
    )
)

@Composable
fun SaiDaBetTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = ColorScheme,
        typography = Typo,
        content = content
    )
}
