package com.android.sopkathon_android1.feature.eunji

import com.android.sopkathon_android1.feature.eunji.model.HomeHypeMissionUiModel
import com.android.sopkathon_android1.feature.eunji.model.HomeRecommendUiModel
import com.android.sopkathon_android1.feature.eunji.model.MissionDetailModel

data class EunjiUiState(
    val recommendMissions: List<HomeRecommendUiModel> = emptyList(),
    val hypeMissions: List<HomeHypeMissionUiModel> = emptyList(),
    val missionDetail: MissionDetailModel = MissionDetailModel(),
)
