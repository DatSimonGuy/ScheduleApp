package com.example.scheduleapp.data.classes

import kotlinx.serialization.Serializable
import java.time.DayOfWeek

@Serializable
data class Schedule(
    val lessons: Map<DayOfWeek, List<Lesson>> = DayOfWeek.entries.associateWith { emptyList() },
    val saveLocation: SaveLocation = SaveLocation.LOCAL,
    val chatId: Long? = null
)

@Serializable
data class ScheduleMap(
    val schedules: Map<String, Schedule> = emptyMap()
) {
    operator fun get(key: String): Schedule? {
        return schedules[key]
    }

    fun count(): Int {
        return schedules.count()
    }
}