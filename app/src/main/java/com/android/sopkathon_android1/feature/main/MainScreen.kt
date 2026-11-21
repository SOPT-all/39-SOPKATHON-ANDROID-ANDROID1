package com.android.sopkathon_android1.feature.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.serialization.Serializable

@Serializable
data object MainRoute

@Composable
fun MainScreen(
    onEunjiClick: () -> Unit,
    onHyeminClick: () -> Unit,
    onHeejinClick: () -> Unit,
    onSeungyeonClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically),
    ) {
        Button(onClick = onEunjiClick, modifier = Modifier.fillMaxWidth()) {
            Text(text = "은지")
        }
        Button(onClick = onHyeminClick, modifier = Modifier.fillMaxWidth()) {
            Text(text = "혜민")
        }
        Button(onClick = onHeejinClick, modifier = Modifier.fillMaxWidth()) {
            Text(text = "희진")
        }
        Button(onClick = onSeungyeonClick, modifier = Modifier.fillMaxWidth()) {
            Text(text = "승연")
        }
    }
}
