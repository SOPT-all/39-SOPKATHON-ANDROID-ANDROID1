package com.android.sopkathon_android1.feature.eunji

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.sopkathon_android1.R
import com.android.sopkathon_android1.data.repository.EunjiRepository
import com.android.sopkathon_android1.feature.eunji.model.HomeHypeMissionUiModel
import com.android.sopkathon_android1.feature.eunji.model.HomeRecommendUiModel
import com.android.sopkathon_android1.feature.eunji.model.MissionDetailModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class EunjiViewModel @Inject constructor(
    private val repository: EunjiRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(EunjiUiState())
    val uiState = _uiState.asStateFlow()

    init {
        getRecommendMission()
        getHypeMission()
    }

    fun getMissionDetail(missionId: Long) {
        _uiState.update { it.copy(missionDetail = MissionDetailModel(), detailLoading = true) }

        viewModelScope.launch {
            try {
                val detail = repository.getMissionDetail(missionId)
                _uiState.update {
                    it.copy(
                        missionDetail = MissionDetailModel(
                            imageUrl = detail.imageUrl.orEmpty(),
                            writerName = detail.writerName,
                            title = detail.title,
                            description = detail.description,
                            instructions = detail.instructions,
                        ),
                    )
                }
            } catch (exception: CancellationException) {
                throw exception
            } catch (exception: Exception) {
                // TODO: 실패 처리 (404 MISSION_NOT_FOUND 등)
            } finally {
                _uiState.update { it.copy(detailLoading = false) }
            }
        }
    }

    fun addMission(missionId: Long) {
        viewModelScope.launch {
            try {
                repository.addMissionToBasket(missionId)
            } catch (exception: CancellationException) {
                throw exception
            } catch (exception: Exception) {
                // TODO: 실패 처리 (409 MISSION_ALREADY_IN_BASKET 등)
            }
        }
    }

    fun getRecommendMission() {
        _uiState.update {
            it.copy(
                recommendMissions = listOf(
                    HomeRecommendUiModel(
                        imgRes = R.drawable.img_home_recommend_1,
                        title = "치즈 슬랑이 만져보기",
                    ),
                    HomeRecommendUiModel(
                        imgRes = R.drawable.img_recommend_2,
                        title = "머랭롤 체험해보기",
                    ),
                    HomeRecommendUiModel(
                        imgRes = R.drawable.img_recommend_3,
                        title = "L을 가져가 챌린지 하기",
                    ),
                    HomeRecommendUiModel(
                        imgRes = R.drawable.img_recommend_4,
                        title = "셀프 사진 네 컷 찍기",
                    ),
                    HomeRecommendUiModel(
                        imgRes = R.drawable.img_recommend_5,
                        title = "집에서 한정선 만들기",
                    ),
                    HomeRecommendUiModel(
                        imgRes = R.drawable.img_recommend_6,
                        title = "집에서 영크크 되기",
                    ),
                ),
            )
        }
    }

    fun getHypeMission() {
        viewModelScope.launch {
            try {
                val missions = repository.getMissions().take(HYPE_MISSION_COUNT)
                _uiState.update {
                    it.copy(
                        hypeMissions = missions.mapIndexed { index, mission ->
                            HomeHypeMissionUiModel(
                                missionId = mission.missionId,
                                imgUrl = mission.imageUrl,
                                title = "${index + 1}위 미션",
                                content = mission.title,
                            )
                        },
                    )
                }
            } catch (exception: CancellationException) {
                throw exception
            } catch (exception: Exception) {
                // TODO: 실패 처리 (현재는 빈 그리드 유지)
            } finally {
                _uiState.update { it.copy(loading = false) }
            }
        }
    }

    private companion object {
        const val HYPE_MISSION_COUNT = 6
    }
}
