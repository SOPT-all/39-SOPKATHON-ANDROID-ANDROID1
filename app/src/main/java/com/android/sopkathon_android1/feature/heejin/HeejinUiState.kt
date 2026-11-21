package com.android.sopkathon_android1.feature.heejin

import androidx.compose.foundation.text.input.TextFieldState

internal const val MISSION_TITLE_MAX_LENGTH = 15
internal const val MISSION_INTRODUCTION_MAX_LENGTH = 100
internal const val MISSION_INSTRUCTIONS_MAX_LENGTH = 100

data class HeejinUiState(
    val title: TextFieldState = TextFieldState(),
    val introduction: TextFieldState = TextFieldState(),
    val instructions: TextFieldState = TextFieldState(),
    val selectedPhotoUri: String? = null,
    val isSubmitting: Boolean = false,
) {
    val isSubmitEnabled: Boolean
        get() = title.text.isNotBlank() &&
            title.text.length <= MISSION_TITLE_MAX_LENGTH &&
            introduction.text.isNotBlank() &&
            introduction.text.length <= MISSION_INTRODUCTION_MAX_LENGTH &&
            instructions.text.isNotBlank() &&
            instructions.text.length <= MISSION_INSTRUCTIONS_MAX_LENGTH &&
            !selectedPhotoUri.isNullOrBlank()
}
