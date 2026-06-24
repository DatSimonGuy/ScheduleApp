package com.example.scheduleapp.data.repository

import android.content.Context
import androidx.datastore.preferences.core.edit
import com.example.scheduleapp.data.classes.ColorTheme
import com.example.scheduleapp.data.classes.RefreshType
import com.example.scheduleapp.data.classes.ScheduleSortMode
import com.example.scheduleapp.data.datastore.SettingKeys
import com.example.scheduleapp.data.datastore.settingsDataStore
import com.example.scheduleapp.elements.schedule.parts.timetable.HourHeight
import com.example.scheduleapp.elements.schedule.parts.timetable.LessonBlockDisplayStyle
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
    val bigButton: Boolean,
    val refreshType: String,
    val currentTheme: String,
    val sortMode: String,
    val showWeekends: Boolean,
    val showTimeBar: Boolean
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
                bigButton = preferences[SettingKeys.bigButton] ?: false,
                refreshType = preferences[SettingKeys.refreshType] ?: RefreshType.AUTOMATIC.name,
                currentTheme = preferences[SettingKeys.currentTheme] ?: ColorTheme.DEFAULT.name,
                sortMode = preferences[SettingKeys.scheduleSortMode] ?: ScheduleSortMode.ALPHABETICAL.name,
                showWeekends = preferences[SettingKeys.showWeekends] ?: true,
                showTimeBar = preferences[SettingKeys.showTimeBar] ?: true
            )
        }

    suspend fun setShowTimeBar(value: Boolean) {
        context.settingsDataStore.edit {
            it[SettingKeys.showTimeBar] = value
        }
    }

    suspend fun setShowWeekends(value: Boolean) {
        context.settingsDataStore.edit {
            it[SettingKeys.showWeekends] = value
        }
    }

    suspend fun setScheduleSortMode(sortMode: ScheduleSortMode) {
        context.settingsDataStore.edit {
            it[SettingKeys.scheduleSortMode] = sortMode.name
        }
    }

    suspend fun setCurrentTheme(theme: ColorTheme) {
        context.settingsDataStore.edit {
            it[SettingKeys.currentTheme] = theme.name
        }
    }

    suspend fun setRefreshType(refreshType: RefreshType) {
        context.settingsDataStore.edit {
            it[SettingKeys.refreshType] = refreshType.name
        }
    }

    suspend fun setBigButton(value: Boolean) {
        context.settingsDataStore.edit {
            it[SettingKeys.bigButton] = value
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