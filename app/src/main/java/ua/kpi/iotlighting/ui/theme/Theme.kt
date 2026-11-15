package ua.kpi.iotlighting.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

private val AmberPrimary = Color(0xFFFFB300)
private val BlueSecondary = Color(0xFF1565C0)
private val TealTertiary = Color(0xFF00897B)
private val DarkBackground = Color(0xFF121212)
private val LightBackground = Color(0xFFFFF8E1)

private val DarkColors = darkColorScheme(
    primary = AmberPrimary,
    secondary = BlueSecondary,
    tertiary = TealTertiary,
    background = DarkBackground
)

private val LightColors = lightColorScheme(
    primary = BlueSecondary,
    secondary = AmberPrimary,
    tertiary = TealTertiary,
    background = LightBackground
)

private val AppTypography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    titleMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.SemiBold,
        fontSize = 18.sp,
        lineHeight = 24.sp
    ),
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp,
        lineHeight = 28.sp
    )
)

@Composable
fun IotLightingTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        typography = AppTypography,
        content = content
    )
}
