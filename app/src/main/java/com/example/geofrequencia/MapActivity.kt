package com.example.geofrequencia

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.geofrequencia.databinding.ActivityMapBinding

class MapActivity : AppCompatActivity() {

    private val fragment: MapFragment by lazy {
        supportFragmentManager.findFragmentById(R.id.fragmentMap) as MapFragment
    }

    private lateinit var binding: ActivityMapBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapBinding.inflate(layoutInflater)

        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()

        initMap()
    }

    private fun initMap(){
        fragment.getMapAsync {  }
    }

    companion object{
        fun open(context: Context){
            context.startActivity(Intent(context, MapActivity::class.java))
        }
    }
}