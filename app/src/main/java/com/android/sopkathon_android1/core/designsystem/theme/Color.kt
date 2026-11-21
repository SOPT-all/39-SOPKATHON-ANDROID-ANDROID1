package com.android.sopkathon_android1.core.designsystem.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

private val black = Color(0xFF000000)
private val white = Color(0xFFFFFFFF)

@Immutable
data class SopkathonA1Colors(
    val black: Color,
    val white: Color,
)

val defaultSOPKATHONANDROID1Colors = SopkathonA1Colors(
    black = black,
    white = white,
)
