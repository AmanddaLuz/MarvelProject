package com.amandaluz.marvelproject

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class CustomApp: Application() {

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG){
            Timber.plant(Timber.DebugTree())
            //TODO("FAZER O HAWK DEPOIS DO HILT E USECASE")
        }
    }
}