package com.example.geofrequencia

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapFragment
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapFragment : SupportMapFragment() {

    private var googleMap: GoogleMap? = null
    private var origin = LatLng(-23.561706, -46.655981)

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

        updateMap()
    }

    private fun updateMap() {
        googleMap?.run {
            animateCamera(CameraUpdateFactory.newLatLngZoom(origin, 17.0F))
            addMarker(MarkerOptions().position(origin))
        }
    }

}