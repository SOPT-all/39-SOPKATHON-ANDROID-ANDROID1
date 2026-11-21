package com.android.sopkathon_android1.feature.heejin

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import kotlinx.serialization.Serializable

@Serializable
data object HeejinRoute

@Composable
fun HeejinScreen() {
    Text(text = "희진")
}
