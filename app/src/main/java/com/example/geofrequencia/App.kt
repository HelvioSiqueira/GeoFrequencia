package com.example.geofrequencia

import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.SystemClock
import android.util.Log
import androidx.work.Configuration
import com.example.geofrequencia.di.androidModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class App: Application(), Configuration.Provider {
    override fun getWorkManagerConfiguration() = Configuration.Builder()
        .setMinimumLoggingLevel(Log.DEBUG)
        .build()

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(androidModule)
        }

        //setReceiverSettings()
        //initAlarmManager()
    }

    private fun setReceiverSettings(){
        val receiver = ComponentName(this, EventReceiver::class.java)

        packageManager.setComponentEnabledSetting(
            receiver,
            PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
            PackageManager.DONT_KILL_APP
        )
    }

    private fun initAlarmManager(){
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

        Log.d("HSV", "Iniciando AlarmManager")

        val flag = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) PendingIntent.FLAG_IMMUTABLE else 0

        val alarm = Intent(this, TestWork::class.java).let {
            PendingIntent.getBroadcast(this, 0, it, flag)
        }

        alarmManager.setRepeating(
            AlarmManager.ELAPSED_REALTIME_WAKEUP,
            SystemClock.elapsedRealtime(),
            AlarmManager.INTERVAL_FIFTEEN_MINUTES,
            alarm
        )
    }
}