package com.example.scheduleapp.elements.home

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.navigation.NavController
import com.example.scheduleapp.data.classes.Lesson
import com.example.scheduleapp.elements.home.parts.CountDownBlock
import kotlin.concurrent.timer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel
) {
    var currentLesson by rememberSaveable { mutableStateOf<Lesson?>(null) }
    var nextLesson by rememberSaveable { mutableStateOf<Lesson?>(null) }

    LaunchedEffect(Unit) {
        currentLesson = viewModel.getCurrentLesson()
        nextLesson = viewModel.getNextLesson()
        Log.e("", currentLesson.toString())
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Home") }) }
    ) { paddingValues ->
        Column(
            Modifier.fillMaxSize().padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CountDownBlock(
                Modifier
                    .fillMaxWidth(0.95f)
                    .height(100.dp)
                    .align(Alignment.CenterHorizontally)
                    .padding(bottom = 16.dp),
                currentLesson?.toString() ?: "No lesson currently",
                currentLesson?.let { { it.percentageTimeLeft } }
            )
            CountDownBlock(
                Modifier
                    .fillMaxWidth(0.95f)
                    .height(100.dp)
                    .align(Alignment.CenterHorizontally)
                    .padding(bottom = 16.dp),
                nextLesson?.toString() ?: "No lessons left",
                timer = nextLesson?.let { { it.timeUntil ?: "" } }
            )
        }
    }
}