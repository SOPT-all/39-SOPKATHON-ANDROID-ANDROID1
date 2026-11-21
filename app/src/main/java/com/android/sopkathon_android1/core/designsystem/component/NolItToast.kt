package com.android.sopkathon_android1.core.designsystem.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.android.sopkathon_android1.R
import com.android.sopkathon_android1.core.designsystem.theme.NolItTheme

enum class NolItToastType(val message: String, val iconRes: Int) {
    SUCCESS("AI인증 완료되었습니다", R.drawable.ic_toast_success),
    FAILURE("AI인증에 실패했습니다", R.drawable.ic_toast_error),
    LOADING("AI 판독 중 ...", R.drawable.ic_toast_loading),
    MISSING_FIELDS("모든 항목을 입력해주세요.", R.drawable.ic_toast_error),
}

@Composable
fun NolItToast(
    type: NolItToastType,
    modifier: Modifier = Modifier,
) {
    val isError = type == NolItToastType.FAILURE || type == NolItToastType.MISSING_FIELDS

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(
                if (isError) NolItTheme.colors.red else NolItTheme.colors.textPrimary,
            )
            .padding(horizontal = 5.dp, vertical = 4.dp),
        contentAlignment = Alignment.Center,
    ) {
        if (type == NolItToastType.LOADING) {
            Image(
                painter = painterResource(type.iconRes),
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .size(20.dp),
            )
            Text(
                text = type.message,
                style = NolItTheme.typography.caption1Md,
                color = NolItTheme.colors.background,
                maxLines = 1,
            )
        } else {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Image(
                    painter = painterResource(type.iconRes),
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                )
                Spacer(modifier = Modifier.width(if (type == NolItToastType.MISSING_FIELDS) 7.dp else 10.dp))
                Text(
                    text = type.message,
                    style = NolItTheme.typography.caption1Md,
                    color = NolItTheme.colors.background,
                    maxLines = 1,
                )
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 215, heightDp = 194)
@Composable
private fun NolItToastPreview() {
    NolItTheme {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            NolItToast(type = NolItToastType.SUCCESS)
            NolItToast(type = NolItToastType.FAILURE)
            NolItToast(type = NolItToastType.LOADING)
            NolItToast(type = NolItToastType.MISSING_FIELDS)
        }
    }
}
