package com.android.sopkathon_android1.feature.hyemin

import com.android.sopkathon_android1.core.designsystem.component.ChallengeTab
import com.android.sopkathon_android1.data.model.HyeminMission
import com.android.sopkathon_android1.data.model.HyeminReview

data class HyeminUiState(
    val missions: List<HyeminMission> = emptyList(),
    val reviews: List<HyeminReview> = emptyList(),
    val likeError: Boolean = false,
    val loading: Boolean = true,
    val error: Boolean = false,
    val tab: ChallengeTab = ChallengeTab.MY,
    val completedOnly: Boolean = false,
    val oldestFirst: Boolean = false,
)
