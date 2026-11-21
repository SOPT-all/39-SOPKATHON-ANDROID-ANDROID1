package com.android.sopkathon_android1.data.dto.response

import com.android.sopkathon_android1.data.model.MissionDetail
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** GET /v1/missions/{missionId} 의 data */
@Serializable
data class MissionDetailResponseDto(
    @SerialName("imageUrl")
    val imageUrl: String? = null,
    @SerialName("writerName")
    val writerName: String = "",
    @SerialName("title")
    val title: String = "",
    @SerialName("description")
    val description: String = "",
    @SerialName("instructions")
    val instructions: String = "",
)

fun MissionDetailResponseDto.toModel(): MissionDetail = MissionDetail(
    imageUrl = imageUrl,
    writerName = writerName,
    title = title,
    description = description,
    instructions = instructions,
)
