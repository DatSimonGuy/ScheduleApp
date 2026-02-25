package com.example.scheduleapp.data.repository

import android.content.Context
import android.util.Log
import com.example.scheduleapp.data.classes.Lesson
import com.example.scheduleapp.data.classes.Schedule
import com.example.scheduleapp.data.classes.ScheduleMap
import com.example.scheduleapp.data.datastore.scheduleDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import java.time.DayOfWeek

class ScheduleRepository(private val context: Context) {
    val scheduleMap: Flow<ScheduleMap> = context.scheduleDataStore.data

    suspend fun saveSchedule(name: String, schedule: Schedule) {
        context.scheduleDataStore.updateData { currentDb ->
            currentDb.copy(
                schedules = currentDb.schedules + (name to schedule)
            )
        }
    }

    suspend fun deleteSchedule(name: String) {
        context.scheduleDataStore.updateData { currentDb ->
            currentDb.copy(
                schedules = currentDb.schedules - name
            )
        }
    }

    suspend fun addLesson(scheduleName: String, dayOfWeek: DayOfWeek, lesson: Lesson) {
        context.scheduleDataStore.updateData { currentDb ->
            val currentSchedule = currentDb.schedules[scheduleName]
                ?: return@updateData currentDb
            val currentDayLessons = currentSchedule.lessons[dayOfWeek] ?: emptyList()
            val updatedDayLessons = (currentDayLessons + lesson)
                .sortedBy { it.startTime }
            val updatedSchedule = currentSchedule.copy(
                lessons = currentSchedule.lessons + (dayOfWeek to updatedDayLessons)
            )
            currentDb.copy(
                schedules = currentDb.schedules + (scheduleName to updatedSchedule)
            )
        }
    }

    suspend fun updateLesson(scheduleName: String, lesson: Lesson) {
        context.scheduleDataStore.updateData { currentDb ->
            val currentSchedule = currentDb.schedules[scheduleName] ?: return@updateData currentDb
            val day = currentSchedule.lessons
                .asSequence()
                .first { day ->
                    day.value.find {
                        it.id == lesson.id
                    } != null
                }
            val lessons = day.value.map {
                if (it.id == lesson.id) lesson else it
            }
            val updatedLessonMap = currentSchedule.lessons.toMutableMap().apply {
                put(day.key, lessons)
            }
            val updatedSchedulesMap = currentDb.schedules.toMutableMap().apply {
                put(scheduleName, currentSchedule.copy(lessons = updatedLessonMap))
            }
            currentDb.copy(
                schedules = updatedSchedulesMap
            )
        }
    }

    suspend fun removeLesson(scheduleName: String, lessonId: String) {
        context.scheduleDataStore.updateData { currentDb ->
            val schedule = currentDb.schedules[scheduleName] ?: return@updateData currentDb
            val day = schedule.lessons
                .asSequence()
                .first { day ->
                    day.value.find {
                        it.id == lessonId
                    } != null
                }
            val lessons = day.value.dropWhile { it.id == lessonId }
            val updatedLessonMap = schedule.lessons.toMutableMap().apply {
                put(day.key, lessons)
            }
            val updatedSchedulesMap = currentDb.schedules.toMutableMap().apply {
                put(scheduleName, schedule.copy(lessons = updatedLessonMap))
            }
            currentDb.copy(
                schedules = updatedSchedulesMap
            )
        }
    }

    suspend fun getLesson(scheduleName: String, id: String): Lesson? {
        val schedule = scheduleMap.first()[scheduleName]
        return schedule?.lessons
            ?.asSequence()
            ?.flatMap { it.value }
            ?.find { it.id == id }
    }

}