package com.texturas.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*;
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp

private val LightColors = lightColorScheme(
    primary = Color(0xFF6750A4),
    secondary = Color(0xFF625B71),
    tertiary = Color(0xFF7D5260),
    surface = Color(0xFFFFFBFE),
    background = Color(0xFFFFFBFE),
    error = Color(0xFFB3261E),
    onPrimary = Color(0xFFFFFFFF),
    onSecondary = Color(0xFFFFFFFF),
    onTertiary = Color(0xFFFFFFFF),
    onSurface = Color(0xFF1D1B20),
    onBackground = Color(0xFF1D1B20),
    outline = Color(0xFF79747E),
    outlineVariant = Color(0xFFCAC4D0),
    inverseOnSurface = Color(0xFFF4EFF4),
    inverseSurface = Color(0xFF313033),
    inversePrimary = Color(0xFFD0BCFF),
    surfaceTint = Color(0xFF6750A4)
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFFD0BCFF),
    secondary = Color(0xFFCCC2DC),
    tertiary = Color(0xFFEFB8C8),
    surface = Color(0xFF1D1B20),
    background = Color(0xFF1D1B20),
    error = Color(0xFFCF6679),
    onPrimary = Color(0xFF381E72),
    onSecondary = Color(0xFF332D41),
    onTertiary = Color(0xFF492532),
    onSurface = Color(0xFFE6E1E5),
    onBackground = Color(0xFFE6E1E5),
    outline = Color(0xFF938F99),
    outlineVariant = Color(0xFF4A454F),
    inverseOnSurface = Color(0xFF1D1B20),
    inverseSurface = Color(0xFFE6E1E5),
    inversePrimary = Color(0xFF6750A4),
    surfaceTint = Color(0xFFD0BCFF)
)

@Composable
fun TexturasTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColors else LightColors

    MaterialTheme(
        colorScheme = colors,
        typography = Typography,
        content = content
    )
}

private val Typography = Typography(
    // Use default Material 3 typography as a base, but we can adjust if needed
    bodyLarge = TextStyle(fontWeight = FontWeight.Normal, fontSize = 16.sp),
    bodyMedium = TextStyle(fontWeight = FontWeight.Normal, fontSize = 14.sp),
    bodySmall = TextStyle(fontWeight = FontWeight.Normal, fontSize = 12.sp),
    titleLarge = TextStyle(fontWeight = FontWeight.Bold, fontSize = 22.sp),
    titleMedium = TextStyle(fontWeight = FontWeight.Bold, fontSize = 18.sp),
    titleSmall = TextStyle(fontWeight = FontWeight.Bold, fontSize = 16.sp),
    labelLarge = TextStyle(fontWeight = FontWeight.Medium, fontSize = 14.sp),
    labelMedium = TextStyle(fontWeight = FontWeight.Medium, fontSize = 12.sp),
    labelSmall = TextStyle(fontWeight = FontWeight.Medium, fontSize = 11.sp)
)

// Extension functions for the theme
val ColorScheme.customPrimary: Color
    get() = primary

val ColorScheme.customSurface: Color
    get() = surface

val ColorScheme.customOnSurface: Color
    get() = onSurface

val ColorScheme.customOutline: Color
    get() = outline
