package ru.eng.ai.view.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import ru.eng.ai.tool.handleThemeChange

@Composable
fun EngCustomTheme(content: @Composable () -> Unit) {
    val isDarkTheme = isSystemInDarkTheme()
    val colorSystem = remember(isDarkTheme) {
        if (isDarkTheme) darkColorSystem else lightColorSystem
    }

    LaunchedEffect(Unit) {
        handleThemeChange(isDarkTheme)
    }

    CompositionLocalProvider(
        LocalColorSystem provides colorSystem,
        LocalTypography provides engTypography
    ) {
        content.invoke()
    }
}