package com.android.sopkathon_android1.core.designsystem.extension

import androidx.compose.foundation.clickable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp

fun Modifier.noRippleClickable(
    enabled: Boolean = true,
    onClickLabel: String? = null,
    role: Role? = null,
    onClick: () -> Unit,
): Modifier = clickable(
    interactionSource = null,
    indication = null,
    enabled = enabled,
    onClickLabel = onClickLabel,
    role = role,
    onClick = onClick,
)

fun Modifier.sh1(
    shape: Shape = RectangleShape
): Modifier =
    this.dropShadow(
        shape = shape,
        shadow = Shadow(
            radius = 8.dp,
            spread = 0.dp,
            color = Color.Black.copy(alpha = 0.15f),
            offset = DpOffset(x = (-1).dp, y = 3.dp)
        )
    )
