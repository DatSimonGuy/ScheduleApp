package com.example.scheduleapp.data.classes

import com.example.scheduleapp.utils.LocalDateListSerializer
import com.example.scheduleapp.utils.LocalDateSerializer
import com.example.scheduleapp.utils.LocalTimeSerializer
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonIgnoreUnknownKeys
import java.time.Duration
import java.time.LocalDate
import java.time.LocalTime

@OptIn(ExperimentalSerializationApi::class)
@JsonIgnoreUnknownKeys
@Serializable
data class Lesson @OptIn(ExperimentalMultiplatform::class) constructor(
    var id: String = "",
    val subject: String,
    @SerialName("start")
    @Serializable(with = LocalTimeSerializer::class)
    val startTime: LocalTime,
    @SerialName("end")
    @Serializable(with = LocalTimeSerializer::class)
    val endTime: LocalTime,
    val room: String,
    val teacher: String = "",
    @SerialName("type")
    val lessonType: LessonType,
    val occurrence: Occurrence,
    @SerialName("start_day")
    @Serializable(with = LocalDateSerializer::class)
    val startDate: LocalDate?,
    @SerialName("end_day")
    @Serializable(with = LocalDateSerializer::class)
    val endDate: LocalDate?,
    @SerialName("active_dates")
    @Serializable(with = LocalDateListSerializer::class)
    val activeDays: List<LocalDate>? = null
) {
    val duration: Float get() = Duration.between(startTime, endTime).toMinutes().toFloat() / 60
    fun isActive(
        date: LocalDate
    ): Boolean {
        return when(occurrence) {
            Occurrence.ONCE -> startDate?.isEqual(date)
            Occurrence.SELECTED_DAYS -> activeDays?.contains(date) == true
            else -> if(startDate != null && endDate != null) date in startDate..endDate else false
        } == true
    }
}
