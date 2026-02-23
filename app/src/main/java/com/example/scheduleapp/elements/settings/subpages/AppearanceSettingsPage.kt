package com.example.scheduleapp.elements.settings.subpages

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.scheduleapp.elements.formElements.SettingsSelector
import com.example.scheduleapp.elements.formElements.ToggleCard
import com.example.scheduleapp.elements.settings.SettingsViewModel
import com.example.scheduleapp.elements.timetable.HourHeight

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppearanceSettingsPage(
    viewModel: SettingsViewModel
) {
    val ui by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = { TopAppBar(title = { Text("Appearance") }) }
    ) { paddingValues ->
        Column(
            modifier = Modifier.fillMaxWidth().padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ToggleCard(
                modifier = Modifier.fillMaxWidth(0.95f).padding(bottom = 8.dp),
                label = "Add schedule button in fab",
                checked = ui.addScheduleInFab,
                onCheckedChange = {
                    viewModel.onAddScheduleInFabChange(it)
                }
            )
            SettingsSelector(
                modifier = Modifier.fillMaxWidth(0.95f).padding(bottom = 8.dp),
                label = "Hour cell height",
                onSelectionChange = {
                    viewModel.onHourHeightChange(HourHeight.valueOf(it))
                },
                items = HourHeight.entries.map { it.name },
                selectedItem = ui.hourHeight.name
            )
        }
    }

}