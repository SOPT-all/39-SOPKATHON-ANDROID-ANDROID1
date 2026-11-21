package com.android.sopkathon_android1.feature.hyemin

import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.android.sopkathon_android1.core.designsystem.component.*
import com.android.sopkathon_android1.core.designsystem.theme.NolItTheme
import com.android.sopkathon_android1.core.designsystem.extension.noRippleClickable
import com.android.sopkathon_android1.data.model.HyeminMission
import com.android.sopkathon_android1.feature.hyemin.component.HyeminMissionDescription
import kotlinx.coroutines.delay

@Composable
fun HyeminDetailRoute(
    onBack: () -> Unit,
    viewModel: HyeminDetailViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val picker = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        uri?.let { viewModel.setPhoto(it.toString()) }
    }
    BackHandler(enabled = !state.canInteract) { /* 완료 요청 중에는 화면을 유지한다. */ }
    LaunchedEffect(state.finished) { if (state.finished) onBack() }
    LaunchedEffect(state.verificationState) {
        if (state.verificationState == HyeminVerificationState.SUCCESS ||
            state.verificationState == HyeminVerificationState.FAILURE
        ) {
            delay(2500) // 서버 연결 할 때 바꿔야함
            viewModel.dismissVerificationToast()
        }
    }
    HyeminDetailScreen(
        uiState = state, review = viewModel.review,
        onClose = onBack, onSave = viewModel::saveDraft,
        onPhotoClick = { picker.launch(androidx.activity.result.PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)) },
        onLike = viewModel::toggleLike, onComplete = viewModel::completeMission,
        onRetry = viewModel::loadMission,
    )
}

@Composable
fun HyeminDetailScreen(
    uiState: HyeminDetailUiState,
    review: TextFieldState,
    onClose: () -> Unit,
    onSave: () -> Unit,
    onPhotoClick: () -> Unit,
    onLike: () -> Unit,
    onComplete: () -> Unit,
    onRetry: () -> Unit,
) {
    Box(
        Modifier
            .fillMaxSize()
            .background(NolItTheme.colors.background)
            .statusBarsPadding()
            .navigationBarsPadding()
            .imePadding(),
    ) {
        when {
            uiState.loading -> CircularProgressIndicator(Modifier.align(Alignment.Center))
            uiState.mission == null -> Column(Modifier.align(Alignment.Center)) {
                Text("미션을 불러오지 못했어요")
                Text("다시 시도", Modifier.noRippleClickable(role = androidx.compose.ui.semantics.Role.Button, onClick = onRetry).padding(12.dp), color = NolItTheme.colors.secondary)
                Text("닫기", Modifier.noRippleClickable(role = androidx.compose.ui.semantics.Role.Button, onClick = onClose).padding(12.dp), color = NolItTheme.colors.secondary)
            }
            else -> HyeminDetailContent(uiState, review, onClose, onSave, onPhotoClick, onLike, onComplete)
        }
        val toastType = when (uiState.verificationState) {
            HyeminVerificationState.IDLE -> null
            HyeminVerificationState.LOADING -> NolItToastType.LOADING
            HyeminVerificationState.SUCCESS -> NolItToastType.SUCCESS
            HyeminVerificationState.FAILURE -> NolItToastType.FAILURE
        }
        toastType?.let { type ->
            NolItToast(
                type = type,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 86.dp)
                    .width(if (type == NolItToastType.LOADING) 156.dp else 215.dp),
            )
        }
        uiState.message?.let {
            Surface(Modifier.align(Alignment.BottomCenter).padding(16.dp), color = NolItTheme.colors.textPrimary, shape = RoundedCornerShape(24.dp)) {
                Text(it, Modifier.padding(12.dp), color = Color.White)
            }
        }
    }
}

