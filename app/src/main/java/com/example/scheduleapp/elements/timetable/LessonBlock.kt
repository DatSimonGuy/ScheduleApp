package com.example.scheduleapp.elements.timetable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.scheduleapp.data.classes.Lesson

@Composable
fun LessonBlock(
    modifier: Modifier = Modifier,
    hourHeight: HourHeight,
    lesson: Lesson
) {
    val topOffset = hourHeight.value * lesson.startTime.hour
    val height = hourHeight.value * lesson.duration
    ElevatedCard(
        modifier
            .fillMaxWidth()
            .offset(y=topOffset)
            .height(height)
            .clickable(onClick = {

            }),
        colors = CardDefaults.cardColors(
            containerColor = Color.Red
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                text = lesson.subject,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontWeight = FontWeight.Bold
            )
            Text(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                text = lesson.startTime.toString() + " - " + lesson.endTime.toString(),
                textAlign = TextAlign.Center
            )
            Text(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                text = lesson.room,
                textAlign = TextAlign.Center
            )
        }
    }
}