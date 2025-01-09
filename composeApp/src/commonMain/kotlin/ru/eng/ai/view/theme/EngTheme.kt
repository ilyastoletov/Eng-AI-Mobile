package ru.eng.ai.view.theme

import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.Composable

object EngTheme {

    val colors: ColorSystem
        @ReadOnlyComposable
        @Composable
        get() = LocalColorSystem.current

    val typography: Typography
        @ReadOnlyComposable
        @Composable
        get() = LocalTypography.current

}