package com.example.geofrequencia.di

import com.example.geofrequencia.MapViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val androidModule = module{
    single { this }

    viewModel {
        MapViewModel(app = get())
    }
}