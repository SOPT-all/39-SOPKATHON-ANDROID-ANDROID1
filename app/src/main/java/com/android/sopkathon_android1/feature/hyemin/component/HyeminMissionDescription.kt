package com.android.sopkathon_android1.feature.hyemin.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.android.sopkathon_android1.core.designsystem.theme.NolItTheme

@Composable
fun HyeminMissionDescription(
    description: String,
    instructions: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 123.dp)
            .background(NolItTheme.colors.primarySoft, RoundedCornerShape(8.dp))
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp),
    ) {
        Text(text = "미션 상세설명", style = NolItTheme.typography.body2Sb)
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            if (description.isNotBlank()) {
                Text(
                    text = description,
                    style = NolItTheme.typography.caption2Rg,
                    color = NolItTheme.colors.neutral800,
                )
            }
            if (instructions.isNotBlank()) {
                Text(
                    text = instructions,
                    style = NolItTheme.typography.caption2Rg,
                    color = NolItTheme.colors.neutral800,
                )
            }
        }
    }
}
