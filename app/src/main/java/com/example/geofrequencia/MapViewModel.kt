package com.example.geofrequencia

import android.annotation.SuppressLint
import android.app.Application
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

    private var googleApiClient: GoogleApiClient? = null
    private val locationClient: FusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(getContext())
    }

    private val connectionStatus = MutableLiveData<GoogleApiConnectionStatus>()
    private val currentLocationError = MutableLiveData<LocationError>()

    private val mapState = MutableLiveData<MapState>().apply {
        value = MapState()
    }

    private val currentLocation = MutableLiveData<LatLng>()

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }

    fun getCurrentLocation(): MutableLiveData<LatLng> {
        return currentLocation
    }

    fun getConnectionStatus(): MutableLiveData<GoogleApiConnectionStatus> {
        return connectionStatus
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
                        connectionStatus.value = GoogleApiConnectionStatus(true)
                    }

                    override fun onConnectionSuspended(p0: Int) {
                        connectionStatus.value = GoogleApiConnectionStatus(false)
                        googleApiClient?.connect()
                    }
                }).addOnConnectionFailedListener { connectionResult ->
                    connectionStatus.value = GoogleApiConnectionStatus(false, connectionResult)
                }.build()
        }
        googleApiClient?.connect()
    }

    fun disconnectGoogleApicClient() {
        connectionStatus.value = GoogleApiConnectionStatus(false)
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
                continuation.resume(true)
            } else {
                continuation.resume(true)
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
                    startLocationUpdates()
                    null
                } else {
                    LocationError.ErrorLocationUnavailable
                }
            } catch (e: Exception) {
                LocationError.ErrorLocationUnavailable
            }
        }
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val location = locationResult.lastLocation

            if (location != null) {
                currentLocation.value = LatLng(location.latitude, location.longitude)
            }
        }
    }

    @SuppressLint("MissingPermission")
    fun startLocationUpdates() {
        val locationRequest =
            LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 5 * 1000).build()

        locationClient.requestLocationUpdates(locationRequest, locationCallback, null)
    }

    fun stopLocationUpdates() {
        LocationServices.getFusedLocationProviderClient(getContext())
            .removeLocationUpdates(locationCallback)
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
        object ErrorLocationUnavailable : LocationError()
    }
}