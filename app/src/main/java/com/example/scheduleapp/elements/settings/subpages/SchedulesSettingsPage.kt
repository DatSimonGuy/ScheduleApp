package com.example.scheduleapp.elements.settings.subpages

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ImportExport
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.scheduleapp.R
import com.example.scheduleapp.data.classes.SaveLocation
import com.example.scheduleapp.data.classes.Schedule
import com.example.scheduleapp.data.classes.ScheduleSortMode
import com.example.scheduleapp.elements.formElements.choice.ChoiceDialog
import com.example.scheduleapp.elements.formElements.choice.SettingsSelector
import com.example.scheduleapp.elements.forms.ImportForm
import com.example.scheduleapp.elements.forms.NewScheduleForm
import com.example.scheduleapp.elements.forms.states.rememberImportFormState
import com.example.scheduleapp.elements.forms.states.rememberScheduleFormState
import com.example.scheduleapp.elements.settings.SettingsViewModel
import com.example.scheduleapp.elements.settings.parts.DeleteScheduleDialog
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SchedulesSettingsPage(
    viewModel: SettingsViewModel,
    navController: NavController
) {
    val ui by viewModel.uiState.collectAsStateWithLifecycle()
    val selectionMap = remember { mutableStateMapOf<String, Boolean>() }
    var newScheduleFormExpanded by rememberSaveable { mutableStateOf(false) }
    var importFormExpanded by rememberSaveable { mutableStateOf(false) }
    var showDeleteDialog by rememberSaveable { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val snackHostState = remember { SnackbarHostState() }
    var editedSchedule: Pair<String, Schedule>? by rememberSaveable { mutableStateOf(null) }
    val scheduleFormState = rememberScheduleFormState()
    val importFormState = rememberImportFormState()
    val context = LocalContext.current
    var sortModeDialogExpanded by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(importFormExpanded) {
        importFormState.fillFields(ui.recentChatId)
    }

    LaunchedEffect(newScheduleFormExpanded) {
        scheduleFormState.fillFields(
            chatId = ui.recentChatId
        )
        editedSchedule?.let {
            scheduleFormState.fillFields(
                it.first,
                it.second.saveLocation == SaveLocation.LOCAL,
                it.second.chatId
            )
        }
    }

    if (showDeleteDialog) {
        val scheduleName = selectionMap.filter { it.value }.keys.firstOrNull() ?: ""
        DeleteScheduleDialog(
            onDismiss = {
                showDeleteDialog = false
            },
            scheduleName = scheduleName,
            onSuccess = { localOnly, repeat ->
                if (repeat) {
                    selectionMap.forEach { (name, toDelete) ->
                        scope.launch {
                            if (!toDelete) {
                                return@launch
                            }
                            ui.schedules[name]?.let {
                                viewModel.removeSchedule(name, it, localOnly)
                            }
                            if(name == ui.defaultSchedule) {
                                viewModel.onDefaultScheduleChange(
                                    ui.schedules.schedules.keys.firstOrNull()
                                )
                            }
                        }
                    }
                    selectionMap.clear()
                    showDeleteDialog = false
                } else {
                    scope.launch {
                        ui.schedules[scheduleName]?.let {
                            viewModel.removeSchedule(scheduleName, it, localOnly)
                        }
                        if(scheduleName == ui.defaultSchedule) {
                            viewModel.onDefaultScheduleChange(
                                ui.schedules.schedules.keys.firstOrNull()
                            )
                        }
                    }
                    selectionMap.remove(scheduleName)
                    showDeleteDialog = selectionMap.count { it.value } != 0
                }
            }
        )
    }

    if (newScheduleFormExpanded) {
        NewScheduleForm(
            onDismissRequest = { newScheduleFormExpanded = false; editedSchedule = null },
            onSuccess = { name, schedule ->
                scope.launch {
                    editedSchedule?.let {
                        val error = viewModel.editSchedule(it.first, name, it.second)
                        error?.let {
                            snackHostState.showSnackbar(
                                error,
                                withDismissAction = true
                            )
                            return@launch
                        }
                        if (ui.defaultSchedule == it.first) {
                            viewModel.onDefaultScheduleChange(name)
                        }
                        return@launch
                    }
                    val error = viewModel.addNewSchedule(name, schedule, context)
                    error?.let {
                        snackHostState.showSnackbar(
                            error,
                            withDismissAction = true
                        )
                    }
                    newScheduleFormExpanded = false; editedSchedule = null
                }
                newScheduleFormExpanded = false
                viewModel.setScheduleFormPreferences(
                    schedule.chatId
                )
                if (ui.defaultSchedule.isNullOrEmpty()) {
                    viewModel.onDefaultScheduleChange(
                        name
                    )
                }
                scheduleFormState.fillFields("")
            },
            scheduleFormState,
            editedSchedule == null
        )
    }

    if (importFormExpanded) {
        ImportForm(
            onDismissRequest = { importFormExpanded = false },
            onSuccess = { list, chatId ->
                scope.launch {
                    val error = viewModel.importSchedules(chatId, list, context)
                    error?.let {
                        snackHostState.showSnackbar(
                            error,
                            withDismissAction = true
                        )
                    }
                    if (ui.defaultSchedule.isNullOrEmpty()) {
                        viewModel.onDefaultScheduleChange(ui.schedules.schedules.keys.first())
                    }
                }
                viewModel.setScheduleFormPreferences(
                    chatId = chatId
                )
            },
            viewModel,
            formState = importFormState
        )
    }

    if (sortModeDialogExpanded) {
        ChoiceDialog(
            onDismiss = {
                sortModeDialogExpanded = false
            },
            onSelectionChange = { _, i ->
                viewModel.onScheduleSortModeChange(ScheduleSortMode.entries[i])
            },
            stringResource(R.string.sortMode),
            ScheduleSortMode.entries.map { stringResource(it.displayName) },
            selectedItem = stringResource(ui.scheduleSortMode.displayName)
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.schedules)) },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.popBackStack()
                        }
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "")
                    }
                },
                actions = {
                    IconButton(onClick = { newScheduleFormExpanded = true }) {
                        Icon(Icons.Default.Add, "")
                    }
                    IconButton(onClick = { importFormExpanded = true }) {
                        Icon(Icons.Default.ImportExport, "")
                    }
                    IconButton(onClick = { sortModeDialogExpanded = true }) {
                        Icon(Icons.AutoMirrored.Filled.Sort, "")
                    }
                    if (selectionMap.containsValue(true)) {
                        IconButton(
                            onClick = {
                                showDeleteDialog = true
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
                    label = stringResource(R.string.defaultSchedule),
                    items = ui.schedules.schedules.map { it.key },
                    selectedItem = ui.defaultSchedule ?: "None",
                    onSelectionChange = { value, _ ->
                        viewModel.onDefaultScheduleChange(value)
                    },
                    enabled = ui.schedules.schedules.isNotEmpty()
                )
            }
        },
        snackbarHost = {
            SnackbarHost(snackHostState)
        }
    ) { paddingValues ->
        Column (
            Modifier.padding(paddingValues).fillMaxSize()
        ) {
            val displayList = viewModel.sortedSchedules()
            LazyColumn {
                items(displayList) { (scheduleName, schedule) ->
                    ListItem(
                        modifier = Modifier.clickable {
                            selectionMap[scheduleName] = !(selectionMap[scheduleName] ?: false)
                        },
                        headlineContent = { Text(scheduleName) },
                        trailingContent = {
                            Checkbox(
                                selectionMap[scheduleName] == true,
                                onCheckedChange = { selectionMap[scheduleName] = it }
                            )
                        },
                        leadingContent = {
                            IconButton(
                                onClick = {
                                    editedSchedule = Pair(scheduleName, schedule)
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

