package com.bocado.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// Colores de BOCADO según Figma
val BocadoOrange = Color(0xFFFF6B42)
val BocadoBlack = Color(0xFF1A1A1A)
val BocadoGray = Color(0xFF808080)
val BocadoLightGray = Color(0xFFF5F5F5)
val BocadoGreen = Color(0xFF66BB6A)
val BocadoRed = Color(0xFFE63946)
val BocadoWhite = Color(0xFFFFFFFF)

private val LightColorScheme = lightColorScheme(
    primary = BocadoOrange,
    secondary = BocadoGray,
    tertiary = BocadoGreen,
    background = BocadoWhite,
    surface = BocadoLightGray,
    onPrimary = BocadoWhite,
    onSecondary = BocadoWhite,
    onBackground = BocadoBlack,
    onSurface = BocadoBlack,
    error = BocadoRed
)

private val DarkColorScheme = darkColorScheme(
    primary = BocadoOrange,
    secondary = Color(0xFFA9A9A9),
    tertiary = BocadoGreen,
    background = Color(0xFF121212),
    surface = Color(0xFF1E1E1E),
    onPrimary = BocadoBlack,
    onSecondary = BocadoBlack,
    onBackground = BocadoWhite,
    onSurface = BocadoWhite,
    error = BocadoRed
)

val BocadoTypography = Typography(
    headlineLarge = TextStyle(
        fontSize = 32.sp,
        fontWeight = FontWeight.Bold,
        color = BocadoBlack
    ),
    headlineMedium = TextStyle(
        fontSize = 28.sp,
        fontWeight = FontWeight.Bold,
        color = BocadoBlack
    ),
    headlineSmall = TextStyle(
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold,
        color = BocadoBlack
    ),
    titleLarge = TextStyle(
        fontSize = 20.sp,
        fontWeight = FontWeight.SemiBold,
        color = BocadoBlack
    ),
    titleMedium = TextStyle(
        fontSize = 18.sp,
        fontWeight = FontWeight.SemiBold,
        color = BocadoBlack
    ),
    titleSmall = TextStyle(
        fontSize = 16.sp,
        fontWeight = FontWeight.SemiBold,
        color = BocadoBlack
    ),
    bodyLarge = TextStyle(
        fontSize = 16.sp,
        fontWeight = FontWeight.Normal,
        color = BocadoBlack
    ),
    bodyMedium = TextStyle(
        fontSize = 14.sp,
        fontWeight = FontWeight.Normal,
        color = BocadoGray
    ),
    bodySmall = TextStyle(
        fontSize = 12.sp,
        fontWeight = FontWeight.Normal,
        color = BocadoGray
    ),
    labelLarge = TextStyle(
        fontSize = 14.sp,
        fontWeight = FontWeight.Medium,
        color = BocadoOrange
    ),
    labelMedium = TextStyle(
        fontSize = 12.sp,
        fontWeight = FontWeight.Medium,
        color = BocadoOrange
    ),
    labelSmall = TextStyle(
        fontSize = 10.sp,
        fontWeight = FontWeight.Medium,
        color = BocadoOrange
    )
)

@Composable
fun BocadoTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = BocadoTypography,
        content = content
    )
}
