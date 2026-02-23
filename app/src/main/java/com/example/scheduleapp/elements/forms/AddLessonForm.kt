package com.example.scheduleapp.elements.forms

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.scheduleapp.data.classes.Lesson
import com.example.scheduleapp.data.classes.LessonType
import com.example.scheduleapp.data.classes.Occurrence
import com.example.scheduleapp.elements.formElements.AppTimePicker
import com.example.scheduleapp.elements.formElements.FormSelector

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddLessonForm(
    onDismissRequest: () -> Unit,
    onSuccess: () -> Unit,
    editedLesson: Lesson? = null
) {
    ModalBottomSheet(
        onDismissRequest = onDismissRequest
    ) {
        var subject by rememberSaveable { mutableStateOf("") }
        var room by rememberSaveable { mutableStateOf("") }
        var lessonType by rememberSaveable { mutableStateOf(LessonType.LECTURE.name) }
        var occurrence by rememberSaveable { mutableStateOf(Occurrence.WEEKLY.name) }
        val startTimeState = rememberTimePickerState()
        val endTimeState = rememberTimePickerState()
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 8.dp),
            singleLine = true,
            value = subject,
            onValueChange = { subject = it },
            label = { Text("Subject") }
        )
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 8.dp),
            singleLine = true,
            value = room,
            onValueChange = { room = it },
            label = { Text("Room") }
        )
        FormSelector(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 8.dp),
            label = "Lesson type",
            onValueChanged = {
                lessonType = it
            },
            items = LessonType.entries.map { it.name },
            selectedItem = lessonType
        )
        FormSelector(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 8.dp),
            label = "Lesson Occurence",
            onValueChanged = {
                occurrence = it
            },
            items = Occurrence.entries.map { it.name },
            selectedItem = occurrence
        )
        Row(
            Modifier.fillMaxWidth(0.6f).align(Alignment.CenterHorizontally),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            AppTimePicker(timePickerState = startTimeState)
            Text("-", Modifier.align(Alignment.CenterVertically))
            AppTimePicker(timePickerState = endTimeState)
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
                    onSuccess()
                    onDismissRequest()
                }
            ) {
                Text("Ok")
            }
        }
    }
}