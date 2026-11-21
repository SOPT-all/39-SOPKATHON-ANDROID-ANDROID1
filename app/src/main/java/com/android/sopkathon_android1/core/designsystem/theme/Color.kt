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
)
