package com.example.geofrequencia

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationServices

class MyGeofenceMananger(context: Context) {

    private val geofenceClient: GeofencingClient =
        LocationServices.getGeofencingClient(context)

    private val geofencePendingIntent = PendingIntent.getBroadcast(
        context,
        0,
        Intent(context, GeofenceReceiver::class.java),
        PendingIntent.FLAG_UPDATE_CURRENT
    )

    private val geofence = Geofence.Builder()
        .setRequestId("1")
        .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_DWELL or Geofence.GEOFENCE_TRANSITION_EXIT or Geofence.GEOFENCE_TRANSITION_ENTER)
        .setLoiteringDelay(30000)
        .setCircularRegion(-4.8662983, -43.371905, 80F)
        .setExpirationDuration(Geofence.NEVER_EXPIRE)
        .build()

    private val request = GeofencingRequest.Builder()
        .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
        .addGeofence(geofence)
        .build()

    @SuppressLint("MissingPermission")
    fun defGeofence(context: Context) {
        LocationServices.getGeofencingClient(context).addGeofences(request, geofencePendingIntent).addOnCompleteListener {
            if (it.isSuccessful) {
                Log.d("HSV", "Geofense setado")
            } else if (it.isCanceled) {
                Log.d("HSV", "Geofense não setado")
            }
        }
    }
}