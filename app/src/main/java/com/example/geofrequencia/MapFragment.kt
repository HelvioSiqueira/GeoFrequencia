package com.example.geofrequencia

import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*

class MapFragment : SupportMapFragment() {

    private val viewModel: MapViewModel by lazy {
        ViewModelProvider(requireActivity())[MapViewModel::class.java]
    }

    private var googleMap: GoogleMap? = null

    private var markerCurrentLocation: Marker? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        getMapAsync {

        }
    }

    override fun getMapAsync(callback: OnMapReadyCallback) {
        super.getMapAsync {
            googleMap = it
            setupMap()
            callback.onMapReady(googleMap!!)
        }
    }

    private fun setupMap() {
        googleMap?.apply {
            mapType = GoogleMap.MAP_TYPE_NORMAL
            uiSettings.isMapToolbarEnabled = true
            uiSettings.isMyLocationButtonEnabled = true
            uiSettings.isZoomControlsEnabled = true

            onMapLongClick()
        }

        viewModel.getMapState().observe(this, Observer { mapState ->
            if (mapState != null) {
                updateMap(mapState)
            }
        })

        viewModel.getCurrentLocation().observe(this, Observer { currentLocation ->
            if (currentLocation != null) {
                val map = MapViewModel.MapState(origin = currentLocation)
                //updateMap(map)

                if (markerCurrentLocation == null) {
                    val icon = BitmapDescriptorFactory
                        .fromResource(R.drawable.blue_marker)
                    markerCurrentLocation = googleMap?.addMarker(
                        MarkerOptions()
                            .title("Está aqui")
                            .icon(icon)
                            .position(currentLocation)
                    )
                }
                markerCurrentLocation?.position = currentLocation
            }
        })
    }

    private fun onMapLongClick() {

        val pit = PendingIntent.getBroadcast(
            requireContext(),
            0,
            Intent(requireContext(), GeofenceReceiver::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        viewModel.setGeofence(pit, LatLng(4.868556714817011, -43.37071660906077))
    }

    private fun updateMap(mapState: MapViewModel.MapState) {
        googleMap?.run {
            clear()
            markerCurrentLocation = null
            val geofenceInfo = mapState.geofenceInfo

            if (geofenceInfo != null) {
                val latLng = LatLng(geofenceInfo.latitude, geofenceInfo.longitude)
                addCircle(
                    CircleOptions()
                        .strokeWidth(2f)
                        .fillColor(0x990000FF.toInt())
                        .center(latLng)
                        .radius(geofenceInfo.radius.toDouble())
                )
            }

            val origin = mapState.origin

            if (origin != null) {
                addMarker(
                    MarkerOptions()
                        .position(origin)
                        .title("Local Atual")
                )

                animateCamera(CameraUpdateFactory.newLatLngZoom(origin, 17.0F))
            }
        }
    }
}