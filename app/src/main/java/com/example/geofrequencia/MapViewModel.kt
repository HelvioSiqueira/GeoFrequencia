package com.example.geofrequencia

import android.annotation.SuppressLint
import android.app.Application
import android.os.Bundle
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.*
import java.lang.Exception
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class MapViewModel(app: Application) : AndroidViewModel(app), CoroutineScope {
    private val job = Job()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + job

    private var googleApiClient: GoogleApiClient? = null
    private val locationClient: FusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(getContext())
    }

    private val conectionStatus = MutableLiveData<GoogleApiConnectionStatus>()
    private val currentLocationError = MutableLiveData<LocationError>()

    private val mapState = MutableLiveData<MapState>().apply {
        value = MapState()
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }

    fun getConnectionStatus(): MutableLiveData<GoogleApiConnectionStatus> {
        return conectionStatus
    }

    fun getCurrentLocationError(): MutableLiveData<LocationError> {
        return currentLocationError
    }

    fun getMapState(): MutableLiveData<MapState> {
        return mapState
    }

    fun connectGoogleApiClient() {
        if (googleApiClient == null) {
            googleApiClient = GoogleApiClient.Builder(getContext())
                .addApi(LocationServices.API)
                .addConnectionCallbacks(object : GoogleApiClient.ConnectionCallbacks {
                    override fun onConnected(p0: Bundle?) {
                        conectionStatus.value = GoogleApiConnectionStatus(true)
                    }

                    override fun onConnectionSuspended(p0: Int) {
                        conectionStatus.value = GoogleApiConnectionStatus(false)
                        googleApiClient?.connect()
                    }
                }).addOnConnectionFailedListener { connectionResult ->
                    conectionStatus.value = GoogleApiConnectionStatus(false, connectionResult)
                }.build()
        }
        googleApiClient?.connect()
    }

    fun disconnectGoogleApicClient() {
        conectionStatus.value = GoogleApiConnectionStatus(false)
        if (googleApiClient != null && googleApiClient?.isConnected == true) {
            googleApiClient?.disconnect()
        }
    }

    @SuppressLint("MissingPermission")
    private suspend fun loadLastLocation(): Boolean = suspendCoroutine { continuation ->
        locationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                val latLng = LatLng(location.latitude, location.longitude)
                mapState.value = mapState.value?.copy(origin = latLng)
                continuation.resume(false)
            }
        }
            .addOnFailureListener {
                continuation.resume(false)
            }
            .addOnCanceledListener {
                continuation.resume(false)
            }
    }

    fun requestLocation() {
        launch {
            currentLocationError.value = try {
                val success = withContext(Dispatchers.Default) { loadLastLocation() }
                if (success) {
                    null
                } else {
                    LocationError.ErrorLocationUnavailable
                }
            } catch (e: Exception) {
                LocationError.ErrorLocationUnavailable
            }
        }
    }

    private fun getContext() = getApplication<Application>()

    data class MapState(
        val origin: LatLng? = null
    )

    data class GoogleApiConnectionStatus(
        val success: Boolean,
        val connectionResult: ConnectionResult? = null
    )

    sealed class LocationError {
        object ErrorLocationUnavailable: LocationError()
    }
}