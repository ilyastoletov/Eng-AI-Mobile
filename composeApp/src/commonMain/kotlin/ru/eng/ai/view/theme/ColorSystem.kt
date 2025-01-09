package ru.eng.ai.view.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

val LocalColorSystem = staticCompositionLocalOf { lightColorSystem }

data class ColorSystem(
    val primary: Color,
    val primaryVariant: Color,
    val secondary: Color,
    val dimSecondary: Color,
    val lightSecondary: Color,
    val tertiary: Color,
    val dimTertiary: Color,
    val darkTertiary: Color,
    val background: Color,
    val onBackground: Color
)

val lightColorSystem = ColorSystem(
    primary = Color(0xFFF2EFF6),
    primaryVariant = Color(0xFFE1DEE9),
    secondary = Color(0xFFA09ABC),
    dimSecondary = Color(0xFF31284D),
    lightSecondary = Color(0xFF4B3F6E),
    tertiary = Color(0xFF725DB3),
    dimTertiary = Color(0x6631284D),
    darkTertiary = Color(0x6C5F8D),
    background = Color(0xFF514280),
    onBackground = Color(0xFFBD3761)
)

val darkColorSystem = ColorSystem(
    primary = Color(0x66E1DEE9),
    primaryVariant = Color(0xFFE1DEE9),
    secondary = Color(0xFFF2EFF6),
    dimSecondary = Color(0xFFB6AAC7),
    lightSecondary = Color(0xFF7B6499),
    tertiary = Color(0xFF3E3A50),
    dimTertiary = Color(0xFF1D1D21),
    darkTertiary = Color(0x801D1D21),
    background = Color(0xFF0B0A0B),
    onBackground = Color(0x6631284D)
)