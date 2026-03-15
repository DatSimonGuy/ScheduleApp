package com.example.scheduleapp.data.repository

import android.content.Context
import androidx.datastore.preferences.core.edit
import com.example.scheduleapp.data.datastore.PreferenceKeys
import com.example.scheduleapp.data.datastore.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

data class UserPreferences(
    val recentChatId: Long?
)

class PreferenceRepository(val context: Context)  {
    val preferences: Flow<UserPreferences> = context.preferencesDataStore.data
        .map { preferences ->
            UserPreferences(
                recentChatId = preferences[PreferenceKeys.recentChatId]
            )
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