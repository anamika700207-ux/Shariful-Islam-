package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.data.model.AppThemeOption
import com.example.data.model.DefaultData

@Composable
fun MyApplicationTheme(
    selectedThemeKey: String = "CYBER_RED",
    content: @Composable () -> Unit
) {
    val currentTheme = DefaultData.themes.find { it.key == selectedThemeKey } ?: DefaultData.themes.first()
    val primary = Color(currentTheme.primaryColor)
    val secondary = Color(currentTheme.glowColor)
    val surface = Color(currentTheme.surfaceColor)

    val colorScheme = darkColorScheme(
        primary = primary,
        secondary = secondary,
        background = CyberBackground,
        surface = surface,
        surfaceVariant = CyberSurfaceVariant,
        onPrimary = Color.White,
        onSecondary = Color.White,
        onBackground = TextWhite,
        onSurface = TextWhite,
        outline = Color(currentTheme.strokeColor)
    )

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
