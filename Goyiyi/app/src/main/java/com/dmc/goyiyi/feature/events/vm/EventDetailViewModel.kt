package com.dmc.goyiyi.feature.events.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dmc.goyiyi.feature.events.data.model.Event
import com.dmc.goyiyi.feature.events.data.repository.EventRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EventDetailViewModel @Inject constructor(
    private val repository: EventRepository
) : ViewModel() {

    private val _state = MutableStateFlow<DetailUiState>(DetailUiState.Loading)
    val state: StateFlow<DetailUiState> = _state

    fun loadEvent(id: Int) {
        viewModelScope.launch {
            try {
                _state.value = DetailUiState.Loading
                val data = repository.getEventDetail(id)
                _state.value = DetailUiState.Success(data)
            } catch (e: Exception) {
                _state.value = DetailUiState.Error(e.message ?: "Error desconocido")
            }
        }
    }
}

sealed class DetailUiState {
    object Loading : DetailUiState()
    data class Success(val event: Event) : DetailUiState()
    data class Error(val message: String) : DetailUiState()
}
