package com.jsontextfield.viable

import android.app.Application
import com.jsontextfield.viable.di.initKoin

class ViableApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin(this)
    }
}