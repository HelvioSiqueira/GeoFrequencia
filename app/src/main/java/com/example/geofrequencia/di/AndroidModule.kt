package com.example.geofrequencia.di

import com.example.geofrequencia.MapViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val androidModule = module{
    single { this }

    factory {
        MapViewModel(app = get())
    }
}