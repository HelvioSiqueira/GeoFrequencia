package com.example.geofrequencia

import android.annotation.SuppressLint
import android.app.Application
import android.app.PendingIntent
import android.os.Bundle
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.*
import java.lang.Exception
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class MapViewModel(app: Application) : AndroidViewModel(app), CoroutineScope {
    private val job = Job()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    private val myLocationManager = MyLocationManager(getContext())
    private val myGeofenceManager = MyGeofenceMananger(getContext())

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }

    fun testLocationUpdates() = myLocationManager.startLocationUpdates()
    fun testGeofence() = myGeofenceManager.defGeofence(getContext())

    private fun getContext() = getApplication<Application>()
}