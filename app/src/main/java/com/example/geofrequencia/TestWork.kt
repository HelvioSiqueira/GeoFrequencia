package com.example.geofrequencia

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import android.provider.Settings
import androidx.core.app.NotificationCompat
import androidx.work.*
import com.example.geofrequencia.managers.MyGeofenceManager
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class TestWork(context: Context, workerParams: WorkerParameters) : CoroutineWorker(context, workerParams), KoinComponent {

    private val viewModel: MapViewModel by inject()

    private lateinit var mediaPlayer: MediaPlayer

    override suspend fun doWork(): Result {

            viewModel.testGeofence()

            mediaPlayer = MediaPlayer.create(applicationContext, Settings.System.DEFAULT_RINGTONE_URI)
            mediaPlayer.isLooping = true
            mediaPlayer.start()

        return Result.success()
    }

    override suspend fun getForegroundInfo(): ForegroundInfo {

        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                NOTIFICATION_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(applicationContext, NOTIFICATION_CHANNEL_ID)
            .setContentIntent(PendingIntent.getActivity(applicationContext, 0, Intent(applicationContext, MainActivity::class.java), PendingIntent.FLAG_UPDATE_CURRENT))
            .setSmallIcon(R.drawable.ic_baseline_favorite_24)
            .setOngoing(true)
            .setAutoCancel(true)
            .setOnlyAlertOnce(true)
            .setPriority(NotificationCompat.PRIORITY_MIN)
            .setContentTitle(applicationContext.getString(R.string.app_name))
            .setLocalOnly(true)
            .setVisibility(NotificationCompat.VISIBILITY_SECRET)
            .setContentText("Updating widget")
            .build()

        return ForegroundInfo(123, notification)
    }
    companion object {
        private const val NOTIFICATION_CHANNEL_ID = "11"
        private const val NOTIFICATION_CHANNEL_NAME = "Work Service"
    }
}
