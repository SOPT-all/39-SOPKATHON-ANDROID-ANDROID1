package com.android.sopkathon_android1.feature.eunji.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.android.sopkathon_android1.core.designsystem.theme.NolItTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MissionDetailBottomSheet(
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    sheetState: SheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
    content: @Composable ColumnScope.() -> Unit,
) {
    val shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp)

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        modifier = modifier,
        sheetState = sheetState,
        shape = shape,
        containerColor = NolItTheme.colors.background,
        scrimColor = Color.Black.copy(alpha = 0.5f),
        dragHandle = null,
    ) {
        MissionDetailBottomSheetDragHandle(modifier = Modifier.align(Alignment.CenterHorizontally))
        content()
    }
}

@Composable
private fun MissionDetailBottomSheetDragHandle(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .padding(top = 12.dp)
            .size(width = 60.dp, height = 3.dp)
            .background(
                color = NolItTheme.colors.neutral400,
                shape = CircleShape,
            ),
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
private fun MissionDetailBottomSheetPreview() {
    NolItTheme {
        MissionDetailBottomSheet(
            onDismissRequest = {},
        ) {
            Text(
                text = "여기에 미션 상세 내용 추가",
                style = NolItTheme.typography.body2Md,
                color = NolItTheme.colors.textPrimary,
                modifier = Modifier.padding(20.dp),
            )
        }
    }
}
