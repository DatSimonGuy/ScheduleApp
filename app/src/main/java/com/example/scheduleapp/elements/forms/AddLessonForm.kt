package com.example.scheduleapp.elements.forms

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.getSelectedDate
import androidx.compose.material3.getSelectedEndDate
import androidx.compose.material3.getSelectedStartDate
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.scheduleapp.data.classes.Lesson
import com.example.scheduleapp.data.classes.LessonType
import com.example.scheduleapp.data.classes.Occurrence
import com.example.scheduleapp.elements.formElements.AppDatePicker
import com.example.scheduleapp.elements.formElements.AppDateRangePicker
import com.example.scheduleapp.elements.formElements.AppTimePicker
import com.example.scheduleapp.elements.formElements.FormSelector
import com.example.scheduleapp.elements.formElements.MultipleDatesPicker
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddLessonForm(
    onDismissRequest: () -> Unit,
    onSuccess: (Lesson, DayOfWeek) -> Unit,
    startingDay: DayOfWeek = DayOfWeek.MONDAY
) {
    val sheetState = rememberModalBottomSheetState(true)
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState
    ) {
        var subject by rememberSaveable { mutableStateOf("") }
        var room by rememberSaveable { mutableStateOf("") }
        var teacher by rememberSaveable { mutableStateOf("") }
        var lessonType by rememberSaveable { mutableStateOf(LessonType.LECTURE.name) }
        var occurrence by rememberSaveable { mutableStateOf(Occurrence.WEEKLY.name) }
        var dayOfWeek by rememberSaveable { mutableStateOf(startingDay.name) }
        val startTimeState = rememberTimePickerState()
        val endTimeState = rememberTimePickerState()
        val startDateState = rememberDatePickerState()
        val dateRangeState = rememberDateRangePickerState()
        val selectedDates = remember { mutableStateListOf<LocalDate>() }
        val formFieldModifier = Modifier
                                    .fillMaxWidth(0.95f)
                                    .align(Alignment.CenterHorizontally)
                                    .padding(bottom = 8.dp)

        Column(
            Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(bottom = 16.dp)
        ) {
            OutlinedTextField(
                modifier = formFieldModifier,
                singleLine = true,
                value = subject,
                onValueChange = { subject = it },
                label = { Text("Subject") }
            )
            OutlinedTextField(
                modifier = formFieldModifier,
                singleLine = true,
                value = room,
                onValueChange = { room = it },
                label = { Text("Room") }
            )
            OutlinedTextField(
                modifier = formFieldModifier,
                singleLine = true,
                value = teacher,
                onValueChange = { teacher = it },
                label = { Text("Teacher") }
            )
            FormSelector(
                modifier = formFieldModifier,
                label = "Lesson type",
                onValueChanged = {
                    lessonType = it
                },
                items = LessonType.entries.map { it.name },
                selectedItem = lessonType
            )
            FormSelector(
                modifier = formFieldModifier,
                label = "Day of week",
                onValueChanged = {
                    dayOfWeek = it
                },
                items = DayOfWeek.entries.map { it.name },
                selectedItem = dayOfWeek
            )
            Row(
                Modifier.fillMaxWidth(0.6f).align(Alignment.CenterHorizontally),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                AppTimePicker(timePickerState = startTimeState)
                Text("-", Modifier.align(Alignment.CenterVertically))
                AppTimePicker(timePickerState = endTimeState)
            }
            FormSelector(
                modifier = formFieldModifier,
                label = "Lesson Occurence",
                onValueChanged = {
                    occurrence = it
                },
                items = Occurrence.entries.map { it.name },
                selectedItem = occurrence
            )
            val currentOccurrence = Occurrence.valueOf(occurrence)
            if (currentOccurrence != Occurrence.ONCE && currentOccurrence != Occurrence.SELECTED_DAYS) {
                AppDateRangePicker(
                    formFieldModifier,
                    "Lesson start/end dates",
                    dateRangeState
                )
            }
            if (currentOccurrence == Occurrence.ONCE) {
                AppDatePicker(
                    formFieldModifier,
                    "Occurrence date",
                    startDateState
                )
            }
            if (currentOccurrence == Occurrence.SELECTED_DAYS) {
                MultipleDatesPicker(
                    formFieldModifier,
                    selectedDates
                )
            }
            Row(
                Modifier
                    .align(Alignment.End)
                    .padding(end = 16.dp, top = 48.dp, bottom = 16.dp)
            ) {
                OutlinedButton(
                    modifier = Modifier.padding(end = 8.dp),
                    onClick = onDismissRequest
                ) {
                    Text("Cancel")
                }
                Button(
                    onClick = {
                        onSuccess(
                            Lesson(
                                UUID.randomUUID().toString(),
                                subject,
                                LocalTime.of(startTimeState.hour, startTimeState.minute),
                                LocalTime.of(endTimeState.hour, endTimeState.minute),
                                room,
                                teacher,
                                LessonType.valueOf(lessonType),
                                Occurrence.valueOf(occurrence),

                                if (occurrence == Occurrence.ONCE.name) startDateState.getSelectedDate() else dateRangeState.getSelectedStartDate(),
                                dateRangeState.getSelectedEndDate(),
                                selectedDates.toList()
                            ),
                            DayOfWeek.valueOf(dayOfWeek),
                        )
                        onDismissRequest()
                    }
                ) {
                    Text("Ok")
                }
            }
        }
    }
}