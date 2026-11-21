package com.android.sopkathon_android1.feature.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.sopkathon_android1.data.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: MainRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow<MainUiState>(MainUiState.Loading)
    val uiState = _uiState.asStateFlow()
    private var loadJob: Job? = null

    init {
        loadItems()
    }

    fun loadItems() {
        loadJob?.cancel()
        loadJob = viewModelScope.launch {
            _uiState.value = MainUiState.Loading
            _uiState.value = try {
                val items = repository.getItems()
                if (items.isEmpty()) MainUiState.Empty else MainUiState.Success(items)
            } catch (exception: CancellationException) {
                throw exception
            } catch (exception: Exception) {
                MainUiState.Error
            }
        }
    }
}
