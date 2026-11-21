package com.android.sopkathon_android1.feature.eunji

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.sopkathon_android1.data.repository.EunjiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class EunjiViewModel @Inject constructor(
    private val repository: EunjiRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow<EunjiUiState>(EunjiUiState.Loading)
    val uiState = _uiState.asStateFlow()
    private var loadJob: Job? = null

    init {
        loadItems()
    }

    fun loadItems() {
        loadJob?.cancel()
        loadJob = viewModelScope.launch {
            _uiState.value = EunjiUiState.Loading
            _uiState.value = try {
                val items = repository.getItems()
                if (items.isEmpty()) EunjiUiState.Empty else EunjiUiState.Success(items)
            } catch (exception: CancellationException) {
                throw exception
            } catch (exception: Exception) {
                EunjiUiState.Error
            }
        }
    }
}
