package com.dmc.goyiyi.core.network

import com.dmc.goyiyi.feature.events.data.model.Event
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("v1/eventos")
    suspend fun getEvents(): List<Event>

    @GET("v1/eventos/{id}")
    suspend fun getEventDetail(
        @Path("id") id:Int):Event
}
