package com.example.scheduleapp.elements.forms.states

import androidx.compose.material3.DatePickerState
import androidx.compose.material3.DateRangePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.getSelectedDate
import androidx.compose.material3.getSelectedEndDate
import androidx.compose.material3.getSelectedStartDate
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.material3.setSelectedDate
import androidx.compose.material3.setSelection
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.example.scheduleapp.data.classes.Lesson
import com.example.scheduleapp.data.classes.LessonType
import com.example.scheduleapp.data.classes.Occurrence
import com.example.scheduleapp.elements.forms.validation.addLessonFormValidate
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime
import java.util.UUID

class LessonFormState @OptIn(ExperimentalMaterial3Api::class) constructor(
    val subject: MutableState<String>,
    val room: MutableState<String>,
    val teacher: MutableState<String>,
    val type: MutableState<String>,
    val occurrence: MutableState<String>,
    val dayOfWeek: MutableState<String>,
    val startTime: TimePickerState,
    val endTime: TimePickerState,
    val startDate: DatePickerState,
    val dateRange: DateRangePickerState,
    val selectedDates: SnapshotStateList<LocalDate>
) {
    @OptIn(ExperimentalMaterial3Api::class)
    fun validateAndMap(lessonId: String?): Pair<Lesson?, Pair<String?, String?>> {
        val startTime = LocalTime.of(startTime.hour, startTime.minute)
        val endTime = LocalTime.of(endTime.hour, endTime.minute)

        val (subjectErr, timeErr) = addLessonFormValidate(subject.value, startTime, endTime)

        if (subjectErr != null || timeErr != null) {
            return null to (subjectErr to timeErr)
        }

        val lesson = Lesson(
            id = lessonId ?: UUID.randomUUID().toString(),
            subject = subject.value,
            startTime = startTime,
            endTime = endTime,
            room = room.value,
            teacher = teacher.value,
            lessonType = LessonType.valueOf(type.value),
            occurrence = Occurrence.valueOf(occurrence.value),
            startDate = if (occurrence.value == Occurrence.ONCE.name)
                startDate.getSelectedDate()
            else dateRange.getSelectedStartDate() ?: LocalDate.now(),
            endDate = dateRange.getSelectedEndDate() ?: LocalDate.now(),
            activeDays = selectedDates.toList()
        )
        return lesson to (null to null)
    }

    @OptIn(ExperimentalMaterial3Api::class)
    fun fillFields(lesson: Lesson) {
        subject.value = lesson.subject
        room.value = lesson.room
        teacher.value = lesson.teacher
        type.value = lesson.lessonType.name
        occurrence.value = lesson.occurrence.name
        startTime.hour = lesson.startTime.hour
        startTime.minute = lesson.startTime.minute
        endTime.hour = lesson.endTime.hour
        endTime.minute = lesson.endTime.minute
        startDate.setSelectedDate(lesson.startDate)
        lesson.endDate?.let { end ->
            dateRange.setSelection(lesson.startDate, end)
        }
        selectedDates.clear()
        lesson.activeDays?.let { selectedDates.addAll(it) }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun rememberLessonFormState(lesson: Lesson?, initialDay: DayOfWeek): LessonFormState {
    val subject = rememberSaveable { mutableStateOf(lesson?.subject ?: "") }
    val room = rememberSaveable { mutableStateOf(lesson?.room ?: "") }
    val teacher = rememberSaveable { mutableStateOf(lesson?.teacher ?: "") }
    val type = rememberSaveable { mutableStateOf(lesson?.lessonType?.name ?: LessonType.LECTURE.name) }
    val occurrence = rememberSaveable { mutableStateOf(lesson?.occurrence?.name ?: Occurrence.WEEKLY.name) }
    val day = rememberSaveable { mutableStateOf(initialDay.name) }

    val start = rememberTimePickerState(lesson?.startTime?.hour ?: 0, lesson?.startTime?.minute ?: 0)
    val end = rememberTimePickerState(lesson?.endTime?.hour ?: 0, lesson?.endTime?.minute ?: 0)
    val date = rememberDatePickerState()
    val range = rememberDateRangePickerState()
    lesson?.startDate?.let { startDate ->
        date.setSelectedDate(startDate)
        lesson.endDate?.let {
            range.setSelection(startDate, it)
        }
    }
    val dates = remember { mutableStateListOf<LocalDate>().apply { lesson?.activeDays?.let { addAll(it) } } }

    return remember {
        LessonFormState(subject, room, teacher, type, occurrence, day, start, end, date, range, dates)
    }
}