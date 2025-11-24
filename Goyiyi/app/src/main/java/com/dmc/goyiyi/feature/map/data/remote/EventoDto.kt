package com.dmc.goyiyi.feature.map.data.remote

import com.google.gson.annotations.SerializedName

data class EventoDto(
    val id: String,
    val nombre: String,
    val estado: String,
    val tipo: String,
    val organizador: String,
    @SerializedName("contLikes") val likes: Int,
    @SerializedName("contDislikes") val dislikes: Int
)