package com.android.sopkathon_android1.core.designsystem.component.tag

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.android.sopkathon_android1.core.designsystem.theme.NolItTheme

@Composable
fun InfoTag(
    text: String,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .heightIn(min = 28.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(NolItTheme.colors.secondary)
            .padding(horizontal = 8.dp, vertical = 5.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            style = NolItTheme.typography.caption1Md,
            color = NolItTheme.colors.background,
            textAlign = TextAlign.Center,
        )
    }
}

@Preview(name = "InfoTag", showBackground = true)
@Composable
private fun InfoTagPreview() {
    NolItTheme {
        InfoTag(text = "지우 · 고등학생")
    }
}
