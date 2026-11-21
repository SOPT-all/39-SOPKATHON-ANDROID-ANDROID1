package com.android.sopkathon_android1.core.designsystem.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.android.sopkathon_android1.R
import com.android.sopkathon_android1.core.designsystem.extension.noRippleClickable
import com.android.sopkathon_android1.core.designsystem.theme.NolItTheme

@Composable
fun NolItMissionCompleteHypeButton(
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val shape = RoundedCornerShape(16.dp)
    val backgroundColor = if (isSelected) NolItTheme.colors.neutral600 else NolItTheme.colors.neutral25
    val borderColor = if (isSelected) backgroundColor else NolItTheme.colors.border
    val textColor = if (isSelected) NolItTheme.colors.background else NolItTheme.colors.neutral400
    val iconColor = if (isSelected) NolItTheme.colors.primary else NolItTheme.colors.neutral400
    val iconRes = if (isSelected) {
        R.drawable.ic_mission_complete_hype_selected
    } else {
        R.drawable.ic_mission_complete_hype_unselected
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape)
            .background(backgroundColor)
            .border(width = 1.dp, color = borderColor, shape = shape)
            .semantics { selected = isSelected }
            .noRippleClickable(role = Role.Button, onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 18.dp),
        contentAlignment = Alignment.Center,
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Image(
                painter = painterResource(iconRes),
                contentDescription = null,
                colorFilter = ColorFilter.tint(iconColor),
                modifier = Modifier.size(width = 20.dp, height = 34.dp),
            )
            Text(
                text = "이런 도전 좋아요!",
                style = NolItTheme.typography.h2Sb,
                color = textColor,
                maxLines = 1,
            )
        }
    }
}

@Preview(name = "NolItMissionCompleteHypeButton", showBackground = true, widthDp = 294)
@Composable
private fun NolItMissionCompleteHypeButtonPreview() {
    NolItTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            NolItMissionCompleteHypeButton(isSelected = false, onClick = {})
            NolItMissionCompleteHypeButton(isSelected = true, onClick = {})
        }
    }
}
