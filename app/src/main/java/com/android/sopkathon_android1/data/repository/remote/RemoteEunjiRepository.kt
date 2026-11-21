package com.android.sopkathon_android1.data.repository.remote

import com.android.sopkathon_android1.data.datasource.remote.EunjiRemoteDataSource
import com.android.sopkathon_android1.data.dto.response.toModel
import com.android.sopkathon_android1.data.model.Mission
import com.android.sopkathon_android1.data.model.MissionDetail
import com.android.sopkathon_android1.data.repository.EunjiRepository
import javax.inject.Inject

// 로그인 기능이 없어 사용자 ID를 고정한다.
private const val USER_ID = 1L

// 4xx/5xx 응답은 Retrofit이 HttpException으로 던지고, 그대로 호출부에 전달한다.
class RemoteEunjiRepository @Inject constructor(
    private val remoteDataSource: EunjiRemoteDataSource,
) : EunjiRepository {
    override suspend fun getMissions(): List<Mission> =
        remoteDataSource.findMissions().data.orEmpty().map { it.toModel() }

    override suspend fun getMissionDetail(missionId: Long): MissionDetail =
        requireNotNull(remoteDataSource.getMissionDetail(missionId).data) {
            "미션 상세 응답에 data가 없습니다."
        }.toModel()

    override suspend fun addMissionToBasket(missionId: Long) {
        remoteDataSource.addMissionToBasket(userId = USER_ID, missionId = missionId)
    }
}
