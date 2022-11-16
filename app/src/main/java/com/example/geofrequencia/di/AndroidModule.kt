package com.example.geofrequencia.di

import com.example.geofrequencia.MapViewModel
import org.koin.dsl.module

val androidModule = module{
    single { this }

    factory {
        MapViewModel(app = get())
    }
}