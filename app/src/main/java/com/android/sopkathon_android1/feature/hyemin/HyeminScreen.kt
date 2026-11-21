package com.android.sopkathon_android1.feature.hyemin

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.android.sopkathon_android1.R
import com.android.sopkathon_android1.core.designsystem.component.*
import com.android.sopkathon_android1.core.designsystem.theme.NolItTheme
import com.android.sopkathon_android1.data.model.HyeminMission

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
        onSort = viewModel::toggleSort,
        onMissionClick = { onMissionClick(it.id) },
        onRetry = viewModel::loadMissions,
    )
}

@Composable
fun HyeminScreen(
    uiState: HyeminUiState,
    onTabSelected: (ChallengeTab) -> Unit,
    onCompletedChanged: (Boolean) -> Unit,
    onSort: () -> Unit,
    onMissionClick: (HyeminMission) -> Unit,
    onRetry: () -> Unit,
) {
    val state = uiState
    Column(Modifier
        .background(NolItTheme.colors.background)
        .fillMaxSize()
        .verticalScroll(rememberScrollState())) {
        Row(
            Modifier
                .fillMaxWidth()
                .height(40.dp)
                .padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Image(painterResource(R.drawable.img_nolit_logo), null, Modifier.size(26.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                Image(painterResource(R.drawable.ic_mission_bell), "알림", Modifier.size(24.dp))
                Image(painterResource(R.drawable.ic_mission_setting), "설정", Modifier.size(24.dp))
            }
        }
        Column(Modifier.padding(horizontal = 20.dp)) {
            Spacer(Modifier.height(16.dp))
            Text(
                "도전",
                style = NolItTheme.typography.h1Sb.copy(fontSize = 28.sp, lineHeight = 38.sp)
            )

            Spacer(Modifier.height(8.dp))

            NolItChallengeTabs(state.tab, onTabSelected, ourLabel = "모두의 도전")

            Spacer(Modifier.height(if (state.tab == ChallengeTab.OUR) 31.dp else 12.dp))

            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(if (state.tab == ChallengeTab.MY) "담은 미션" else "전체 미션", style = sectionStyle())
                if (state.tab == ChallengeTab.MY) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("완료", style = NolItTheme.typography.caption1Md)
                        Spacer(Modifier.width(8.dp))
                        Switch(
                            checked = state.completedOnly,
                            onCheckedChange = onCompletedChanged,
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = NolItTheme.colors.border,
                                uncheckedThumbColor = NolItTheme.colors.secondary,
                                uncheckedTrackColor = NolItTheme.colors.border,
                                uncheckedBorderColor = Color.Transparent,
                                checkedBorderColor = Color.Transparent
                            ),
                            modifier = Modifier.height(28.dp),
                        )
                    }
                } else {
                    TextButton(onClick = onSort, contentPadding = PaddingValues(0.dp)) {
                        Text(
                            if (state.oldestFirst) "오래된순 ⌄" else "최신순 ⌄",
                            style = NolItTheme.typography.body2Md
                        )
                    }
                }
            }
            Spacer(Modifier.height(if (state.tab == ChallengeTab.OUR) 28.dp else 12.dp))
            when {
                state.loading -> Box(
                    Modifier
                        .fillMaxWidth()
                        .height(230.dp),
                    contentAlignment = Alignment.Center
                ) { CircularProgressIndicator(color = NolItTheme.colors.secondary) }

                state.error -> Column {
                    Text("미션을 불러오지 못했어요")
                    TextButton(onClick = onRetry) { Text("다시 시도") }
                }

                else -> {
                    val missions =
                        state.missions.filter { state.tab == ChallengeTab.OUR || it.completed == state.completedOnly }
                            .let { if (state.oldestFirst && state.tab == ChallengeTab.OUR) it.reversed() else it }
                    if (missions.isEmpty()) Text(
                        "아직 미션이 없어요",
                        style = NolItTheme.typography.body2Md
                    )
                    missions.forEach { mission ->
                        ChallengeMissionCard(
                            label = mission.dateLabel,
                            review = mission.testimonial,
                            title = mission.title,
                            imageUrl = mission.imageUrl,
                            onClick = { if (!mission.completed) onMissionClick(mission) },
                        )
                        Spacer(Modifier.height(12.dp))
                    }
                }
            }
            Spacer(Modifier.height(20.dp))
        }
    }
}

@Composable
private fun sectionStyle(): TextStyle =
    NolItTheme.typography.body1Sb.copy(fontSize = 18.sp, lineHeight = 25.sp)

@Preview(showBackground = true, widthDp = 360, heightDp = 740)
@Composable
private fun HyeminScreenPreview() {
    NolItTheme {
        HyeminScreen(
            uiState = HyeminUiState(loading = false),
            onTabSelected = {}, onCompletedChanged = {}, onSort = {},
            onMissionClick = {}, onRetry = {},
        )
    }
}
