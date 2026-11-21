package com.android.sopkathon_android1.core.designsystem.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf

val LocalSOPKATHONANDROID1Colors = staticCompositionLocalOf { defaultSOPKATHONANDROID1Colors }
val LocalSOPKATHONANDROID1Typography = staticCompositionLocalOf<SOPKATHONANDROID1Typography>{error("No Typography Provided")}
@Composable
fun SOPKATHONANDROID1Theme(
    color: SopkathonA1Colors = defaultSOPKATHONANDROID1Colors,
    typography: SOPKATHONANDROID1Typography = defaultSOPKATHONANDROID1Typography,
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(
        LocalSOPKATHONANDROID1Colors provides color,
        LocalSOPKATHONANDROID1Typography provides typography
    ) {
        content()
    }
}

object SOPKATHONANDROID1Theme {
    val colors: SopkathonA1Colors
        @Composable
        get() = LocalSOPKATHONANDROID1Colors.current
    val typography: SOPKATHONANDROID1Typography
        @Composable
        get() = LocalSOPKATHONANDROID1Typography.current
}

