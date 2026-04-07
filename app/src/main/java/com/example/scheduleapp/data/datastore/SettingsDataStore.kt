package com.example.scheduleapp.data.datastore

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore

val Context.settingsDataStore by preferencesDataStore(
    name = "settings"
)

object SettingKeys {
    val hourHeight = stringPreferencesKey("hourHeight")
    val lessonBlockDisplayStyle = stringPreferencesKey("lessonBlockDisplayStyle")
    val addScheduleInFab = booleanPreferencesKey("fabAddSchedule")
    val defaultSchedule = stringPreferencesKey("defaultSchedule")
    val startTime = stringPreferencesKey("startTime")
    val startPage = intPreferencesKey("startPage")
    val textButtons = booleanPreferencesKey("textButtons")
    val bigButton = booleanPreferencesKey("bigButton")
}