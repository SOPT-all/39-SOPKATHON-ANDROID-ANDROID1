package com.android.sopkathon_android1.feature.heejin

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

@HiltViewModel
class HeejinViewModel @Inject constructor() : ViewModel() {
    private val _uiState = MutableStateFlow(HeejinUiState())
    val uiState = _uiState.asStateFlow()

    fun onPhotoSelected(uri: String?) {
        if (uri.isNullOrBlank()) return
        _uiState.update { it.copy(selectedPhotoUri = uri) }
    }

    fun prepareSubmission(): HeejinMissionDraft? {
        val state = _uiState.value
        if (!state.isSubmitEnabled || state.isSubmitting) return null
        val draft = HeejinMissionDraft(
            title = state.title.text.toString(),
            introduction = state.introduction.text.toString(),
            instructions = state.instructions.text.toString(),
            photoUri = state.selectedPhotoUri,
        )
        _uiState.update { it.copy(isSubmitting = true) }
        return draft
    }
}
