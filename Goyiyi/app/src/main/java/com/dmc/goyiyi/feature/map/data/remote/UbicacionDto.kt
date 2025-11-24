package com.dmc.goyiyi.feature.map.data.remote

import com.google.gson.annotations.SerializedName

data class UbicacionDto(
    val idEvento: String,
    val idUbicacion: String,
    val lat: Double,
    @SerializedName("lon") val lng: Double,
    val nombreLugar: String?,
    val direccionFormateada: String?
)
