package com.android.sopkathon_android1.feature.eunji

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.android.sopkathon_android1.R
import com.android.sopkathon_android1.core.designsystem.component.BaseAsyncImage
import com.android.sopkathon_android1.core.designsystem.component.CtaButton
import com.android.sopkathon_android1.core.designsystem.component.HomeMissionCard
import com.android.sopkathon_android1.core.designsystem.component.NolItHeader
import com.android.sopkathon_android1.core.designsystem.component.NolItHomeDailyPickCard
import com.android.sopkathon_android1.core.designsystem.theme.NolItTheme
import com.android.sopkathon_android1.feature.eunji.component.MissionDetailBottomSheet
import com.android.sopkathon_android1.feature.eunji.model.HomeHypeMissionUiModel
import com.android.sopkathon_android1.feature.eunji.model.HomeRecommendUiModel
import com.android.sopkathon_android1.feature.eunji.model.MissionDetailModel
import kotlinx.coroutines.launch

@Composable
fun EunjiRoute(viewModel: EunjiViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var showBottomSheet by rememberSaveable { mutableStateOf(false) }
    var selectedMissionId by rememberSaveable { mutableStateOf<Long?>(null) }

    EunjiScreen(
        uiState = uiState,
        onPlusClick = viewModel::addMission,
        onCardClick = { missionId ->
            selectedMissionId = missionId
            viewModel.getMissionDetail(missionId)
            showBottomSheet = true
        },
        showBottomSheet = showBottomSheet,
        selectedMissionId = selectedMissionId,
        onBottomSheetDismiss = {
            showBottomSheet = false
            selectedMissionId = null
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EunjiScreen(
    uiState: EunjiUiState,
    onPlusClick: (Long) -> Unit,
    onCardClick: (Long) -> Unit,
    showBottomSheet: Boolean,
    selectedMissionId: Long?,
    onBottomSheetDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val recommendPagerState = rememberPagerState(pageCount = { uiState.recommendMissions.size })
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val coroutineScope = rememberCoroutineScope()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(color = NolItTheme.colors.background),
    ) {
        if (uiState.loading) {
            CircularProgressIndicator(
                color = NolItTheme.colors.secondary,
                modifier = Modifier.align(Alignment.Center),
            )
        } else {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                NolItHeader()

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "오늘의 추천 미션",
                    color = NolItTheme.colors.textPrimary,
                    style = NolItTheme.typography.h1Sb,
                    modifier = Modifier.padding(horizontal = 20.dp),
                )

                Spacer(modifier = Modifier.height(16.dp))

                HorizontalPager(
                    state = recommendPagerState,
                    pageSize = PageSize.Fixed(280.dp),
                    contentPadding = PaddingValues(horizontal = 20.dp),
                    pageSpacing = 10.dp,
                    modifier = Modifier.fillMaxWidth(),
                ) { page ->
                    val mission = uiState.recommendMissions[page]
                    NolItHomeDailyPickCard(
                        title = mission.title,
                        imgRes = mission.imgRes,
                        onClick = {},
                        modifier = Modifier.aspectRatio(280f / 240f),
                    )
                }

                Spacer(modifier = Modifier.height(34.dp))

                Text(
                    text = "요즘 뜨는 미션",
                    color = NolItTheme.colors.textPrimary,
                    style = NolItTheme.typography.h2Sb,
                    modifier = Modifier.padding(horizontal = 20.dp),
                )

                Spacer(modifier = Modifier.height(4.dp))

                LazyColumn(

                ) {
                    itemsIndexed(uiState.hypeMissions.chunked(2)) { rowIndex, rowMissions ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp)
                                .padding(top = if (rowIndex == 0) 0.dp else 8.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            rowMissions.forEach { mission ->
                                HomeMissionCard(
                                    imgUrl = mission.imgUrl,
                                    title = mission.title,
                                    content = mission.content,
                                    onCardClick = {onCardClick(mission.missionId)},
                                    onPlusClick = {onPlusClick(mission.missionId)},
                                    modifier = Modifier.weight(1f),
                                )
                            }

                            // 마지막 줄이 1개일 때 카드가 한 줄을 다 차지하지 않도록 빈 칸 유지
                            if (rowMissions.size < 2) {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }

                    item { Spacer(modifier = Modifier.height(47.dp)) }
                }
            }
        }
    }


    val missionDetail = uiState.missionDetail
    if (showBottomSheet) {
        MissionDetailBottomSheet(
            onDismissRequest = onBottomSheetDismiss,
            sheetState = sheetState,
        ) {
            if (uiState.detailLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(400.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator(color = NolItTheme.colors.secondary)
                }
            } else {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(horizontal = 30.dp)
                    ) {
                        Spacer(modifier = Modifier.height(27.dp))

                        Text(
                            text = "미션 상세",
                            color = NolItTheme.colors.textPrimary,
                            style = NolItTheme.typography.body1Sb
                        )

                        Spacer(modifier = Modifier.height(21.dp))

                        BaseAsyncImage(
                            imgUrl = missionDetail.imageUrl,
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .aspectRatio(300f/230f)
                        )

                        Spacer(modifier = Modifier.height(21.dp))

                        Text(
                            text = missionDetail.title,
                            color = NolItTheme.colors.textPrimary,
                            style = NolItTheme.typography.h2Sb
                        )

                        Text(
                            text = missionDetail.writerName,
                            color = NolItTheme.colors.textSecondary,
                            style = NolItTheme.typography.body1Sb
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = missionDetail.description,
                            color = NolItTheme.colors.textPrimary,
                            style = NolItTheme.typography.body2Md
                        )

                        Spacer(modifier = Modifier.height(21.dp))

                        Text(
                            text = "이렇게 체험해요",
                            color = NolItTheme.colors.textPrimary,
                            style = NolItTheme.typography.body1Sb
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = missionDetail.instructions,
                            color = NolItTheme.colors.textPrimary,
                            style = NolItTheme.typography.body2Md
                        )

                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    CtaButton(
                        text = "내 도전에 담기",
                        onClick = {
                            selectedMissionId?.let(onPlusClick)
                            coroutineScope.launch { sheetState.hide() }.invokeOnCompletion {
                                if (!sheetState.isVisible) onBottomSheetDismiss()
                            }
                        },
                        modifier = Modifier
                            .padding(horizontal = 25.dp)
                    )

                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun EunjiScreenPreview() {
    NolItTheme {
        EunjiScreen(
            uiState = EunjiUiState(
                loading = false,
                recommendMissions = listOf(
                    HomeRecommendUiModel(imgRes = R.drawable.img_home_recommend_1, title = "치즈 슬랑이 만져보기"),
                    HomeRecommendUiModel(imgRes = R.drawable.img_recommend_2, title = "집에서 한정선 만들기"),
                ),
                hypeMissions = List(6) { index ->
                    HomeHypeMissionUiModel(
                        missionId = index + 1L,
                        imgUrl = null,
                        title = "${index + 1}위 미션",
                        content = "셀프사진 네컷 찍어보기",
                    )
                },
                missionDetail = MissionDetailModel(
                    imageUrl = "https://example.com/images/mission1.jpg",
                    writerName = "지우",
                    title = "셀프 사진 네 컷 찍어보기",
                    description = "친구와 함께, 또는 혼자서도 좋아요. 좋아하는 포즈로 오늘의 나를 남겨보세요.",
                    instructions = "1. 가까운 셀프 사진 부스를 찾아요.\n2. 원하는 배경과 포즈로 네 컷을 찍어요.\n3. 찍은 사진과 한 줄 후기를 인증해요.",
                ),
            ),
            onPlusClick = {},
            onCardClick = {},
            showBottomSheet = false,
            selectedMissionId = null,
            onBottomSheetDismiss = {},
        )
    }
}
