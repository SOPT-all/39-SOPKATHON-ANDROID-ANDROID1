package com.android.sopkathon_android1.feature.seungyeon

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.android.sopkathon_android1.core.designsystem.theme.NolItTheme

@Composable
fun SeungyeonRoute() {
    SeungyeonScreen()
}

@Composable
fun SeungyeonScreen() {
    Text(text = "승연")
}

@Preview(showBackground = true)
@Composable
private fun SeungyeonScreenPreview() {
    NolItTheme { SeungyeonScreen() }
}
