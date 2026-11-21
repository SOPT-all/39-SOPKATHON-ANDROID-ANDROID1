package com.android.sopkathon_android1.feature.seungyeon

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import kotlinx.serialization.Serializable

@Serializable
data object SeungyeonRoute

@Composable
fun SeungyeonScreen() {
    Text(text = "승연")
}
