package com.android.sopkathon_android1.data.repository

import com.android.sopkathon_android1.data.model.Mission
import com.android.sopkathon_android1.data.model.MissionDetail

/** 모든 함수는 실패 시 예외를 던진다. */
interface EunjiRepository {
    /** 홈 전체 미션 목록(Hype 내림차순). 빈 목록도 정상 결과다. */
    suspend fun getMissions(): List<Mission>

    suspend fun getMissionDetail(missionId: Long): MissionDetail

    /** 내 도전에 미션 담기 */
    suspend fun addMissionToBasket(missionId: Long)
}
