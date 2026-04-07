package com.example.scheduleapp.data.repository

import android.content.Context
import androidx.datastore.preferences.core.edit
import com.example.scheduleapp.data.datastore.SettingKeys
import com.example.scheduleapp.data.datastore.settingsDataStore
import com.example.scheduleapp.elements.schedule.timetable.HourHeight
import com.example.scheduleapp.elements.schedule.timetable.LessonBlockDisplayStyle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalTime
import java.time.format.DateTimeFormatter

data class UserSettings(
    val hourHeight: String,
    val lessonBlockDisplayStyle: String,
    val addScheduleInFab: Boolean,
    val defaultSchedule: String?,
    val startTime: String?,
    val startPage: Int?,
    val textButtons: Boolean,
    val bigButton: Boolean
)

class SettingsRepository(private val context: Context) {
    val settingsFlow: Flow<UserSettings> = context.settingsDataStore.data
        .map { preferences ->
            UserSettings(
                hourHeight = preferences[SettingKeys.hourHeight] ?: HourHeight.MEDIUM.name,
                lessonBlockDisplayStyle = preferences[SettingKeys.lessonBlockDisplayStyle] ?: LessonBlockDisplayStyle.NORMAL.name,
                addScheduleInFab = preferences[SettingKeys.addScheduleInFab] ?: false,
                defaultSchedule = preferences[SettingKeys.defaultSchedule],
                startTime = preferences[SettingKeys.startTime],
                startPage = preferences[SettingKeys.startPage],
                textButtons = preferences[SettingKeys.textButtons] ?: false,
                bigButton = preferences[SettingKeys.bigButton] ?: false
            )
        }

    suspend fun setBigButton(value: Boolean) {
        context.settingsDataStore.edit {
            it[SettingKeys.bigButton] = value
        }
    }

    suspend fun setTextButtons(value: Boolean) {
        context.settingsDataStore.edit {
            it[SettingKeys.textButtons] = value
        }
    }

    suspend fun setStartPage(page: Int?) {
        context.settingsDataStore.edit {
            page?.let { pg ->
                it[SettingKeys.startPage] = pg
                return@edit
            }
            it.remove(SettingKeys.startPage)
        }
    }

    suspend fun setStartHour(hour: LocalTime) {
        context.settingsDataStore.edit {
            val formatter = DateTimeFormatter.ofPattern("HH:mm")
            it[SettingKeys.startTime] = hour.format(formatter)
        }
    }

    suspend fun setHourHeight(height: HourHeight) {
        context.settingsDataStore.edit {
            it[SettingKeys.hourHeight] = height.name
        }
    }

    suspend fun setLessonBlockDisplayStyle(style: LessonBlockDisplayStyle) {
        context.settingsDataStore.edit {
            it[SettingKeys.lessonBlockDisplayStyle] = style.name
        }
    }

    suspend fun setAddScheduleInFab(value: Boolean) {
        context.settingsDataStore.edit {
            it[SettingKeys.addScheduleInFab] = value
        }
    }

    suspend fun setDefaultSchedule(value: String?) {
        context.settingsDataStore.edit {
            if (value != null) {
                it[SettingKeys.defaultSchedule] = value
            } else {
                it.remove(SettingKeys.defaultSchedule)
            }
        }
    }
}