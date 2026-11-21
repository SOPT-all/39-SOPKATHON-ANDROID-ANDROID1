package com.android.sopkathon_android1.feature.seungyeon.model

/** 사용자 상세 조회 응답의 badge 값을 화면 문구로 변환한다. */
enum class Badge(val serverValue: String, val label: String) {
    CHALLENGER("challenger", "도전자"),
    THIRD("third", "3급"),
    SECOND("second", "2급"),
    FIRST("first", "1급"),
    ;

    companion object {
        fun from(value: String): Badge =
            entries.find { it.serverValue == value } ?: CHALLENGER
    }
}
