package com.example.geofrequencia

import android.app.Application
import android.util.Log
import androidx.work.Configuration

class App: Application(), Configuration.Provider {
    override fun getWorkManagerConfiguration() = Configuration.Builder()
        .setMinimumLoggingLevel(Log.DEBUG)
        .build()
}