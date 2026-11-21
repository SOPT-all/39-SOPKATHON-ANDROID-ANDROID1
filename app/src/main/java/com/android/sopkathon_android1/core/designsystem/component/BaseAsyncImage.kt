package com.android.sopkathon_android1.core.designsystem.component

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import coil3.compose.AsyncImage
import com.android.sopkathon_android1.R

@Composable
fun BaseAsyncImage(
    imgUrl: String?,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    contentScale: ContentScale = ContentScale.Crop,
    @DrawableRes errorRes: Int = R.drawable.img_card_error,
) {
    AsyncImage(
        model = imgUrl,
        contentDescription = contentDescription,
        contentScale = contentScale,
        placeholder = painterResource(errorRes),
        error = painterResource(errorRes),
        modifier = modifier,
    )
}
