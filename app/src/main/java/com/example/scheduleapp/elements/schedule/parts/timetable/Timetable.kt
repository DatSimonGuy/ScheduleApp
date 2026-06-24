package com.example.scheduleapp.elements.schedule.parts.timetable

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.scheduleapp.data.classes.Lesson
import com.example.scheduleapp.elements.schedule.ScheduleViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Locale

@Composable
fun TimeTable(
    modifier: Modifier = Modifier,
    title: String = "",
    hourHeight: HourHeight,
    startHour: Int,
    lessons: List<Lesson>,
    onLessonClick: (String) -> Unit,
    lessonBlockDisplayStyle: LessonBlockDisplayStyle,
    date: LocalDate,
    viewModel: ScheduleViewModel
) {
    val ui by viewModel.uiState.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()
    val timeTableStartHour = startHour.coerceAtMost(lessons.firstOrNull()?.startTime?.hour ?: startHour)
    val dateFormatter = DateTimeFormatter
        .ofLocalizedDate(FormatStyle.SHORT)
        .withLocale(Locale.getDefault())
    Column (
        modifier.fillMaxSize()
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = title,
            textAlign = TextAlign.Center
        )
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = dateFormatter.format(date),
            textAlign = TextAlign.Center
        )
        Row(
            Modifier.verticalScroll(scrollState)
        ) {
            HoursColumn(
                timeTableStartHour,
                Modifier.weight(1f),
                hourHeight
            )

            Box(
                modifier = Modifier.weight(5f)
            ) {
                LessonsBox(
                    Modifier.fillMaxWidth(),
                    timeTableStartHour,
                    hourHeight,
                    lessons,
                    { lessons, date ->
                        viewModel.groupOverlappingLessons(lessons, date)
                    },
                    date,
                    onLessonClick,
                    lessonBlockDisplayStyle,
                    ui.currentTheme
                )

                if (ui.showTimeBar && date == LocalDate.now()) {
                    val elapsedHours = ui.currentTime.toSecondOfDay() / 3600f - timeTableStartHour
                    val totalRowHeight = hourHeight.value + 10.dp
                    val calculatedOffset = (totalRowHeight * elapsedHours) + 16.dp
                    TimeBar(topOffset = calculatedOffset)
                }
            }
        }
    }
}