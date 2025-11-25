package com.dmc.goyiyi.feature.events.data.remote.model

data class OpinionRequest(
    val idEvento: String,
    val idUsuario: String,
    val comentarios: String,
    val valoracion: Int,
    val estado: String = "POSTEADO",
    val img: String
)
