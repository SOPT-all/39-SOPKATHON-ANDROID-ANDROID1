package com.android.sopkathon_android1.feature.hyemin

import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.android.sopkathon_android1.R
import com.android.sopkathon_android1.core.designsystem.component.*
import com.android.sopkathon_android1.core.designsystem.theme.NolItTheme
import com.android.sopkathon_android1.core.designsystem.extension.noRippleClickable
import com.android.sopkathon_android1.data.model.HyeminMission

@Composable
fun HyeminDetailRoute(
    onBack: () -> Unit,
    viewModel: HyeminDetailViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val picker = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        uri?.let { viewModel.setPhoto(it.toString()) }
    }
    BackHandler(enabled = state.submitting) { /* 완료 요청 중에는 화면을 유지한다. */ }
    LaunchedEffect(state.finished) { if (state.finished) onBack() }
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
    Box(Modifier.fillMaxSize().background(NolItTheme.colors.background)) {
        when {
            uiState.loading -> CircularProgressIndicator(Modifier.align(Alignment.Center))
            uiState.mission == null -> Column(Modifier.align(Alignment.Center)) {
                Text("미션을 불러오지 못했어요")
                Text("다시 시도", Modifier.noRippleClickable(role = androidx.compose.ui.semantics.Role.Button, onClick = onRetry).padding(12.dp), color = NolItTheme.colors.secondary)
                Text("닫기", Modifier.noRippleClickable(role = androidx.compose.ui.semantics.Role.Button, onClick = onClose).padding(12.dp), color = NolItTheme.colors.secondary)
            }
            else -> HyeminDetailContent(uiState, review, onClose, onSave, onPhotoClick, onLike, onComplete)
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
    Column(Modifier
        .fillMaxSize()
        .imePadding()
        .verticalScroll(rememberScrollState())) {
        Row(
            Modifier
                .fillMaxWidth()
                .height(40.dp)
                .padding(horizontal = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(Modifier.size(40.dp).noRippleClickable(enabled = !state.submitting, role = androidx.compose.ui.semantics.Role.Button, onClick = onClose), contentAlignment = Alignment.Center) {
                Image(painterResource(R.drawable.ic_mission_close), "닫기", Modifier.size(22.dp))
            }
            Box(Modifier.height(40.dp).noRippleClickable(enabled = !state.submitting, role = androidx.compose.ui.semantics.Role.Button, onClick = onSave).padding(horizontal = 8.dp), contentAlignment = Alignment.Center) {
                Text(
                    "임시저장",
                    style = NolItTheme.typography.body1Md.copy(fontSize = 18.sp, lineHeight = 25.sp)
                )
            }
        }
        Column(Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)) {
            Spacer(Modifier.height(16.dp))
            Text(
                "미션을 인증해주세요!",
                style = NolItTheme.typography.h2Sb.copy(fontSize = 24.sp, lineHeight = 33.sp)
            )
            Spacer(Modifier.height(28.dp))
            Text(mission.title, color = NolItTheme.colors.neutral800, style = sectionStyle())
            Spacer(Modifier.height(36.dp))
            Text("대표 사진", style = sectionStyle())
            Text(
                "갤러리에서 1장 이상 추가",
                color = NolItTheme.colors.neutral500,
                style = NolItTheme.typography.body2Md
            )
            Spacer(Modifier.height(36.dp))
            NolItPhotoUpload(
                state.photo,
                Modifier
                    .align(Alignment.CenterHorizontally)
                    .size(180.dp)
                    .noRippleClickable(
                        enabled = !state.submitting,
                        onClickLabel = "갤러리에서 사진 선택",
                        onClick = onPhotoClick
                    )
            )
            Spacer(Modifier.height(36.dp))
            Text("한 줄 후기", style = sectionStyle())
            Spacer(Modifier.height(8.dp))
            NolItTextField(
                state = review,
                maxLength = 100,
                placeholder = "예) 어쩌고 저쩌고 이렇게 만들어요",
                lineLimits = TextFieldLineLimits.MultiLine()
            )
            Spacer(Modifier.height(12.dp))
            Row(
                Modifier
                    .align(Alignment.CenterHorizontally)
                    .widthIn(max = 262.dp)
                    .fillMaxWidth()
                    .height(70.dp)
                    .background(
                        if (state.liked) NolItTheme.colors.primarySoft else NolItTheme.colors.neutral25,
                        RoundedCornerShape(16.dp)
                    )
                    .border(
                        1.dp,
                        if (state.liked) NolItTheme.colors.secondary else NolItTheme.colors.border,
                        RoundedCornerShape(16.dp)
                    )
                    .noRippleClickable(
                        enabled = !state.submitting,
                        role = androidx.compose.ui.semantics.Role.Checkbox,
                        onClick = onLike
                    ),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Image(
                    painterResource(R.drawable.ic_mission_hype),
                    null,
                    Modifier.size(width = 20.dp, height = 34.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    "이런 도전 좋아요!",
                    color = if (state.liked) NolItTheme.colors.secondary else NolItTheme.colors.neutral400,
                    style = NolItTheme.typography.h2Sb.copy(fontSize = 24.sp, lineHeight = 33.sp)
                )
            }
            Spacer(Modifier.height(32.dp))
            CtaButton(
                text = if (state.submitting) "인증 중..." else "도전 완료하기",
                onClick = onComplete,
                enabled = state.photo != null && review.text.isNotBlank() && !state.submitting,
                modifier = Modifier.fillMaxWidth().height(50.dp),
            )
            Spacer(Modifier.height(20.dp))
        }
    }
}

@Composable
private fun sectionStyle(): TextStyle =
    NolItTheme.typography.body1Sb.copy(fontSize = 18.sp, lineHeight = 25.sp)

@Preview(showBackground = true, widthDp = 360, heightDp = 900)
@Composable
private fun HyeminDetailScreenPreview() {
    NolItTheme {
        HyeminDetailScreen(
            uiState = HyeminDetailUiState(loading = false, mission = HyeminMission(missionBasketId = 12, missionId = 3, title = "셀프 사진 네 컷 찍어보기", imageUrl = "", createdAt = "2026-11-21T18:30:00")),
            review = remember { TextFieldState() },
            onClose = {}, onSave = {}, onPhotoClick = {}, onLike = {}, onComplete = {}, onRetry = {},
        )
    }
}
