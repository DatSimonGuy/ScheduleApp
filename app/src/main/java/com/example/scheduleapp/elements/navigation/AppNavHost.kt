package com.example.scheduleapp.elements.navigation

import Destination
import SettingsDestination
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.example.scheduleapp.data.repository.PreferenceRepository
import com.example.scheduleapp.data.repository.ScheduleRepository
import com.example.scheduleapp.data.repository.SettingsRepository
import com.example.scheduleapp.elements.home.HomeScreen
import com.example.scheduleapp.elements.home.HomeViewModel
import com.example.scheduleapp.elements.home.HomeViewModelFactory
import com.example.scheduleapp.elements.schedule.ScheduleScreen
import com.example.scheduleapp.elements.schedule.ScheduleViewModelFactory
import com.example.scheduleapp.elements.schedule.parts.LessonPage
import com.example.scheduleapp.elements.settings.SettingsScreen
import com.example.scheduleapp.elements.settings.SettingsViewModelFactory
import com.example.scheduleapp.elements.settings.subpages.AboutSettingsPage
import com.example.scheduleapp.elements.settings.subpages.AccessibilitySettingsPage
import com.example.scheduleapp.elements.settings.subpages.AppearanceSettingsPage
import com.example.scheduleapp.elements.settings.subpages.GeneralSettingsPage
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

    val preferenceRepository = remember {
        PreferenceRepository(context)
    }

    val settingsVMFactory = remember {
        SettingsViewModelFactory(
            settingsRepository,
            scheduleRepository,
            preferenceRepository
        )
    }

    val homeVMFactory = remember {
        HomeViewModelFactory(
            settingsRepository,
            scheduleRepository,
            preferenceRepository
        )
    }

    val scheduleVMFactory = remember {
        ScheduleViewModelFactory(
            navController,
            settingsRepository,
            scheduleRepository,
            preferenceRepository
        )
    }

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {

        composable<Destination.Home> {
            val viewModel = viewModel<HomeViewModel>(factory = homeVMFactory)
            HomeScreen(navController, viewModel)
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
                LessonPage(lessonRoute.lessonId, viewModel, navController, lessonRoute.dayOfWeek)
            }
        }

        navigation<Destination.Settings>(
            startDestination = SettingsDestination.SettingsPage
        ) {
            composable<SettingsDestination.SettingsPage> { backStackEntry ->
                val viewModel = backStackEntry.settingsViewModel(navController, settingsVMFactory)
                SettingsScreen(navController, viewModel)
            }

            composable<SettingsDestination.GeneralSettings> { backStackEntry ->
                val viewModel = backStackEntry.settingsViewModel(navController, settingsVMFactory)
                GeneralSettingsPage(viewModel, navController)
            }

            composable<SettingsDestination.SchedulesSettings> { backStackEntry ->
                val viewModel = backStackEntry.settingsViewModel(navController, settingsVMFactory)
                SchedulesSettingsPage(viewModel, navController)
            }

            composable<SettingsDestination.AppearanceSettings> { backStackEntry ->
                val viewModel = backStackEntry.settingsViewModel(navController, settingsVMFactory)
                AppearanceSettingsPage(viewModel, navController)
            }

            composable<SettingsDestination.AccessibilitySettings> { backStackEntry ->
                val viewModel = backStackEntry.settingsViewModel(navController, settingsVMFactory)
                AccessibilitySettingsPage(viewModel, navController)
            }

            composable<SettingsDestination.AboutSettings> { backStackEntry ->
                val viewModel = backStackEntry.settingsViewModel(navController, settingsVMFactory)
                AboutSettingsPage(viewModel, navController)
            }
        }
    }
}