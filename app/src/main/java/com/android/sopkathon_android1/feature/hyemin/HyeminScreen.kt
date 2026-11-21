package com.android.sopkathon_android1.feature.hyemin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.android.sopkathon_android1.core.designsystem.component.*
import com.android.sopkathon_android1.core.designsystem.theme.NolItTheme
import com.android.sopkathon_android1.core.designsystem.extension.noRippleClickable
import com.android.sopkathon_android1.data.model.HyeminMission
import com.android.sopkathon_android1.feature.hyemin.component.HyeminMissionListHeader

@Composable
fun HyeminRoute(
    onMissionClick: (String) -> Unit,
    viewModel: HyeminViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    androidx.lifecycle.compose.LifecycleEventEffect(androidx.lifecycle.Lifecycle.Event.ON_RESUME) {
        viewModel.loadMissions()
    }
    HyeminScreen(
        uiState = state,
        onTabSelected = viewModel::selectTab,
        onCompletedChanged = viewModel::filterCompleted,
        onMissionClick = { onMissionClick(it.missionId.toString()) },
        onRetry = viewModel::loadMissions,
        onReviewLikeClick = viewModel::toggleReviewLike,
    )
}

@Composable
fun HyeminScreen(
    uiState: HyeminUiState,
    onTabSelected: (ChallengeTab) -> Unit,
    onCompletedChanged: (Boolean) -> Unit,
    onMissionClick: (HyeminMission) -> Unit,
    onRetry: () -> Unit,
    onReviewLikeClick: (Long) -> Unit,
) {
    val state = uiState
    Column(Modifier
        .background(NolItTheme.colors.background)
        .fillMaxSize()) {
        NolItHeader()
        Column(Modifier.padding(horizontal = 20.dp)) {
            Spacer(Modifier.height(16.dp))
            Text(
                text = "도전",
                modifier = Modifier.fillMaxWidth(),
                style = NolItTheme.typography.h1Sb.copy(fontSize = 28.sp, lineHeight = 38.sp)
            )
        }

        val missions = state.missions
            .filter { it.completed == state.completedOnly }
            .sortedByDescending { it.createdAt }
        val reviews = state.reviews.sortedByDescending { it.createdAt }

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 8.dp, bottom = 20.dp),
        ) {
            item(key = "tabs") {
                NolItChallengeTabs(state.tab, onTabSelected, ourLabel = "모두의 도전")
                Spacer(Modifier.height(if (state.tab == ChallengeTab.OUR) 31.dp else 12.dp))
            }
            item(key = "listHeader") {
                HyeminMissionListHeader(
                    selectedTab = state.tab,
                    completedOnly = state.completedOnly,
                    onCompletedChanged = onCompletedChanged,
                )
                Spacer(Modifier.height(if (state.tab == ChallengeTab.OUR) 28.dp else 12.dp))
                if (state.tab == ChallengeTab.OUR && state.likeError) {
                    Text("좋아요 변경에 실패했어요. 다시 시도해주세요", style = NolItTheme.typography.body2Md)
                }
            }
            when {
                state.loading -> item(key = "loading") {
                    Box(
                        Modifier.fillMaxWidth().height(230.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        CircularProgressIndicator(color = NolItTheme.colors.secondary)
                    }
                }
                state.error -> item(key = "error") {
                    Column {
                        Text(if (state.tab == ChallengeTab.OUR) "인증글을 불러오지 못했어요" else "미션을 불러오지 못했어요")
                        Text("다시 시도", Modifier.noRippleClickable(role = androidx.compose.ui.semantics.Role.Button, onClick = onRetry).padding(12.dp), color = NolItTheme.colors.secondary)
                    }
                }
                state.tab == ChallengeTab.OUR && reviews.isEmpty() -> item(key = "emptyReviews") {
                    Text("아직 인증글이 없어요", style = NolItTheme.typography.body2Md)
                }
                state.tab == ChallengeTab.OUR -> items(reviews, key = { "review-${it.reviewId}" }) { review ->
                    ChallengeMissionCard(
                        label = review.createdAt,
                        review = "${review.writerName} “${review.content}”",
                        title = review.title,
                        imageUrl = review.imageUrl,
                        likeCount = review.likeCount,
                        isLiked = review.isLiked,
                        onLikeClick = { onReviewLikeClick(review.reviewId) },
                    )
                    Spacer(Modifier.height(12.dp))
                }
                missions.isEmpty() -> item(key = "emptyMissions") {
                    Text("아직 미션이 없어요", style = NolItTheme.typography.body2Md)
                }
                else -> items(missions, key = { "basket-${it.missionBasketId}" }) { mission ->
                    ChallengeMissionCard(
                        label = mission.createdAt,
                        review = "",
                        title = mission.title,
                        imageUrl = mission.imageUrl,
                        onClick = { if (!mission.completed) onMissionClick(mission) },
                    )
                    Spacer(Modifier.height(12.dp))
                }
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 740)
@Composable
private fun HyeminScreenPreview() {
    NolItTheme {
        HyeminScreen(
            uiState = HyeminUiState(loading = false),
            onTabSelected = {}, onCompletedChanged = {},
            onMissionClick = {}, onRetry = {}, onReviewLikeClick = {},
        )
    }
}
