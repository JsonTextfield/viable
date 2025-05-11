package com.jsontextfield.viable.di

import com.jsontextfield.viable.data.database.ViaRailRoomDatabase
import com.jsontextfield.viable.data.database.getDatabaseBuilder
import com.jsontextfield.viable.data.database.getRoomDatabase
import org.koin.core.module.Module
import org.koin.dsl.module

actual fun platformModule(): Module {
    return module {
        single<ViaRailRoomDatabase> {
            getRoomDatabase(getDatabaseBuilder())
        }
    }
}