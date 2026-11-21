package com.android.sopkathon_android1.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.android.sopkathon_android1.core.designsystem.extension.noRippleClickable
import com.android.sopkathon_android1.core.designsystem.theme.NolItTheme

@Composable
fun NolItHomeDailyPickCard(
    title: String,
    imageUrl: String?,
    authorName: String,
    studentType: String,
    durationText: String,
    categoryText: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val authorText = listOf(authorName, studentType)
        .filter { it.isNotBlank() }
        .joinToString(" · ")

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(30.dp))
            .background(NolItTheme.colors.surface)
            .noRippleClickable(role = Role.Button, onClick = onClick),
    ) {
        BaseAsyncImage(
            imgUrl = imageUrl,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.matchParentSize(),
        )
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(Color.Black.copy(alpha = 0.4f)),
        )
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 175.dp, bottom = 17.dp),
            verticalArrangement = Arrangement.spacedBy(5.dp),
        ) {
            Text(
                text = title,
                style = NolItTheme.typography.h2Sb,
                color = NolItTheme.colors.background,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                NolItInfoTag(
                    text = authorText,
                    modifier = Modifier.widthIn(min = 86.dp),
                )
                NolItInfoTag(text = durationText, modifier = Modifier.widthIn(min = 50.dp))
                NolItInfoTag(text = categoryText, modifier = Modifier.widthIn(min = 50.dp))
            }
        }
    }
}

@Preview(name = "NolItHomeDailyPickCard", showBackground = true, widthDp = 280)
@Composable
private fun NolItHomeDailyPickCardPreview() {
    NolItTheme {
        NolItHomeDailyPickCard(
            title = "치즈 슬랑이 만져보기",
            imageUrl = null,
            authorName = "지우",
            studentType = "고등학생",
            durationText = "15m",
            categoryText = "HYPE",
            onClick = {},
        )
    }
}
