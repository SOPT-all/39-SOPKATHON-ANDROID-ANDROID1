package com.android.sopkathon_android1.feature.main

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
import com.android.sopkathon_android1.data.model.MainItem
import kotlinx.serialization.Serializable

@Serializable
data object MainRoute

@Composable
fun MainRouteScreen(viewModel: MainViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    MainScreen(uiState = uiState, onRetry = viewModel::loadItems)
}

@Composable
fun MainScreen(
    uiState: MainUiState,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(text = "화면을 선택해 주세요")
        Text(text = "임시 목록 예제")
        when (uiState) {
            MainUiState.Loading -> CircularProgressIndicator()
            is MainUiState.Success -> LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                items(items = uiState.items, key = { it.id }) { item ->
                    Text(text = item.title)
                }
            }
            MainUiState.Empty -> {
                Text(text = "표시할 항목이 없어요")
                Button(onClick = onRetry) { Text(text = "새로고침") }
            }
            MainUiState.Error -> {
                Text(text = "목록을 불러오지 못했어요")
                Button(onClick = onRetry) { Text(text = "다시 시도") }
            }
        }
    }
}

@Preview(name = "로딩", showBackground = true)
@Composable
private fun MainLoadingPreview() {
    SOPKATHONANDROID1Theme { MainScreen(MainUiState.Loading, onRetry = {}) }
}

@Preview(name = "성공", showBackground = true)
@Composable
private fun MainSuccessPreview() {
    SOPKATHONANDROID1Theme {
        MainScreen(
            MainUiState.Success(listOf(MainItem("sample-1", "샘플 항목 1"))),
            onRetry = {},
        )
    }
}

@Preview(name = "빈 목록", showBackground = true)
@Composable
private fun MainEmptyPreview() {
    SOPKATHONANDROID1Theme { MainScreen(MainUiState.Empty, onRetry = {}) }
}

@Preview(name = "오류", showBackground = true)
@Composable
private fun MainErrorPreview() {
    SOPKATHONANDROID1Theme { MainScreen(MainUiState.Error, onRetry = {}) }
}
