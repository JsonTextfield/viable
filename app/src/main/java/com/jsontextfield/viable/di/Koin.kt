package com.jsontextfield.viable.di

import android.content.Context
import androidx.room.Room
import com.jsontextfield.viable.data.database.ViaRailDatabase
import com.jsontextfield.viable.data.repositories.ITrainRepository
import com.jsontextfield.viable.data.repositories.TrainRepository
import com.jsontextfield.viable.network.Downloader
import com.jsontextfield.viable.ui.ViableViewModel
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val networkModule = module {
    single<OkHttpClient> { OkHttpClient() }
    single<Downloader> { Downloader(get<OkHttpClient>()) }
}

val dataModule = module {
    single<ViaRailDatabase> {
        Room.databaseBuilder(
            androidApplication(),
            ViaRailDatabase::class.java,
            "viarail.db"
        ).createFromAsset("via.db").build()
    }
    single<ITrainRepository> {
        TrainRepository(
            get<ViaRailDatabase>(),
            get<Downloader>(),
        )
    }
}

val viewModelModule = module {
    factoryOf(::ViableViewModel)
}

fun initKoin(context: Context) {
    startKoin {
        androidContext(context)
        modules(
            networkModule,
            dataModule,
            viewModelModule,
        )
    }
}
