package org.example.project.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.example.project.model.DescriptionBean
import org.example.project.model.KtorWeatherApi
import org.example.project.model.TempBean
import org.example.project.model.WeatherBean
import org.example.project.model.WindBean

suspend fun main() {
//    val viewModel = MainViewModel()
//    viewModel.loadWeathers("Toulouse")
//    //viewModel.loadWeathers("Paris")
//    //attente
//    while (viewModel.runInProgress.value) {
//        delay(500)
//    }
//    //Affichage de la liste et du message d'erreur
//    println("List : ${viewModel.dataList.value}")
//    println("ErrorMessage : ${viewModel.errorMessage.value}")

}

open class MainViewModel(
    val ktorWeatherApi: KtorWeatherApi,
    val dispatcher : CoroutineDispatcher = Dispatchers.IO) : ViewModel() {
    //MutableStateFlow est une donnée observable
    val dataList = MutableStateFlow(emptyList<WeatherBean>())
    val runInProgress = MutableStateFlow(false)
    val errorMessage = MutableStateFlow("")


    fun loadFakeData(runInProgress :Boolean = false, errorMessage:String = "" ) {
        this.runInProgress.value = runInProgress
        this.errorMessage.value = errorMessage

        dataList.value = listOf(
            WeatherBean(
                id = 1,
                name = "Paris",
                main = TempBean(temp = 18.5),
                weather = listOf(
                    DescriptionBean(description = "ciel dégagé", icon = "https://picsum.photos/200")
                ),
                wind = WindBean(speed = 5.0)
            ),
            WeatherBean(
                id = 2,
                name = "Toulouse",
                main = TempBean(temp = 22.3),
                weather = listOf(
                    DescriptionBean(description = "partiellement nuageux", icon = "https://picsum.photos/201")
                ),
                wind = WindBean(speed = 3.2)
            ),
            WeatherBean(
                id = 3,
                name = "Toulon",
                main = TempBean(temp = 25.1),
                weather = listOf(
                    DescriptionBean(description = "ensoleillé", icon = "https://picsum.photos/202")
                ),
                wind = WindBean(speed = 6.7)
            ),
            WeatherBean(
                id = 4,
                name = "Lyon",
                main = TempBean(temp = 19.8),
                weather = listOf(
                    DescriptionBean(description = "pluie légère", icon = "https://picsum.photos/203")
                ),
                wind = WindBean(speed = 4.5)
            )
        ).shuffled() //shuffled() pour avoir un ordre différent à chaque appel
    }

    open fun loadWeathers(cityName: String): Job {
        runInProgress.value = true
        errorMessage.value = ""


        return viewModelScope.launch(dispatcher) {
            try {
                if(cityName.length < 3) {
                    throw Exception("Il faut au moins 3 caratchères")
                }
                dataList.value = ktorWeatherApi.loadWeathers(cityName)
            } catch (e: Exception) {
                e.printStackTrace()
                errorMessage.value = e.message ?: "Une erreur est survenue"
            } finally {
                runInProgress.value = false
            }
        }
    }
}