package com.dmc.goyiyi.feature.events.data.remote.model

import com.dmc.goyiyi.feature.events.data.model.Event

data class EventResponse(
    val ok: Boolean,
    val data: Event?
)
