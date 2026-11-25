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
    primary = MintEmeraldPrimary,
    onPrimary = Color(0xFF021409),
    primaryContainer = PitchMintContainer,
    onPrimaryContainer = PitchMintHighlight,
    secondary = PitchMintHighlight,
    onSecondary = Color(0xFF021409),
    secondaryContainer = Color(0xFF103320),
    onSecondaryContainer = Color(0xFFB9FCD1),
    tertiary = AmberTimer,
    background = StadiumDeepBlack,
    onBackground = TextWhitePrimary,
    surface = PitchCardSurface,
    onSurface = TextWhitePrimary,
    surfaceVariant = PitchCardVariant,
    onSurfaceVariant = TextSilverSecondary,
    outline = PitchBorderGreen,
    error = AlertCoral
)

private val FootballLightColorScheme = lightColorScheme(
    primary = LightPrimaryEmerald,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFD1FAE5),
    onPrimaryContainer = Color(0xFF043A1C),
    secondary = TurfAccentTeal,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFCCFBF1),
    onSecondaryContainer = Color(0xFF064E3B),
    tertiary = AmberTimer,
    background = LightPitchBg,
    onBackground = LightBlackInk,
    surface = LightCardSurface,
    onSurface = LightBlackInk,
    surfaceVariant = LightSurfaceVariant,
    onSurfaceVariant = LightMutedInk,
    outline = LightBorderGreen,
    error = AlertCoral
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
                @Suppress("DEPRECATION")
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
