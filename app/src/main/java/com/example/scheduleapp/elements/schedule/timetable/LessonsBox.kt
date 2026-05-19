package com.example.scheduleapp.elements.schedule.timetable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.example.scheduleapp.data.classes.ColorTheme
import com.example.scheduleapp.data.classes.Lesson
import java.time.LocalDate

@Composable
fun LessonsBox(
    modifier: Modifier,
    startHour: Int,
    hourHeight: HourHeight,
    lessons: List<Lesson>,
    groupFunction: (List<Lesson>, LocalDate) -> List<List<Lesson>>,
    date: LocalDate,
    onLessonClick: (String) -> Unit,
    lessonBlockDisplayStyle: LessonBlockDisplayStyle,
    theme: ColorTheme,
    fontSize: TextUnit = TextUnit.Unspecified
) {
    Box (
        modifier
    ) {
        Column(
            Modifier.padding(top = 16.dp)
        ) {
            repeat(24-startHour) {
                ElevatedCard(
                    Modifier
                        .padding(bottom = 10.dp)
                        .height(hourHeight.value)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                ) { }
            }
        }
        val groupedLessons = remember(lessons) {
            groupFunction(lessons, date)
        }

        groupedLessons.forEach { group ->
            LessonBlock(
                hourHeight = hourHeight,
                startHour = startHour,
                lessons = group,
                onLessonClick = onLessonClick,
                displayStyle = lessonBlockDisplayStyle,
                date = date,
                theme,
                fontSize
            )
        }
    }
}