package com.android.sopkathon_android1.feature.hyemin.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.android.sopkathon_android1.R
import com.android.sopkathon_android1.core.designsystem.theme.NolItTheme

@Composable
fun HyeminLatestSort(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.heightIn(min = 48.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(text = "최신순", style = NolItTheme.typography.body2Md)
        Image(
            painter = painterResource(R.drawable.ic_mission_sort_down),
            contentDescription = null,
            modifier = Modifier.size(20.dp),
        )
    }
}
