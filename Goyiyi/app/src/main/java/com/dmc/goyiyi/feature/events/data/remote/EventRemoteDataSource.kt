package com.dmc.goyiyi.feature.events.data.remote

import com.dmc.goyiyi.core.network.ApiService
import com.dmc.goyiyi.feature.events.data.model.Event

class EventRemoteDataSource(private val api: ApiService) {
    suspend fun fetchEvents() = api.getEvents()

    suspend fun fetchEventsDetail(id: Int) =
        api.getEventDetail(id)
}
