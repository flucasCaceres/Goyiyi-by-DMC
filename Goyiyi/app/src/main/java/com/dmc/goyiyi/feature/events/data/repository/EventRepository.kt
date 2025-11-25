package com.dmc.goyiyi.feature.events.data.repository

import com.dmc.goyiyi.feature.events.data.model.Event
import com.dmc.goyiyi.feature.events.data.model.Opinion
import com.dmc.goyiyi.feature.events.data.remote.EventRemoteDataSource
import com.dmc.goyiyi.feature.events.data.remote.model.OpinionRequest
import com.google.firebase.auth.FirebaseAuth


class EventRepository(
    private val remote: EventRemoteDataSource
) {
    init {
        println("🚨 EVENT REPOSITORY CREADO → instancia = $this")
    }
    suspend fun getEvents(): List<Event> =
        remote.fetchEvents()

    suspend fun getEventDetail(id: String): Event =
        remote.fetchEventsDetail(id)

    suspend fun getOpinionesPorEvento(idEvento: String): List<Opinion> =
        remote.fetchOpinionesPorEvento(idEvento)

    suspend fun postOpinion(idEvento: String, comentario: String, valoracion: Int) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
            ?: throw IllegalStateException("Usuario no autenticado")

        val request = OpinionRequest(
            idEvento = idEvento,
            idUsuario = userId,
            comentarios = comentario,
            valoracion = valoracion,
            estado = "POSTEADO",
            img = "https://misimagenes.com/fotos/default.jpg"
        )

        remote.postOpinion(request)
    }
}

