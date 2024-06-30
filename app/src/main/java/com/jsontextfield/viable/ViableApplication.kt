package com.jsontextfield.viable

import android.app.Application
import com.jsontextfield.viable.data.database.ViaRailDatabase
import com.jsontextfield.viable.data.repositories.ViableRepository

class ViableApplication : Application() {
    // Using by lazy so the database and the repository are only created when they're needed
    // rather than when the application starts
    private val database by lazy { ViaRailDatabase.getInstance(this) }
    val repository by lazy { ViableRepository(database) }
}