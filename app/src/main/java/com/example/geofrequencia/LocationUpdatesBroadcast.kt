package com.example.geofrequencia

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.google.android.gms.location.LocationAvailability
import com.google.android.gms.location.LocationResult

class LocationUpdatesBroadcast : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {

        LocationResult.extractResult(intent)?.let { locationResult ->
            Toast.makeText(context, locationResult.lastLocation.toString(), Toast.LENGTH_LONG)
                .show()
        }
    }
}