package com.dmc.goyiyi.feature.map.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dmc.goyiyi.feature.map.data.model.EventMapUiModel
import com.dmc.goyiyi.feature.map.data.remote.MapRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EventMapViewModel @Inject constructor(
    private val repository: MapRepository
) : ViewModel() {

    private val _eventPins = MutableStateFlow<List<EventMapUiModel>>(emptyList())
    val eventPins: StateFlow<List<EventMapUiModel>> get() = _eventPins

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> get() = _loading

    private val _error = MutableStateFlow("")
    val error: StateFlow<String> get() = _error

    init {
        loadRealEvents()
    }

    fun loadRealEvents() {
        viewModelScope.launch {
            _loading.value = true                // <--- IMPORTANTE
            try {
                _eventPins.value = repository.getEventPins()
            } catch (e: Exception) {
                _error.value = e.message ?: "Error desconocido"
            } finally {
                _loading.value = false
            }
        }
    }
}
