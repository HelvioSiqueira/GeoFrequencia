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
import com.example.geofrequencia.broadcasts.GeofenceBroadcast
import com.example.geofrequencia.broadcasts.GeofenceUpdateBroadcast
import com.example.geofrequencia.di.androidModule
import com.example.geofrequencia.managers.NotifManager
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.context.startKoin
import java.util.*

class App: Application(), Configuration.Provider, KoinComponent{

    private val viewModel: MapViewModel by inject()

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

        setReceiverSettings()
        initAlarmManager()
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
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as? AlarmManager

        Log.d("HSV", "Iniciando AlarmManager")

        val calen = Calendar.getInstance()
        calen.timeInMillis = System.currentTimeMillis()

        calen.add(Calendar.SECOND, 60)

        val flag = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) PendingIntent.FLAG_IMMUTABLE else 0

        val alarm = Intent(this, EventReceiver::class.java).let {
            PendingIntent.getBroadcast(this, 0, it, flag)
        }


        alarmManager?.setRepeating(
            AlarmManager.RTC_WAKEUP,
            SystemClock.elapsedRealtime(),
            AlarmManager.INTERVAL_FIFTEEN_MINUTES,
            alarm
        )
        //alarmManager?.set(AlarmManager.RTC_WAKEUP, calen.timeInMillis, alarm)


    }
}