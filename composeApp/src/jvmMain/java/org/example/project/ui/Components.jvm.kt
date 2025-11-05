package org.example.project.ui

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.example.project.model.WeatherBean
import org.example.project.ui.screens.PictureRowItem

@Composable
actual fun WeatherGallery(modifier: Modifier, urlList: List<WeatherBean>, onPictureClick: (WeatherBean) -> Unit) {

    // 1️⃣ État du scroll de la LazyRow
    val state = rememberLazyListState()

    // 2️⃣ Scope pour lancer la coroutine
    val coroutineScope = rememberCoroutineScope()

    LazyRow(
        state = state,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
            .draggable(
                orientation = Orientation.Horizontal,
                state = rememberDraggableState { delta ->
                    coroutineScope.launch {
                        state.scrollBy(-delta)
                    }
                },
            )
    ) {
        items(urlList.size) {
            PictureRowItem(
                data = urlList[it],
                onPictureClick = onPictureClick
            )
        }
    }
}