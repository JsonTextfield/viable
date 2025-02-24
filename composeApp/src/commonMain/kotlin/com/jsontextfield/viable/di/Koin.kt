package com.jsontextfield.viable.di

import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.jsontextfield.viable.data.database.DatabaseFactory
import com.jsontextfield.viable.data.datasource.IViaRailDataSource
import com.jsontextfield.viable.data.datasource.ViaRailRemoteDataSource
import com.jsontextfield.viable.data.repositories.ITrainRepository
import com.jsontextfield.viable.data.repositories.TrainRepository
import com.jsontextfield.viable.network.TrainService
import com.jsontextfield.viable.ui.ViableViewModel
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.ktor.client.HttpClient
import kotlinx.coroutines.runBlocking
import org.jetbrains.compose.resources.getString
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module
import viable.composeapp.generated.resources.Res
import viable.composeapp.generated.resources.supabase_key

expect val platformModule: Module

val networkModule = module {
    single<HttpClient> { HttpClient() }
    single<TrainService> { TrainService(get<HttpClient>()) }
    single<SupabaseClient> {
        runBlocking {
            createSupabaseClient(
                supabaseUrl = "https://mqfvppcezfwunrgqgucv.supabase.co",
                supabaseKey = getString(Res.string.supabase_key),
            ) {
                install(Postgrest)
            }
        }
    }
}

val dataModule = module {
    single {
        get<DatabaseFactory>().create()
            .setDriver(BundledSQLiteDriver())
            .build()
    }
    single<IViaRailDataSource> {
        //ViaRailLocalDataSource(get<ViaRailDatabase>())
        ViaRailRemoteDataSource(get<SupabaseClient>())
    }
    single<ITrainRepository> {
        TrainRepository(
            get<IViaRailDataSource>(),
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
