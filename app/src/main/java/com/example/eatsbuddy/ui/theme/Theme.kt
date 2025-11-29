package com.example.eatsbuddy.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = GreenLight,
    onPrimary = White,
    primaryContainer = GreenDark,
    onPrimaryContainer = GreenAccent,
    secondary = GreenAccent,
    onSecondary = Black,
    secondaryContainer = GreenDark,
    onSecondaryContainer = GreenLight,
    tertiary = GreenContainer,
    background = Color(0xFF121212),
    onBackground = White,
    surface = Color(0xFF1E1E1E),
    onSurface = White,
    surfaceVariant = Color(0xFF2D2D2D),
    onSurfaceVariant = LightGray
)

private val LightColorScheme = lightColorScheme(
    primary = GreenPrimary,
    onPrimary = White,
    primaryContainer = GreenSurface,
    onPrimaryContainer = GreenDark,
    secondary = GreenLight,
    onSecondary = White,
    secondaryContainer = GreenContainer,
    onSecondaryContainer = GreenDark,
    tertiary = GreenAccent,
    background = White,
    onBackground = Black,
    surface = White,
    onSurface = Black,
    surfaceVariant = OffWhite,
    onSurfaceVariant = DarkGray
)

@Composable
fun EatsBuddyTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            @Suppress("DEPRECATION")
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}