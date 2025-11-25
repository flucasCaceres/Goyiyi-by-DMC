package com.dmc.goyiyi.feature.events.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dmc.goyiyi.feature.events.data.model.Event
import com.dmc.goyiyi.feature.events.data.model.Opinion
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

    // NUEVO: flujo solo para las opiniones del evento
    private val _opinionesPreview = MutableStateFlow<List<Opinion>>(emptyList())
    val opinionesPreview: StateFlow<List<Opinion>> = _opinionesPreview

    fun loadEvent(id: String) {
        viewModelScope.launch {
            _state.value = DetailUiState.Loading
            try {
                val data = repository.getEventDetail(id)
                _state.value = DetailUiState.Success(data)

                // cuando el evento carga OK, disparamos carga de opiniones
                loadOpiniones(id)
            } catch (e: Exception) {
                _state.value = DetailUiState.Error(e.message ?: "Error desconocido")
            }
        }
    }

    // carga de opiniones en paralelo, sin romper la pantalla si falla
    private fun loadOpiniones(idEvento: String) {
        viewModelScope.launch {
            try {
                val opiniones = repository.getOpinionesPorEvento(idEvento)
                _opinionesPreview.value = opiniones.take(3) // mostramos solo las primeras 3
            } catch (e: Exception) {
                // Por ahora no propagamos error a UI; si querés luego ponemos un mensaje tipo
                // "No se pudieron cargar las opiniones"
            }
        }
    }
}

sealed class DetailUiState {
    object Loading : DetailUiState()
    data class Success(val event: Event) : DetailUiState()
    data class Error(val message: String) : DetailUiState()
}
