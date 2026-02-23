package com.example.scheduleapp.data.datastore

import android.R
import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore

val Context.settingsDataStore by preferencesDataStore(
    name = "settings"
)

object SettingKeys {
    val hourHeight = stringPreferencesKey("hourHeight")
    val addScheduleInFab = booleanPreferencesKey("fabAddSchedule")
    val defaultSchedule = stringPreferencesKey("defaultSchedule")
}