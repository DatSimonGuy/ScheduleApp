package com.example.scheduleapp.elements.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import com.example.scheduleapp.data.classes.Schedule
import com.example.scheduleapp.data.classes.ScheduleMap
import com.example.scheduleapp.data.repository.ScheduleRepository
import com.example.scheduleapp.data.repository.SettingsRepository
import com.example.scheduleapp.elements.timetable.HourHeight
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


data class Settings(
    val hourHeight: HourHeight = HourHeight.MEDIUM,
    val schedules: ScheduleMap = ScheduleMap(),
    var addScheduleInFab: Boolean = false,
    var defaultSchedule: String? = null
)

class SettingsViewModel(
    private val repository: SettingsRepository,
    private val scheduleRepository: ScheduleRepository
) : ViewModel() {
    val _uiState = MutableStateFlow(Settings())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.settingsFlow.collect { settings ->
                _uiState.update { currentState ->
                    currentState.copy(
                        hourHeight = HourHeight.valueOf(settings.hourHeight),
                        addScheduleInFab = settings.addScheduleInFab,
                        defaultSchedule = settings.defaultSchedule
                    )
                }
            }
        }
        viewModelScope.launch {
            scheduleRepository.scheduleMap.collect { scheduleMap ->
                _uiState.update { currentState ->
                    currentState.copy(
                        schedules = scheduleMap
                    )
                }
            }
        }
    }

    fun onHourHeightChange(hourHeight: HourHeight) {
        viewModelScope.launch {
            repository.setHourHeight(hourHeight)
        }
    }

    fun onAddScheduleInFabChange(value: Boolean) {
        viewModelScope.launch {
            repository.setAddScheduleInFab(value)
        }
    }

    fun onDefaultScheduleChange(value: String?) {
        viewModelScope.launch {
            repository.setDefaultSchedule(value)
        }
    }

    fun addNewSchedule(name: String, isPrivate: Boolean, schedule: Schedule = Schedule()) {
        viewModelScope.launch {
            if (isPrivate) {
                scheduleRepository.saveSchedule(name, schedule)
            }
        }
    }

    fun deleteSchedule(name: String, isPrivate: Boolean) {
        viewModelScope.launch {
            if (isPrivate) {
                scheduleRepository.deleteSchedule(name)
            }
        }
        if (uiState.value.schedules.count() < 1) {
            onDefaultScheduleChange(null)
        }
    }
}