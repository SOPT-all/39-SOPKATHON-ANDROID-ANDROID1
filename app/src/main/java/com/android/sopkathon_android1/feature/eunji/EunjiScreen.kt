package com.android.sopkathon_android1.feature.eunji

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.android.sopkathon_android1.core.designsystem.theme.SOPKATHONANDROID1Theme
import com.android.sopkathon_android1.data.model.EunjiItem

@Composable
fun EunjiRoute(viewModel: EunjiViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    EunjiScreen(uiState = uiState, onRetry = viewModel::loadItems)
}

@Composable
fun EunjiScreen(
    uiState: EunjiUiState,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(text = "은지")
        Text(text = "임시 목록 예제")
        when (uiState) {
            EunjiUiState.Loading -> CircularProgressIndicator()
            is EunjiUiState.Success -> LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                items(items = uiState.items, key = { it.id }) { item ->
                    Text(text = item.title)
                }
            }
            EunjiUiState.Empty -> {
                Text(text = "표시할 항목이 없어요")
                Button(onClick = onRetry) { Text(text = "새로고침") }
            }
            EunjiUiState.Error -> {
                Text(text = "목록을 불러오지 못했어요")
                Button(onClick = onRetry) { Text(text = "다시 시도") }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun EunjiScreenPreview() {
    SOPKATHONANDROID1Theme {
        EunjiScreen(
            uiState = EunjiUiState.Success(
                items = listOf(EunjiItem(id = "sample-1", title = "샘플 항목 1")),
            ),
            onRetry = {},
        )
    }
}
