package com.jsontextfield.viable.di

import android.content.Context
import androidx.room.Room
import com.jsontextfield.viable.data.database.ViaRailRoomDatabase
import com.jsontextfield.viable.data.repositories.ITrainRepository
import com.jsontextfield.viable.data.repositories.TrainRepository
import com.jsontextfield.viable.network.TrainService
import com.jsontextfield.viable.ui.ViableViewModel
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin
import org.koin.dsl.module

val networkModule = module {
    single<HttpClient> {
        HttpClient {
            install(ContentNegotiation) {
                json(
                    Json {
                        ignoreUnknownKeys = true
                    }
                )
            }
        }
    }
    single<TrainService> { TrainService(get<HttpClient>()) }
}

val dataModule = module {
    single<ViaRailRoomDatabase> {
        Room
            .databaseBuilder(
                androidApplication(),
                ViaRailRoomDatabase::class.java,
                "viarail.db"
            ).createFromAsset("via.db")
            .fallbackToDestructiveMigration()
            .build()
    }
    single<ITrainRepository> {
        TrainRepository(
            get<ViaRailRoomDatabase>(),
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
