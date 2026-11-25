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
    onPrimary = StadiumDeepBlack,
    primaryContainer = PitchMintContainer,
    onPrimaryContainer = Color(0xFFA7F3D0),
    secondary = PitchMintHighlight,
    onSecondary = StadiumDeepBlack,
    secondaryContainer = Color(0xFF113829),
    onSecondaryContainer = Color(0xFFD1FAE5),
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
    onPrimaryContainer = StadiumForestDark,
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
    darkTheme: Boolean = false,
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
