package com.dmc.goyiyi.core.network

import com.dmc.goyiyi.feature.events.data.model.Event
import com.dmc.goyiyi.feature.events.data.remote.model.EventResponse
import com.dmc.goyiyi.feature.map.data.remote.EventoDto
import com.dmc.goyiyi.feature.map.data.remote.UbicacionDto
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("eventos")
    suspend fun getEvents(): List<Event>

    @GET("eventos/{id}")
    suspend fun getEventDetail(
        @Path("id") id: String
    ): EventResponse
    @GET("eventos")
    suspend fun getEventosDto(): List<EventoDto>
    @GET("ubicaciones")
    suspend fun getUbicaciones(): List<UbicacionDto>

    @GET("ubicaciones/{idEvento}")
    suspend fun getUbicacionesPorEvento(
        @Path("idEvento") idEvento: String
    ): List<UbicacionDto>
}