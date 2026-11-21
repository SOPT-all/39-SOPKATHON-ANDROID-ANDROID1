package com.android.sopkathon_android1.data.model

/** 홈 미션 목록 항목. 서버 DTO와 분리된 앱 내부 모델이다. */
data class Mission(
    val missionId: Long,
    val imageUrl: String?,
    val title: String,
    val hypeCount: Int,
)
