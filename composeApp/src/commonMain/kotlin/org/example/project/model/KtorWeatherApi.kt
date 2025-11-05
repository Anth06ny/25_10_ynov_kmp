package org.example.project.model

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.isSuccess
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

//Suspend sera expliqué dans le chapitre des coroutines
suspend fun main() {
    //Création et réglage du client
    val client = HttpClient {
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

    val res = KtorWeatherApi(client).loadWeathers("Toulouse")
    for (r in res) {
        println(r.getResume())
    }
    client.close()
}

class KtorWeatherApi(val httpClient: HttpClient) {

   companion object {
       private const val API_URL =
           "https://api.openweathermap.org/data/2.5"
   }

    //GET Le JSON reçu sera parser en List<MuseumObject>,
    //Crash si le JSON ne correspond pas
    suspend fun loadWeathers(cityName: String): List<WeatherBean> {
        val response = httpClient.get("$API_URL/find?appid=b80967f0a6bd10d23e44848547b26550&units=metric&lang=fr&q=$cityName") {
//            headers {
//                append("Authorization", "Bearer YOUR_TOKEN")
//                append("Custom-Header", "CustomValue")
//            }
        }
        if (!response.status.isSuccess()) {
            throw Exception("Erreur API: ${response.status} - ${response.bodyAsText()}")
        }

        return response.body<WeatheAPIResult>().list.onEach { w ->
            w.weather.forEach {
                it.icon = "https://openweathermap.org/img/wn/${it.icon}@4x.png"
            }
        }
        //possibilité de typer le body
        //.body<List<MuseumObject>>()
    }

    //Ferme le Client mais celui ci ne sera plus utilisable. Uniquement pour le main
    fun close() = httpClient.close()

}

//DATA CLASS
@Serializable
data class WeatheAPIResult(
    val list: List<WeatherBean>,
)

@Serializable
data class WeatherBean(
    val id: Int,
    val main: TempBean,
    val name: String,
    val weather: List<DescriptionBean>,
    val wind: WindBean
) {
    fun getResume() = """
        Il fait ${main.temp}° à $name (id=$id) avec un vent de ${wind.speed} m/s
        -Description : ${weather.firstOrNull()?.description ?: "-"}
        -Icône : ${weather.firstOrNull()?.icon ?: "-"}
    """.trimIndent()
}

@Serializable
data class TempBean(
    val temp: Double,
)

@Serializable
data class DescriptionBean(
    val description: String,
    var icon: String,
)

@Serializable
data class WindBean(
    val speed: Double
)