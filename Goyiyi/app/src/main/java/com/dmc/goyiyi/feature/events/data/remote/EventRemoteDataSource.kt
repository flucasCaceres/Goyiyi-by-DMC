package com.dmc.goyiyi.feature.events.data.remote

import com.dmc.goyiyi.core.network.ApiService
import com.dmc.goyiyi.feature.events.data.model.Event

class EventRemoteDataSource(private val api: ApiService) {
    suspend fun fetchEvents() = api.getEvents()

    suspend fun fetchEventsDetail(id: String): Event {
        return api.getEventDetail(id).data
            ?: throw IllegalStateException("Evento no encontrado")
    }

}
