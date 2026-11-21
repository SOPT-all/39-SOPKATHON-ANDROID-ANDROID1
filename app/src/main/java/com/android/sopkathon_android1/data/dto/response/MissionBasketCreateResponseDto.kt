package com.android.sopkathon_android1.data.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** POST /v1/users/{userId}/mission-baskets 의 data */
@Serializable
data class MissionBasketCreateResponseDto(
    @SerialName("basketId")
    val basketId: Long,
    @SerialName("missionId")
    val missionId: Long,
    @SerialName("isCompleted")
    val isCompleted: Boolean,
)
