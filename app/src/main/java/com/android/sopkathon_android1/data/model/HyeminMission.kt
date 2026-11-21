package com.android.sopkathon_android1.data.model

data class HyeminMission(
    val id: String,
    val title: String,
    val testimonial: String,
    val dateLabel: String = "TODAY",
    val completed: Boolean = false,
    val instructions: String,
    val imageUrl: String? = null,
)
