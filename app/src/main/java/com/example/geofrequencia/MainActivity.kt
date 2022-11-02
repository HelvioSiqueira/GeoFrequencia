package com.example.geofrequencia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        showMapActivity()
    }

    private fun showMapActivity(){
        MapActivity.open(this)
    }

}