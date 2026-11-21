package com.android.sopkathon_android1.core.designsystem.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// TODO: res/font에 Pretendard(.ttf) 추가 후 아래 주석 풀고 FontFamily.Default 줄 삭제
// private val pretendard = FontFamily(
//     Font(R.font.pretendard_bold, FontWeight.Bold),
//     Font(R.font.pretendard_semibold, FontWeight.SemiBold),
//     Font(R.font.pretendard_medium, FontWeight.Medium),
//     Font(R.font.pretendard_regular, FontWeight.Normal),
// )
private val pretendard = FontFamily.Default

@Immutable
data class SOPKATHONANDROID1Typography(
    // TODO: 피그마 타이포 스타일로 교체·추가 (예: head1B20, body1M14 ...)
    val sample: TextStyle,
)

val defaultSOPKATHONANDROID1Typography = SOPKATHONANDROID1Typography(
    // TODO: 임시값
    sample = TextStyle(
        fontFamily = pretendard,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
    ),
)
