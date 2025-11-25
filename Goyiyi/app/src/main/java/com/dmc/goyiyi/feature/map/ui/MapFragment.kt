package com.dmc.goyiyi.feature.map.ui

import android.Manifest
import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.DrawableRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.dmc.goyiyi.R
import com.dmc.goyiyi.databinding.FragmentMapBinding
import com.dmc.goyiyi.feature.map.data.model.EventMapUiModel
import com.dmc.goyiyi.ui.bottomsheet.EventBottomSheet
import com.dmc.goyiyi.feature.map.util.GeoJsonUtils
import com.dmc.goyiyi.feature.map.vm.EventMapViewModel
import com.dmc.goyiyi.feature.map.vm.MapViewModel
import com.dmc.goyiyi.ui.MainActivity
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.launch
import org.maplibre.android.camera.CameraPosition
import org.maplibre.android.camera.CameraUpdateFactory
import org.maplibre.android.geometry.LatLng
import org.maplibre.android.geometry.LatLngBounds
import org.maplibre.android.maps.MapLibreMap
import org.maplibre.android.style.expressions.Expression
import org.maplibre.android.style.layers.Property
import org.maplibre.android.style.layers.PropertyFactory.*
import org.maplibre.android.style.layers.SymbolLayer
import org.maplibre.android.style.layers.CircleLayer
import org.maplibre.geojson.Point
import org.maplibre.android.style.sources.GeoJsonSource
import com.dmc.goyiyi.ui.bottomsheet.MultiEventBottomSheet
import com.dmc.goyiyi.util.LoadingOverlay
import com.dmc.goyiyi.util.asLoadingOverlay

class MapFragment : Fragment(R.layout.fragment_map) {

    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding!!

    private val vm: MapViewModel by activityViewModels()
    private val eventVm: EventMapViewModel by activityViewModels()

    private lateinit var loadingOverlay: LoadingOverlay

    private fun mostrarCarga() {
        loadingOverlay.show()
    }

    private fun ocultarCarga() {
        loadingOverlay.hide()
    }




    private val GOYA = LatLng(-29.1440, -59.2650)
    private val GOYA_BOUNDS = LatLngBounds.Builder()
        .include(LatLng(-29.05, -59.40))
        .include(LatLng(-29.23, -59.13))
        .build()

    private val fused by lazy { LocationServices.getFusedLocationProviderClient(requireContext()) }
    private var mapRef: MapLibreMap? = null
    private var currentLocCts: CancellationTokenSource? = null

