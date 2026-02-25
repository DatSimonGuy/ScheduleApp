package com.example.scheduleapp.elements.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import com.example.scheduleapp.elements.schedule.ScheduleViewModel
import com.example.scheduleapp.elements.schedule.ScheduleViewModelFactory
import com.example.scheduleapp.elements.settings.SettingsViewModel
import com.example.scheduleapp.elements.settings.SettingsViewModelFactory

@Composable
fun NavBackStackEntry.scheduleViewModel(
    navController: NavHostController,
    factory: ScheduleViewModelFactory
): ScheduleViewModel {
    val parentEntry = remember(this) {
        navController.getBackStackEntry(Destination.Schedule)
    }
    return viewModel(parentEntry, factory = factory)
}

@Composable
fun NavBackStackEntry.settingsViewModel(
    navController: NavHostController,
    factory: SettingsViewModelFactory
): SettingsViewModel {
    val parentEntry = remember(this) {
        navController.getBackStackEntry(Destination.Settings)
    }
    return viewModel(parentEntry, factory = factory)
}