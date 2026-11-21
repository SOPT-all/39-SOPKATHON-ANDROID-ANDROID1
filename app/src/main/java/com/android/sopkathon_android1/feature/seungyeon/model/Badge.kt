package com.android.sopkathon_android1.feature.seungyeon.model

import androidx.annotation.DrawableRes
import com.android.sopkathon_android1.R

enum class Badge(
    val serverValue: String,
    val label: String,
    @DrawableRes val imageRes: Int,
    val description: String,
) {
    CHALLENGER(
        serverValue = "challenger",
        label = "초심자",
        imageRes = R.drawable.badge_beginner,
        description = "이제 막 놀잇에 입문하신 당신!",
    ),
    THIRD(
        serverValue = "third",
        label = "인싸",
        imageRes = R.drawable.badge_insa,
        description = "새로운 놀잇 푹빠지신 당신!",
    ),
    SECOND(
        serverValue = "second",
        label = "배짱이",
        imageRes = R.drawable.badge_baejjangi,
        description = "안해본 도전이 있나요? 여유가 넘치시는 당신!",
    ),
    FIRST(
        serverValue = "first",
        label = "한량",
        imageRes = R.drawable.badge_hanryang,
        description = "놀잇의 그랜드 마스터, 모든 걸 정복한 당신!",
    ),
    ;
    companion object {
        fun from(value: String): Badge =
            entries.find { it.serverValue == value } ?: CHALLENGER
    }
}
