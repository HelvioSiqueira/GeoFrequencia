package com.example.geofrequencia

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.work.WorkManager
import com.example.geofrequencia.managers.MyGeofenceMananger
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class MapViewModel(app: Application) : AndroidViewModel(app), CoroutineScope {
    private val job = Job()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    private val myGeofenceManager = MyGeofenceMananger(getContext())

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }

    fun testGeofence(){

        myGeofenceManager.defGeofence()
    }

    private fun getContext() = getApplication<Application>()
}