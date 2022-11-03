package com.example.geofrequencia

import android.content.Context
import android.content.SharedPreferences
import com.google.android.gms.location.Geofence
import com.google.gson.Gson

class GeofenceDb(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("geofenceDb", Context.MODE_PRIVATE)

    fun getGeofence(id: String): GeofenceInfo? {
        val gson = Gson()
        val json = prefs.getString(id, null)

        return if (json != null) {
            gson.fromJson(json, GeofenceInfo::class.java)
        } else {
            null
        }
    }

    fun saveGeofence(geofence: GeofenceInfo){
        val gson = Gson()
        val json = gson.toJson(geofence)
        val editor = prefs.edit()
        editor.putString(geofence.id, json)
        editor.apply()
    }
}