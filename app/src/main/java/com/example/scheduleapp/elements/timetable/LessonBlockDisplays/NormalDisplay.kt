package com.example.scheduleapp.elements.timetable.LessonBlockDisplays

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.scheduleapp.data.classes.Lesson

@Composable
fun NormalDisplay(
    lesson: Lesson,
    textColor: Color
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Transparent)
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            modifier = Modifier,
            text = lesson.subject,
            textAlign = TextAlign.Center,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            fontWeight = FontWeight.ExtraBold,
            color = textColor
        )
        Text(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            text = lesson.startTime.toString() + " - " + lesson.endTime.toString(),
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            color = textColor
        )
        Text(
            text = stringResource(lesson.lessonType.getDisplayName()),
            textAlign = TextAlign.Center,
            maxLines = 1,
            color = textColor
        )
        Text(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            text = lesson.room,
            textAlign = TextAlign.Center,
            color = textColor
        )
    }
}