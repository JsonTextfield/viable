package com.jsontextfield.viable

import android.app.Application
import com.jsontextfield.viable.di.initKoin
import org.koin.android.ext.koin.androidContext

class ViableApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidContext(this@ViableApplication)
        }
    }
}