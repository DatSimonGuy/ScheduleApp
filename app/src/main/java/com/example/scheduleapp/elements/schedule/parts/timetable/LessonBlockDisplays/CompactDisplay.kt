package com.example.scheduleapp.elements.schedule.parts.timetable.LessonBlockDisplays

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.scheduleapp.data.classes.Lesson

@Composable
fun CompactDisplay(
    lesson: Lesson,
    textColor: Color,
    fontSize: TextUnit = TextUnit.Unspecified,
    isLandscape: Boolean
) {
    val lineHeight = 8.sp
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Transparent)
            .padding(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        if (!isLandscape) {
            Text(
                text = "(${stringResource(lesson.lessonType.displayName).substring(0,2)}) ${lesson.subject} ${lesson.room}",
                textAlign = TextAlign.Center,
                maxLines = if(lesson.duration < 1) 1 else 3,
                overflow = TextOverflow.Ellipsis,
                fontWeight = FontWeight.Bold,
                color = textColor,
                fontSize = fontSize,
                lineHeight = lineHeight
            )
        } else {
            Text(
                text = "(${stringResource(lesson.lessonType.displayName).substring(0,2)}) ${lesson.subject}",
                textAlign = TextAlign.Center,
                maxLines = if(lesson.duration < 1) 1 else 3,
                overflow = TextOverflow.Ellipsis,
                fontWeight = FontWeight.Bold,
                color = textColor,
                fontSize = fontSize,
                lineHeight = lineHeight
            )
            Text(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                text = lesson.room,
                textAlign = TextAlign.Center,
                color = textColor,
                fontSize = fontSize,
                lineHeight = lineHeight
            )
        }
    }
}