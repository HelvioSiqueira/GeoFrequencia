package com.example.geofrequencia

import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder
import android.provider.Settings
import android.util.Log

class NotifService: Service() {

    override fun onBind(p0: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        if(intent != null){
            start()
        }

        return START_STICKY
    }

    private fun start(){
        Log.d("HSV", "Setando")
        viewModel.testService()
    }

    companion object{
        lateinit var viewModel: MapViewModel

        fun defViewModel(viewModelRecebido: MapViewModel){
            viewModel = viewModelRecebido
        }
    }
}