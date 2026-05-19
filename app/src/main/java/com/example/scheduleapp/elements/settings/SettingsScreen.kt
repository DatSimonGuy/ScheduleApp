package com.example.scheduleapp.elements.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Accessibility
import androidx.compose.material.icons.filled.Brush
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.scheduleapp.R
import com.example.scheduleapp.elements.settings.parts.SettingsCategory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    navController: NavController,
    viewModel: SettingsViewModel
) {
    val categories = mapOf(
        Icons.Default.Settings to stringResource(R.string.general) to SettingsDestination.GeneralSettings,
        Icons.Default.CalendarToday to stringResource(R.string.schedules) to SettingsDestination.SchedulesSettings,
        Icons.Default.Brush to stringResource(R.string.appearance) to SettingsDestination.AppearanceSettings,
        Icons.Default.Accessibility to stringResource(R.string.accessibility) to SettingsDestination.AccessibilitySettings,
        Icons.Default.Info to stringResource(R.string.about) to SettingsDestination.AboutSettings
    )
    Scaffold(
        topBar = { TopAppBar(title = { Text(stringResource(R.string.settings)) }) }
    ) { paddingValues ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            categories.forEach {
                SettingsCategory(
                    it.key.second,
                    {navController.navigate(it.value)},
                    it.key.first,
                    Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }
    }
}