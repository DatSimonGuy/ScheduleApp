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
fun CompactDisplay(
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
            text = "(${stringResource(lesson.lessonType.getDisplayName()).first()}) ${lesson.subject}",
            textAlign = TextAlign.Center,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            fontWeight = FontWeight.Bold,
            color = textColor
        )
        Text(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            text =  "${lesson.room} | ${lesson.startTime} - ${lesson.endTime}",
            textAlign = TextAlign.Center,
            color = textColor
        )
    }
}