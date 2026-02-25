package com.example.scheduleapp.elements.timetable

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.dp
import com.example.scheduleapp.data.classes.Lesson
import com.example.scheduleapp.elements.timetable.LessonBlockDisplays.CompactDisplay
import com.example.scheduleapp.elements.timetable.LessonBlockDisplays.ExtendedDisplay
import com.example.scheduleapp.elements.timetable.LessonBlockDisplays.NormalDisplay
import com.example.scheduleapp.utils.textColorForBackground


@Composable
fun LessonBlock(
    modifier: Modifier = Modifier,
    hourHeight: HourHeight,
    lesson: Lesson,
    onClick: (String) -> Unit,
    displayStyle: LessonBlockDisplayStyle
) {
    val topOffset = (hourHeight.value + 10.dp) * lesson.startTime.hour + 16.dp
    val height = (hourHeight.value + 10.dp) * lesson.duration - 8.dp
    val textColor = textColorForBackground(lesson.lessonType.color)
    var style by rememberSaveable { mutableStateOf(displayStyle) }

    LaunchedEffect(height) {
        style = when {
            height < 100.dp -> LessonBlockDisplayStyle.COMPACT
            height < 150.dp -> LessonBlockDisplayStyle.NORMAL
            else -> LessonBlockDisplayStyle.EXTENDED
        }
    }

    ElevatedCard(
        modifier
            .fillMaxWidth()
            .offset(y = topOffset)
            .height(height)
            .clickable(onClick = {
                onClick(lesson.id)
            }),
        colors = CardDefaults.cardColors(
            containerColor = lesson.lessonType.color
        )
    ) {
        when(style) {
            LessonBlockDisplayStyle.NORMAL -> NormalDisplay(lesson, textColor)
            LessonBlockDisplayStyle.COMPACT -> CompactDisplay(lesson, textColor)
            LessonBlockDisplayStyle.EXTENDED -> ExtendedDisplay(lesson, textColor)
        }
    }
}