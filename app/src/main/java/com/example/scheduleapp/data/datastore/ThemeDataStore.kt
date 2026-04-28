package com.example.scheduleapp.data.datastore

import android.content.Context
import androidx.datastore.dataStore
import com.example.scheduleapp.utils.ThemeSerializer

val Context.themeDataStore by dataStore(
    fileName = "custom_theme.json",
    serializer = ThemeSerializer
)