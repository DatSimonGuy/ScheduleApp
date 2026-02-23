package com.example.scheduleapp.elements.settings.subpages

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.scheduleapp.data.classes.Schedule
import com.example.scheduleapp.elements.formElements.SettingsSelector
import com.example.scheduleapp.elements.forms.NewScheduleForm
import com.example.scheduleapp.elements.settings.SettingsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SchedulesSettingsPage(
    viewModel: SettingsViewModel
) {
    val ui by viewModel.uiState.collectAsStateWithLifecycle()
    val selectionMap = remember { mutableStateMapOf<String, Boolean>() }
    var newScheduleFormExpanded by rememberSaveable { mutableStateOf(false) }
    var editedSchedule: Triple<String, Boolean, Schedule>? by rememberSaveable { mutableStateOf(null) }

    if (newScheduleFormExpanded) {
        NewScheduleForm(
            onDismissRequest = { newScheduleFormExpanded = false; editedSchedule = null },
            onSuccess = { name, isPrivate ->
                viewModel.addNewSchedule(name, isPrivate, editedSchedule?.third ?: Schedule())
                editedSchedule?.let { viewModel.deleteSchedule(it.first, it.second) }
            },
            editedSchedule?.first ?: "",
            editedSchedule?.second ?: true,
        )
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Schedules") },
                actions = {
                    IconButton(onClick = { newScheduleFormExpanded = true }) {
                        Icon(Icons.Default.Add, "")
                    }
                    if (selectionMap.containsValue(true)) {
                        IconButton(
                            onClick = {
                                selectionMap.forEach { (key, value) ->
                                    if (value) {
                                        viewModel.deleteSchedule(key, true)
                                        selectionMap[key] = false
                                    }
                                }
                            }
                        ) {
                            Icon(Icons.Default.Delete, "")
                        }
                    }
                }
            )
        },
        bottomBar = {
            Column (
                Modifier.fillMaxWidth().padding(bottom = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                SettingsSelector(
                    Modifier.fillMaxWidth(0.95f),
                    label = "Default schedule",
                    items = ui.schedules.schedules.map { it.key },
                    selectedItem = ui.defaultSchedule ?: "None",
                    onSelectionChange = {
                        viewModel.onDefaultScheduleChange(it)
                    },
                    enabled = ui.defaultSchedule != null
                )
            }
        }
    ) { paddingValues ->
        Column (
            Modifier.padding(paddingValues).fillMaxHeight()
        ) {
            LazyColumn {
                ui.schedules.schedules.forEach { (key, schedule) ->
                    item {
                        ListItem(
                            modifier = Modifier.clickable {
                                selectionMap[key] = !(selectionMap[key] ?: false)
                            },
                            headlineContent = { Text(key) },
                            trailingContent = {
                                Checkbox(
                                    selectionMap[key] == true,
                                    onCheckedChange = { selectionMap[key] = it }
                                )
                            },
                            leadingContent = {
                                IconButton(
                                    onClick = {
                                        editedSchedule = Triple(key, true, schedule)
                                        newScheduleFormExpanded = true
                                    }
                                ) {
                                    Icon(Icons.Default.Edit, "")
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

