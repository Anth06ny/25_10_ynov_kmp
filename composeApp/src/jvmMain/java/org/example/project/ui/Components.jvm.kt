package org.example.project.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.example.project.model.WeatherBean
import org.example.project.ui.screens.PictureRowItem

@androidx.compose.runtime.Composable
actual fun WeatherGallery(modifier:Modifier, urlList: List<WeatherBean>, onPictureClick: (WeatherBean)->Unit) {
    //.filter { it.name.contains(searchText.value, true) }
    //Permet de remplacer très facilement le RecyclerView. LazyRow existe aussi
    LazyColumn  (
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
    ) {
        items(urlList.size) {
            PictureRowItem(
                data = urlList[it],
                onPictureClick = onPictureClick
            )
        }
    }
}