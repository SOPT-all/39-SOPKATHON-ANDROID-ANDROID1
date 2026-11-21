package com.android.sopkathon_android1.core.designsystem.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

// Brand
private val primary = Color(0xFFD7FF3F)
private val primarySoft = Color(0xFFF1FFC4)
private val secondary = Color(0xFF7048F5)

// Neutral
private val background = Color(0xFFFFFFFF)
private val surface = Color(0xFFF5F5F3)
private val textPrimary = Color(0xFF202020)
private val textSecondary = Color(0xFF737373)
private val border = Color(0xFFDDDDDD)

// Accent
private val red = Color(0xFFFF1300)

// Neutral
private val neutral25 = Color(0xFFFCFCFD)
private val neutral50 = Color(0xFFF8F9FB)
private val neutral100 = Color(0xFFF1F3F6)
private val neutral150 = Color(0xFFE9ECF0)
private val neutral200 = Color(0xFFE0E4EA)
private val neutral250 = Color(0xFFD5DAE2)
private val neutral300 = Color(0xFFC8CFD9)
private val neutral400 = Color(0xFF99A3B3)
private val neutral500 = Color(0xFF707C8F)
private val neutral600 = Color(0xFF536075)
private val neutral700 = Color(0xFF3D485B)
private val neutral800 = Color(0xFF293345)
private val neutral900 = Color(0xFF192232)
private val neutral950 = Color(0xFF101724)
private val neutral975 = Color(0xFF090E17)

@Immutable
data class NolItColors(
    val primary: Color,
    val primarySoft: Color,
    val secondary: Color,
    val background: Color,
    val surface: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val border: Color,
    val red: Color,
    val neutral25: Color,
    val neutral50: Color,
    val neutral100: Color,
    val neutral150: Color,
    val neutral200: Color,
    val neutral250: Color,
    val neutral300: Color,
    val neutral400: Color,
    val neutral500: Color,
    val neutral600: Color,
    val neutral700: Color,
    val neutral800: Color,
    val neutral900: Color,
    val neutral950: Color,
    val neutral975: Color,
)

val defaultNolItColors = NolItColors(
    primary = primary,
    primarySoft = primarySoft,
    secondary = secondary,
    background = background,
    surface = surface,
    textPrimary = textPrimary,
    textSecondary = textSecondary,
    border = border,
    red = red,
    neutral25 = neutral25,
    neutral50 = neutral50,
    neutral100 = neutral100,
    neutral150 = neutral150,
    neutral200 = neutral200,
    neutral250 = neutral250,
    neutral300 = neutral300,
    neutral400 = neutral400,
    neutral500 = neutral500,
    neutral600 = neutral600,
    neutral700 = neutral700,
    neutral800 = neutral800,
    neutral900 = neutral900,
    neutral950 = neutral950,
    neutral975 = neutral975,
)
