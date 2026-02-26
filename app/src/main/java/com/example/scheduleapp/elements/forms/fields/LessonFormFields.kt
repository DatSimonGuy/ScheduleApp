package com.example.scheduleapp.elements.forms.fields

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.example.scheduleapp.data.classes.Lesson
import com.example.scheduleapp.data.classes.LessonType
import com.example.scheduleapp.data.classes.Occurrence
import com.example.scheduleapp.elements.formElements.AppDatePicker
import com.example.scheduleapp.elements.formElements.AppDateRangePicker
import com.example.scheduleapp.elements.formElements.AppTimePicker
import com.example.scheduleapp.elements.formElements.FormSelector
import com.example.scheduleapp.elements.formElements.MultipleDatesPicker
import com.example.scheduleapp.elements.forms.states.LessonFormState
import java.time.DayOfWeek

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LessonFormFields(
    modifier: Modifier,
    state: LessonFormState,
    subjectError: String? = null,
    timeError: String? = null,
    editing: Boolean = true,
) {
    val fieldModifier = Modifier.fillMaxWidth(0.95f)
                                .padding(bottom = 16.dp)
    Column(
        modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = state.subject.value,
            modifier = fieldModifier,
            readOnly = !editing,
            label = { Text("Subject") },
            onValueChange = {
                state.subject.value = it
            },
            singleLine = true,
            isError = subjectError != null,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
        )
        subjectError?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                modifier = fieldModifier
            )
        }
        OutlinedTextField(
            value = state.room.value,
            modifier = fieldModifier,
            readOnly = !editing,
            label = { Text("Room") },
            onValueChange = {
                state.room.value = it
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
        )
        OutlinedTextField(
            value = state.teacher.value,
            modifier = fieldModifier,
            readOnly = !editing,
            label = { Text("Teacher") },
            onValueChange = {
                state.teacher.value = it
            },
            singleLine = true,
        )
        FormSelector(
            fieldModifier,
            "Day of week",
            {
                state.dayOfWeek.value = it
            },
            DayOfWeek.entries.map { it.name },
            state.dayOfWeek.value,
            editing
        )
        Row(
            fieldModifier.align(Alignment.CenterHorizontally),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            AppTimePicker(timePickerState = state.startTime, enabled = editing)
            Text("-", Modifier.align(Alignment.CenterVertically))
            AppTimePicker(timePickerState = state.endTime, enabled = editing, isError = timeError != null)
        }
        timeError?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                modifier = fieldModifier
            )
        }
        FormSelector(
            fieldModifier,
            "Lesson type",
            {
                state.type.value = it
            },
            LessonType.entries.map { it.name },
            state.type.value,
            editing
        )
        FormSelector(
            fieldModifier,
            "Occurrence",
            {
                state.occurrence.value = it
            },
            Occurrence.entries.map { it.name },
            state.occurrence.value,
            editing
        )
        val currentOccurrence = Occurrence.valueOf(state.occurrence.value)
        if (currentOccurrence != Occurrence.ONCE && currentOccurrence != Occurrence.SELECTED_DAYS) {
            AppDateRangePicker(
                fieldModifier,
                "Lesson start/end dates",
                state.dateRange,
                editing
            )
        }
        if (currentOccurrence == Occurrence.ONCE) {
            AppDatePicker(
                fieldModifier,
                "Occurrence date",
                state.startDate,
                editing
            )
        }
        if (currentOccurrence == Occurrence.SELECTED_DAYS) {
            MultipleDatesPicker(
                fieldModifier,
                state.selectedDates,
                editing
            )
        }
    }
}