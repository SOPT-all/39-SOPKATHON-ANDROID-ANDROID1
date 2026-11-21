package com.android.sopkathon_android1.core.designsystem.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.android.sopkathon_android1.R

private val pretendard = FontFamily(
    Font(R.font.pretendard_bold, FontWeight.Bold),
    Font(R.font.pretendard_semibold, FontWeight.SemiBold),
    Font(R.font.pretendard_medium, FontWeight.Medium),
    Font(R.font.pretendard_regular, FontWeight.Normal),
)

@Immutable
data class NolItTypography(
    // Title
    val h1Sb: TextStyle,
    val h2Sb: TextStyle,
    val h3Sb: TextStyle,
    // Body
    val body1Md: TextStyle,
    val body1Sb: TextStyle,
    val body2Sb: TextStyle,
    val body2Md: TextStyle,
    // Caption
    val caption1Md: TextStyle,
    val caption1B: TextStyle,
    val caption2Rg: TextStyle,
    val caption3Rg: TextStyle,
)

val defaultNolItTypography: NolItTypography
    @Composable get() {
        val baseStyle = TextStyle(
            fontFamily = pretendard,
            color = defaultNolItColors.textPrimary,
            letterSpacing = (-0.02).em,
        )
        return NolItTypography(
            h1Sb = baseStyle.copy(
                fontWeight = FontWeight.SemiBold, fontSize = 22.sp, lineHeight = 1.5.em
            ),
            h2Sb = baseStyle.copy(
                fontWeight = FontWeight.SemiBold, fontSize = 20.sp, lineHeight = 1.5.em
            ),
            h3Sb = baseStyle.copy(
                fontWeight = FontWeight.SemiBold, fontSize = 18.sp, lineHeight = 1.5.em
            ),
            body1Md = baseStyle.copy(
                fontWeight = FontWeight.Medium, fontSize = 16.sp, lineHeight = 1.5.em
            ),
            body1Sb = baseStyle.copy(
                fontWeight = FontWeight.SemiBold, fontSize = 16.sp, lineHeight = 1.5.em
            ),
            body2Sb = baseStyle.copy(
                fontWeight = FontWeight.SemiBold, fontSize = 14.sp, lineHeight = 20.sp
            ),
            body2Md = baseStyle.copy(
                fontWeight = FontWeight.Normal, fontSize = 14.sp, lineHeight = 20.sp
            ),
            caption1Md = baseStyle.copy(
                fontWeight = FontWeight.Medium, fontSize = 12.sp, lineHeight = 17.sp
            ),
            caption1B = baseStyle.copy(
                fontWeight = FontWeight.Bold, fontSize = 12.sp, lineHeight = 17.sp
            ),
            caption2Rg = baseStyle.copy(
                fontWeight = FontWeight.Normal, fontSize = 10.sp, lineHeight = 17.sp
            ),
            caption3Rg = baseStyle.copy(
                fontWeight = FontWeight.Normal, fontSize = 8.sp, lineHeight = 10.sp
            ),
        )
    }
