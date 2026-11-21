package com.android.sopkathon_android1.data.dto.response

import com.android.sopkathon_android1.data.model.Mission
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** GET /v1/missions 의 data 항목 */
@Serializable
data class MissionListResponseDto(
    @SerialName("missionId")
    val missionId: Long,
    @SerialName("imageUrl")
    val imageUrl: String? = null,
    @SerialName("title")
    val title: String,
    @SerialName("hypeCount")
    val hypeCount: Int = 0,
)

fun MissionListResponseDto.toModel(): Mission = Mission(
    missionId = missionId,
    imageUrl = imageUrl,
    title = title,
    hypeCount = hypeCount,
)
