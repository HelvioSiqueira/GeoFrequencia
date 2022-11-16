package com.example.geofrequencia.managers

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.geofrequencia.broadcasts.GeofenceBroadcast
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationServices

class MyGeofenceManager(context: Context) {

    //Cria o cliente do Geofence
    private val geofenceClient: GeofencingClient =
        LocationServices.getGeofencingClient(context)

    //PendingIntent que irá executar o GeofenceBroadCast
    private val geofencePendingIntent = PendingIntent.getBroadcast(
        context,
        0,
        Intent(context, GeofenceBroadcast::class.java),
        PendingIntent.FLAG_UPDATE_CURRENT
    )

    //Objeto geofence que irá ser a área demarcada
    private val geofence = Geofence.Builder()
        .setRequestId("1")
        .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_DWELL or Geofence.GEOFENCE_TRANSITION_EXIT or Geofence.GEOFENCE_TRANSITION_ENTER)
        .setLoiteringDelay(30000)
        .setCircularRegion(-4.8662983, -43.371905, 80F)
        .setExpirationDuration(Geofence.NEVER_EXPIRE)
        .build()

    //Request do geofence e tipo e trigger
    private val request = GeofencingRequest.Builder()
        .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
        .addGeofence(geofence)
        .build()

    //Seta o Geofence
    @SuppressLint("MissingPermission")
    fun defGeofence() {
        geofenceClient.addGeofences(request, geofencePendingIntent).addOnCompleteListener {
            if (it.isSuccessful) {
                Log.d("HSV", "Geofense setado")
            } else if (it.isCanceled) {
                Log.d("HSV", "Geofense não setado")
            }
        }
    }
}