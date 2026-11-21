package com.android.sopkathon_android1.feature.hyemin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.sopkathon_android1.core.designsystem.component.ChallengeTab
import com.android.sopkathon_android1.data.repository.HyeminRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class HyeminViewModel @Inject constructor(private val repository: HyeminRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(HyeminUiState())
    val uiState = _uiState.asStateFlow()
    private var loadJob: Job? = null

    init { loadMissions() }

    fun loadMissions() {
        loadJob?.cancel()
        loadJob = viewModelScope.launch {
            _uiState.update { it.copy(loading = true, error = false) }
            try {
                if (_uiState.value.tab == ChallengeTab.MY) {
                    val missions = repository.getMissions()
                    _uiState.update { it.copy(missions = missions, loading = false) }
                } else {
                    val reviews = repository.getReviews()
                    _uiState.update { it.copy(reviews = reviews, loading = false) }
                }
            } catch (e: CancellationException) { throw e
            } catch (e: Exception) { _uiState.update { it.copy(loading = false, error = true) } }
        }
    }

    fun selectTab(tab: ChallengeTab) {
        if (_uiState.value.tab == tab) return
        _uiState.update { it.copy(tab = tab, likeError = false) }
        loadMissions()
    }

    fun toggleReviewLike(reviewId: Long) {
        viewModelScope.launch {
            try {
                val review = repository.toggleReviewLike(reviewId)
                _uiState.update { state -> state.copy(
                    reviews = state.reviews.map { if (it.reviewId == reviewId) review else it },
                    likeError = false,
                ) }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiState.update { it.copy(likeError = true) }
            }
        }
    }
    fun filterCompleted(value: Boolean) { _uiState.update { it.copy(completedOnly = value) } }
}
