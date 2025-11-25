package com.dmc.goyiyi.core.network

import com.dmc.goyiyi.core.network.dto.LoginRequest
import com.dmc.goyiyi.core.network.dto.LoginResponse
import com.dmc.goyiyi.core.network.dto.RegisterRequest
import com.dmc.goyiyi.core.network.dto.RegisterResponse
import com.dmc.goyiyi.feature.events.data.model.Event
import com.dmc.goyiyi.feature.events.data.remote.model.EventResponse
import com.dmc.goyiyi.feature.map.data.remote.EventoDto
import com.dmc.goyiyi.feature.map.data.remote.UbicacionDto
import com.dmc.goyiyi.feature.events.data.model.Opinion
import com.dmc.goyiyi.feature.events.data.remote.model.OpinionRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.Response

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

    @POST("usuarios/registro")
    suspend fun register(@Body request: RegisterRequest): RegisterResponse

    @POST("usuarios/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    @GET("opiniones/evento/{idEvento}")
    suspend fun getOpinionesPorEvento(
        @Path("idEvento") idEvento: String
    ): List<Opinion>

    @POST("opiniones")
    suspend fun postOpinion(@Body body: OpinionRequest): Response<Void>


}