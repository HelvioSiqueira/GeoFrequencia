package com.example.geofrequencia

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MapActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)
    }

    companion object{
        fun open(context: Context){
            context.startActivity(Intent(context, MapActivity::class.java))
        }
    }
}