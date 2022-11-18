package com.example.geofrequencia.broadcasts

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.geofrequencia.MapViewModel
import com.example.geofrequencia.managers.NotifManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class GeofenceUpdateBroadcast : BroadcastReceiver(), KoinComponent {

    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    private val viewModel: MapViewModel by inject()

    override fun onReceive(context: Context, intent: Intent) {

        Log.d("HSV", intent.action.toString())

        scope.launch {
            viewModel.testGeofence()
        }
    }
}