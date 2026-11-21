package com.android.sopkathon_android1.core.designsystem.component.card

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.android.sopkathon_android1.core.designsystem.component.tag.InfoTag
import com.android.sopkathon_android1.core.designsystem.theme.NolItTheme

@Composable
fun HomeDailyPickCard(
    title: String,
    imageModel: Any?,
    authorName: String,
    studentType: String,
    durationText: String,
    categoryText: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    imageContentDescription: String? = null,
) {
    val placeholder = ColorPainter(NolItTheme.colors.surface)
    val authorText = listOf(authorName, studentType)
        .filter { it.isNotBlank() }
        .joinToString(" · ")

    Box(
        modifier = modifier
            .width(280.dp)
            .heightIn(min = 258.dp)
            .clip(RoundedCornerShape(30.dp))
            .background(NolItTheme.colors.surface)
            .clickable(role = Role.Button, onClick = onClick),
    ) {
        AsyncImage(
            model = imageModel,
            contentDescription = imageContentDescription,
            placeholder = placeholder,
            error = placeholder,
            fallback = placeholder,
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
                .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 17.dp),
            verticalArrangement = Arrangement.spacedBy(5.dp),
        ) {
            Text(
                text = title,
                style = NolItTheme.typography.h2Sb,
                color = Color.White,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                InfoTag(
                    text = authorText,
                    modifier = Modifier.widthIn(min = 86.dp),
                )
                InfoTag(text = durationText, modifier = Modifier.widthIn(min = 50.dp))
                InfoTag(text = categoryText, modifier = Modifier.widthIn(min = 50.dp))
            }
        }
    }
}

@Preview(name = "HomeDailyPickCard", showBackground = true)
@Composable
private fun HomeDailyPickCardPreview() {
    NolItTheme {
        HomeDailyPickCard(
            title = "치즈 슬랑이 만져보기",
            imageModel = null,
            authorName = "지우",
            studentType = "고등학생",
            durationText = "15m",
            categoryText = "HYPE",
            onClick = {},
        )
    }
}
