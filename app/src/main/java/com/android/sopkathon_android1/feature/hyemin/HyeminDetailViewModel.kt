package com.android.sopkathon_android1.feature.hyemin

import androidx.compose.foundation.text.input.TextFieldState
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.sopkathon_android1.data.datasource.SelectedImageSource
import com.android.sopkathon_android1.data.model.HyeminMissionDraft
import com.android.sopkathon_android1.data.model.ImageUploadValidationException
import com.android.sopkathon_android1.data.model.MissionVerificationResult
import com.android.sopkathon_android1.data.repository.HyeminRepository
import com.android.sopkathon_android1.data.repository.ImageRepository
import com.android.sopkathon_android1.data.repository.MissionCompletionRepository
import com.android.sopkathon_android1.data.repository.MissionBasketRepository
import com.android.sopkathon_android1.data.repository.remote.ImageUploadException
import com.android.sopkathon_android1.data.repository.remote.MissionCompletionException
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import java.util.logging.Logger
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class HyeminDetailViewModel @Inject constructor(
    private val repository: HyeminRepository,
    private val missionBasketRepository: MissionBasketRepository,
    private val imageRepository: ImageRepository,
    private val completionRepository: MissionCompletionRepository,
    private val selectedImageSource: SelectedImageSource,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val missionBasketId: String = checkNotNull(savedStateHandle["missionBasketId"])
    private val _uiState = MutableStateFlow(HyeminDetailUiState())
    val uiState = _uiState.asStateFlow()
    val review = TextFieldState()
    private var loadJob: Job? = null
    private var uploadedPhoto: Pair<String, String>? = null

    init { loadMission() }

    fun loadMission() {
        loadJob?.cancel()
        loadJob = viewModelScope.launch {
            _uiState.update { it.copy(loading = true, message = null) }
            try {
                val mission = missionBasketRepository.getMissions().firstOrNull {
                    it.missionBasketId.toString() == missionBasketId
                }
                val detail = mission?.let { missionBasketRepository.getMissionDetail(it.missionId) }
                val draft = repository.getDraft(missionBasketId) ?: HyeminMissionDraft()
                review.edit { replace(0, length, draft.review) }
                _uiState.update { it.copy(mission = mission, description = detail?.description.orEmpty(), instructions = detail?.instructions.orEmpty(), loading = false, photo = draft.photo, liked = draft.liked) }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiState.update { it.copy(mission = null, loading = false) }
            }
        }
    }

    fun setPhoto(uri: String) {
        if (!_uiState.value.canInteract) return
        if (_uiState.value.photo != uri) uploadedPhoto = null
        _uiState.update { it.copy(photo = uri, message = null, verificationState = HyeminVerificationState.IDLE) }
    }
    fun toggleLike() { if (_uiState.value.canInteract) _uiState.update { it.copy(liked = !it.liked) } }

    fun saveDraft() {
        val state = _uiState.value
        if (!state.canInteract || state.mission == null) return
        _uiState.update { it.copy(submitting = true) }
        viewModelScope.launch {
            try {
                repository.saveDraft(missionBasketId, HyeminMissionDraft(state.photo, review.text.toString(), state.liked))
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
            var stage = "IMAGE_READ"
            try {
                val imageKey = uploadedPhoto?.takeIf { it.first == photo }?.second ?: run {
                    val selectedImage = selectedImageSource.read(photo)
                    val uploadedImage = try {
                        stage = "IMAGE_UPLOAD"
                        imageRepository.uploadImage(selectedImage.file, selectedImage.mimeType)
                    } finally {
                        selectedImage.file.delete()
                    }
                    uploadedPhoto = photo to uploadedImage.imageKey
                    uploadedImage.imageKey
                }
                stage = "MISSION_COMPLETION"
                Logger.getLogger("MissionSubmission").info("completion_request basketId=${state.mission.missionBasketId}")
                val result = completionRepository.completeMission(
                    state.mission.missionBasketId,
                    imageKey,
                    text,
                )
                Logger.getLogger("MissionSubmission").info("completion_response result=$result")
                stage = "LOCAL_COMPLETION_UPDATE"
                repository.markCompleted(state.mission.missionId)
                _uiState.update { it.copy(
                    submitting = false,
                    completionSaved = true,
                    message = if (result == MissionVerificationResult.FAILURE) "인증에는 실패했지만 후기가 저장되었어요." else null,
                    verificationState = when (result) {
                        MissionVerificationResult.SUCCESS -> HyeminVerificationState.SUCCESS
                        MissionVerificationResult.FAILURE -> HyeminVerificationState.FAILURE
                    },
                ) }
            } catch (e: CancellationException) {
                _uiState.update { it.copy(submitting = false, verificationState = HyeminVerificationState.IDLE) }
                throw e
            } catch (e: Exception) {
                val httpStatus = when (e) {
                    is ImageUploadException -> e.httpStatus
                    is MissionCompletionException -> e.httpStatus
                    else -> null
                }
                val errorCode = when (e) {
                    is ImageUploadException -> e.code
                    is ImageUploadValidationException -> e.code
                    is MissionCompletionException -> e.code
                    else -> null
                }
                Logger.getLogger("MissionSubmission").warning(
                    "submission_failed stage=$stage httpStatus=$httpStatus code=$errorCode type=${e.javaClass.simpleName}",
                )
                if (e is MissionCompletionException && e.code == "MISSION_ALREADY_COMPLETED") {
                    repository.markCompleted(state.mission.missionId)
                    _uiState.update { it.copy(
                        submitting = false,
                        completionSaved = true,
                        verificationState = HyeminVerificationState.FAILURE,
                        message = "이미 완료된 미션이에요.",
                    ) }
                    return@launch
                }
                val imageCode = when (e) {
                    is ImageUploadException -> e.code
                    is ImageUploadValidationException -> e.code
                    is MissionCompletionException -> e.code
                    else -> null
                }
                val message = when (imageCode) {
                    "IMAGE_EMPTY" -> "이미지 파일을 확인해주세요."
                    "IMAGE_TYPE_NOT_SUPPORTED" -> "JPG, PNG 이미지만 업로드할 수 있어요."
                    "IMAGE_SIZE_EXCEEDED" -> "이미지는 최대 10MB까지 업로드할 수 있어요."
                    "IMAGE_UPLOAD_FAILED" -> "이미지 업로드에 실패했어요. 다시 시도해주세요."
                    "INVALID_INPUT" -> "입력값을 확인해주세요."
                    "MISSION_BASKET_NOT_FOUND" -> "수행 중인 미션을 찾을 수 없어요."
                    "MISSION_ALREADY_COMPLETED" -> "이미 완료된 미션이에요."
                    "MISSION_AI_UNAVAILABLE" -> "현재 이미지 인증을 사용할 수 없어요. 잠시 후 다시 시도해주세요."
                    "MISSION_AI_TIMEOUT" -> "이미지 인증 시간이 초과됐어요. 다시 시도해주세요."
                    "INTERNAL_SERVER_ERROR" -> "오류가 발생했어요. 다시 시도해주세요."
                    else -> null
                }
                _uiState.update { it.copy(submitting = false, verificationState = HyeminVerificationState.FAILURE, message = message) }
            }
        }
    }

    fun dismissVerificationToast() {
        _uiState.update { state ->
            when (state.verificationState) {
                HyeminVerificationState.SUCCESS -> state.copy(verificationState = HyeminVerificationState.IDLE, finished = true)
                HyeminVerificationState.FAILURE -> state.copy(
                    verificationState = HyeminVerificationState.IDLE,
                    finished = state.completionSaved,
                )
                else -> state
            }
        }
    }
}
