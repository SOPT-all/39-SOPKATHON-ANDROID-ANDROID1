package com.android.sopkathon_android1.feature.hyemin

import com.android.sopkathon_android1.data.model.HyeminMission

enum class HyeminVerificationState { IDLE, LOADING, SUCCESS, FAILURE }

data class HyeminDetailUiState(
    val mission: HyeminMission? = null,
    val description: String = "",
    val instructions: String = "",
    val loading: Boolean = true,
    val photo: String? = null,
    val liked: Boolean = false,
    val submitting: Boolean = false,
    val verificationState: HyeminVerificationState = HyeminVerificationState.IDLE,
    val finished: Boolean = false,
    val message: String? = null,
) {
    val canInteract: Boolean
        get() = !submitting && !finished && verificationState != HyeminVerificationState.SUCCESS
}
