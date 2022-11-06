package com.example.geofrequencia

import android.content.Context
import android.media.MediaPlayer
import android.provider.Settings
import android.util.Log
import androidx.concurrent.futures.CallbackToFutureAdapter
import androidx.work.*
import com.example.geofrequencia.managers.MyGeofenceMananger
import com.example.geofrequencia.managers.MyLocationManager
import com.google.common.util.concurrent.ListenableFuture
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

class TestWork(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {

    private val listenableWorker = Listenable(context, workerParams)

    private val myGeofenceMananger = MyGeofenceMananger(context)
    private val myLocationManager = MyLocationManager(context)

    override fun doWork(): Result {

        return Result.success()
    }

    override fun getForegroundInfoAsync(): ListenableFuture<ForegroundInfo> {

        return CallbackToFutureAdapter.getFuture {
            Log.d("HSV", "TESTE")
        }
    }
}

class Listenable(context: Context, workerParams: WorkerParameters): ListenableWorker(context, workerParams){

    private val myLocationManager = MyLocationManager(context)

    override fun startWork(): ListenableFuture<Result> {

        return CallbackToFutureAdapter.getFuture { _->
            myLocationManager.startLocationUpdates()
        }
    }
}