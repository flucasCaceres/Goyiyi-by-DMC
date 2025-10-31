// feature/map/util/GeoJsonUtils.kt
package com.dmc.goyiyi.feature.map.util

import org.maplibre.android.geometry.LatLng

object GeoJsonUtils {
    fun point(lon: Double, lat: Double): String =
        """{"type":"Point","coordinates":[$lon,$lat]}"""

    fun emptyFeatureCollection(): String =
        """{"type":"FeatureCollection","features":[]}"""

    fun featureCollection(points: List<LatLng>): String {
        val feats = points.joinToString(",") { p ->
            """{"type":"Feature","geometry":{"type":"Point","coordinates":[${p.longitude},${p.latitude}]}}"""
        }
        return """{"type":"FeatureCollection","features":[$feats]}"""
    }
}
