package com.android.sopkathon_android1.feature.hyemin.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.sopkathon_android1.core.designsystem.component.ChallengeTab
import com.android.sopkathon_android1.core.designsystem.component.NolItToggle
import com.android.sopkathon_android1.core.designsystem.theme.NolItTheme

@Composable
fun HyeminMissionListHeader(
    selectedTab: ChallengeTab,
    completedOnly: Boolean,
    onCompletedChanged: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(if (selectedTab == ChallengeTab.MY) "담은 미션" else "전체 미션", style = NolItTheme.typography.body1Sb.copy(fontSize = 18.sp, lineHeight = 25.sp))
        if (selectedTab == ChallengeTab.MY) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("완료", style = NolItTheme.typography.caption1Md)
                Spacer(Modifier.width(8.dp))
                NolItToggle(
                    checked = completedOnly,
                    onCheckedChange = onCompletedChanged,
                )
            }
        } else {
            HyeminLatestSort()

        }
    }
}
