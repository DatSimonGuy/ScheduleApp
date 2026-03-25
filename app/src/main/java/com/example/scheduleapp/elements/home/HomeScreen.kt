package com.example.scheduleapp.elements.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.scheduleapp.elements.home.parts.CountDownBlock
import kotlinx.coroutines.delay

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel
) {
    LaunchedEffect(Unit) {
        while (true) {
            viewModel.updateScheduleInfo()
            delay(1000)
        }
    }

    val ui by viewModel.uiState.collectAsStateWithLifecycle()
    Scaffold(
        topBar = { TopAppBar(title = { Text("Home") }) }
    ) { paddingValues ->
        Column(
            Modifier.fillMaxSize().padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column {
                Text(
                    "Current Lesson",
                    Modifier.padding(bottom = 8.dp),
                )
                CountDownBlock(
                    Modifier
                        .fillMaxWidth(0.95f)
                        .align(Alignment.CenterHorizontally)
                        .padding(bottom = 16.dp),
                    if (ui.currentLesson != null) "${ui.currentLesson?.subject} in ${ui.currentLesson?.room}"
                            else "No lesson currently",
                    ui.currentLesson?.let { {
                           1 - it.percentageTimeLeft(ui.currentTime)
                        }
                    },
                    timer = ui.currentLesson?.let { lesson -> {
                            lesson.timeLeft(ui.currentTime)?.let { "Time left: $it" } ?: ""
                        }
                    }
                )
            }

            Column {
                Text(
                    "Next lesson",
                    Modifier.padding(bottom = 8.dp),
                )
                CountDownBlock(
                    Modifier
                        .fillMaxWidth(0.95f)
                        .align(Alignment.CenterHorizontally)
                        .padding(bottom = 16.dp),
                    if (ui.nextLesson != null) "${ui.nextLesson?.subject} in ${ui.nextLesson?.room}"
                            else "No lessons left",
                    timer = ui.nextLesson?.let { lesson -> {
                            lesson.timeUntil(ui.currentTime)?.let { "Time until start: $it" } ?: ""
                        }
                    }
                )
            }
        }
    }
}