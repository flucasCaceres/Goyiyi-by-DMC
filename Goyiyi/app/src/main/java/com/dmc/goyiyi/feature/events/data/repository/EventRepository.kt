package com.dmc.goyiyi.feature.events.data.repository

import com.dmc.goyiyi.feature.events.data.model.Event
import com.dmc.goyiyi.feature.events.data.remote.EventRemoteDataSource

class EventRepository(private val remote: EventRemoteDataSource) {
    suspend fun getEvents() = remote.fetchEvents()

    suspend fun getEventDetail(id: String): Event {
        return remote.fetchEventsDetail(id)
    }

}
