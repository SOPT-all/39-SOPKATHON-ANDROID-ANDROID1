package com.android.sopkathon_android1.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.android.sopkathon_android1.core.designsystem.theme.NolItTheme

enum class ChallengeTab {
    MY,
    OUR,
}

@Composable
fun NolItChallengeTabs(
    selectedTab: ChallengeTab,
    onTabSelected: (ChallengeTab) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(30.dp))
            .background(NolItTheme.colors.secondary)
            .padding(4.dp)
            .selectableGroup(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        ChallengeTab.entries.forEach { tab ->
            val selected = selectedTab == tab
            Box(
                modifier = Modifier
                    .weight(1f)
                    .then(
                        if (selected) {
                            Modifier.background(NolItTheme.colors.primary, RoundedCornerShape(20.dp))
                        } else {
                            Modifier
                        },
                    )
                    .selectable(
                        selected = selected,
                        onClick = { onTabSelected(tab) },
                        role = Role.Tab,
                    )
                    .padding(vertical = 6.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = if (tab == ChallengeTab.MY) "내 도전" else "우리의 도전",
                    modifier = Modifier.fillMaxWidth(),
                    style = if (selected && tab == ChallengeTab.OUR) {
                        NolItTheme.typography.body2Sb
                    } else {
                        NolItTheme.typography.body2Md
                    },
                    color = if (selected) NolItTheme.colors.textPrimary else NolItTheme.colors.background,
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun NolItChallengeTabsPreview() {
    var selectedTab by remember { mutableStateOf(ChallengeTab.OUR) }

    NolItTheme {
        NolItChallengeTabs(
            selectedTab = selectedTab,
            onTabSelected = { selectedTab = it },
            modifier = Modifier.width(320.dp),
        )
    }
}
