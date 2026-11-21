package com.android.sopkathon_android1.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.android.sopkathon_android1.R
import com.android.sopkathon_android1.core.designsystem.extension.noRippleClickable
import com.android.sopkathon_android1.core.designsystem.extension.sh1
import com.android.sopkathon_android1.core.designsystem.theme.NolItTheme

@Composable
fun HomeMissionCard(
    imgUrl: String?,
    title: String,
    content: String,
    onCardClick: () ->  Unit,
    onPlusClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .noRippleClickable(onClick = onCardClick)
            .fillMaxWidth()
            .sh1(shape = RoundedCornerShape(8.dp))
            .clip(shape = RoundedCornerShape(8.dp))
            .background(color = NolItTheme.colors.background)
    ) {
        BaseAsyncImage(
            imgUrl = imgUrl,
            modifier = Modifier
                .aspectRatio(1f)
                .clip(RoundedCornerShape(8.dp)),
        )

        Spacer(modifier = Modifier.height(9.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column {
                Text(
                    text = title,
                    color = NolItTheme.colors.textPrimary,
                    style = NolItTheme.typography.caption1B,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )

                Text(
                    text = content,
                    color = NolItTheme.colors.textPrimary,
                    style = NolItTheme.typography.caption2Rg,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }

            Icon(
                painter = painterResource(id = R.drawable.ic_plus),
                contentDescription = null,
                tint = NolItTheme.colors.textPrimary,
                modifier = Modifier
                    .padding(vertical = 3.dp)
                    .noRippleClickable(onClick = onPlusClick)
                    .background(
                        color = NolItTheme.colors.primary,
                        shape = CircleShape
                    )
                    .padding(all = 7.dp)
                    .size(14.dp)
            )
        }

        Spacer(modifier = Modifier.height(10.dp))
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeMissionCardPreview(){
    NolItTheme {
        Column(
            modifier = Modifier
                .background(color = NolItTheme.colors.surface)
                .padding(20.dp)
        ) {
            HomeMissionCard(
                imgUrl = null,
                title = "1위 미션",
                content = "셀프사진 네컷 찍어보기",
                onPlusClick = {},
                onCardClick = {},
                modifier = Modifier.width(156.dp)
            )
        }
    }
}
