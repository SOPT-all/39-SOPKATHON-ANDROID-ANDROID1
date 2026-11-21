package com.android.sopkathon_android1.core.designsystem.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.android.sopkathon_android1.R
import com.android.sopkathon_android1.core.designsystem.extension.noRippleClickable
import com.android.sopkathon_android1.core.designsystem.theme.NolItTheme

@Composable
fun NolItHeader(
    modifier: Modifier = Modifier,
    type: NolItHeaderType = NolItHeaderType.DEFAULT,
    onCloseClick: () -> Unit = {},
) {
    when (type) {
        NolItHeaderType.DEFAULT -> NolItDefaultHeader(modifier)
        NolItHeaderType.VERIFICATION -> NolItVerificationHeader(onCloseClick, modifier)
    }
}

enum class NolItHeaderType {
    DEFAULT,
    VERIFICATION,
}

@Composable
private fun NolItDefaultHeader(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
            Image(
                painter = painterResource(R.drawable.img_logo),
                contentDescription = "로고",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .size(26.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(NolItTheme.colors.textPrimary),
            )
        }
        Row(
            modifier = Modifier.width(50.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Image(
                painter = painterResource(R.drawable.ic_bell),
                contentDescription = "알림",
                modifier = Modifier.size(24.dp),
            )
            Image(
                painter = painterResource(R.drawable.ic_setting),
                contentDescription = "설정",
                modifier = Modifier.size(24.dp),
            )
        }
    }
}

@Composable
private fun NolItVerificationHeader(
    onCloseClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(40.dp)
            .padding(horizontal = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image(
            painter = painterResource(R.drawable.ic_close),
            contentDescription = "닫기",
            modifier = Modifier
                .size(22.dp)
                .noRippleClickable(role = Role.Button, onClick = onCloseClick),
        )
        Text(
            text = "임시저장",
            modifier = Modifier.padding(8.dp),
            style = NolItTheme.typography.body1Md,
            color = Color.Black,
        )
    }
}

@Preview(name = "NolItHeader", showBackground = true, widthDp = 360)
@Composable
private fun NolItHeaderPreview() {
    NolItTheme {
        NolItHeader()
    }
}

@Preview(name = "NolItHeader Verification", showBackground = true, widthDp = 360)
@Composable
private fun NolItVerificationHeaderPreview() {
    NolItTheme {
        NolItHeader(type = NolItHeaderType.VERIFICATION)
    }
}
