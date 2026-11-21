package com.android.sopkathon_android1.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.android.sopkathon_android1.R
import com.android.sopkathon_android1.core.designsystem.theme.NolItTheme

enum class NolItBottomTab(val label: String, val iconRes: Int) {
    HOME("홈", R.drawable.ic_nav_home),
    CHALLENGE("도전", R.drawable.ic_nav_challenge),
    CREATE("미션 제작", R.drawable.ic_nav_create),
    MY("MY", R.drawable.ic_nav_my),
}

@Composable
fun NolItBottomNavigationBar(
    selectedTab: NolItBottomTab?,
    onTabSelected: (NolItBottomTab) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(NolItTheme.colors.background)
            .navigationBarsPadding(),
    ) {
        HorizontalDivider(thickness = 1.dp, color = NolItTheme.colors.border)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(67.dp)
                .padding(horizontal = 26.dp)
                .selectableGroup(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            NolItBottomTab.entries.forEach { tab ->
                NolItBottomNavigationBarItem(
                    tab = tab,
                    selected = selectedTab == tab,
                    onClick = { onTabSelected(tab) },
                )
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 68)
@Composable
private fun NolItBottomNavigationBarPreview() {
    var selectedTab by remember { mutableStateOf(NolItBottomTab.HOME) }

    NolItTheme {
        NolItBottomNavigationBar(
            selectedTab = selectedTab,
            onTabSelected = { selectedTab = it },
        )
    }
}
