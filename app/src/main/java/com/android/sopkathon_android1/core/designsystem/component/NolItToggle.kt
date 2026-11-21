package com.android.sopkathon_android1.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.android.sopkathon_android1.core.designsystem.theme.NolItTheme

@Composable
fun NolItToggle(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    Box(
        modifier = modifier
            .size(width = 40.dp, height = 20.dp)
            .alpha(if (enabled) 1f else 0.5f)
            .background(NolItTheme.colors.border, CircleShape)
            .toggleable(
                value = checked,
                enabled = enabled,
                role = Role.Switch,
                interactionSource = null,
                indication = null,
                onValueChange = onCheckedChange,
            )
            .padding(2.dp),
        contentAlignment = if (checked) Alignment.CenterEnd else Alignment.CenterStart,
    ) {
        Box(
            modifier = Modifier
                .size(16.dp)
                .background(
                    color = if (checked) NolItTheme.colors.secondary else NolItTheme.colors.background,
                    shape = CircleShape,
                ),
        )
    }
}

@Preview(showBackground = true, name = "토글 켜짐 · 꺼짐")
@Composable
private fun NolItTogglePreview() {
    var checked by remember { mutableStateOf(true) }
    var unchecked by remember { mutableStateOf(false) }

    NolItTheme {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            NolItToggle(checked = checked, onCheckedChange = { checked = it })
            NolItToggle(checked = unchecked, onCheckedChange = { unchecked = it })
        }
    }
}
