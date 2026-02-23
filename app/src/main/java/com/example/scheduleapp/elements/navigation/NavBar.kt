package com.example.scheduleapp.elements.navigation

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.rememberNavController

@Composable
fun Navbar(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val startDestination: Destination = Destination.all[0]
    var selectedDestination by rememberSaveable { mutableIntStateOf(startDestination.displayName!!) }

    Scaffold(
        modifier = modifier,
        bottomBar = {
            NavigationBar (
                windowInsets = NavigationBarDefaults.windowInsets
            ) {
                Destination.all.forEach { destination ->
                    NavigationBarItem(
                        selected = selectedDestination == destination.displayName,
                        onClick = {
                            navController.navigate(route = destination)
                            selectedDestination = destination.displayName!!
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