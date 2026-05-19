package com.example.scheduleapp.elements.schedule.timetable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.scheduleapp.data.classes.Lesson
import com.example.scheduleapp.elements.schedule.ScheduleViewModel
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Locale

@Composable
fun WeekViewTable(
    modifier: Modifier = Modifier,
    title: String = "",
    hourHeight: HourHeight,
    startHour: Int,
    lessons: List<List<Lesson>>,
    onLessonClick: (String, Int) -> Unit,
    lessonBlockDisplayStyle: LessonBlockDisplayStyle,
    startDate: LocalDate,
    viewModel: ScheduleViewModel
) {
    val scrollState = rememberScrollState()
    val ui by viewModel.uiState.collectAsStateWithLifecycle()
    val timeTableStartHour = startHour.coerceAtMost(
        lessons.take(
            if (ui.showWeekends) 7 else 5
        ).flatten().minByOrNull { it.startTime }?.startTime?.hour ?: startHour
    )
    val dateFormatter = DateTimeFormatter
        .ofLocalizedDate(FormatStyle.SHORT)
        .withLocale(Locale.getDefault())

    Column (
        modifier = modifier.fillMaxSize()
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = title + " ${dateFormatter.format(startDate)}-${dateFormatter.format(startDate.plusDays(7))}",
            textAlign = TextAlign.Center
        )
        Row(
            Modifier.fillMaxWidth()
        ) {
            Spacer(Modifier.weight(1f))
            repeat(if (ui.showWeekends) 7 else 5) { dayIndex ->
                Text(
                    modifier = Modifier.weight(2f),
                    text = DayOfWeek.of(dayIndex+1).getDisplayName(
                        java.time.format.TextStyle.SHORT,
                        Locale.getDefault()
                    ),
                    textAlign = TextAlign.Center
                )
            }
        }
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(scrollState)
        ) {
            HoursColumn(
                startHour = timeTableStartHour,
                modifier = Modifier.weight(1f),
                hourHeight = hourHeight
            )
            repeat(if (ui.showWeekends) 7 else 5) { dayIndex ->
                Column(
                    modifier = Modifier
                        .padding(end = 4.dp)
                        .weight(2f)
                ) {
                    LessonsBox(
                        modifier = Modifier.fillMaxWidth(),
                        startHour = timeTableStartHour,
                        hourHeight = hourHeight,
                        lessons = lessons.getOrNull(dayIndex) ?: emptyList(),
                        groupFunction = { lessons, date ->
                            viewModel.groupOverlappingLessons(lessons, date)
                        },
                        date = startDate.plusDays(dayIndex.toLong()),
                        onLessonClick = {
                            onLessonClick(it, dayIndex+1)
                        },
                        lessonBlockDisplayStyle = lessonBlockDisplayStyle,
                        theme = ui.currentTheme,
                        10.sp
                    )
                }
            }

        }
    }
}