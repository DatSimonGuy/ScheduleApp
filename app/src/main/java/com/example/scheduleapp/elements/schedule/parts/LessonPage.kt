package com.example.scheduleapp.elements.schedule.parts

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
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
import com.example.scheduleapp.data.classes.Lesson
import com.example.scheduleapp.elements.forms.fields.LessonFormFields
import com.example.scheduleapp.elements.forms.states.rememberLessonFormState
import com.example.scheduleapp.elements.schedule.ScheduleViewModel
import kotlinx.coroutines.launch
import java.time.DayOfWeek

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LessonPage(
    lessonId: String,
    viewModel: ScheduleViewModel,
    navController: NavController,
    selectedDay: DayOfWeek = DayOfWeek.MONDAY
) {
    val ui by viewModel.uiState.collectAsStateWithLifecycle()
    var lesson: Lesson? = null
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val formState = rememberLessonFormState(null, selectedDay)
    var showConfirmDialog by rememberSaveable { mutableStateOf(false) }
    var editing by rememberSaveable { mutableStateOf(false) }
    var subjectError by rememberSaveable { mutableStateOf<String?>(null) }
    var timeError by rememberSaveable { mutableStateOf<String?>(null) }
    val pagerOptions = listOf(
        stringResource(R.string.lessonInfo),
        stringResource(R.string.notes)
    )
    var notesText = rememberSaveable { mutableStateOf("") }
    val pagerState = rememberPagerState(initialPage = 0, pageCount = { pagerOptions.count() })

    LaunchedEffect(Unit) {
        lesson = viewModel.getLesson(ui.selectedSchedule ?: "", lessonId)
        lesson?.let {
            formState.fillFields(it)
            notesText.value = it.notes ?: ""
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
                        stringResource(R.string.youSure),
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
                            Text(stringResource(R.string.cancel))
                        }
                        Button(
                            onClick = {
                                viewModel.removeLesson(
                                    ui.selectedSchedule ?: "", lessonId,
                                    selectedDay,
                                    context
                                )
                                viewModel.navController.popBackStack()
                            }
                        ) {
                            Text(stringResource(R.string.ok))
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
                    Text(stringResource(R.string.selectedLesson))
                },
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
                    if (!ui.textButtons) {
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
                                    viewModel.updateLesson(
                                        ui.selectedSchedule ?: "",
                                        lesson,
                                        selectedDay,
                                        formState.dayOfWeek.value,
                                        context
                                    )
                                    editing = false
                                }
                            ) {
                                Icon(Icons.Default.Check, "")
                            }
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            Modifier.padding(paddingValues).fillMaxSize()
        ) {
            if (ui.textButtons) {
                Row (
                    Modifier.fillMaxWidth().padding(bottom = 8.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    OutlinedButton(
                        onClick = {
                            showConfirmDialog = true
                        },
                        Modifier.padding(end = 8.dp)
                    ) {
                        Text(stringResource(R.string.delete))
                    }
                    OutlinedButton(
                        onClick = {
                            editing = !editing
                            if (!editing) {
                                lesson?.let {
                                    formState.fillFields(it)
                                }
                            }
                        },
                        Modifier.padding(end = 8.dp)
                    ) {
                        if(!editing) {
                            Text(stringResource(R.string.edit))
                        } else {
                            Text(stringResource(R.string.cancel))
                        }
                    }
                    if (editing) {
                        OutlinedButton (
                            onClick = {
                                val (lesson, errs) = formState.validateAndMap(lessonId)
                                if (lesson == null) {
                                    subjectError = errs.first
                                    timeError = errs.second
                                    return@OutlinedButton
                                }
                                viewModel.updateLesson(
                                    ui.selectedSchedule ?: "", lesson,
                                    selectedDay,
                                    formState.dayOfWeek.value,
                                    context
                                )
                                editing = false
                            },
                            Modifier.padding(end = 8.dp)
                        ) {
                            Text(stringResource(R.string.confirm))
                        }
                    }
                }
            }
            SingleChoiceSegmentedButtonRow(
                Modifier.fillMaxWidth().padding(bottom = 8.dp)
            ) {
                pagerOptions.forEachIndexed { i, value ->
                    SegmentedButton(
                        shape = SegmentedButtonDefaults.itemShape(
                            index = i,
                            count = pagerOptions.count()
                        ),
                        selected = pagerState.currentPage == i,
                        onClick = {
                            scope.launch {
                                pagerState.animateScrollToPage(i)
                            }
                        },
                        label = { Text(value) }
                    )
                }
            }
            HorizontalPager(
                pagerState,
                beyondViewportPageCount = 1
            ) {
                when(it) {
                    0 -> LessonFormFields(
                        Modifier.verticalScroll(rememberScrollState()),
                        formState,
                        subjectError,
                        timeError,
                        editing
                    )
                    1 -> {
                        NotesPage(
                            notesText,
                            onSave = { newNotes ->
                                lesson?.let { lesson ->
                                    viewModel.updateLesson(
                                        ui.selectedSchedule ?: "",
                                        lesson.copy(notes = newNotes),
                                        selectedDay,
                                        selectedDay,
                                        context
                                    )
                                }
                            }
                        )
                    }
                    else -> {  }
                }
            }
        }
    }
}

@Composable
fun NotesPage(
    notes: MutableState<String>,
    onSave: (String) -> Unit
) {
    var notesSaved by rememberSaveable { mutableStateOf(true) }

    Column(
        Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.End
    ) {
        OutlinedTextField(
            value = notes.value,
            onValueChange = {
                notes.value = it
                notesSaved = false
            },
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(bottom = 8.dp)
        )
        Button(
            onClick = {
                onSave(notes.value)
                notesSaved = true
            },
            enabled = !notesSaved
        ) {
            Text(stringResource(R.string.saveNotes))
        }
    }
}