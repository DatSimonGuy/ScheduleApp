package com.example.scheduleapp.elements.schedule.parts.timetable

import androidx.compose.foundation.layout.Box
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
                    val date = startDate.plusDays(dayIndex.toLong())
                    Box {
                        LessonsBox(
                            Modifier.fillMaxWidth(),
                            timeTableStartHour,
                            hourHeight,
                            lessons.getOrNull(dayIndex) ?: emptyList(),
                            { lessons, date ->
                                viewModel.groupOverlappingLessons(lessons, date)
                            },
                            date,
                            onLessonClick = {
                                onLessonClick(it, dayIndex+1)
                            },
                            lessonBlockDisplayStyle,
                            ui.currentTheme
                        )

                        if (ui.showTimeBar && date == LocalDate.now()) {
                            val elapsedHours = (ui.currentTime.toSecondOfDay()) / 3600f - timeTableStartHour
                            val totalRowHeight = hourHeight.value + 10.dp
                            val calculatedOffset = (totalRowHeight * elapsedHours) + 16.dp
                            TimeBar(topOffset = calculatedOffset)
                        }
                    }
                }
            }

        }
    }
}