package com.android.sopkathon_android1.data.dto.hyemin

import kotlinx.serialization.Serializable

@Serializable
data class MissionBasketDto(
    val missionBasketId: Long,
    val missionId: Long,
    val title: String,
    val imageUrl: String,
    val createdAt: String,
)
