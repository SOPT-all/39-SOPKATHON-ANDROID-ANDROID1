package com.android.sopkathon_android1.feature.hyemin

import com.android.sopkathon_android1.core.designsystem.component.ChallengeTab
import com.android.sopkathon_android1.data.model.HyeminMission

data class HyeminUiState(
    val missions: List<HyeminMission> = emptyList(),
    val loading: Boolean = true,
    val error: Boolean = false,
    val tab: ChallengeTab = ChallengeTab.MY,
    val completedOnly: Boolean = false,
    val oldestFirst: Boolean = false,
)
