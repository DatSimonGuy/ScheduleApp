package com.example.scheduleapp.elements.settings.subpages

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.scheduleapp.R
import com.example.scheduleapp.elements.formElements.choice.SettingsSelector
import com.example.scheduleapp.elements.formElements.time.SettingsTimePicker
import com.example.scheduleapp.elements.formElements.choice.ToggleCard
import com.example.scheduleapp.elements.settings.SettingsViewModel
import com.example.scheduleapp.elements.schedule.timetable.HourHeight
import com.example.scheduleapp.elements.schedule.timetable.LessonBlockDisplayStyle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppearanceSettingsPage(
    viewModel: SettingsViewModel,
    navController: NavController
) {
    val ui by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.appearance)) },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.popBackStack()
                        }
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier.fillMaxWidth().padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ToggleCard(
                modifier = Modifier.fillMaxWidth(0.95f).padding(bottom = 8.dp),
                label = stringResource(R.string.fabScheduleButton),
                checked = ui.addScheduleInFab,
                onCheckedChange = {
                    viewModel.onAddScheduleInFabChange(it)
                }
            )
            SettingsSelector(
                modifier = Modifier.fillMaxWidth(0.95f).padding(bottom = 8.dp),
                label = stringResource(R.string.hourCellHeight),
                onSelectionChange = { _, i ->
                    viewModel.onHourHeightChange(HourHeight.entries[i])
                    if (ui.hourHeight == HourHeight.SHORT && ui.lessonBlockDisplayStyle == LessonBlockDisplayStyle.EXTENDED) {
                        viewModel.onLessonBlockDisplayStyleChange(LessonBlockDisplayStyle.NORMAL)
                    }
                },
                items = HourHeight.entries.map { stringResource(it.displayName) },
                selectedItem = stringResource(ui.hourHeight.displayName)
            )
            SettingsSelector(
                modifier = Modifier.fillMaxWidth(0.95f).padding(bottom = 8.dp),
                label = stringResource(R.string.lessonBlockStyle),
                onSelectionChange = { _, i ->
                    viewModel.onLessonBlockDisplayStyleChange(LessonBlockDisplayStyle.entries[i])
                },
                items = LessonBlockDisplayStyle.entries.filter {
                    it.ordinal - 1 <= ui.hourHeight.ordinal
                }.map { stringResource(it.displayName) },
                selectedItem = stringResource(ui.lessonBlockDisplayStyle.displayName)
            )
            SettingsTimePicker(
                Modifier.fillMaxWidth(0.95f).padding(bottom = 8.dp),
                label = stringResource(R.string.startTimetableFrom),
                rememberTimePickerState(ui.startHour?.hour ?: 0, ui.startHour?.minute ?: 0)
            ) {
                viewModel.onStartHourChange(it)
            }
        }
    }

}