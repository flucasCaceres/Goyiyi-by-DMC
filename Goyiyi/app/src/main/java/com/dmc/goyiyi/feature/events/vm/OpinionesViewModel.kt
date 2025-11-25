package com.dmc.goyiyi.feature.events.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dmc.goyiyi.feature.events.data.model.Opinion
import com.dmc.goyiyi.feature.events.data.repository.EventRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OpinionesViewModel @Inject constructor(
    private val repository: EventRepository
) : ViewModel() {

    private val _opiniones = MutableStateFlow<List<Opinion>>(emptyList())
    val opiniones: StateFlow<List<Opinion>> = _opiniones

    fun loadOpiniones(idEvento: String) {
        viewModelScope.launch {
            val data = repository.getOpinionesPorEvento(idEvento)
            _opiniones.value = data
        }
    }
}
