package com.example.scheduleapp.elements.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.scheduleapp.R
import com.example.scheduleapp.elements.home.parts.CountDownBlock

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel
) {
    val ui by viewModel.uiState.collectAsStateWithLifecycle()
    val timeLeftText = stringResource(R.string.timeLeft)
    val timeUntilText = stringResource(R.string.timeUntil)

    LaunchedEffect(ui.currentTime) {
        viewModel.updateScheduleInfo()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.home),
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier
                .align(Alignment.Start)
                .padding(horizontal = 16.dp, vertical = 16.dp)
        )

        Column(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .padding(bottom = 16.dp)
        ) {
            Text(
                text = stringResource(R.string.currentLesson),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            CountDownBlock(
                modifier = Modifier.fillMaxWidth(),
                text = if (ui.currentLesson != null) "${ui.currentLesson?.subject} in ${ui.currentLesson?.room}"
                else stringResource(R.string.noLessonsCurrently),
                progress = ui.currentLesson?.let {
                    { 1 - it.percentageTimeLeft(ui.currentTime) }
                },
                timer = ui.currentLesson?.let { lesson ->
                    {
                        lesson.timeLeft(ui.currentTime)?.let { "$timeLeftText: $it" } ?: ""
                    }
                }
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .padding(bottom = 16.dp)
        ) {
            Text(
                text = stringResource(R.string.nextLesson),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            CountDownBlock(
                modifier = Modifier.fillMaxWidth(),
                text = if (ui.nextLesson != null) "${ui.nextLesson?.subject} in ${ui.nextLesson?.room}"
                else stringResource(R.string.noLessonsLeft),
                timer = ui.nextLesson?.let { lesson ->
                    {
                        lesson.timeUntil(ui.currentTime)?.let { "$timeUntilText: $it" } ?: ""
                    }
                }
            )
        }
    }
}