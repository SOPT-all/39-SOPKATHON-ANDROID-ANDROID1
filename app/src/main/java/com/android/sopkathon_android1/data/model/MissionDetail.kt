package com.android.sopkathon_android1.data.model

/** 미션 상세. 서버 DTO와 분리된 앱 내부 모델이다. */
data class MissionDetail(
    val imageUrl: String?,
    val writerName: String,
    val title: String,
    val description: String,
    val instructions: String,
)
