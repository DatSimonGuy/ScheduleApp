package com.example.scheduleapp.elements.schedule.timetable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.CompareArrows
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.scheduleapp.data.classes.ColorTheme
import com.example.scheduleapp.data.classes.Lesson
import com.example.scheduleapp.data.classes.getColors
import com.example.scheduleapp.elements.schedule.timetable.LessonBlockDisplays.CompactDisplay
import com.example.scheduleapp.elements.schedule.timetable.LessonBlockDisplays.ExtendedDisplay
import com.example.scheduleapp.elements.schedule.timetable.LessonBlockDisplays.NormalDisplay
import com.example.scheduleapp.utils.textColorForBackground
import java.time.LocalDate

@Composable
fun LessonBlock(
    hourHeight: HourHeight,
    startHour: Int,
    lessons: List<Lesson>,
    onLessonClick: (String) -> Unit,
    displayStyle: LessonBlockDisplayStyle,
    date: LocalDate,
    currentTheme: ColorTheme
) {
    var currentIndex by remember { mutableStateOf(0) }
    val context = LocalContext.current
    val currentLesson = lessons[currentIndex]
    val topOffset = (hourHeight.value + 10.dp) * (currentLesson.start - startHour) + 16.dp
    val blockHeight = (hourHeight.value + 10.dp) * currentLesson.duration - 8.dp
    var style by rememberSaveable { mutableStateOf(displayStyle) }
    val colors = getColors(currentTheme, context)
    val lessonColor = colors[currentLesson.lessonType] ?: currentLesson.lessonType.color
    val textColor = textColorForBackground(lessonColor)

    Row(
        Modifier
            .fillMaxWidth()
            .offset(y = topOffset)
            .height(blockHeight),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        ElevatedCard(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .alpha(if (currentLesson.isActive(date)) 1.0f else 0.5f)
                .clickable { onLessonClick(currentLesson.id) },
            colors = CardDefaults.cardColors(
                containerColor = lessonColor
            )
        ) {
            when (style) {
                LessonBlockDisplayStyle.NORMAL -> NormalDisplay(currentLesson, textColor)
                LessonBlockDisplayStyle.COMPACT -> CompactDisplay(currentLesson, textColor)
                LessonBlockDisplayStyle.EXTENDED -> ExtendedDisplay(currentLesson, textColor)
            }
        }

        if (lessons.size > 1) {
            ElevatedCard(
                modifier = Modifier
                    .width(48.dp)
                    .fillMaxHeight()
                    .clickable {
                        currentIndex = (currentIndex + 1) % lessons.size
                    },
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                )
            ) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.AutoMirrored.Filled.CompareArrows, contentDescription = "Switch")
                        Text("${currentIndex + 1}/${lessons.size}", style = MaterialTheme.typography.labelSmall)
                    }
                }
            }
        }
    }
}