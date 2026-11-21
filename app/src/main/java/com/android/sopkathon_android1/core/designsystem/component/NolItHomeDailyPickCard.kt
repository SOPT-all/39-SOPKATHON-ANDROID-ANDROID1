package com.android.sopkathon_android1.core.designsystem.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.android.sopkathon_android1.R
import com.android.sopkathon_android1.core.designsystem.extension.noRippleClickable
import com.android.sopkathon_android1.core.designsystem.theme.NolItTheme

@Composable
fun NolItHomeDailyPickCard(
    title: String,
    @DrawableRes imgRes: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(30.dp))
            .noRippleClickable(role = Role.Button, onClick = onClick),
    ) {
        Image(
            painter = painterResource(id = imgRes),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .matchParentSize()
                .aspectRatio(280f/240f)
                .drawWithContent {
                    drawContent()
                    drawRect(Color.Black.copy(alpha = 0.4f))
                },
        )

        Text(
            text = title,
            style = NolItTheme.typography.h2Sb,
            color = NolItTheme.colors.background,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 16.dp, bottom = 22.dp)
        )
    }
}

@Preview(name = "NolItHomeDailyPickCard", showBackground = true, widthDp = 280)
@Composable
private fun NolItHomeDailyPickCardPreview() {
    NolItTheme {
        NolItHomeDailyPickCard(
            title = "치즈 슬랑이 만져보기",
            imgRes = R.drawable.img_home_recommend_1,
            onClick = {},
            modifier = Modifier.size(280.dp, 240.dp)
        )
    }
}
