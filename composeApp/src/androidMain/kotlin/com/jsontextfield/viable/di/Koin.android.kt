package com.jsontextfield.viable.di

import android.content.Context
import com.jsontextfield.viable.data.database.ViaRailRoomDatabase
import com.jsontextfield.viable.data.database.getDatabaseBuilder
import org.koin.core.module.Module
import org.koin.dsl.module

actual fun platformModule(): Module {
    return module {
        single<ViaRailRoomDatabase> {
            getDatabaseBuilder(get<Context>()).build()
        }
    }
}