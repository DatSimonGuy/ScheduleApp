package com.example.scheduleapp.data.datastore
import android.content.Context
import androidx.datastore.dataStore
import com.example.scheduleapp.utils.ScheduleMapSerializer

val Context.scheduleDataStore by dataStore(
    fileName = "schedules.json",
    serializer = ScheduleMapSerializer
)