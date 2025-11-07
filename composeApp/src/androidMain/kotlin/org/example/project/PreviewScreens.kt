package org.example.project

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import org.example.project.di.apiModule
import org.example.project.di.viewModelModule
import org.example.project.model.DescriptionBean
import org.example.project.model.TempBean
import org.example.project.model.WeatherBean
import org.example.project.model.WindBean
import org.example.project.model.databaseModule
import org.example.project.ui.MyError
import org.example.project.ui.screens.DetailScreen
import org.example.project.ui.screens.SearchScreen
import org.example.project.ui.theme.AppTheme
import org.example.project.viewmodel.MainViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.compose.KoinApplicationPreview
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun InitKoinForPreview(content: @Composable () -> Unit) {
    val context = LocalContext.current
    KoinApplicationPreview(application = {
        androidContext(context)
        modules(viewModelModule)
        modules(databaseModule())
        modules(apiModule)
    }, content)
}


@Preview(showBackground = true, showSystemUi = true)
@Preview(
    showBackground = true, showSystemUi = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL, locale = "fr"
)
@Composable
fun SearchScreenPreview() {

    InitKoinForPreview {
        AppTheme {
            Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

                val mainViewModel: MainViewModel = koinViewModel<MainViewModel>()
                mainViewModel.loadFakeData(true, "un message d'erreur")
                SearchScreen(
                    modifier = Modifier.padding(innerPadding),
                    mainViewModel = mainViewModel
                )
            }
        }
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Preview(
    showBackground = true, showSystemUi = true,
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
            or android.content.res.Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
fun DetailScreenPreview() {

    AppTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            DetailScreen(
                modifier = Modifier.padding(innerPadding),
                //jeu de donnée pour la Preview
                data = WeatherBean(
                    id = 2,
                    name = "Toulouse",
                    main = TempBean(temp = 22.3),
                    weather = listOf(
                        DescriptionBean(description = "partiellement nuageux", icon = "https://picsum.photos/201")
                    ),
                    wind = WindBean(speed = 3.2)
                )
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Preview(showBackground = true, showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun MyErrorPreview() {
    AppTheme() {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            Column(modifier = Modifier.padding(innerPadding)) {
                //Je mets 2 versions pour tester avec et sans message d'erreur
                MyError(errorMessage = "Avec message d'erreur")
                Text("Sans erreur : ")
                MyError(errorMessage = "")
                Text("----------")
            }
        }
    }
}
