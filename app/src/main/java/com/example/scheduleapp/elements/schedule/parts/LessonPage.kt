package com.example.scheduleapp.elements.schedule.parts

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.scheduleapp.data.classes.Lesson
import com.example.scheduleapp.elements.forms.fields.LessonFormFields
import com.example.scheduleapp.elements.forms.states.rememberLessonFormState
import com.example.scheduleapp.elements.schedule.ScheduleViewModel
import java.time.DayOfWeek

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LessonPage(
    lessonId: String,
    viewModel: ScheduleViewModel,
    selectedDay: DayOfWeek = DayOfWeek.MONDAY
) {
    val ui = viewModel.uiState.collectAsStateWithLifecycle()
    var lesson: Lesson? = null
    val formState = rememberLessonFormState(lesson, selectedDay)
    var showConfirmDialog by rememberSaveable { mutableStateOf(false) }
    var editing by rememberSaveable { mutableStateOf(false) }
    var subjectError by rememberSaveable { mutableStateOf<String?>(null) }
    var timeError by rememberSaveable { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        lesson = viewModel.getLesson(ui.value.selectedSchedule ?: "", lessonId)
        lesson?.let {
            formState.fillFields(it)
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
                Modifier
                    .fillMaxWidth(0.8f)
                    .fillMaxHeight(0.2f)
            ) {
                Column(
                    Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                ){
                    Text(
                        "Are you sure?",
                    )
                    Spacer(Modifier.weight(1f))
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
                            if (!editing) {
                                lesson?.let {
                                    formState.fillFields(it)
                                }
                            }
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
                                val (lesson, errs) = formState.validateAndMap(lessonId)
                                if (lesson == null) {
                                    subjectError = errs.first
                                    timeError = errs.second
                                    return@IconButton
                                }
                                viewModel.updateLesson(ui.value.selectedSchedule ?: "", lesson,
                                    DayOfWeek.valueOf(formState.dayOfWeek.value))
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
        LessonFormFields(Modifier
            .padding(paddingValues)
            .verticalScroll(rememberScrollState()), formState, subjectError, timeError, editing)
    }
}