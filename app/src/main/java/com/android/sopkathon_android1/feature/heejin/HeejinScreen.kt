package com.android.sopkathon_android1.feature.heejin

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.PickVisualMediaRequest
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.android.sopkathon_android1.core.designsystem.component.CtaButton
import com.android.sopkathon_android1.core.designsystem.component.NolItPhotoUpload
import com.android.sopkathon_android1.core.designsystem.component.NolItTextField
import com.android.sopkathon_android1.core.designsystem.extension.noRippleClickable
import com.android.sopkathon_android1.core.designsystem.theme.NolItTheme

@Composable
fun HeejinRoute(
    onSubmit: (HeejinMissionDraft) -> Unit = {},
    viewModel: HeejinViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val photoPicker = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        viewModel.onPhotoSelected(uri?.toString())
    }
    HeejinScreen(
        uiState = uiState,
        onPhotoClick = {
            photoPicker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        },
        onSubmitClick = { viewModel.prepareSubmission()?.let(onSubmit) },
    )
}

@Composable
fun HeejinScreen(
    uiState: HeejinUiState,
    onPhotoClick: () -> Unit,
    onSubmitClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val isPreview = LocalInspectionMode.current
    val scrollState = rememberScrollState()
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(NolItTheme.colors.background)
            .then(
                if (isPreview) Modifier
                else Modifier
                    .fillMaxSize()
                    .windowInsetsPadding(WindowInsets.safeDrawing)
                    .imePadding()
                    .verticalScroll(scrollState),
            ),
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 20.dp),
            verticalArrangement = Arrangement.spacedBy(44.dp),
        ) {
            Text(
                text = "미션을 만들어주세요!",
                modifier = Modifier.padding(bottom = 4.dp),
                style = NolItTheme.typography.h2Sb,
                color = NolItTheme.colors.textPrimary,
            )
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("미션 제목", style = NolItTheme.typography.body1Sb, color = NolItTheme.colors.textPrimary)
                NolItTextField(
                    state = uiState.title,
                    maxLength = MISSION_TITLE_MAX_LENGTH,
                    placeholder = "예) 한정선 만들기",
                    modifier = Modifier.fillMaxWidth(),
                )
            }
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("소개", style = NolItTheme.typography.body1Sb, color = NolItTheme.colors.textPrimary)
                NolItTextField(
                    state = uiState.introduction,
                    maxLength = MISSION_INTRODUCTION_MAX_LENGTH,
                    placeholder = "예) 한정선은 ‘과일 찹쌀떡 브랜드’에요.",
                    modifier = Modifier.fillMaxWidth(),
                    lineLimits = TextFieldLineLimits.MultiLine(),
                )
            }
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text("진행 방법", style = NolItTheme.typography.body1Sb, color = NolItTheme.colors.textPrimary)
                NolItTextField(
                    state = uiState.instructions,
                    maxLength = MISSION_INSTRUCTIONS_MAX_LENGTH,
                    placeholder = "단계를 작성하실 땐 3단계로 작성해 주세요.",
                    modifier = Modifier.fillMaxWidth(),
                    lineLimits = TextFieldLineLimits.MultiLine(),
                )
            }
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(37.dp),
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text("대표 사진", style = NolItTheme.typography.body1Sb, color = NolItTheme.colors.textPrimary)
                    Text(
                        "갤러리에서 1장 이상 추가",
                        style = NolItTheme.typography.body2Md,
                        color = NolItTheme.colors.neutral500,
                    )
                }
                NolItPhotoUpload(
                    imgUrl = uiState.selectedPhotoUri,
                    modifier = Modifier
                        .size(180.dp)
                        .noRippleClickable(role = Role.Button, onClickLabel = "대표 사진 선택", onClick = onPhotoClick),
                )
            }
            CtaButton(
                text = "미션 제작 완료하기",
                onClick = onSubmitClick,
                enabled = !uiState.isSubmitting,
                modifier = Modifier.padding(top = 20.dp),
            )
        }
    }
}

@Preview(name = "미션 제작 전체 화면", showBackground = true, widthDp = 360, heightDp = 1120)
@Composable
private fun HeejinScreenPreview() {
    val uiState = remember { HeejinUiState() }
    NolItTheme {
        HeejinScreen(uiState = uiState, onPhotoClick = {}, onSubmitClick = {})
    }
}
