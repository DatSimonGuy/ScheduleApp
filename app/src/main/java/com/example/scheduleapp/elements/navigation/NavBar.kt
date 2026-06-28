package com.example.scheduleapp.elements.navigation

import Destination
import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BorderVertical
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailDefaults
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.scheduleapp.data.datastore.SettingKeys
import com.example.scheduleapp.data.datastore.SettingKeys.navBarRight
import com.example.scheduleapp.data.datastore.settingsDataStore
import isScheduleDestination
import isSettingsDestination
import kotlinx.coroutines.flow.map

@Composable
fun Navbar(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    val startDestination by remember(context) {
        context.settingsDataStore.data.map { preferences ->
            val savedId = preferences[SettingKeys.startPage]
            Destination.main.firstOrNull { it.id == savedId } ?: Destination.Home
        }
    }.collectAsState(initial = null)

    val navBarRight by remember(context) {
        context.settingsDataStore.data.map { preferences ->
            val isRight = preferences[SettingKeys.navBarRight]
            isRight != false
        }
    }.collectAsState(initial = true)

    val defaultInsets = NavigationRailDefaults.windowInsets

    val railInsets = if (navBarRight) {
        defaultInsets.only(WindowInsetsSides.Top + WindowInsetsSides.Bottom + WindowInsetsSides.Right)
    } else {
        defaultInsets.only(WindowInsetsSides.Top + WindowInsetsSides.Bottom + WindowInsetsSides.Left)
    }

    if (startDestination == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }

    if (isLandscape) {
        Row(
            Modifier.fillMaxSize()
        ) {
            if (navBarRight) {
                AppNavHost(
                    navController,
                    startDestination!!,
                    modifier = Modifier.weight(1f)
                )
            }

            NavigationRail(
                windowInsets = railInsets
            ) {
                Spacer(Modifier.weight(1f))
                Destination.main.forEach { destination ->
                    val isSelected = navBackStackEntry?.destination?.let { currentNavDest ->
                        when (destination) {
                            Destination.Home -> currentNavDest.hasRoute<Destination.Home>()
                            Destination.Schedule -> currentNavDest.isScheduleDestination()
                            Destination.Settings -> currentNavDest.isSettingsDestination()
                            else -> false
                        }
                    } ?: false
                    NavigationRailItem(
                        selected = isSelected,
                        onClick = {
                            navController.navigate(destination) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = destination != Destination.Settings
                            }
                        },
                        icon = {
                            Icon(
                                when (destination) {
                                    Destination.Home -> Icons.Default.Home
                                    Destination.Schedule -> Icons.Default.DateRange
                                    Destination.Settings -> Icons.Default.Settings
                                    else -> {
                                        Icons.Default.BorderVertical
                                    }
                                },
                                contentDescription = stringResource(destination.displayName!!)
                            )
                        },
                        label = { Text(stringResource(destination.displayName!!)) },
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                    Spacer(Modifier.weight(1f))
                }
            }
            if (!navBarRight) {
                AppNavHost(
                    navController,
                    startDestination!!,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    } else {
        Scaffold(
            modifier = modifier,
            bottomBar = {
                NavigationBar (
                    windowInsets = NavigationBarDefaults.windowInsets
                ) {
                    Destination.main.forEach { destination ->
                        val isSelected = navBackStackEntry?.destination?.let { currentNavDest ->
                            when (destination) {
                                Destination.Home -> currentNavDest.hasRoute<Destination.Home>()
                                Destination.Schedule -> currentNavDest.isScheduleDestination()
                                Destination.Settings -> currentNavDest.isSettingsDestination()
                                else -> false
                            }
                        } ?: false
                        NavigationBarItem(
                            selected = isSelected,
                            onClick = {
                                navController.navigate(destination) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = destination != Destination.Settings
                                }
                            },
                            icon = {
                                Icon(
                                    when(destination) {
                                        Destination.Home -> Icons.Default.Home
                                        Destination.Schedule -> Icons.Default.DateRange
                                        Destination.Settings -> Icons.Default.Settings
                                        else -> {
                                            Icons.Default.BorderVertical
                                        }
                                    },
                                    contentDescription = stringResource(destination.displayName!!)
                                )
                            },
                            label = { Text(stringResource(destination.displayName!!)) }
                        )
                    }
                }
            }
        ) { innerPadding ->
            AppNavHost(
                navController,
                startDestination!!,
                Modifier.padding(innerPadding)
            )
        }
    }
}