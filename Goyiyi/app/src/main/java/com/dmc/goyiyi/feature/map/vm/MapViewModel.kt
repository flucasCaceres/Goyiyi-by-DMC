package com.dmc.goyiyi.feature.map.vm

import androidx.lifecycle.ViewModel
import org.maplibre.android.geometry.LatLng
import org.maplibre.android.camera.CameraPosition

class MapViewModel : ViewModel() {

    var lastCameraPosition: CameraPosition? = null
    var lastUserLatLng: LatLng? = null
    var locationPermissionGranted: Boolean = false
}