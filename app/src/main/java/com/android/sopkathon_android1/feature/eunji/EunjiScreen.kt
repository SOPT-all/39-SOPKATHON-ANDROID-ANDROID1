package com.android.sopkathon_android1.feature.eunji

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import kotlinx.serialization.Serializable

@Serializable
data object EunjiRoute

/**
 * API 데이터를 화면에 연결할 때의 Hilt ViewModel 사용 예시:
 * ```kotlin
 * class EunjiRepository @Inject constructor(
 *     private val api: ResourceApi,
 * ) {
 *     suspend fun load(id: Long) = api.getResource(id)
 * }
 *
 * @HiltViewModel
 * class EunjiViewModel @Inject constructor(
 *     private val repository: EunjiRepository,
 * ) : ViewModel()
 *
 * @Composable
 * fun EunjiScreen(viewModel: EunjiViewModel = hiltViewModel()) { Text("은지") }
 * ```
 * `hiltViewModel()`을 사용하려면 `androidx.hilt:hilt-navigation-compose` 의존성을 추가한다.
 */
@Composable
fun EunjiScreen() {
    Text(text = "은지")
}
