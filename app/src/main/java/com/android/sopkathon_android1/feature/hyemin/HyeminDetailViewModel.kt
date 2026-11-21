package com.android.sopkathon_android1.feature.hyemin

import androidx.compose.foundation.text.input.TextFieldState
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.sopkathon_android1.data.model.HyeminMissionDraft
import com.android.sopkathon_android1.data.model.MissionVerificationResult
import com.android.sopkathon_android1.data.repository.HyeminRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class HyeminDetailViewModel @Inject constructor(
    private val repository: HyeminRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val missionId: String = checkNotNull(savedStateHandle["missionId"])
    private val _uiState = MutableStateFlow(HyeminDetailUiState())
    val uiState = _uiState.asStateFlow()
    val review = TextFieldState()
    private var loadJob: Job? = null

    init { loadMission() }

    fun loadMission() {
        loadJob?.cancel()
        loadJob = viewModelScope.launch {
            _uiState.update { it.copy(loading = true, message = null) }
            try {
                val mission = repository.getMissions().firstOrNull { it.missionId.toString() == missionId }
                val detail = mission?.let { repository.getMissionDetail(it.missionId) }
                val draft = repository.getDraft(missionId) ?: HyeminMissionDraft()
                review.edit { replace(0, length, draft.review) }
                _uiState.update { it.copy(mission = mission, description = detail?.description.orEmpty(), instructions = detail?.instructions.orEmpty(), loading = false, photo = draft.photo, liked = draft.liked) }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiState.update { it.copy(loading = false) }
            }
        }
    }

    fun setPhoto(uri: String) { if (_uiState.value.canInteract) _uiState.update { it.copy(photo = uri) } }
    fun toggleLike() { if (_uiState.value.canInteract) _uiState.update { it.copy(liked = !it.liked) } }

    fun saveDraft() {
        val state = _uiState.value
        if (!state.canInteract || state.mission == null) return
        _uiState.update { it.copy(submitting = true) }
        viewModelScope.launch {
            try {
                repository.saveDraft(missionId, HyeminMissionDraft(state.photo, review.text.toString(), state.liked))
                _uiState.update { it.copy(submitting = false, finished = true) }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiState.update { it.copy(submitting = false, message = "임시저장에 실패했어요. 다시 시도해주세요") }
            }
        }
    }

    fun completeMission() {
        val state = _uiState.value
        val photo = state.photo?.takeIf { it.isNotBlank() } ?: return
        val text = review.text.toString().trim()
        if (state.mission == null || state.mission.completed || text.isEmpty() || !state.canInteract) return
        _uiState.update { it.copy(submitting = true, message = null, verificationState = HyeminVerificationState.LOADING) }
        viewModelScope.launch {
            try {
                val result = repository.completeMission(missionId, photo, text, state.liked)
                _uiState.update { it.copy(
                    submitting = false,
                    verificationState = when (result) {
                        MissionVerificationResult.SUCCESS -> HyeminVerificationState.SUCCESS
                        MissionVerificationResult.FAILURE -> HyeminVerificationState.FAILURE
                    },
                ) }
            } catch (e: CancellationException) {
                _uiState.update { it.copy(submitting = false, verificationState = HyeminVerificationState.IDLE) }
                throw e
            } catch (e: Exception) {
                _uiState.update { it.copy(submitting = false, verificationState = HyeminVerificationState.FAILURE) }
            }
        }
    }

    fun dismissVerificationToast() {
        _uiState.update { state ->
            when (state.verificationState) {
                HyeminVerificationState.SUCCESS -> state.copy(verificationState = HyeminVerificationState.IDLE, finished = true)
                HyeminVerificationState.FAILURE -> state.copy(verificationState = HyeminVerificationState.IDLE)
                else -> state
            }
        }
    }
}
