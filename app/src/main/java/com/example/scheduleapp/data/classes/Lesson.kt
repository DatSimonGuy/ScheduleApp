package com.example.scheduleapp.data.classes

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.scheduleapp.utils.LocalDateListSerializer
import com.example.scheduleapp.utils.LocalDateSerializer
import com.example.scheduleapp.utils.LocalTimeSerializer
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonIgnoreUnknownKeys
import java.time.Duration
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

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
    val start: Float get() = startTime.hour + startTime.minute / 60.0f

    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    fun percentageTimeLeft(currentTime: LocalTime): Float {
        if(!isActive(LocalDate.now()) || startTime > LocalTime.now()) return 0.0f
        return Duration.between(LocalTime.now(), endTime).toMinutes() / (duration * 60)
    }

    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    fun timeUntil(currentTime: LocalTime): String? {
        if(!isActive(LocalDate.now()) || startTime <= LocalTime.now()) return null
        val duration = Duration.between(LocalTime.now().minusHours(1), startTime).toSeconds()
        val formatter = DateTimeFormatter.ofPattern("HH:mm:ss")
        return LocalTime.of(duration.toInt() / 3600, duration.toInt() / 60, duration.toInt() % 60).format(formatter)
    }

    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    fun timeLeft(currentTime: LocalTime): String? {
        if(!isActive(LocalDate.now()) || startTime > LocalTime.now()) return null
        val duration = Duration.between(LocalTime.now(), endTime).toSeconds()
        val formatter = DateTimeFormatter.ofPattern("HH:mm:ss")
        return LocalTime.of(duration.toInt() / 3600, duration.toInt() / 60, duration.toInt() % 60).format(formatter)
    }

    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    fun isActive(
        date: LocalDate
    ): Boolean {
        return when(occurrence) {
            Occurrence.ONCE -> startDate?.isEqual(date)
            Occurrence.SELECTED_DAYS -> activeDays?.contains(date) == true
            Occurrence.EVERY_TWO -> ChronoUnit.WEEKS.between(startDate, date) % 2 == 0L
                    && if(startDate != null && endDate != null) date in startDate..endDate else false
            Occurrence.EVERY_THREE -> ChronoUnit.WEEKS.between(startDate, date) % 3 == 0L
                    && if(startDate != null && endDate != null) date in startDate..endDate else false
            else -> if(startDate != null && endDate != null) date in startDate..endDate else false
        } == true
    }
}
