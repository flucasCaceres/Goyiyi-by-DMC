package com.dmc.goyiyi.feature.map.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dmc.goyiyi.feature.map.data.model.EventMapUiModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class EventMapViewModel : ViewModel() {

    private val _eventPins = MutableStateFlow<List<EventMapUiModel>>(emptyList())
    val eventPins: StateFlow<List<EventMapUiModel>> = _eventPins

    init {
        loadMockEvents()
    }

    private fun loadMockEvents() {
        viewModelScope.launch {

            _eventPins.value = listOf(
                EventMapUiModel(
                    id = 1,
                    nombre = "Festival del Río",
                    lat = -29.1450,
                    lng = -59.2655,
                    estado = "SUCEDIENDO",
                    tipo = "PRINCIPAL",
                    organizador = "Municipalidad de Goya",
                    likes = 120,
                    dislikes = 5
                ),
                EventMapUiModel(
                    id = 2,
                    nombre = "Expo Rural Goya",
                    lat = -29.1420,
                    lng = -59.2600,
                    estado = "CANCELADO",
                    tipo = "SUBEVENTO",
                    organizador = "Sociedad Rural",
                    likes = 30,
                    dislikes = 15
                ),
                EventMapUiModel(
                    id = 3,
                    nombre = "Torneo de Ajedrez",
                    lat = -29.1505,
                    lng = -59.2700,
                    estado = "REPROGRAMADO",
                    tipo = "PRINCIPAL",
                    organizador = "Club Social",
                    likes = 50,
                    dislikes = 2
                ),
                EventMapUiModel(
                    id = 4,
                    nombre = "Mercado Artesanal",
                    lat = -29.1380,
                    lng = -59.2605,
                    estado = "PROGRAMADO",
                    tipo = "PRINCIPAL",
                    organizador = "Asoc. Artesanos",
                    likes = 200,
                    dislikes = 40
                ),
                EventMapUiModel(
                    id = 5,
                    nombre = "Puesto de Entradas",
                    lat = -29.1400,
                    lng = -59.2680,
                    estado = "PAUSADO",
                    tipo = "PUNTO DE VENTA ENTRADAS",
                    organizador = "Goyiyi Tickets",
                    likes = 10,
                    dislikes = 1
                )
            )
        }
    }
}
