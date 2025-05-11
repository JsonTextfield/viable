package com.jsontextfield.viable.di

import com.jsontextfield.viable.data.database.ViaRailRoomDatabase
import com.jsontextfield.viable.data.repositories.ITrainRepository
import com.jsontextfield.viable.data.repositories.TrainRepository
import com.jsontextfield.viable.network.TrainService
import com.jsontextfield.viable.ui.ViableViewModel
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

expect fun platformModule(): Module

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
    single<ITrainRepository> {
        TrainRepository(
            get<ViaRailRoomDatabase>(),
            get<TrainService>(),
        )
    }
}

val viewModelModule = module {
    viewModelOf(::ViableViewModel)
}

fun initKoin(config: KoinAppDeclaration? = null) {
    startKoin {
        config?.invoke(this)
        modules(
            networkModule,
            dataModule,
            viewModelModule,
            platformModule(),
        )
    }
}
