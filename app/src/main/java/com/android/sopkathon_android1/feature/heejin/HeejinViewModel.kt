package com.android.sopkathon_android1.feature.heejin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.sopkathon_android1.data.dto.heejin.CreateMissionRequestDto
import com.android.sopkathon_android1.data.repository.HeejinApiException
import com.android.sopkathon_android1.data.repository.HeejinRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class HeejinViewModel @Inject constructor(
    private val repository: HeejinRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(HeejinUiState())
    val uiState = _uiState.asStateFlow()

    fun onPhotoSelected(uri: String?) {
        if (uri.isNullOrBlank() || _uiState.value.isSubmitting) return
        _uiState.update { it.copy(selectedPhotoUri = uri) }
    }

    fun submitMission() {
        val state = _uiState.value
        if (state.isSubmitting) return
        if (!state.isSubmitEnabled) {
            _uiState.update { it.copy(errorMessage = "필수 항목과 진행 방법 1~3단계를 확인해 주세요.") }
            return
        }
        val draft = HeejinMissionDraft(
            title = state.title.text.toString(),
            introduction = state.introduction.text.toString(),
            instructions = state.instructions.text.toString(),
            photoUri = state.selectedPhotoUri,
        )
        val photoUri = draft.photoUri ?: return
        _uiState.update { it.copy(isSubmitting = true, errorMessage = null) }
        viewModelScope.launch {
            try {
                val imageKey = repository.uploadImage(photoUri)
                val missionId = repository.createMission(
                    request = CreateMissionRequestDto(
                        title = draft.title,
                        description = draft.introduction,
                        instructions = draft.instructions,
                        imageKey = imageKey,
                    ),
                )
                _uiState.update { it.copy(createdMissionId = missionId) }
            } catch (exception: CancellationException) {
                _uiState.update { it.copy(isSubmitting = false) }
                throw exception
            } catch (exception: Exception) {
                val message = when ((exception as? HeejinApiException)?.code) {
                    "INVALID_INPUT" -> "입력 내용과 대표 사진을 확인해 주세요."
                    "USER_NOT_FOUND" -> "사용자 정보를 찾을 수 없어요."
                    else -> "미션 등록에 실패했어요. 다시 시도해 주세요."
                }
                _uiState.update { it.copy(isSubmitting = false, errorMessage = message) }
            }
        }
    }

    fun onErrorShown() {
        _uiState.update { it.copy(errorMessage = null) }
    }

    fun onMissionCreatedHandled() {
        _uiState.update { it.copy(createdMissionId = null) }
    }
}
