package com.example.scheduleapp.elements.schedule

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FloatingActionButtonMenu
import androidx.compose.material3.FloatingActionButtonMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.ToggleFloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.scheduleapp.data.api.DSBApi
import com.example.scheduleapp.data.classes.Lesson
import com.example.scheduleapp.data.classes.SaveLocation
import com.example.scheduleapp.elements.formElements.ChoiceDialog
import com.example.scheduleapp.elements.forms.AddLessonForm
import com.example.scheduleapp.elements.forms.NewScheduleForm
import com.example.scheduleapp.elements.navigation.ScheduleDestination
import com.example.scheduleapp.elements.schedule.parts.AddSchedulePrompt
import com.example.scheduleapp.elements.timetable.TimeTable
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale
import java.util.UUID

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ScheduleScreen(
    navController: NavController,
    viewModel: ScheduleViewModel,
) {
    val ui by viewModel.uiState.collectAsStateWithLifecycle()
    var showScheduleForm by rememberSaveable { mutableStateOf(false) }
    var fabExpanded by rememberSaveable { mutableStateOf(false) }
    var showScheduleSelector by rememberSaveable { mutableStateOf(false) }
    var showAddLessonFrom by rememberSaveable { mutableStateOf(false) }
    val currentSchedule by viewModel.currentScheduleFlow.collectAsStateWithLifecycle()
    val snackHostState = remember { SnackbarHostState() }
    val pagerState = rememberPagerState(initialPage = LocalDate.now().dayOfWeek.ordinal) { Int.MAX_VALUE }

    LaunchedEffect(ui.selectedSchedule) {
        if (currentSchedule != null && currentSchedule?.saveLocation == SaveLocation.DSB) {
            val (remoteSchedule, error) = DSBApi(currentSchedule!!.chatId!!, "")
                .getSchedule(ui.selectedSchedule ?: "")
            if (error != null) {
                snackHostState.showSnackbar(
                    error,
                    withDismissAction = true
                )
            }
            if (remoteSchedule != null) {
                viewModel.addSchedule(ui.selectedSchedule ?: "", remoteSchedule)
            }
        }
    }

    if (showScheduleForm) {
        NewScheduleForm(
            onDismissRequest = { showScheduleForm = false },
            onSuccess = { name, schedule ->
                viewModel.addSchedule(name, schedule)
            }
        )
    }

    if (showScheduleSelector) {
        ChoiceDialog(
            onDismiss = {
                showScheduleSelector = false
            },
            onSelectionChange = {
                viewModel.setCurrentSchedule(it)
                showScheduleSelector = false
            },
            label = "Select schedule",
            items = ui.schedules.schedules.map { it.key },
            selectedItem = ui.selectedSchedule ?: ""
        )
    }

    if (showAddLessonFrom) {
        AddLessonForm(
            onDismissRequest = {
                showAddLessonFrom = false
            },
            onSuccess = { lesson, selectedDay ->
                viewModel.addNewLesson(selectedDay, lesson)
            },
            DayOfWeek.of(pagerState.currentPage%7+1)
        )
    }

    if (ui.schedules.count() < 1) {
        AddSchedulePrompt { showScheduleForm = true }
        return
    }

    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        floatingActionButton = {
            FloatingActionButtonMenu (
                expanded = fabExpanded,
                button = {
                    ToggleFloatingActionButton(
                        checked = fabExpanded,
                        onCheckedChange = {
                            fabExpanded = !fabExpanded
                        }
                    ) {
                        if (!fabExpanded) {
                            Icon(Icons.Default.Edit, "")
                        } else {
                            Icon(Icons.Default.Close, "")
                        }
                    }
                }
            ) {
                if (ui.showAddSchedule) {
                    FloatingActionButtonMenuItem(
                        onClick = {
                            showScheduleForm = true
                            fabExpanded = false
                        },
                        text = { Text("Add a new schedule") },
                        icon = { Icon(Icons.Default.Add, "") }
                    )
                }
                FloatingActionButtonMenuItem(
                    onClick = {
                        showScheduleSelector = true
                        fabExpanded = false
                    },
                    text = { Text("Select schedule") },
                    icon = { Icon(Icons.Default.CalendarToday, "") }
                )
                FloatingActionButtonMenuItem(
                    onClick = {
                        showAddLessonFrom = true
                        fabExpanded = false
                    },
                    text = { Text("Add a new lesson") },
                    icon = { Icon(Icons.Default.AddCircleOutline, "") }
                )
            }
        },
        snackbarHost = {
            SnackbarHost(
                hostState = snackHostState
            )
        }
    ) { paddingValues ->
        HorizontalPager(
            modifier = Modifier.padding(paddingValues),
            state = pagerState
        ) { page ->
            val day = DayOfWeek.of(page % 7 + 1)
            TimeTable(
                title = "${ui.selectedSchedule} - ${day.getDisplayName(TextStyle.FULL, Locale.getDefault())}",
                hourHeight = ui.hourHeight,
                lessons = currentSchedule?.lessons[day] ?: emptyList(),
                onLessonClick = {
                    navController.navigate(ScheduleDestination.LessonScreen(DayOfWeek.of(pagerState.currentPage%7+1), it))
                },
                lessonBlockDisplayStyle = ui.lessonBlockDisplayStyle,
                date = LocalDate.now().plusDays((pagerState.settledPage - LocalDate.now().dayOfWeek.ordinal).toLong())
            )
        }
    }
}