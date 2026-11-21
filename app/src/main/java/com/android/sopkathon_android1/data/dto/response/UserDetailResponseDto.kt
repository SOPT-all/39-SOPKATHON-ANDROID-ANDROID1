package com.android.sopkathon_android1.data.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** GET /v1/users/{userId} 사용자 상세 정보 조회 응답 */
@Serializable
data class UserDetailResponseDto(
    @SerialName("name")
    val name: String,
    @SerialName("age")
    val age: String,
    @SerialName("profileImageUrl")
    val profileImageUrl: String? = null,
    @SerialName("completedMissionCount")
    val completedMissionCount: Long,
    @SerialName("receivedLikeCount")
    val receivedLikeCount: Long,
    @SerialName("badge")
    val badge: String,
)
