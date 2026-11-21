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
data class SOPKATHONANDROID1Typography(
    // Title
    val h1Sb: TextStyle,
    val h2Sb: TextStyle,
    val h3Sb: TextStyle,
    // Body
    val body1Md: TextStyle,
    val body1Sb: TextStyle,
    val body2Sb: TextStyle,
    val body2Rg: TextStyle,
    // Caption
    val caption1Md: TextStyle,
    val caption1B: TextStyle,
    val caption2Rg: TextStyle,
    val caption3Rg: TextStyle,
) {
    val groupedTypography: List<Pair<String, List<Pair<String, TextStyle>>>>
        get() = listOf(
            "Title" to listOf(
                "h1-sb" to h1Sb,
                "h2-sb" to h2Sb,
                "h3-sb" to h3Sb,
            ),
            "Body" to listOf(
                "body1-md" to body1Md,
                "body1-sb" to body1Sb,
                "body2-sb" to body2Sb,
                "body2-rg" to body2Rg,
            ),
            "Caption" to listOf(
                "caption1-md" to caption1Md,
                "caption1-b" to caption1B,
                "caption2-rg" to caption2Rg,
                "caption3-rg" to caption3Rg,
            ),
        )
}

val defaultSOPKATHONANDROID1Typography: SOPKATHONANDROID1Typography
    @Composable get() {
        val baseStyle = TextStyle(
            fontFamily = pretendard,
            color = defaultSOPKATHONANDROID1Colors.black,
        )
        return SOPKATHONANDROID1Typography(
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
            body2Rg = baseStyle.copy(
                fontWeight = FontWeight.Normal, fontSize = 14.sp, lineHeight = 20.sp
            ),
            caption1Md = baseStyle.copy(
                fontWeight = FontWeight.Medium, fontSize = 12.sp, lineHeight = 18.sp
            ),
            caption1B = baseStyle.copy(
                fontWeight = FontWeight.Bold, fontSize = 12.sp, lineHeight = 18.sp
            ),
            caption2Rg = baseStyle.copy(
                fontWeight = FontWeight.Normal, fontSize = 10.sp, lineHeight = 14.sp
            ),
            caption3Rg = baseStyle.copy(
                fontWeight = FontWeight.Normal, fontSize = 8.sp, lineHeight = 10.sp
            ),
        )
    }
