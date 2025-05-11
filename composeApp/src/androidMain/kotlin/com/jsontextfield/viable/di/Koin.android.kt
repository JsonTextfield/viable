package com.jsontextfield.viable.di

import android.content.Context
import com.jsontextfield.viable.data.database.IViaRailDatabase
import com.jsontextfield.viable.data.database.getDatabaseBuilder
import com.jsontextfield.viable.data.database.getRoomDatabase
import org.koin.core.module.Module
import org.koin.dsl.module

actual fun platformModule(): Module {
    return module {
        single<IViaRailDatabase> {
            getRoomDatabase(getDatabaseBuilder(get<Context>()))
        }
    }
}