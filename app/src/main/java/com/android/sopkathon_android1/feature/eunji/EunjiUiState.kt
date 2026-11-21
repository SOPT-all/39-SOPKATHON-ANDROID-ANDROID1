package com.android.sopkathon_android1.feature.eunji

import com.android.sopkathon_android1.feature.eunji.model.HomeHypeMissionUiModel
import com.android.sopkathon_android1.feature.eunji.model.HomeRecommendUiModel
import com.android.sopkathon_android1.feature.eunji.model.MissionDetailModel

data class EunjiUiState(
    // 홈 미션 목록 API를 다 불러오기 전까지 true
    val loading: Boolean = true,
    // 바텀시트 미션 상세 API를 불러오는 동안 true
    val detailLoading: Boolean = false,
    val recommendMissions: List<HomeRecommendUiModel> = emptyList(),
    val hypeMissions: List<HomeHypeMissionUiModel> = emptyList(),
    val missionDetail: MissionDetailModel = MissionDetailModel(),
)
