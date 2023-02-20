package com.irfan.dansjob.app

import android.app.Application
import com.irfan.dansjob.di.dansLibraries
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class DansApp : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            modules(dansLibraries)
            androidContext(this@DansApp)
        }
    }
}