@Composable
private fun HyeminDetailContent(
    state: HyeminDetailUiState,
    review: TextFieldState,
    onClose: () -> Unit,
    onSave: () -> Unit,
    onPhotoClick: () -> Unit,
    onLike: () -> Unit,
    onComplete: () -> Unit,
) {
    val mission = state.mission ?: return
    Column(Modifier.fillMaxSize()) {
        NolItHeader(
            type = NolItHeaderType.VERIFICATION,
            onCloseClick = onClose,
            onSaveClick = onSave,
            enabled = state.canInteract,
        )
        LazyColumn(
            modifier = Modifier.weight(1f).fillMaxWidth(),
            contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 16.dp, bottom = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            item(key = "title") {
                Text(
                    text = "미션을 인증해주세요!",
                    modifier = Modifier.fillMaxWidth(),
                    style = NolItTheme.typography.h2Sb.copy(fontSize = 24.sp, lineHeight = 33.sp),
                )
                Spacer(Modifier.height(28.dp))
            }
            item(key = "description") {
                Column(Modifier.fillMaxWidth()) {
                    Text(mission.title, color = NolItTheme.colors.neutral800, style = NolItTheme.typography.body1Sb)
                    Spacer(Modifier.height(8.dp))
                    HyeminMissionDescription(
                        description = state.description,
                        instructions = state.instructions,
                    )
                    Spacer(Modifier.height(36.dp))
                }
            }
            item(key = "photo") {
                Column(Modifier.fillMaxWidth()) {
                    Text("대표 사진", style = NolItTheme.typography.body1Sb)
                    Text(
                        "갤러리에서 1장 추가",
                        color = NolItTheme.colors.neutral500,
                        style = NolItTheme.typography.body2Md,
                    )
                    Spacer(Modifier.height(36.dp))
                    NolItPhotoUpload(
                        state.photo,
                        Modifier
                            .align(Alignment.CenterHorizontally)
                            .size(180.dp)
                            .noRippleClickable(
                                enabled = state.canInteract,
                                onClickLabel = "갤러리에서 사진 선택",
                                onClick = onPhotoClick,
                            ),
                    )
                    Spacer(Modifier.height(36.dp))
                }
            }
            item(key = "review") {
                Column(Modifier.fillMaxWidth()) {
                    Text("한 줄 후기", style = NolItTheme.typography.body1Sb)
                    Spacer(Modifier.height(8.dp))
                    NolItTextField(
                        state = review,
                        maxLength = 100,
                        placeholder = "예) 오랜만에 사진을 혼자서 찍으니 참 좋았어요!",
                        lineLimits = TextFieldLineLimits.MultiLine(),
                    )
                    Spacer(Modifier.height(12.dp))
                }
            }
            item(key = "hype") {
                NolItMissionCompleteHypeButton(
                    isSelected = state.liked,
                    onClick = onLike,
                    enabled = state.canInteract,
                    modifier = Modifier.widthIn(max = 262.dp).fillMaxWidth().height(70.dp),
                )
                Spacer(Modifier.height(32.dp))
            }
            item(key = "complete") {
                CtaButton(
                    text = "도전 완료하기",
                    onClick = onComplete,
                    enabled = !state.photo.isNullOrBlank() && review.text.isNotBlank() && state.canInteract && !mission.completed,
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                )
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 900)
@Composable
private fun HyeminDetailScreenPreview() {
    NolItTheme {
        HyeminDetailScreen(
            uiState = HyeminDetailUiState(loading = false, description = "좋아하는 표정으로 나만의 네 컷 사진을 남겨보세요.", instructions = "1. 셀프 사진관 또는 휴대폰 준비\n2. 좋아하는 표정으로 네 컷 촬영\n3. 아래에 사진과 후기를 등록", mission = HyeminMission(missionBasketId = 12, missionId = 3, title = "셀프 사진 네 컷 찍어보기", imageUrl = "", createdAt = "2026-11-21T18:30:00")),
            review = remember { TextFieldState() },
            onClose = {}, onSave = {}, onPhotoClick = {}, onLike = {}, onComplete = {}, onRetry = {},
        )
    }
}
