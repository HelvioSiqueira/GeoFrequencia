package com.example.geofrequencia

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.geofrequencia.managers.MyGeofenceManager
import com.example.geofrequencia.managers.MyLocationManager

class MapViewModel(app: Application) : AndroidViewModel(app) {

    private val myGeofenceManager = MyGeofenceManager(getContext())
    private val myLocationManager = MyLocationManager(getContext())

    var noLocal: Boolean = false

    fun testGeofence(){
        myGeofenceManager.defGeofence()
        myLocationManager.startLocationUpdates()
    }

    private fun getContext() = getApplication<Application>()
}