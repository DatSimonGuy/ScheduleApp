package com.example.scheduleapp.elements.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
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

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        val settingsVMFactory = SettingsViewModelFactory(settingsRepository, scheduleRepository)
        val scheduleVMFactory = ScheduleViewModelFactory(settingsRepository, scheduleRepository)
        composable<Destination.Home> {

        }

        composable<Destination.Schedule> {
            val viewModel: ScheduleViewModel = viewModel(
                factory = scheduleVMFactory
            )
            ScheduleScreen(navController, viewModel)
        }

        composable<Destination.Settings> {
            SettingsScreen(navController)
        }

        composable<SettingsDestination.SchedulesSettings> {
            val viewModel: SettingsViewModel = viewModel(factory = settingsVMFactory)
            SchedulesSettingsPage(viewModel)
        }

        composable<SettingsDestination.AppearanceSettings> {
            val viewModel: SettingsViewModel = viewModel(factory = settingsVMFactory)
            AppearanceSettingsPage(viewModel)
        }

        composable<SettingsDestination.AboutSettings> {
            val viewModel: SettingsViewModel = viewModel(factory = settingsVMFactory)
            AboutSettingsPage(viewModel)
        }

        composable<ScheduleDestination.LessonScreen> { backEntry ->
            val lessonRoute: ScheduleDestination.LessonScreen = backEntry.toRoute()
            val viewModel: ScheduleViewModel = viewModel(factory = scheduleVMFactory)
            LessonPage(lessonRoute.lessonId, viewModel)
        }
    }
}