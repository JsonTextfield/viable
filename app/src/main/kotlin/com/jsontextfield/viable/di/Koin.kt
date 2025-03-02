package com.jsontextfield.viable.di

import android.content.Context
import androidx.room.Room
import com.jsontextfield.viable.data.database.ViaRailDatabase
import com.jsontextfield.viable.data.repositories.ITrainRepository
import com.jsontextfield.viable.data.repositories.TrainRepository
import com.jsontextfield.viable.network.TrainService
import com.jsontextfield.viable.ui.ViableViewModel
import io.ktor.client.HttpClient
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin
import org.koin.dsl.module

val networkModule = module {
    single<HttpClient> { HttpClient() }
    single<TrainService> { TrainService(get<HttpClient>()) }
}

val dataModule = module {
    single<ViaRailDatabase> {
        Room
            .databaseBuilder(
                androidApplication(),
                ViaRailDatabase::class.java,
                "viarail.db"
            ).createFromAsset("via.db")
            .fallbackToDestructiveMigration()
            .build()
    }
    single<ITrainRepository> {
        TrainRepository(
            get<ViaRailDatabase>(),
            get<TrainService>(),
        )
    }
}

val viewModelModule = module {
    single<ViableViewModel> {
        ViableViewModel(get<ITrainRepository>())
    }
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