    private companion object {
        const val SRC_USER = "src_user_location"
        const val LYR_USER = "lyr_user_location"
        const val IMG_USER = "img_user_pin"

        const val SRC_EVENTS = "src_events"
        const val LYR_EVENTS = "lyr_events"
    }

    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { granted ->
        val ok = granted[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                granted[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        vm.locationPermissionGranted = ok
        if (ok) refreshUserLocation() else clearUserPin()
    }

    @SuppressLint("UnsafeRepeatOnLifecycleDetector")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentMapBinding.bind(view)

        loadingOverlay = binding.overlaySpinner.root.asLoadingOverlay()

        binding.mapView.onCreate(savedInstanceState)
        vm.locationPermissionGranted = hasLocationPermission()
        eventVm.loadRealEvents()
        mostrarCarga()

        binding.mapView.getMapAsync { map ->
            mapRef = map
            map.setStyle(
                "https://api.maptiler.com/maps/0199cbca-451f-7cfe-95e6-963d8d628d96/style.json?key=j73X6NNd8LAS77DbHyVB") { style ->

                // ---- Limites de cámara ----
                map.setLatLngBoundsForCameraTarget(GOYA_BOUNDS)
                map.setMinZoomPreference(10.0)
                map.setMaxZoomPreference(19.0)

                // ---- Cámara inicial ----
                vm.lastCameraPosition?.let {
                    map.cameraPosition = it
                } ?: run {
                    map.cameraPosition = CameraPosition.Builder()
                        .target(GOYA)
                        .zoom(13.0)
                        .tilt(0.0)
                        .build()
                }

                // ---- Icono del usuario ----
                if (style.getImage(IMG_USER) == null) {
                    style.addImage(IMG_USER, vectorToBitmap(R.drawable.ic_pin_blue, 64, 64))
                }
                if (style.getSource(SRC_USER) == null)
                    style.addSource(GeoJsonSource(SRC_USER))
                if (style.getLayer(LYR_USER) == null) {
                    style.addLayer(
                        SymbolLayer(LYR_USER, SRC_USER).withProperties(
                            iconImage(IMG_USER),
                            iconAllowOverlap(true),
                            iconIgnorePlacement(true),
                            iconAnchor(Property.ICON_ANCHOR_BOTTOM)
                        )
                    )
                }

                // ---- Iconos de eventos ----
                val eventIcons = listOf(
                    "ic_pin_sucediendo" to R.drawable.ic_pin_sucediendo,
                    "ic_pin_cancelado" to R.drawable.ic_pin_cancelado,
                    "ic_pin_reprogramado" to R.drawable.ic_pin_reprogramado,
                    "ic_pin_pausado" to R.drawable.ic_pin_pausado
                )

                for ((name, res) in eventIcons) {
                    if (style.getImage(name) == null) {
                        style.addImage(name, vectorToBitmap(res, 64, 64))
                    }
                }

                // ---- Source + Layer de eventos ----
                if (style.getSource(SRC_EVENTS) == null) {
                    style.addSource(
                        GeoJsonSource(
                            SRC_EVENTS,
                            GeoJsonUtils.emptyFeatureCollection(),
                            org.maplibre.android.style.sources.GeoJsonOptions()
                                .withCluster(true)
                                .withClusterRadius(50)
                                .withClusterMaxZoom(14)
                        )
                    )
                }

                if (style.getLayer(LYR_EVENTS) == null) {
                    // ---- Capa de eventos individuales (NO clusters) ----
                    val eventLayer = SymbolLayer(LYR_EVENTS, SRC_EVENTS)
                        .withProperties(
                            iconImage(Expression.get("icon")),
                            iconAllowOverlap(true),
                            iconIgnorePlacement(true),
                            iconAnchor(Property.ICON_ANCHOR_BOTTOM)
                        )
                        .withFilter(
                            Expression.not(Expression.has("point_count")) // solo puntos individuales
                        )

                    style.addLayer(eventLayer)

                    // ---- Capa para clusters (círculo) ----
                    val clusterLayer = CircleLayer("lyr_cluster", SRC_EVENTS)
                        .withProperties(
                            circleColor("#FF8800"),
                            circleRadius(20f)
                        )
                        .withFilter(
                            Expression.has("point_count")
                        )
                    style.addLayer(clusterLayer)

                    // ---- Capa para números dentro del cluster ----
                    val clusterCountLayer = SymbolLayer("lyr_cluster_count", SRC_EVENTS)
                        .withProperties(
                            textField(Expression.toString(Expression.get("point_count"))),
                            textSize(14f),
                            textColor("#FFFFFF"),
                            textAllowOverlap(true),
                            textIgnorePlacement(true)
                        )
                        .withFilter(
                            Expression.has("point_count")
                        )
                    style.addLayer(clusterCountLayer)
                }


                // ---- Pin usuario inicial ----
                vm.lastUserLatLng?.let { setUserPin(it) } ?: clearUserPin()

                binding.myLocationBtn.visibility = View.VISIBLE
                binding.myLocationBtn.setOnClickListener {
                    refreshUserLocation(centerCamera = true)
                }
                // ---- CLICK EN PINS ----
                // ---- CLICK EN PINS / CLUSTERS ----
                // ---- CLICK EN PINS Y MULTI-PINS ----
                map.addOnMapClickListener { point ->

                    val screenPoint = map.projection.toScreenLocation(point)
                    val touchSize = 60f

                    val rect = android.graphics.RectF(
                        screenPoint.x - touchSize,
                        screenPoint.y - touchSize,
                        screenPoint.x + touchSize,
                        screenPoint.y + touchSize
                    )

                    val candidates = map.queryRenderedFeatures(rect, LYR_EVENTS)

                    if (candidates.isNotEmpty()) {

                        val nearest = candidates.minByOrNull { feature ->
                            val geo = feature.geometry() as? org.maplibre.geojson.Point
                                ?: return@minByOrNull Float.MAX_VALUE

                            val projected = map.projection.toScreenLocation(
                                LatLng(geo.latitude(), geo.longitude())
                            )

                            val dx = projected.x - screenPoint.x
                            val dy = projected.y - screenPoint.y
                            dx * dx + dy * dy
                        }

                        nearest?.let { feature ->
                            val geo = feature.geometry() as? org.maplibre.geojson.Point
                            val clickedLat = geo?.latitude()
                            val clickedLng = geo?.longitude()

                            if (clickedLat != null && clickedLng != null) {

                                val epsilon = 1e-6

                                val eventsAtSameSpot = eventVm.eventPins.value.filter { e ->
                                    kotlin.math.abs(e.lat - clickedLat) < epsilon &&
                                            kotlin.math.abs(e.lng - clickedLng) < epsilon
                                }

                                when {
                                    eventsAtSameSpot.size == 1 -> {
                                        showEventBottomSheet(eventsAtSameSpot.first())
                                    }
                                    eventsAtSameSpot.size > 1 -> {
                                        showMultiEventBottomSheet(eventsAtSameSpot)
                                    }
                                }

                                return@addOnMapClickListener true
                            }
                        }
                    }

                    val clusterFeatures = map.queryRenderedFeatures(screenPoint, "lyr_cluster")
                    if (clusterFeatures.isNotEmpty()) {
                        val clusterFeature = clusterFeatures[0]
                        val geometry = clusterFeature.geometry() as? org.maplibre.geojson.Point

                        geometry?.let { p ->
                            map.animateCamera(
                                CameraUpdateFactory.newLatLngZoom(
                                    LatLng(p.latitude(), p.longitude()),
                                    16.0
                                )
                            )
                        }

                        return@addOnMapClickListener true
                    }

                    false
                }





                // ---------------------------------------------------------
                //  AHORA SÍ: COLECTAMOS LOS EVENTOS DENTRO DEL STYLE CALLBACK
                // ---------------------------------------------------------
                lifecycleScope.launch {
                    repeatOnLifecycle(Lifecycle.State.STARTED) {
                        eventVm.eventPins.collect { events ->

                            val features = events.map { event ->
                                val iconName = when (event.estado.uppercase()) {
                                    "SUCEDIENDO" -> "ic_pin_sucediendo"
                                    "CANCELADO" -> "ic_pin_cancelado"
                                    "REPROGRAMADO" -> "ic_pin_reprogramado"
                                    "PROGRAMADO", "PAUSADO", "FINALIZADO" -> "ic_pin_pausado"
                                    else -> "ic_pin_pausado"
                                }

                                GeoJsonUtils.pointWithProperties(
                                    event.lng,
                                    event.lat,
                                    mapOf(
                                        "id" to event.id,
                                        "name" to event.nombre,
                                        "icon" to iconName
                                    )
                                )
                            }

                            val json = GeoJsonUtils.featureCollectionRaw(features)
                            Log.d("GEOJSON_DEBUG", json)

                            val src = style.getSource(SRC_EVENTS) as? GeoJsonSource
                            Log.d("GEOJSON_SET", "source encontrado? = ${src != null}")
                            src?.setGeoJson(json)
                        }
                    }
                }
                // PATCH MAP 3 — observar loading del ViewModel
                lifecycleScope.launch {
                    repeatOnLifecycle(Lifecycle.State.STARTED) {
                        eventVm.loading.collect { isLoading ->
                            if (isLoading) {
                                mostrarCarga()
                            } else {
                                ocultarCarga()
                            }
                        }
                    }
                }

            }
        }
    }

    override fun onStart() {
        super.onStart()
        binding.mapView.onStart()
        if (vm.locationPermissionGranted) {
            refreshUserLocation(centerCamera = false)
        }
    }

    override fun onResume() {
        super.onResume()
        binding.mapView.onResume()
        eventVm.loadRealEvents()
        if (vm.locationPermissionGranted) {
            refreshUserLocation(centerCamera = false)
        }
    }

    override fun onPause() {
        binding.mapView.onPause()
        vm.lastCameraPosition = mapRef?.cameraPosition
        super.onPause()
    }

    override fun onDestroyView() {
        currentLocCts?.cancel()
        mapRef = null
        _binding = null
        super.onDestroyView()
    }

    private fun hasLocationPermission(): Boolean {
        val ctx = requireContext()
        val fine = ContextCompat.checkSelfPermission(ctx, Manifest.permission.ACCESS_FINE_LOCATION)
        val coarse = ContextCompat.checkSelfPermission(ctx, Manifest.permission.ACCESS_COARSE_LOCATION)
        return fine == android.content.pm.PackageManager.PERMISSION_GRANTED ||
                coarse == android.content.pm.PackageManager.PERMISSION_GRANTED
    }

    private fun vectorToBitmap(
        @DrawableRes drawableId: Int,
        widthPx: Int? = null,
        heightPx: Int? = null
    ): Bitmap {
        val drawable = AppCompatResources.getDrawable(requireContext(), drawableId)
            ?: error("Drawable not found: $drawableId")
        val w = widthPx ?: drawable.intrinsicWidth.coerceAtLeast(64)
        val h = heightPx ?: drawable.intrinsicHeight.coerceAtLeast(64)
        val bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bmp)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return bmp
    }

