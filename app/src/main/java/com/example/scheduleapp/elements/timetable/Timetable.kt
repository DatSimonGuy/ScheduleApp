package com.example.scheduleapp.elements.timetable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.scheduleapp.data.classes.Lesson
import java.time.LocalTime

@Composable
fun TimeTable(
    modifier: Modifier = Modifier,
    title: String = "",
    hourHeight: HourHeight,
    lessons: List<Lesson>,
    onLessonClick: (String) -> Unit
) {
    val scrollState = rememberScrollState()
    Column (
        modifier.fillMaxSize()
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = title,
            textAlign = TextAlign.Center
        )
        Row (
            Modifier.verticalScroll(scrollState)
        ) {
            Column(
                Modifier.weight(1f)
            ) {
                repeat(24) { i ->
                    val time = LocalTime.of(i, 0)
                    Text(
                        time.toString(),
                        modifier = Modifier
                            .padding(bottom = 10.dp)
                            .height(hourHeight.value)
                            .align(Alignment.CenterHorizontally),
                        textAlign = TextAlign.Center,
                    )
                }
            }
            Box (
                Modifier.weight(5f)
            ) {
                Column(
                    Modifier.padding(top = 16.dp)
                ) {
                    repeat(24) {
                        ElevatedCard(
                            Modifier
                                .padding(bottom = 10.dp)
                                .height(hourHeight.value)
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(MaterialTheme.colorScheme.surfaceContainer)
                        ) { }
                    }
                }
                lessons.forEach {
                    LessonBlock(hourHeight = hourHeight, lesson = it, onClick = onLessonClick)
                }
            }
        }
    }
}