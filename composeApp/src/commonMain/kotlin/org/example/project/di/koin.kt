package org.example.project.di

import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.ContentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.serialization.json.Json
import org.example.project.model.KtorWeatherApi
import org.example.project.model.databaseModule
import org.example.project.viewmodel.MainViewModel
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

//Si besoin du contexte, pour le passer en paramètre
fun initKoin(appDeclaration: KoinAppDeclaration = {}) =
    startKoin {
        appDeclaration()
        modules(apiModule, databaseModule(), viewModelModule)
    }.koin

// Version pour iOS et Desktop
fun initKoin() = initKoin {}

val apiModule = module {
    //Création d'un singleton pour le client HTTP
    single {
        HttpClient {
            install(Logging) {
                //(import io.ktor.client.plugins.logging.Logger)
                logger = object : Logger {
                    override fun log(message: String) {
                        println(message)
                    }
                }
                level = LogLevel.INFO  // TRACE, HEADERS, BODY, etc.
            }
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true }, contentType = ContentType.Any)
            }
            expectSuccess = true //Exception si code >= 300
            //engine { proxy = ProxyBuilder.http("monproxy:1234") }
        }
    }
    //single { KtorWeatherApi(get()) }

    //Nouvelle version avec injection automatique des instances
    //D'abord faire l'import de PhotographerAPI, sinon singleOf n'en voudra pas
    singleOf(::KtorWeatherApi)
}

//Version spécifique au ViewModel
val viewModelModule = module {

    factory { Dispatchers.IO }

    //D'abord faire l'import de MainViewModel, sinon viewModelOf n'en voudra pas
//    viewModel {
//        MainViewModel(get(), get())
//    }
    viewModelOf(::MainViewModel)
}