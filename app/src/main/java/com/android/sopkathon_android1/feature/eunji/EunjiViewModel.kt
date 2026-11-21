package com.android.sopkathon_android1.feature.eunji

import androidx.lifecycle.ViewModel
import com.android.sopkathon_android1.R
import com.android.sopkathon_android1.feature.eunji.model.HomeHypeMissionUiModel
import com.android.sopkathon_android1.feature.eunji.model.HomeRecommendUiModel
import com.android.sopkathon_android1.feature.eunji.model.MissionDetailModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

@HiltViewModel
class EunjiViewModel @Inject constructor() : ViewModel() {
    private val _uiState = MutableStateFlow(EunjiUiState())
    val uiState = _uiState.asStateFlow()

    init {
        getRecommendMission()
        getHypeMission()
    }

    fun getMissionDetail(missionId: Long) {
        // TODO: 서버 연결 시 missionId로 실제 상세 조회
        _uiState.update {
            it.copy(
                missionDetail = MissionDetailModel(
                    imageUrl = "https://example.com/images/mission1.jpg",
                    writerName = "지우",
                    title = "셀프 사진 네 컷 찍어보기",
                    description = "친구와 함께, 또는 혼자서도 좋아요. 좋아하는 포즈로 오늘의 나를 남겨보세요.",
                    instructions = "1. 가까운 셀프 사진 부스를 찾아요.\n2. 원하는 배경과 포즈로 네 컷을 찍어요.\n3. 찍은 사진과 한 줄 후기를 인증해요.",
                ),
            )
        }
    }

    fun addMission(missionId: Long) {
        // TODO: 미션 담기
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
        _uiState.update {
            it.copy(
                hypeMissions = listOf(
                    HomeHypeMissionUiModel(missionId = 1L, imgUrl = null, title = "1위 미션", content = "셀프사진 네컷 찍어보기"),
                    HomeHypeMissionUiModel(missionId = 2L, imgUrl = null, title = "2위 미션", content = "셀프사진 네컷 찍어보기"),
                    HomeHypeMissionUiModel(missionId = 3L, imgUrl = null, title = "3위 미션", content = "셀프사진 네컷 찍어보기"),
                    HomeHypeMissionUiModel(missionId = 4L, imgUrl = null, title = "4위 미션", content = "셀프사진 네컷 찍어보기"),
                    HomeHypeMissionUiModel(missionId = 5L, imgUrl = null, title = "5위 미션", content = "셀프사진 네컷 찍어보기"),
                    HomeHypeMissionUiModel(missionId = 6L, imgUrl = null, title = "6위 미션", content = "셀프사진 네컷 찍어보기"),
                ),
            )
        }
    }
}
