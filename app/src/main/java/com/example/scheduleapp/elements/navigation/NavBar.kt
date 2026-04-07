package com.example.scheduleapp.elements.navigation

import Destination
import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BorderVertical
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.scheduleapp.data.datastore.SettingKeys
import com.example.scheduleapp.data.datastore.settingsDataStore
import kotlinx.coroutines.flow.first

@Composable
fun Navbar(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val navController = rememberNavController()
    val startDestination = Destination.Home
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentNavDestination = navBackStackEntry?.destination

    LaunchedEffect(Unit) {
        val dest = Destination.main.firstOrNull { destination ->
            context.settingsDataStore.data.first()[SettingKeys.startPage] == destination.id
        }
        dest?.let {
            navController.navigate(it)
        }
    }

    fun isSelected(destination: Destination): Boolean {
        val className = destination::class.simpleName ?: ""
        return currentNavDestination?.route?.contains(className, ignoreCase = true) == true
    }

    Scaffold(
        modifier = modifier,
        bottomBar = {
            NavigationBar (
                windowInsets = NavigationBarDefaults.windowInsets
            ) {
                Destination.main.forEach { destination ->
                    NavigationBarItem(
                        selected = isSelected(destination),
                        onClick = {
                            navController.navigate(destination) {
                                popUpTo(navController.graph.startDestinationId) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
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
            startDestination,
            Modifier.padding(innerPadding)
        )
    }
}