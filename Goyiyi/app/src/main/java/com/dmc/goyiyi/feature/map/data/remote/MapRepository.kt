package com.dmc.goyiyi.feature.map.data.remote

import com.dmc.goyiyi.core.network.ApiService
import com.dmc.goyiyi.feature.map.data.model.EventMapUiModel
import javax.inject.Inject

class MapRepository @Inject constructor(
    private val api: ApiService
) {

    suspend fun getEventPins(): List<EventMapUiModel> {
        val eventos = api.getEventosDto()
        val ubicaciones = api.getUbicaciones()

        if (eventos.isEmpty() || ubicaciones.isEmpty()) return emptyList()

        val ubicacionesPorEvento = ubicaciones.groupBy { it.idEvento }

        return eventos.flatMap { evento ->
            val ubicList = ubicacionesPorEvento[evento.id] ?: emptyList()

            ubicList.map { u ->
                EventMapUiModel(
                    id = evento.id,
                    nombre = evento.nombre,
                    lat = u.lat,
                    lng = u.lng,
                    estado = evento.estado,
                    tipo = evento.tipo,
                    organizador = evento.organizador,
                    likes = evento.likes,
                    dislikes = evento.dislikes
                )
            }
        }
    }
}
