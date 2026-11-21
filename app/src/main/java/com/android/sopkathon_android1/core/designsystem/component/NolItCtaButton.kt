package com.android.sopkathon_android1.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.android.sopkathon_android1.core.designsystem.extension.noRippleClickable
import com.android.sopkathon_android1.core.designsystem.theme.NolItTheme

@Composable
fun CtaButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    val shape = RoundedCornerShape(30.dp)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape)
            .background(if (enabled) NolItTheme.colors.secondary else NolItTheme.colors.surface)
            .then(
                if (enabled) Modifier
                else Modifier.border(width = 1.dp, color = NolItTheme.colors.border, shape = shape)
            )
            .noRippleClickable(enabled = enabled, role = Role.Button, onClick = onClick)
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            modifier = Modifier.padding(14.dp),
            text = text,
            style = if (enabled) NolItTheme.typography.body1Sb else NolItTheme.typography.body1Md,
            color = if (enabled) NolItTheme.colors.background else NolItTheme.colors.textPrimary,
        )
    }
}

@Preview(name = "활성")
@Composable
private fun CtaButtonEnabledPreview() {
    NolItTheme {
        CtaButton(text = "선택한 미션 수행하기", onClick = {})
    }
}

@Preview(name = "비활성")
@Composable
private fun CtaButtonDisabledPreview() {
    NolItTheme {
        CtaButton(text = "선택한 미션 수행하기", onClick = {}, enabled = false)
    }
}
