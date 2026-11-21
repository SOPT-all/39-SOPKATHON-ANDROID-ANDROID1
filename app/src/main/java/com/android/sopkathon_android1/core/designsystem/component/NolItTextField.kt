package com.android.sopkathon_android1.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.maxLength
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.android.sopkathon_android1.core.designsystem.theme.NolItTheme

@Composable
fun NolItTextField(
    state: TextFieldState,
    maxLength: Int,
    placeholder: String,
    modifier: Modifier = Modifier,
    lineLimits: TextFieldLineLimits = TextFieldLineLimits.SingleLine,
    errorMessage: String? = null,
) {
    val singleLine = lineLimits == TextFieldLineLimits.SingleLine
    val isError = state.text.length >= maxLength
    var isFocused by remember { mutableStateOf(false) }
    val isActive = isFocused || state.text.isNotEmpty()
    val captionColor = when {
        isError -> NolItTheme.colors.red
        isActive -> NolItTheme.colors.textPrimary
        else -> NolItTheme.colors.neutral500
    }

    Column(modifier = modifier) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = if (isActive) NolItTheme.colors.primarySoft else NolItTheme.colors.neutral100,
                    shape = RoundedCornerShape(12.dp),
                )
                .then(
                    if (isError || isActive) {
                        Modifier.border(
                            width = 1.dp,
                            color = if (isError) NolItTheme.colors.red else NolItTheme.colors.secondary,
                            shape = RoundedCornerShape(12.dp),
                        )
                    } else {
                        Modifier
                    },
                )
                .padding(
                    start = if (singleLine) 13.dp else 12.dp,
                    end = if (singleLine) 13.dp else 12.dp,
                    top = if (singleLine) 14.dp else 12.dp,
                    bottom = if (singleLine) 14.dp else 88.dp,
                ),
            contentAlignment = if (singleLine) Alignment.CenterStart else Alignment.TopStart,
        ) {
            BasicTextField(
                state = state,
                modifier = Modifier
                    .fillMaxWidth()
                    .onFocusChanged { isFocused = it.isFocused },
                inputTransformation = InputTransformation.maxLength(maxLength),
                lineLimits = lineLimits,
                textStyle = NolItTheme.typography.body2Md.copy(color = NolItTheme.colors.textPrimary),
                cursorBrush = SolidColor(NolItTheme.colors.secondary),
                decorator = { innerTextField ->
                    Box {
                        if (state.text.isEmpty()) {
                            Text(
                                text = placeholder,
                                style = NolItTheme.typography.body2Md,
                                color = NolItTheme.colors.neutral500,
                            )
                        }
                        innerTextField()
                    }
                },
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 5.dp),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (isError && errorMessage != null) {
                Text(
                    text = errorMessage,
                    style = NolItTheme.typography.caption3Rg,
                    color = NolItTheme.colors.red,
                )
                Spacer(modifier = Modifier.width(10.dp))
            }
            Text(
                text = "${state.text.length}/$maxLength",
                style = NolItTheme.typography.caption3Rg,
                color = captionColor,
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 320, heightDp = 228)
@Composable
private fun NolItTextFieldPreview() {
    val shortState = rememberTextFieldState()
    val longState = rememberTextFieldState()

    NolItTheme {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            NolItTextField(
                state = shortState,
                maxLength = 40,
                placeholder = "예) 한정선 만들기",
                errorMessage = "40자 미만으로 작성 해주세요",
            )
            NolItTextField(
                state = longState,
                maxLength = 100,
                placeholder = "예) 어쩌고 저쩌고 이렇게 만들어요",
                lineLimits = TextFieldLineLimits.MultiLine(),
            )
        }
    }
}
