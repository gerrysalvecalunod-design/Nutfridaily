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

private val BadmintonDarkColorScheme = darkColorScheme(
    primary = ElectricCyanPrimary,
    onPrimary = ArenaNavyBlack,
    primaryContainer = CourtCyanContainer,
    onPrimaryContainer = Color(0xFFBAE6FD),
    secondary = SkyBlueHighlight,
    onSecondary = ArenaNavyBlack,
    secondaryContainer = Color(0xFF152C52),
    onSecondaryContainer = Color(0xFFE0F2FE),
    tertiary = AmberTimer,
    background = ArenaNavyBlack,
    onBackground = TextWhitePrimary,
    surface = CourtCardSurface,
    onSurface = TextWhitePrimary,
    surfaceVariant = CourtCardVariant,
    onSurfaceVariant = TextSilverSecondary,
    outline = CourtBorderBlue,
    error = AlertCoral
)

private val BadmintonLightColorScheme = lightColorScheme(
    primary = LightPrimaryNavy,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFD6E9FF),
    onPrimaryContainer = ArenaDeepNavy,
    secondary = CobaltAccent,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFE0F2FE),
    onSecondaryContainer = Color(0xFF075985),
    tertiary = AmberTimer,
    background = LightCourtBg,
    onBackground = LightBlackInk,
    surface = LightCardSurface,
    onSurface = LightBlackInk,
    surfaceVariant = LightSurfaceVariant,
    onSurfaceVariant = LightMutedInk,
    outline = LightBorderBlue,
    error = AlertCoral
)

@Composable
fun BadmintonProTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) BadmintonDarkColorScheme else BadmintonLightColorScheme

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
