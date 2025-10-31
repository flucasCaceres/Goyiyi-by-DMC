package com.dmc.goyiyi.data

import com.dmc.goyiyi.model.Evento

class EventoRepositorio {
    fun obtenerEventos(): List<Evento> = listOf(
        Evento("1", "Concierto Plaza Central", -31.4167, -64.1833), // Córdoba
        Evento("2", "Feria Gastronómica",       -34.6037, -58.3816)  // CABA
    )
}
