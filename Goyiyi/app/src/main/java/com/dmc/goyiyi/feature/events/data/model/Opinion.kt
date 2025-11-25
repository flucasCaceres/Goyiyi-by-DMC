package com.dmc.goyiyi.feature.events.data.model

data class Opinion(
    val id: String?,          // en algunos casos viene null en tu JSON
    val idEvento: String,
    val idUsuario: String,
    val comentarios: String?,
    val img: String?,
    val valoracion: Int,
    val estado: String,
    val fechaCreacion: String,
    val fechaModificacion: String
)
