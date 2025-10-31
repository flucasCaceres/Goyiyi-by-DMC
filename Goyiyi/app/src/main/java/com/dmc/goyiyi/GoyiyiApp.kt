package com.dmc.goyiyi

import android.app.Application
import org.maplibre.android.MapLibre
import org.maplibre.android.WellKnownTileServer

class GoyiyiApp : Application() {
    override fun onCreate() {
        super.onCreate()
        MapLibre.getInstance(
            /* context = */ this,
            /* apiKey  = */ "j73X6NNd8LAS77DbHyVB",
            /* server  = */ WellKnownTileServer.MapLibre
        )
    }
}
