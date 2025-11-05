package org.example.project

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.example.project.di.apiModule
import org.example.project.di.viewModelModule
import org.example.project.ui.AppNavigation
import org.example.project.ui.theme.AppTheme
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinApplication

@Composable
@Preview
fun App() {
    KoinApplication(application = {
        modules(apiModule, viewModelModule)
    }) {
        AppTheme {
            Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                AppNavigation(
                    modifier = Modifier.padding(innerPadding)
                )
            }
        }
    }
}