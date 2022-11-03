package com.example.geofrequencia

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapFragment
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

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
        }

        viewModel.getMapState().observe(this, Observer { mapState ->
            if (mapState != null) {
                updateMap(mapState)
            }
        })

        viewModel.getCurrentLocation().observe(this, Observer { currentLocation ->
            if (currentLocation != null) {
                val map = MapViewModel.MapState(origin = currentLocation)
                updateMap(map)
            }
        })
    }

    private fun updateMap(mapState: MapViewModel.MapState) {
        googleMap?.run {
            clear()
            markerCurrentLocation = null
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