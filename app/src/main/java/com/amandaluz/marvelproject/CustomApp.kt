package com.amandaluz.marvelproject

import android.app.Application
import timber.log.Timber

class CustomApp: Application() {

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG){
            Timber.plant(Timber.DebugTree())
        }
    }
}