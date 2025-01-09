package ru.eng.ai.view.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier

@Composable
fun EngCustomTheme(
    isDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(
        LocalColorSystem provides if (isDarkTheme) darkColorSystem else lightColorSystem,
        LocalTypography provides engTypography
    ) {
        content.invoke()
    }
}