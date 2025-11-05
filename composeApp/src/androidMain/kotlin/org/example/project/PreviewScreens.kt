package org.example.project

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import org.example.project.model.DescriptionBean
import org.example.project.model.TempBean
import org.example.project.model.WeatherBean
import org.example.project.model.WindBean
import org.example.project.ui.MyError
import org.example.project.ui.screens.DetailScreen
import org.example.project.ui.screens.SearchScreen
import org.example.project.ui.theme.AppTheme
import org.example.project.viewmodel.MainViewModel


@Preview(showBackground = true, showSystemUi = true)
@Preview(
    showBackground = true, showSystemUi = true,
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES or android.content.res.Configuration.UI_MODE_TYPE_NORMAL, locale = "fr"
)
@Composable
fun SearchScreenPreview() {
    //Il faut remplacer NomVotreAppliTheme par le thème de votre application
    //Utilisé par exemple dans MainActivity.kt sous setContent {...}
    AppTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            val mainViewModel : MainViewModel = viewModel()
            mainViewModel.loadFakeData(true, "un message d'erreur")
            SearchScreen(
                modifier = Modifier.padding(innerPadding),
                mainViewModel = mainViewModel
            )
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
