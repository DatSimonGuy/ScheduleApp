package com.example.scheduleapp.data.classes

import com.example.scheduleapp.utils.LocalDateListSerializer
import com.example.scheduleapp.utils.LocalDateSerializer
import com.example.scheduleapp.utils.LocalTimeSerializer
import kotlinx.serialization.Serializable
import java.time.Duration
import java.time.LocalDate
import java.time.LocalTime

@Serializable
data class Lesson(
    val id: String = "",
    val subject: String,
    @Serializable(with = LocalTimeSerializer::class)
    val startTime: LocalTime,
    @Serializable(with = LocalTimeSerializer::class)
    val endTime: LocalTime,
    val room: String,
    val teacher: String = "",
    val lessonType: LessonType,
    val occurrence: Occurrence,
    @Serializable(with = LocalDateSerializer::class)
    val startDate: LocalDate?,
    @Serializable(with = LocalDateSerializer::class)
    val endDate: LocalDate?,
    @Serializable(with = LocalDateListSerializer::class)
    val activeDays: List<LocalDate>?
) {
    val duration: Int get() = Duration.between(startTime, endTime).toHours().toInt()
}
