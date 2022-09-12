package com.amandaluz.marvelproject

import android.app.Application
import com.amandaluz.marvelproject.core.ModuleHawk
import com.orhanobut.hawk.Hawk
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.qualifiers.ApplicationContext
import timber.log.Timber

@HiltAndroidApp
class CustomApp: Application() {

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG){
            Timber.plant(Timber.DebugTree())
            //TODO("FAZER O HAWK DEPOIS DO HILT E USECASE")
            ModuleHawk.init(applicationContext)
        }
    }
}