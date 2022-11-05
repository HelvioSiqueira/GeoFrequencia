package com.example.geofrequencia

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.work.WorkManager
import com.example.geofrequencia.managers.MyGeofenceMananger
import com.example.geofrequencia.managers.MyLocationManager
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

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
    fun testGeofence() = myGeofenceManager.defGeofence()

    fun testService(){
        launch {
            withContext(Dispatchers.Default){
                testLocationUpdates()
                testGeofence()
            }
        }
    }

    private fun getContext() = getApplication<Application>()
}