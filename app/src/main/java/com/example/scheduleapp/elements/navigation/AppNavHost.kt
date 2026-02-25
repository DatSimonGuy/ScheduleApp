package com.example.scheduleapp.elements.navigation

import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.example.scheduleapp.data.repository.ScheduleRepository
import com.example.scheduleapp.data.repository.SettingsRepository
import com.example.scheduleapp.elements.schedule.ScheduleScreen
import com.example.scheduleapp.elements.settings.SettingsScreen
import com.example.scheduleapp.elements.settings.SettingsViewModel
import com.example.scheduleapp.elements.settings.SettingsViewModelFactory
import com.example.scheduleapp.elements.schedule.ScheduleViewModel
import com.example.scheduleapp.elements.schedule.ScheduleViewModelFactory
import com.example.scheduleapp.elements.schedule.parts.LessonPage
import com.example.scheduleapp.elements.settings.subpages.AboutSettingsPage
import com.example.scheduleapp.elements.settings.subpages.AppearanceSettingsPage
import com.example.scheduleapp.elements.settings.subpages.SchedulesSettingsPage

@Composable
fun AppNavHost(
    navController: NavHostController,
    startDestination: Destination,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current.applicationContext

    val settingsRepository = remember {
        SettingsRepository(context)
    }

    val scheduleRepository = remember {
        ScheduleRepository(context)
    }

    val settingsVMFactory = remember {
        SettingsViewModelFactory(settingsRepository, scheduleRepository)
    }

    val scheduleVMFactory = remember {
        ScheduleViewModelFactory(navController, settingsRepository, scheduleRepository)
    }

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {

        composable<Destination.Home> {

        }

        navigation<Destination.Schedule>(
            startDestination = ScheduleDestination.ScheduleScreen
        ) {
            composable<ScheduleDestination.ScheduleScreen> { backStackEntry ->
                val viewModel = backStackEntry.scheduleViewModel(navController, scheduleVMFactory)
                ScheduleScreen(navController, viewModel)
            }

            composable<ScheduleDestination.LessonScreen> { backStackEntry ->
                val viewModel = backStackEntry.scheduleViewModel(navController, scheduleVMFactory)
                val lessonRoute = backStackEntry.toRoute<ScheduleDestination.LessonScreen>()
                LessonPage(lessonRoute.lessonId, viewModel)
            }
        }

        navigation<Destination.Settings>(
            startDestination = SettingsDestination.SettingsPage
        ) {
            composable<SettingsDestination.SettingsPage> { backStackEntry ->
                val viewModel = backStackEntry.settingsViewModel(navController, settingsVMFactory)
                SettingsScreen(navController, viewModel)
            }

            composable<SettingsDestination.SchedulesSettings> { backStackEntry ->
                val viewModel = backStackEntry.settingsViewModel(navController, settingsVMFactory)
                SchedulesSettingsPage(viewModel)
            }

            composable<SettingsDestination.AppearanceSettings> { backStackEntry ->
                val viewModel = backStackEntry.settingsViewModel(navController, settingsVMFactory)
                AppearanceSettingsPage(viewModel)
            }

            composable<SettingsDestination.AboutSettings> { backStackEntry ->
                val viewModel = backStackEntry.settingsViewModel(navController, settingsVMFactory)
                AboutSettingsPage(viewModel)
            }
        }
    }
}