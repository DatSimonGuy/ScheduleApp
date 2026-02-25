package com.example.scheduleapp.elements.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.scheduleapp.elements.navigation.SettingsDestination
import com.example.scheduleapp.elements.settings.parts.SettingsCategory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    navController: NavController,
    viewModel: SettingsViewModel
) {
    val categories = mapOf(
        "Schedules" to SettingsDestination.SchedulesSettings,
        "Appearance" to SettingsDestination.AppearanceSettings,
        "About" to SettingsDestination.AboutSettings
    )
    Scaffold(
        topBar = { TopAppBar(title = { Text("Settings") }) }
    ) { paddingValues ->
        Column(
            Modifier.fillMaxSize().padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            categories.forEach {
                SettingsCategory(it.key, {navController.navigate(it.value)})
            }
        }
    }
}