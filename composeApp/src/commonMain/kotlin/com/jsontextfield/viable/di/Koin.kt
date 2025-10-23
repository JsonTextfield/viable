package com.jsontextfield.viable.di

import com.jsontextfield.viable.data.database.IViaRailDatabase
import com.jsontextfield.viable.data.repositories.ITrainRepository
import com.jsontextfield.viable.data.repositories.TrainRepository
import com.jsontextfield.viable.network.TrainService
import com.jsontextfield.viable.ui.ViableViewModel
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

expect fun platformModule(): Module

val networkModule = module {
    singleOf(::TrainService)
}

val dataModule = module {
    single<ITrainRepository> {
        TrainRepository(
            get<IViaRailDatabase>(),
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
