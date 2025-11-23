package com.dmc.goyiyi.feature.map.marker

import com.dmc.goyiyi.R
import com.dmc.goyiyi.feature.map.data.model.EventMapUiModel

object MarkerFactory {

    fun getDrawableResFor(event: EventMapUiModel): Int {
        return when (event.estado.uppercase()) {
            "SUCEDIENDO" -> R.drawable.ic_pin_sucediendo
            "CANCELADO" -> R.drawable.ic_pin_cancelado
            "REPROGRAMADO" -> R.drawable.ic_pin_reprogramado
            "PROGRAMADO", "PAUSADO", "FINALIZADO" -> R.drawable.ic_pin_pausado
            else -> R.drawable.ic_pin_pausado
        }
    }
}
