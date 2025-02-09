package com.jsontextfield.viable.di

import com.jsontextfield.viable.data.database.DatabaseFactory
import com.jsontextfield.viable.data.database.ViaRailDatabase
import com.jsontextfield.viable.data.repositories.ITrainRepository
import com.jsontextfield.viable.data.repositories.TrainRepository
import com.jsontextfield.viable.network.TrainService
import com.jsontextfield.viable.ui.ViableViewModel
import io.ktor.client.HttpClient
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

expect val platformModule: Module

val networkModule = module {
    single<HttpClient> { HttpClient() }
    single<TrainService> { TrainService(get<HttpClient>()) }
}

val dataModule = module {
    single {
        get<DatabaseFactory>().create()
            //.setDriver(BundledSQLiteDriver())
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
    factoryOf(::ViableViewModel)
}

fun initKoin(config: KoinAppDeclaration? = null) {
    startKoin {
        config?.invoke(this)
        modules(
            networkModule,
            dataModule,
            viewModelModule,
            platformModule,
        )
    }
}
