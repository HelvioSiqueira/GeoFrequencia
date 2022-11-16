package com.example.geofrequencia

import android.app.Application
import android.util.Log
import androidx.work.Configuration
import com.example.geofrequencia.di.androidModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.dsl.koinApplication

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
    }
}