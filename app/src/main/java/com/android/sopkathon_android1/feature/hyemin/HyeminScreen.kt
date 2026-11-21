package com.android.sopkathon_android1.feature.hyemin

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.android.sopkathon_android1.core.designsystem.theme.NolItTheme

@Composable
fun HyeminRoute() {
    HyeminScreen()
}

@Composable
fun HyeminScreen() {
    Text(text = "혜민")
}

@Preview(showBackground = true)
@Composable
private fun HyeminScreenPreview() {
    NolItTheme { HyeminScreen() }
}
