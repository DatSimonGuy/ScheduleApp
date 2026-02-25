package com.example.scheduleapp.elements.timetable.LessonBlockDisplays

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.scheduleapp.data.classes.Lesson
import com.example.scheduleapp.data.classes.Occurrence
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Locale

@Composable
fun ExtendedDisplay(
    lesson: Lesson,
    textColor: Color
) {
    val dateFormatter: DateTimeFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)
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
            text = "${stringResource(lesson.lessonType.getDisplayName())} - ${stringResource(lesson.occurrence.getDisplayName())}",
            textAlign = TextAlign.Center,
            maxLines = 1,
            color = textColor
        )
        Text(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            text = "${lesson.room} | ${lesson.teacher}",
            textAlign = TextAlign.Center,
            color = textColor
        )
        Spacer(Modifier.weight(1f))
        when (lesson.occurrence) {
            Occurrence.ONCE -> {
                Text(
                    text = lesson.startDate?.format(dateFormatter) ?: "",
                    textAlign = TextAlign.Center,
                    color = textColor
                )
            }
            Occurrence.SELECTED_DAYS -> {
                Text(
                    text = lesson.activeDays?.filter { it >= LocalDate.now() }?.joinToString { it.format(dateFormatter) } ?: "",
                    textAlign = TextAlign.Center,
                    color = textColor,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            else -> {
                Text(
                    text = "${lesson.startDate?.format(dateFormatter)} - ${lesson.endDate?.format(dateFormatter)}",
                    textAlign = TextAlign.Center,
                    color = textColor,
                    maxLines = 1
                )
            }
        }
    }
}