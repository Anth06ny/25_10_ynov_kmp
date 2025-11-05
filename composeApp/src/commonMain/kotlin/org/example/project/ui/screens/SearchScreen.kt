package org.example.project.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import kotlinproject.composeapp.generated.resources.Res
import kotlinproject.composeapp.generated.resources.bt_load_data
import kotlinproject.composeapp.generated.resources.clear_filter
import kotlinproject.composeapp.generated.resources.img
import org.example.project.model.WeatherBean
import org.example.project.ui.MyError
import org.example.project.ui.WeatherGallery
import org.example.project.viewmodel.MainViewModel
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel


@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    mainViewModel: MainViewModel = koinViewModel<MainViewModel>(),
    onPictureClick: (WeatherBean) -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        var searchText = rememberSaveable { mutableStateOf("") }

        SearchBar(
            searchText = searchText,
            onSearchEvent = {
                mainViewModel.loadWeathers(searchText.value)
            }
        )

        val list = mainViewModel.dataList.collectAsStateWithLifecycle().value
        val errorMessage by mainViewModel.errorMessage.collectAsStateWithLifecycle()
        val runInProgress by mainViewModel.runInProgress.collectAsStateWithLifecycle()

        MyError(errorMessage = errorMessage)

        AnimatedVisibility(visible = runInProgress) {
            CircularProgressIndicator()
        }


        WeatherGallery(modifier = Modifier.weight(1f), list, onPictureClick = onPictureClick)


        Row {
            Button(
                onClick = { searchText.value = "" },
                contentPadding = ButtonDefaults.ButtonWithIconContentPadding
            ) {
                Icon(
                    Icons.Filled.Favorite,
                    contentDescription = "Localized description",
                    modifier = Modifier.size(ButtonDefaults.IconSize)
                )
                Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                Text(stringResource(Res.string.clear_filter))
            }

            Button(
                onClick = { mainViewModel.loadWeathers(searchText.value) },
                contentPadding = ButtonDefaults.ButtonWithIconContentPadding
            ) {
                Icon(
                    Icons.Filled.Favorite,
                    contentDescription = "Localized description",
                    modifier = Modifier.size(ButtonDefaults.IconSize)
                )
                Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                Text(stringResource(Res.string.bt_load_data))
            }
        }
    }
}

@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    searchText: MutableState<String> = mutableStateOf(""),
    onSearchEvent: () -> Unit = {}
) {


    TextField(
        value = searchText.value, //Valeur affichée
        onValueChange = { newValue: String ->
            searchText.value = newValue
        }, //Nouveau texte entrée
        leadingIcon = { //Image d'icône
            Icon(
                imageVector = Icons.Default.Search,
                tint = MaterialTheme.colorScheme.primary,
                contentDescription = null
            )
        },
        singleLine = true,
        label = { //Texte d'aide qui se déplace
            Text("Enter text")
            //Pour aller le chercher dans string.xml, R de votre package com.nom.projet
            //Text(stringResource(R.string.placeholder_search))
        },
        //placeholder = { //Texte d'aide qui disparait
        //Text("Recherche")
        //},

        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search), // Définir le bouton "Entrée" comme action de recherche
        keyboardActions = KeyboardActions(onSearch = {
            onSearchEvent()
        }
        ), // Déclenche l'action définie
        //Comment le composant doit se placer
        modifier = modifier
            .fillMaxWidth() // Prend toute la largeur
            .heightIn(min = 56.dp) //Hauteur minimum
    )
}

@Composable //Composable affichant 1 élément
fun PictureRowItem(
    modifier: Modifier = Modifier,
    data: WeatherBean,
    onPictureClick: (WeatherBean) -> Unit
) {

    var longText by remember { mutableStateOf(false) }

    Row(
        modifier = modifier
            .background(MaterialTheme.colorScheme.tertiaryContainer)
            .fillMaxWidth()
    ) {

        //Permission Internet nécessaire
        AsyncImage(
            model = data.weather.firstOrNull()?.icon ?: "",
            //Pour aller le chercher dans string.xml R de votre package com.nom.projet
            //contentDescription = getString(R.string.picture_of_cat),
            //En dur
            contentDescription = "une photo de chat",
            contentScale = ContentScale.FillWidth,

            //Pour toto.png. Si besoin de choisir l'import pour la classe R, c'est celle de votre package
            //Image d'échec de chargement qui sera utilisé par la preview
            error = painterResource(Res.drawable.img),
            //Image d'attente.
            //placeholder = painterResource(R.drawable.toto),

            onError = { println(it) },
            modifier = Modifier
                .heightIn(max = 100.dp)
                .widthIn(max = 100.dp)
                .clickable {
                    onPictureClick(data)
                }
        )


        Column(
            modifier = Modifier
                .padding(10.dp)
                .clickable {
                    longText = !longText
                }
        ) {
            Text(
                text = data.name,
                fontSize = 20.sp,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = if (longText) data.getResume()
                else
                    data.getResume().take(20) + "...",
                color = MaterialTheme.colorScheme.onTertiaryContainer,
                fontSize = 14.sp,
                modifier = Modifier.animateContentSize()

            )
        }
    }

}