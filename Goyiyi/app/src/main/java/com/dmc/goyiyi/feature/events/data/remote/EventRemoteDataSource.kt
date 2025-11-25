package com.dmc.goyiyi.feature.events.data.remote

import com.dmc.goyiyi.core.network.ApiService
import com.dmc.goyiyi.feature.events.data.model.Opinion
import com.dmc.goyiyi.feature.events.data.remote.model.OpinionRequest

class EventRemoteDataSource(private val api: ApiService) {

    suspend fun fetchEvents() = api.getEvents()

    suspend fun fetchEventsDetail(id: String) =
        api.getEventDetail(id).data ?: throw IllegalStateException("Evento no encontrado")

    suspend fun fetchOpinionesPorEvento(idEvento: String): List<Opinion> =
        api.getOpinionesPorEvento(idEvento)

    suspend fun postOpinion(request: OpinionRequest) {
        val res = api.postOpinion(request)

        println("🔥 REMOTE POST CODE = ${res.code()}")
        println("🔥 REMOTE POST SUCCESS = ${res.isSuccessful}")

        if (!res.isSuccessful) {
            throw IllegalStateException("Error POST Opinión: ${res.code()}")
        }
    }
}
