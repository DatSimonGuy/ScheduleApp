package com.example.scheduleapp.elements.schedule.parts

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.getSelectedDate
import androidx.compose.material3.getSelectedEndDate
import androidx.compose.material3.getSelectedStartDate
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.material3.setSelectedDate
import androidx.compose.material3.setSelection
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.scheduleapp.data.classes.Lesson
import com.example.scheduleapp.data.classes.LessonType
import com.example.scheduleapp.data.classes.Occurrence
import com.example.scheduleapp.elements.formElements.AppDatePicker
import com.example.scheduleapp.elements.formElements.AppDateRangePicker
import com.example.scheduleapp.elements.formElements.AppTimePicker
import com.example.scheduleapp.elements.formElements.FormSelector
import com.example.scheduleapp.elements.formElements.MultipleDatesPicker
import com.example.scheduleapp.elements.schedule.ScheduleViewModel
import java.time.LocalDate
import java.time.LocalTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LessonPage(
    lessonId: String,
    viewModel: ScheduleViewModel
) {
    val ui = viewModel.uiState.collectAsStateWithLifecycle()

    var lesson: Lesson? = null
    var subject by rememberSaveable { mutableStateOf("") }
    var room by rememberSaveable { mutableStateOf("") }
    var teacher by rememberSaveable { mutableStateOf("") }
    val startTimeState = rememberTimePickerState()
    val endTimeState = rememberTimePickerState()
    var selectedType by rememberSaveable { mutableStateOf(LessonType.LECTURE.name) }
    var selectedOccurrence by rememberSaveable { mutableStateOf(Occurrence.WEEKLY.name) }
    val startDateState = rememberDatePickerState()
    val dateRangeState = rememberDateRangePickerState()
    var editing by rememberSaveable { mutableStateOf(false) }
    var showConfirmDialog by rememberSaveable { mutableStateOf(false) }
    val selectedDates by rememberSaveable { mutableStateOf(mutableStateListOf<LocalDate>()) }
    val fieldModifier = Modifier.fillMaxWidth(0.95f)
                                .padding(bottom = 16.dp)

    LaunchedEffect(Unit) {
        lesson = viewModel.getLesson(ui.value.selectedSchedule ?: "", lessonId)
        subject = lesson?.subject ?: ""
        room = lesson?.room ?: ""
        teacher = lesson?.teacher ?: ""
        lesson?.startTime?.let {
            startTimeState.hour = it.hour
            startTimeState.minute = it.minute
        }
        lesson?.endTime?.let {
            endTimeState.hour = it.hour
            endTimeState.minute = it.minute
        }
        lesson?.lessonType?.let {
            selectedType = it.name
        }
        lesson?.startDate?.let { startDate ->
            startDateState.setSelectedDate(startDate)
            lesson?.endDate?.let {
                dateRangeState.setSelection(startDate, it)
            }
        }
        lesson?.activeDays?.let {
            selectedDates.addAll(it)
        }
    }

    if (showConfirmDialog) {
        BasicAlertDialog(
            onDismissRequest = {
                showConfirmDialog = false
            },
            modifier = Modifier.padding(16.dp)
        ) {
            Card (
            ) {
                Text("Are you sure?")
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedButton(
                        onClick = {
                            showConfirmDialog = false
                        }
                    ) {
                        Text("Cancel")
                    }
                    Button(
                        onClick = {
                            viewModel.removeLesson(ui.value.selectedSchedule ?: "", lessonId)
                            viewModel.navController.popBackStack()
                        }
                    ) {
                        Text("Ok")
                    }
                }
            }
        }
    }

    Scaffold (
        topBar = {
            TopAppBar(
                title = {
                    Text("Selected lesson:")
                },
                actions = {
                    IconButton(
                        onClick = {
                            showConfirmDialog = true
                        }
                    ) {
                        Icon(Icons.Default.Delete, "")
                    }
                    IconButton(
                        onClick = {
                            editing = !editing
                        }
                    ) {
                        if(!editing) {
                            Icon(Icons.Default.Edit, "")
                        } else {
                            Icon(Icons.Default.Close, "")
                        }
                    }
                    if (editing) {
                        IconButton(
                            onClick = {
                                if (lesson == null) {
                                    return@IconButton
                                }
                                val newLesson = Lesson(
                                    lesson!!.id,
                                    subject,
                                    LocalTime.of(startTimeState.hour, startTimeState.minute),
                                    LocalTime.of(endTimeState.hour, endTimeState.minute),
                                    room,
                                    teacher,
                                    LessonType.valueOf(selectedType),
                                    Occurrence.valueOf(selectedOccurrence),
                                    startDateState.getSelectedDate() ?: dateRangeState.getSelectedStartDate(),
                                    dateRangeState.getSelectedEndDate(),
                                    selectedDates
                                )
                                viewModel.updateLesson(ui.value.selectedSchedule ?: "", newLesson)
                                editing = false
                            }
                        ) {
                            Icon(Icons.Default.Check, "")
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            Modifier.fillMaxWidth().padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = subject,
                modifier = fieldModifier,
                readOnly = !editing,
                label = { Text("subject") },
                onValueChange = {
                    subject = it
                }
            )
            OutlinedTextField(
                value = room,
                modifier = fieldModifier,
                readOnly = !editing,
                label = { Text("room") },
                onValueChange = {
                    room = it
                }
            )
            OutlinedTextField(
                value = teacher,
                modifier = fieldModifier,
                readOnly = !editing,
                label = { Text("teacher") },
                onValueChange = {
                    teacher = it
                }
            )
            Row(
                Modifier.fillMaxWidth(0.6f).align(Alignment.CenterHorizontally),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                AppTimePicker(timePickerState = startTimeState, enabled = editing)
                Text("-", Modifier.align(Alignment.CenterVertically))
                AppTimePicker(timePickerState = endTimeState, enabled = editing)
            }
            FormSelector(
                fieldModifier,
                "Lesson type",
                onValueChanged = {
                    selectedType = it
                },
                LessonType.entries.map { it.name },
                selectedItem = selectedType,
                enabled = editing
            )
            FormSelector(
                fieldModifier,
                "Occurrence",
                onValueChanged = {
                    selectedOccurrence = it
                },
                Occurrence.entries.map { it.name },
                selectedItem = selectedOccurrence,
                enabled = editing
            )
            val currentOccurrence = Occurrence.valueOf(selectedOccurrence)
            if (currentOccurrence != Occurrence.ONCE && currentOccurrence != Occurrence.SELECTED_DAYS) {
                AppDateRangePicker(
                    fieldModifier,
                    "Lesson start/end dates",
                    dateRangeState,
                    editing
                )
            }
            if (currentOccurrence == Occurrence.ONCE) {
                AppDatePicker(
                    fieldModifier,
                    "Occurrence date",
                    startDateState,
                    editing
                )
            }
            if (currentOccurrence == Occurrence.SELECTED_DAYS) {
                MultipleDatesPicker(
                    fieldModifier,
                    selectedDates,
                    editing
                )
            }
        }
    }
}