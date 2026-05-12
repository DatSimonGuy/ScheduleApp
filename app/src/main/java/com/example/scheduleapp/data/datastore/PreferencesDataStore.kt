package com.example.scheduleapp.data.datastore

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore

val Context.preferencesDataStore by preferencesDataStore(
    name = "preferences"
)

object PreferenceKeys {
    val recentChatId = longPreferencesKey("recentChatId")
    val scheduleOrder = stringPreferencesKey("scheduleOrder")
}