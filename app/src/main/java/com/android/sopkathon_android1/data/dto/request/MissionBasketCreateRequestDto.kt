package com.android.sopkathon_android1.data.dto.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** POST /v1/users/{userId}/mission-baskets 요청 본문 */
@Serializable
data class MissionBasketCreateRequestDto(
    @SerialName("missionId")
    val missionId: Long,
)
