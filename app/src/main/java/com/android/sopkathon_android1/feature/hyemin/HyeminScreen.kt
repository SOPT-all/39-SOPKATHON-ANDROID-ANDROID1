package com.android.sopkathon_android1.feature.hyemin

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import kotlinx.serialization.Serializable

@Serializable
data object HyeminRoute

@Composable
fun HyeminScreen() {
    Text(text = "혜민")
}