    @SuppressLint("MissingPermission")
    private fun refreshUserLocation(centerCamera: Boolean = false) {
        if (!vm.locationPermissionGranted) {
            permissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
            return
        }

        fused.lastLocation.addOnSuccessListener { loc ->
            loc?.let {
                val latLng = LatLng(it.latitude, it.longitude)
                setUserPin(latLng)
                if (centerCamera) {
                    mapRef?.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16.0))
                }
            }
        }
    }

    private fun setUserPin(pos: LatLng) {
        vm.lastUserLatLng = pos
        mapRef?.getStyle { style ->
            (style.getSource(SRC_USER) as? GeoJsonSource)
                ?.setGeoJson(GeoJsonUtils.point(pos.longitude, pos.latitude))
        }
    }

    private fun clearUserPin() {
        mapRef?.getStyle { style ->
            (style.getSource(SRC_USER) as? GeoJsonSource)
                ?.setGeoJson(GeoJsonUtils.emptyFeatureCollection())
        }
    }
    private fun showEventBottomSheet(event: EventMapUiModel) {
        val sheet = EventBottomSheet.newInstance(event)
        sheet.show(parentFragmentManager, "EventBottomSheet")
    }

    private fun showMultiEventBottomSheet(events: List<EventMapUiModel>) {
        val sheet = MultiEventBottomSheet(events) { selected ->
            showEventBottomSheet(selected)
        }
        sheet.show(parentFragmentManager, "MultiEventBottomSheet")
    }

}
