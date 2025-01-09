package ru.eng.ai.view.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import engai.composeapp.generated.resources.Res
import engai.composeapp.generated.resources.wix_bold
import engai.composeapp.generated.resources.wix_medium
import engai.composeapp.generated.resources.wix_semi_bold
import org.jetbrains.compose.resources.Font

val LocalTypography = staticCompositionLocalOf { Typography() }

data class Typography(
    val bold32: TextStyle = TextStyle(),
    val bold24: TextStyle = TextStyle(),
    val bold20: TextStyle = TextStyle(),
    val bold14: TextStyle = TextStyle(),
    val semiBold16: TextStyle = TextStyle(),
    val semiBold12: TextStyle = TextStyle(),
    val medium16: TextStyle = TextStyle(),
    val medium14: TextStyle = TextStyle(),
    val medium12: TextStyle = TextStyle(),
)

val engTypography: Typography
    @Composable
    get() = Typography(
        bold32 = TextStyle(
            fontSize = 32.sp,
            lineHeight = 40.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily(Font(Res.font.wix_bold))
        ),
        bold24 = TextStyle(
            fontSize = 24.sp,
            lineHeight = 30.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily(Font(Res.font.wix_bold))
        ),
        bold20 = TextStyle(
            fontSize = 20.sp,
            lineHeight = 25.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily(Font(Res.font.wix_bold))
        ),
        bold14 = TextStyle(
            fontSize = 14.sp,
            lineHeight = 17.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily(Font(Res.font.wix_bold))
        ),
        semiBold16 = TextStyle(
            fontSize = 16.sp,
            lineHeight = 20.sp,
            fontWeight = FontWeight.SemiBold,
            fontFamily = FontFamily(Font(Res.font.wix_semi_bold))
        ),
        semiBold12 = TextStyle(
            fontSize = 12.sp,
            lineHeight = 15.sp,
            fontWeight = FontWeight.SemiBold,
            fontFamily = FontFamily(Font(Res.font.wix_semi_bold))
        ),
        medium16 = TextStyle(
            fontSize = 16.sp,
            lineHeight = 20.sp,
            fontWeight = FontWeight.Medium,
            fontFamily = FontFamily(Font(Res.font.wix_medium))
        ),
        medium14 = TextStyle(
            fontSize = 14.sp,
            lineHeight = 17.sp,
            fontWeight = FontWeight.Medium,
            fontFamily = FontFamily(Font(Res.font.wix_medium))
        ),
        medium12 = TextStyle(
            fontSize = 12.sp,
            lineHeight = 15.sp,
            fontWeight = FontWeight.Medium,
            fontFamily = FontFamily(Font(Res.font.wix_medium))
        )
    )