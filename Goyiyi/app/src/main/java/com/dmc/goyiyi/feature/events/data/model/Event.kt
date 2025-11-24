package com.dmc.goyiyi.feature.events.data.model

data class Event(
    val id: String,
    val nombre: String,
    val estado: String?,
    val tipo: String?,
    val organizador: String?,
    val contLikes: Int?,
    val contDislikes: Int?,
    val contParticipantesPosibles: Int?,
    val idEventoPadre: Int?,
    val img: String?,
    val enlaceInfo: String?,
    val descripcion: String?,
    val cupoMax: Int?,
    val cupoMin: Int?,
    val apto: String?,
    val contEntradasVendidas: Int?,
    val metodoPago: String?,
    val moneda: String?,
    val precio: String?
)
