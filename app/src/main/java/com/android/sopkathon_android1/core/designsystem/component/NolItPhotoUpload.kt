package com.android.sopkathon_android1.core.designsystem.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.android.sopkathon_android1.R
import com.android.sopkathon_android1.core.designsystem.theme.NolItTheme

@Composable
fun NolItPhotoUpload(
    imgUrl: String?,
    modifier: Modifier = Modifier,
) {
    val imageModifier = modifier.clip(RoundedCornerShape(8.dp))

    if (imgUrl.isNullOrBlank()) {
        Image(
            painter = painterResource(R.drawable.img_photo_upload_placeholder),
            contentDescription = null,
            modifier = imageModifier,
            contentScale = ContentScale.Crop,
        )
    } else {
        BaseAsyncImage(
            imgUrl = imgUrl,
            modifier = imageModifier,
            contentDescription = null,
            errorRes = R.drawable.img_photo_upload_placeholder,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun NolItPhotoUploadPreview() {
    NolItTheme {
        NolItPhotoUpload(
            imgUrl = null,
            modifier = Modifier.size(180.dp),
        )
    }
}
