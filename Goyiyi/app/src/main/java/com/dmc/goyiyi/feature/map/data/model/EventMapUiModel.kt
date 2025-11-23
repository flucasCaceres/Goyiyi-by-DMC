package com.dmc.goyiyi.feature.map.data.model

data class EventMapUiModel(
    val id: Int,
    val nombre: String,
    val lat: Double,
    val lng: Double,
    val estado: String,
    val tipo: String,
    val organizador: String,
    val likes: Int,
    val dislikes: Int
)
