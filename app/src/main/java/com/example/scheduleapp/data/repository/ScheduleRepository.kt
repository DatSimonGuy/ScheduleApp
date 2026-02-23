package com.example.scheduleapp.data.repository

import android.content.Context
import android.util.Log
import com.example.scheduleapp.data.classes.Schedule
import com.example.scheduleapp.data.classes.ScheduleMap
import com.example.scheduleapp.data.datastore.scheduleDataStore
import kotlinx.coroutines.flow.Flow

class ScheduleRepository(private val context: Context) {
    val scheduleMap: Flow<ScheduleMap> = context.scheduleDataStore.data

    suspend fun saveSchedule(name: String, schedule: Schedule) {
        context.scheduleDataStore.updateData { currentDb ->
            currentDb.copy(
                schedules = currentDb.schedules + (name to schedule)
            )
        }
    }

    suspend fun deleteSchedule(name: String) {
        context.scheduleDataStore.updateData { currentDb ->
            currentDb.copy(
                schedules = currentDb.schedules - name
            )
        }
    }

}