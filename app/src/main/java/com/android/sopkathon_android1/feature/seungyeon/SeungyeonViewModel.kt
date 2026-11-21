package com.android.sopkathon_android1.feature.seungyeon

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.sopkathon_android1.data.dto.response.UserDetailResponseDto
import com.android.sopkathon_android1.data.repository.ApiException
import com.android.sopkathon_android1.data.repository.UserRepository
import com.android.sopkathon_android1.feature.seungyeon.model.Badge
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

private const val CURRENT_USER_ID = 1L

private const val DEFAULT_ERROR_MESSAGE = "오류가 발생했습니다. 다시 시도해 주세요."

private fun String?.toErrorMessage(): String = when (this) {
    "USER_NOT_FOUND" -> "사용자를 찾을 수 없습니다."
    else -> DEFAULT_ERROR_MESSAGE
}

@HiltViewModel
class SeungyeonViewModel @Inject constructor(
    private val userRepository: UserRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow<SeungyeonUiState>(SeungyeonUiState.Loading)
    val uiState = _uiState.asStateFlow()
    private var loadJob: Job? = null

    init {
        loadUserDetail()
    }

    fun loadUserDetail() {
        loadJob?.cancel()
        loadJob = viewModelScope.launch {
            _uiState.value = SeungyeonUiState.Loading
            _uiState.value = try {
                val user = userRepository.getUserDetail(CURRENT_USER_ID)
                SeungyeonUiState.Success(user.toUiModel())
            } catch (exception: CancellationException) {
                throw exception
            } catch (exception: ApiException) {
                SeungyeonUiState.Error(exception.code.toErrorMessage())
            } catch (exception: Exception) {
                SeungyeonUiState.Error(DEFAULT_ERROR_MESSAGE)
            }
        }
    }
}

private fun UserDetailResponseDto.toUiModel() = MyPageUiModel(
    name = "${name}님",
    ageGroup = when (age) {
        "WISE" -> "50-64세"
        else -> "10대"
    },
    profileImageUrl = profileImageUrl,
    completedMissionCount = completedMissionCount,
    receivedLikeCount = receivedLikeCount,
    badge = Badge.from(badge),
)
