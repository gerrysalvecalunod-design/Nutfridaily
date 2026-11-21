package com.example.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val FootballDarkColorScheme = darkColorScheme(
    primary = SpotlightGold,
    onPrimary = PitchBlack,
    primaryContainer = TurfDeepGreen,
    onPrimaryContainer = SpotlightGold,
    secondary = CrimsonOpponent,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFF3A1414),
    onSecondaryContainer = Color(0xFFFECACA),
    tertiary = AmberGlow,
    background = PitchBlack,
    onBackground = TextPrimaryDark,
    surface = CardDarkSurface,
    onSurface = TextPrimaryDark,
    surfaceVariant = CardDarkVariant,
    onSurfaceVariant = TextSecondaryDark,
    outline = BorderDarkGreen,
    error = CrimsonOpponent
)

private val FootballLightColorScheme = lightColorScheme(
    primary = GoldCrimsonPrimary,
    onPrimary = Color.White,
    primaryContainer = SurfaceVariantLight,
    onPrimaryContainer = DeepEspressoText,
    secondary = JerseyCrimson,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFFEE2E2),
    onSecondaryContainer = Color(0xFF7F1D1D),
    tertiary = AmberTimer,
    background = BackgroundLight,
    onBackground = DeepEspressoText,
    surface = SurfaceLight,
    onSurface = DeepEspressoText,
    surfaceVariant = SurfaceVariantLight,
    onSurfaceVariant = TextSecondaryLight,
    outline = Color(0xFFE2C9AE),
    error = CrimsonOpponent
)

@Composable
fun FootballProTheme(
    darkTheme: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) FootballDarkColorScheme else FootballLightColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window
            if (window != null) {
                window.statusBarColor = colorScheme.background.toArgb()
                WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
