package com.android.sopkathon_android1.core.designsystem.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf

val LocalNolItColors = staticCompositionLocalOf { defaultNolItColors }
val LocalNolItTypography =
    staticCompositionLocalOf<NolItTypography> { error("No Typography Provided") }

@Composable
fun NolItTheme(
    color: NolItColors = defaultNolItColors,
    typography: NolItTypography = defaultNolItTypography,
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(
        LocalNolItColors provides color, LocalNolItTypography provides typography
    ) {
        content()
    }
}

object NolItTheme {
    val colors: NolItColors
        @Composable get() = LocalNolItColors.current
    val typography: NolItTypography
        @Composable get() = LocalNolItTypography.current
}

