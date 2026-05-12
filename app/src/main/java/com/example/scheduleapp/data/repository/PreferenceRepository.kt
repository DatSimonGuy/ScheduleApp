package com.example.scheduleapp.data.repository

import android.content.Context
import androidx.datastore.preferences.core.edit
import com.example.scheduleapp.data.datastore.PreferenceKeys
import com.example.scheduleapp.data.datastore.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

data class UserPreferences(
    val recentChatId: Long?,
    val scheduleOrder: String?
)

class PreferenceRepository(val context: Context)  {
    val preferences: Flow<UserPreferences> = context.preferencesDataStore.data
        .map { preferences ->
            UserPreferences(
                recentChatId = preferences[PreferenceKeys.recentChatId],
                scheduleOrder = preferences[PreferenceKeys.scheduleOrder]
            )
    }

    suspend fun setScheduleOrder(value: String?)
    {
        context.preferencesDataStore.edit { preferences ->
            value?.let {
                preferences[PreferenceKeys.scheduleOrder] = it
            }
        }
    }

    suspend fun setRecentChatId(chatId: Long?)
    {
        context.preferencesDataStore.edit { preferences ->
            chatId?.let {
                preferences[PreferenceKeys.recentChatId] = it
            }
        }
    }
}