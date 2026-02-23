package com.example.scheduleapp.elements.schedule.parts

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import com.example.scheduleapp.data.classes.Lesson
import com.example.scheduleapp.elements.schedule.ScheduleViewModel

@Composable
fun LessonPage(
    lessonId: String,
    viewModel: ScheduleViewModel
) {
    val ui = viewModel.uiState.collectAsStateWithLifecycle()
    val lesson = viewModel.getLesson(lessonId)
    Scaffold (

    ) { paddingValues ->
        Column(
            Modifier.padding(paddingValues)
        ) {

        }
    }
}