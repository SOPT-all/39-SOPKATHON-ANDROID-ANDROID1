package com.android.sopkathon_android1.feature.heejin

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.android.sopkathon_android1.core.designsystem.theme.NolItTheme

@Composable
fun HeejinRoute() {
    HeejinScreen()
}

@Composable
fun HeejinScreen() {
    Text(text = "희진")
}

@Preview(showBackground = true)
@Composable
private fun HeejinScreenPreview() {
    NolItTheme { HeejinScreen() }
}
