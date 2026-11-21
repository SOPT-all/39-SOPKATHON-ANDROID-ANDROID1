package com.android.sopkathon_android1.feature.seungyeon

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.android.sopkathon_android1.R
import com.android.sopkathon_android1.core.designsystem.component.BaseAsyncImage
import com.android.sopkathon_android1.core.designsystem.component.NolItHeader
import com.android.sopkathon_android1.core.designsystem.extension.noRippleClickable
import com.android.sopkathon_android1.core.designsystem.theme.NolItTheme
import com.android.sopkathon_android1.feature.seungyeon.model.Badge

@Composable
fun SeungyeonRoute(viewModel: SeungyeonViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    SeungyeonScreen(uiState = uiState, onRetry = viewModel::loadUserDetail)
}

@Composable
fun SeungyeonScreen(
    uiState: SeungyeonUiState,
    onRetry: () -> Unit,
) {
    when (uiState) {
        SeungyeonUiState.Loading -> Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            CircularProgressIndicator(color = NolItTheme.colors.secondary)
        }

        is SeungyeonUiState.Error -> Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = uiState.message,
                style = NolItTheme.typography.body1Md,
                color = NolItTheme.colors.textSecondary,
            )
            Spacer(Modifier.height(12.dp))
            Text(
                text = "다시 시도",
                style = NolItTheme.typography.body2Sb,
                color = NolItTheme.colors.secondary,
                modifier = Modifier.noRippleClickable(onClick = onRetry),
            )
        }

        is SeungyeonUiState.Success -> MyPageContent(profile = uiState.profile)
    }
}

@Composable
private fun MyPageContent(profile: MyPageUiModel) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState()),
    ) {
        NolItHeader()
        Spacer(Modifier.height(4.dp))
        Column(modifier = Modifier.padding(horizontal = 20.dp)) {
            Text(
                text = "MY",
                style = NolItTheme.typography.h1Sb,
                color = NolItTheme.colors.textPrimary,
            )
            Spacer(Modifier.height(32.dp))
            Column(verticalArrangement = Arrangement.spacedBy(42.dp)) {
                ProfileCard(
                    name = profile.name,
                    ageGroup = profile.ageGroup,
                    profileImageUrl = profile.profileImageUrl,
                )
                ActivitySection(
                    completedMissionCount = profile.completedMissionCount,
                    receivedLikeCount = profile.receivedLikeCount,
                )
                BadgeSection(badge = profile.badge)
            }
        }
    }
}

@Composable
private fun ProfileCard(name: String, ageGroup: String, profileImageUrl: String?) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(128.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(NolItTheme.colors.secondary)
            .padding(start = 24.dp, end = 28.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Row {
                Text(
                    text = name,
                    style = NolItTheme.typography.h2Sb,
                    color = NolItTheme.colors.neutral25,
                )
                Image(
                    painter = painterResource(R.drawable.ic_profile_bolt),
                    contentDescription = null,
                    modifier = Modifier
                        .offset(y = (-6).dp)
                        .size(14.dp, 17.5.dp),
                )
            }
            Text(
                text = ageGroup,
                style = NolItTheme.typography.body2Md,
                color = NolItTheme.colors.neutral25,
            )
        }
        BaseAsyncImage(
            imgUrl = profileImageUrl,
            errorRes = R.drawable.img_profile_sample,
            modifier = Modifier
                .size(74.dp)
                .clip(CircleShape)
                .background(Color.White),
        )
    }
}

@Composable
private fun SectionTitle(text: String) {
    Text(
        text = text,
        style = NolItTheme.typography.h2Sb,
        color = NolItTheme.colors.textPrimary,
    )
}

@Composable
private fun OutlinedBox(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit,
) {
    val shape = RoundedCornerShape(12.dp)
    Box(
        modifier = modifier
            .height(100.dp)
            .clip(shape)
            .background(NolItTheme.colors.neutral25)
            .border(width = 1.dp, color = NolItTheme.colors.border, shape = shape),
    ) {
        content()
    }
}

@Composable
private fun ActivitySection(completedMissionCount: Long, receivedLikeCount: Long) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        SectionTitle("나의 활동")
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            ActivityCard(
                icon = R.drawable.ic_check,
                count = completedMissionCount.toString(),
                unit = "회",
                label = "완료했어요",
                modifier = Modifier.weight(1f),
            )
            ActivityCard(
                icon = R.drawable.ic_heart_filled,
                count = receivedLikeCount.toString(),
                unit = "개",
                label = "받았어요",
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@Composable
private fun ActivityCard(
    @DrawableRes icon: Int,
    count: String,
    unit: String,
    label: String,
    modifier: Modifier = Modifier,
) {
    OutlinedBox(modifier = modifier) {
        Column(
            modifier = Modifier.padding(start = 12.dp, top = 10.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Image(
                painter = painterResource(icon),
                contentDescription = null,
                modifier = Modifier.size(20.dp),
            )
            Column {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(2.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = count,
                        style = NolItTheme.typography.h2Sb,
                        color = Color.Black,
                    )
                    Text(
                        text = unit,
                        style = NolItTheme.typography.caption1Md,
                        color = NolItTheme.colors.textSecondary,
                    )
                }
                Text(
                    text = label,
                    style = NolItTheme.typography.body2Md,
                    color = NolItTheme.colors.textSecondary,
                )
            }
        }
    }
}

@Composable
private fun BadgeSection(badge: Badge) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        SectionTitle("달성 배지")
        OutlinedBox(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = 15.dp),
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                verticalAlignment = Alignment.Bottom,
            ) {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(NolItTheme.colors.secondary.copy(alpha = 0.08f)),
                    contentAlignment = Alignment.Center,
                ) {
                    Image(
                        painter = painterResource(badge.imageRes),
                        contentDescription = "${badge.label} 배지",
                        modifier = Modifier.size(60.dp),
                    )
                }
                Column {
                    Text(
                        text = "현재 나의 단계",
                        style = NolItTheme.typography.caption1Md,
                        color = NolItTheme.colors.textSecondary,
                    )
                    Text(
                        text = badge.label,
                        style = NolItTheme.typography.h3Md,
                        color = NolItTheme.colors.textPrimary,
                    )
                    Text(
                        text = badge.description,
                        style = NolItTheme.typography.caption3Rg,
                        color = NolItTheme.colors.textSecondary,
                    )
                }
            }
        }
    }
}

@Preview(name = "마이페이지", showBackground = true, backgroundColor = 0xFFFFFFFF, widthDp = 360, heightDp = 800)
@Composable
private fun SeungyeonScreenPreview() {
    NolItTheme {
        SeungyeonScreen(
            uiState = SeungyeonUiState.Success(
                MyPageUiModel(
                    name = "정윤님",
                    ageGroup = "50-64세",
                    profileImageUrl = null,
                    completedMissionCount = 3,
                    receivedLikeCount = 12,
                    badge = Badge.THIRD,
                ),
            ),
            onRetry = {},
        )
    }
}
