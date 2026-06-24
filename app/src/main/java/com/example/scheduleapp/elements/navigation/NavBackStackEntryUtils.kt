package com.example.scheduleapp.elements.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
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
        try {
            navController.getBackStackEntry(Destination.Schedule)
        } catch (_: Exception) {
            null
        }
    }

    return remember(this) {
        val owner = if (parentEntry != null && parentEntry.lifecycle.currentState != Lifecycle.State.DESTROYED) {
            parentEntry
        } else {
            this
        }
        ViewModelProvider(owner, factory)[ScheduleViewModel::class.java]
    }
}

@Composable
fun NavBackStackEntry.settingsViewModel(
    navController: NavHostController,
    factory: SettingsViewModelFactory
): SettingsViewModel {
    val parentEntry = remember(this) {
        try {
            navController.getBackStackEntry(Destination.Settings)
        } catch (_: Exception) {
            null
        }
    }

    return remember(this) {
        val owner = if (parentEntry != null && parentEntry.lifecycle.currentState != Lifecycle.State.DESTROYED) {
            parentEntry
        } else {
            this
        }
        ViewModelProvider(owner, factory)[SettingsViewModel::class.java]
    }
}