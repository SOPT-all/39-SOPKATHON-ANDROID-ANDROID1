package com.android.sopkathon_android1.data.repository

import com.android.sopkathon_android1.data.model.EunjiItem

interface EunjiRepository {
    /** 빈 목록도 정상 결과이며, 조회 실패 시 예외를 던진다. */
    suspend fun getItems(): List<EunjiItem>
}
