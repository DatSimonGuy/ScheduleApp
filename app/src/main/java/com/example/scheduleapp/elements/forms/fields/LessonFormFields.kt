package com.example.scheduleapp.elements.forms.fields

import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.scheduleapp.R
import com.example.scheduleapp.data.classes.LessonType
import com.example.scheduleapp.data.classes.Occurrence
import com.example.scheduleapp.elements.formElements.choice.FormSelector
import com.example.scheduleapp.elements.formElements.dates.AppDatePicker
import com.example.scheduleapp.elements.formElements.dates.AppDateRangePicker
import com.example.scheduleapp.elements.formElements.dates.MultipleDatesPicker
import com.example.scheduleapp.elements.formElements.time.AppTimePicker
import com.example.scheduleapp.elements.forms.states.LessonFormState
import java.time.DayOfWeek
import java.time.format.TextStyle
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LessonFormFields(
    modifier: Modifier,
    state: LessonFormState,
    subjectError: String? = null,
    timeError: String? = null,
    emailError: String? = null,
    editing: Boolean = true,
) {
    val fieldModifier = Modifier.fillMaxWidth(0.95f)
                                .padding(bottom = 16.dp)
    val clipboard = LocalClipboardManager.current
    val uriHandler = LocalUriHandler.current
    val textFieldColors = OutlinedTextFieldDefaults.colors(
        disabledTextColor = MaterialTheme.colorScheme.onSurface,
        disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
        disabledPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
    )
    Column(
        modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = state.subject.value,
            modifier = fieldModifier,
            enabled = editing,
            label = { Text(stringResource(R.string.subject)) },
            onValueChange = {
                state.subject.value = it
            },
            singleLine = true,
            isError = subjectError != null,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            colors = textFieldColors,
            supportingText = {
                subjectError?.let {
                    Text(
                        text = it,
                        color = MaterialTheme.colorScheme.error,
                        modifier = fieldModifier
                    )
                }
            }
        )
        OutlinedTextField(
            value = state.room.value,
            modifier = fieldModifier,
            enabled = editing,
            label = { Text(stringResource(R.string.room)) },
            onValueChange = {
                state.room.value = it
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            colors = textFieldColors
        )
        OutlinedTextField(
            value = state.teacher.value,
            modifier = fieldModifier,
            enabled = editing,
            label = { Text(stringResource(R.string.teacher)) },
            onValueChange = {
                state.teacher.value = it
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            colors = textFieldColors
        )
        OutlinedTextField(
            value = state.teacherMail.value,
            modifier = fieldModifier.combinedClickable(
                onClick = {
                    if (state.teacherMail.value.isEmpty()) {
                        return@combinedClickable
                    }
                    uriHandler.openUri("mailto:${state.teacherMail.value}")
                },
                onLongClick = {
                    clipboard.setText(AnnotatedString(
                        state.teacherMail.value
                    ))
                }
            ),
            enabled = editing,
            label = { Text(stringResource(R.string.teacherMail)) },
            onValueChange = {
                state.teacherMail.value = it
            },
            singleLine = true,
            colors = textFieldColors,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email
            ),
            supportingText = {
                emailError?.let {
                    Text(
                        text = it,
                        color = MaterialTheme.colorScheme.error,
                        modifier = fieldModifier
                    )
                }
            }
        )
        FormSelector(
            fieldModifier,
            stringResource(R.string.dayOfWeek),
            { _, i ->
                state.dayOfWeek.value = DayOfWeek.entries[i];
            },
            DayOfWeek.entries.map { it.getDisplayName(TextStyle.FULL, Locale.getDefault()).capitalize() },
            state.dayOfWeek.value.getDisplayName(TextStyle.FULL, Locale.getDefault()).capitalize(),
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
            stringResource(R.string.lessonType),
            { _, i ->
                state.type.value = LessonType.entries[i];
            },
            LessonType.entries.map { stringResource(it.displayName) },
            stringResource(state.type.value.displayName),
            editing
        )
        FormSelector(
            fieldModifier,
            stringResource(R.string.occurrence),
            { _, i ->
                state.occurrence.value = Occurrence.entries[i];
            },
            Occurrence.entries.map { stringResource(it.displayName) },
            stringResource(state.occurrence.value.displayName),
            editing
        )
        if (state.occurrence.value != Occurrence.ONCE && state.occurrence.value != Occurrence.SELECTED_DAYS) {
            AppDateRangePicker(
                fieldModifier,
                stringResource(R.string.lessonStartEndDates),
                state.dateRange,
                editing
            )
        }
        else if (state.occurrence.value == Occurrence.ONCE) {
            AppDatePicker(
                fieldModifier,
                stringResource(R.string.occurrenceDate),
                state.startDate,
                editing
            )
        }
        else if (state.occurrence.value == Occurrence.SELECTED_DAYS) {
            MultipleDatesPicker(
                fieldModifier,
                state.selectedDates,
                editing
            )
        }
    }
}