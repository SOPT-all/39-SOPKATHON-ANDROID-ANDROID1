package com.android.sopkathon_android1.data.model

data class HyeminMission(
    val missionBasketId: Long,
    val missionId: Long,
    val title: String,
    val imageUrl: String,
    val createdAt: String,
    // 목록 응답에 없는 완료 상태는 Mock에서 별도로 관리한다.
    val completed: Boolean = false,
)
