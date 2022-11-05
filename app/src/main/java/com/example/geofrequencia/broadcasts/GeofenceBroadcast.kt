package com.example.geofrequencia.broadcasts

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.example.geofrequencia.NotifManager
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingEvent

//Broadcast que será disparado quando o usuario entrar, sair ou permanecer na área por 30s
class GeofenceBroadcast : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {

        val geofencingEvent = GeofencingEvent.fromIntent(intent)

        Log.d("HSV", "Entrou no Geofence Receiver")

        if (geofencingEvent!!.hasError()) {
            val errorCode = geofencingEvent.errorCode
            Toast.makeText(context, "Erro no serviço de localização: $errorCode", Toast.LENGTH_LONG)
                .show()
        } else {
            //Usada para quando a mais de uma demarcação(geofence)
            //Ela irá detectar o trigger em cada demarcação
            val geofences = geofencingEvent.triggeringGeofences

            //Detecta o tipo de transição
            val transition = geofencingEvent.geofenceTransition

            geofences?.forEach { geofence ->
                when (transition) {
                    Geofence.GEOFENCE_TRANSITION_ENTER ->
                        Log.d("HSV", "Geofence ID: ${geofence.requestId} ENTROU no perímetro")

                    Geofence.GEOFENCE_TRANSITION_EXIT ->
                        Log.d("HSV", "Geofence ID: ${geofence.requestId} SAIU do perímetro")

                    Geofence.GEOFENCE_TRANSITION_DWELL -> {
                        NotifManager.notification(context, "PERMANECEU no perímetro por 30s")
                        Log.d("HSV", "${geofence.requestId}: PERMANECEU no perímetro por 30s")
                    }
                    else ->
                        Log.d("HSV", "Erro no Geofence: $transition")
                }
            }
        }
    }
}