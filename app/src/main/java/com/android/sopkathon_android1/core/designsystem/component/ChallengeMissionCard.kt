package com.android.sopkathon_android1.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.android.sopkathon_android1.core.designsystem.extension.noRippleClickable
import com.android.sopkathon_android1.core.designsystem.theme.NolItTheme
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

private val missionDateFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd")

fun String.toMissionDateLabel(today: LocalDate = LocalDate.now()): String {
    val date = runCatching { LocalDateTime.parse(this).toLocalDate() }.getOrNull()
        ?: return this
    return if (date == today) "TODAY" else date.format(missionDateFormatter)
}

@Composable
fun ChallengeMissionCard(
    label: String,
    review: String,
    title: String,
    imageUrl: String?,
    modifier: Modifier = Modifier,
    likeCount: Int? = null,
    isLiked: Boolean = false,
    onLikeClick: () -> Unit = {},
    onClick: () -> Unit = {},
) {
    Box(
        modifier = modifier
            .size(width = 320.dp, height = 230.dp)
            .clip(RoundedCornerShape(30.dp))
            .background(NolItTheme.colors.surface)
            .noRippleClickable(onClick = onClick),
    ) {
        BaseAsyncImage(
            imgUrl = imageUrl,
            modifier = Modifier.matchParentSize(),
        )

        Box(
            modifier = Modifier
                .matchParentSize()
                .background(Color.Black.copy(alpha = 0.4f)),
        )

        Text(
            text = label,
            style = NolItTheme.typography.body2Sb,
            color = NolItTheme.colors.primary,
            modifier = Modifier.padding(start = 23.dp, top = 21.dp),
        )

        Row(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 18.dp, end = 22.dp, bottom = 17.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.Bottom,
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = review,
                    style = NolItTheme.typography.caption2Rg,
                    color = Color.White,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = title,
                    style = NolItTheme.typography.h2Sb,
                    color = NolItTheme.colors.neutral25,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
            if (likeCount != null) {
                Text(
                    text = if (isLiked) "♥$likeCount" else "♡$likeCount",
                    style = NolItTheme.typography.h3Md,
                    color = NolItTheme.colors.primary,
                    modifier = Modifier.noRippleClickable(onClick = onLikeClick),
                )
            }
        }
    }
}

@Preview(name = "내 도전 (하트 없음)")
@Composable
private fun ChallengeMissionCardMyPreview() {
    NolItTheme {
        ChallengeMissionCard(
            label = "TODAY",
            review = "민수 50대 “처음엔 어색했는데 재밌었어요.”",
            title = "집에서 한정선 만들기",
            imageUrl = null,
        )
    }
}

@Preview(name = "모두의 도전 (좋아요 누름)")
@Composable
private fun ChallengeMissionCardLikedPreview() {
    NolItTheme {
        ChallengeMissionCard(
            label = "TODAY",
            review = "민수 50대 “처음엔 어색했는데 재밌었어요.”",
            title = "집에서 한정선 만들기",
            imageUrl = null,
            likeCount = 18,
            isLiked = true,
        )
    }
}

@Preview(name = "모두의 도전 (좋아요 안 누름)")
@Composable
private fun ChallengeMissionCardUnlikedPreview() {
    NolItTheme {
        ChallengeMissionCard(
            label = "TODAY",
            review = "민수 50대 “처음엔 어색했는데 재밌었어요.”",
            title = "집에서 한정선 만들기",
            imageUrl = null,
            likeCount = 17,
            isLiked = false,
        )
    }
}
