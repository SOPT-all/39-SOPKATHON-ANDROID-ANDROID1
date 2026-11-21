package com.android.sopkathon_android1.data.dto.heejin

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateMissionResponseDto(
    @SerialName("missionId") val missionId: Long,
)
