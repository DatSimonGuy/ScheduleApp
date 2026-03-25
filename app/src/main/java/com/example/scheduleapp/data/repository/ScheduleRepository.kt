package com.example.scheduleapp.data.repository

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.scheduleapp.data.api.DSBApi
import com.example.scheduleapp.data.classes.Lesson
import com.example.scheduleapp.data.classes.SaveLocation
import com.example.scheduleapp.data.classes.Schedule
import com.example.scheduleapp.data.classes.ScheduleMap
import com.example.scheduleapp.data.datastore.scheduleDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime

class ScheduleRepository(private val context: Context) {
    val scheduleMap: Flow<ScheduleMap> = context.scheduleDataStore.data

    suspend fun saveSchedule(name: String, schedule: Schedule): String? {
        if (schedule.saveLocation == SaveLocation.DSB && schedule.chatId != null) {
            val api = DSBApi(schedule.chatId, "")
            val (presentSchedule, _) = api.getSchedule(name)
            if (presentSchedule != null) {
                return "This schedule already exists"
            }
            val error = api.createSchedule(name)
            error?.let { return it }
        }
        context.scheduleDataStore.updateData { currentDb ->
            currentDb.copy(
                schedules = currentDb.schedules + (name to schedule)
            )
        }
        return null
    }

    suspend fun updateSchedule(scheduleName: String, schedule: Schedule): String? {
        if (schedule.saveLocation == SaveLocation.DSB && schedule.chatId != null) {
            val api = DSBApi(schedule.chatId, "")
            val (updated, error) = api.getSchedule(scheduleName)
            error?.let { return it }
            updated?.let {
                context.scheduleDataStore.updateData { currentDb ->
                    currentDb.copy(
                        schedules = currentDb.schedules + (scheduleName to it)
                    )
                }
            }
        }
        return null
    }

    suspend fun importSchedules(chatId: Long, list: List<String>): String? {
        val api = DSBApi(chatId, "")

        list.forEach {
            val (schedule, error) = api.getSchedule(it)
            error?.let { return error }
            if (schedule == null) {
                return "Error fetching schedule"
            }
            context.scheduleDataStore.updateData { currentDb ->
                currentDb.copy(
                    schedules = currentDb.schedules + (it to schedule)
                )
            }
        }
        return null
    }

    suspend fun getAvailableSchedules(chatId: Long): Pair<List<String>?, String?> {
        val api = DSBApi(chatId, "")
        return api.getSchedules(chatId)
    }

    suspend fun removeSchedule(name: String, schedule: Schedule, localOnly: Boolean = false): String? {
        if (!localOnly && schedule.saveLocation == SaveLocation.DSB && schedule.chatId != null) {
            val api = DSBApi(schedule.chatId, "")
            val error = api.removeSchedule(name)
            error?.let { return it }
        }
        context.scheduleDataStore.updateData { currentDb ->
            currentDb.copy(
                schedules = currentDb.schedules - name
            )
        }
        return null
    }

    suspend fun editSchedule(oldName: String, newName: String, schedule: Schedule): String? {
        if (schedule.saveLocation == SaveLocation.DSB && schedule.chatId != null) {
            val api = DSBApi(schedule.chatId, "")
            val error = api.editSchedule(oldName, newName)
            error?.let { return it }
        }
        context.scheduleDataStore.updateData { currentDb ->
            currentDb.copy(
                schedules = currentDb.schedules - oldName + (newName to schedule)
            )
        }
        return null
    }

    suspend fun addLesson(scheduleName: String, dayOfWeek: DayOfWeek, lesson: Lesson): String? {
        val currentDb = context.scheduleDataStore.data.first()
        val currentSchedule =
            currentDb.schedules[scheduleName] ?: return "This schedule does not exist"
        currentSchedule.let { schedule ->
            if (schedule.saveLocation == SaveLocation.DSB) {
                val api = DSBApi(schedule.chatId ?: 0, "")
                val error = api.addLesson(lesson, dayOfWeek, scheduleName)
                error?.let {
                    return it
                }
            }
            val currentDayLessons = schedule.lessons[dayOfWeek] ?: emptyList()
            val updatedDayLessons = (currentDayLessons + lesson)
                .sortedBy { it.startTime }
            val updatedSchedule = schedule.copy(
                lessons = schedule.lessons + (dayOfWeek to updatedDayLessons)
            )
            context.scheduleDataStore.updateData { currentDb ->
                currentDb.copy(
                    schedules = currentDb.schedules + (scheduleName to updatedSchedule)
                )
            }
        }
        return null
    }

    suspend fun updateLesson(scheduleName: String, lesson: Lesson, oldDay: DayOfWeek, newDay: DayOfWeek) {
        removeLesson(scheduleName, lesson.id, oldDay)
        addLesson(scheduleName, newDay, lesson)
    }

    suspend fun removeLesson(scheduleName: String, lessonId: String, dayOfWeek: DayOfWeek): String? {
        val currentDb = context.scheduleDataStore.data.first()
        val currentSchedule =
            currentDb.schedules[scheduleName] ?: return "This schedule does not exist"
        val schedules =
            currentDb.schedules
        currentSchedule.let { schedule ->
            if (schedule.saveLocation == SaveLocation.DSB) {
                val api = DSBApi(schedule.chatId ?: 0, "")
                val error = api.removeLesson(lessonId, dayOfWeek, scheduleName)
                error?.let {
                    return it
                }
            }
            val day = schedule.lessons
                .asSequence()
                .first { day ->
                    day.value.find {
                        it.id == lessonId
                    } != null
                }
            val lessons = day.value.filterNot { it.id == lessonId }
            val updatedLessonMap = schedule.lessons.toMutableMap().apply {
                put(day.key, lessons)
            }
            schedules.let {
                val updatedSchedulesMap = it.toMutableMap().apply {
                    put(scheduleName, schedule.copy(lessons = updatedLessonMap))
                }
                context.scheduleDataStore.updateData { currentDb ->
                    currentDb.copy(
                        schedules = updatedSchedulesMap
                    )
                }
            }
        }
        return null
    }

    suspend fun getLesson(scheduleName: String, id: String): Lesson? {
        val schedule = scheduleMap.first()[scheduleName]
        return schedule?.lessons
            ?.asSequence()
            ?.flatMap { it.value }
            ?.find { it.id == id }
    }

    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    suspend fun getNextLesson(scheduleName: String) : Lesson? {
        val schedule = scheduleMap.first()[scheduleName]
        return schedule?.lessons[LocalDate.now().dayOfWeek]?.firstOrNull {
            it.startTime > LocalTime.now() && it.isActive(LocalDate.now())
        }
    }

    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    suspend fun getCurrentLesson(scheduleName: String): Lesson? {
        val schedule = scheduleMap.first()[scheduleName]
        return schedule?.lessons[LocalDate.now().dayOfWeek]?.firstOrNull {
            LocalTime.now() in it.startTime..it.endTime && it.isActive(LocalDate.now())
        }
    }

}