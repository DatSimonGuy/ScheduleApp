package com.example.scheduleapp.elements.schedule

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scheduleapp.data.classes.Schedule
import com.example.scheduleapp.data.classes.ScheduleMap
import com.example.scheduleapp.data.repository.ScheduleRepository
import com.example.scheduleapp.data.repository.SettingsRepository
import com.example.scheduleapp.elements.timetable.HourHeight
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ScheduleUiState(
    val hourHeight: HourHeight = HourHeight.MEDIUM,
    val schedules: ScheduleMap = ScheduleMap(),
    val selectedSchedule: String? = null,
    val showAddSchedule: Boolean = false
)

class ScheduleViewModel(
    val settingsRepository: SettingsRepository,
    val scheduleRepository: ScheduleRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(ScheduleUiState())
    val uiState = _uiState.asStateFlow()
    val currentSchedule get() = uiState.value.schedules[uiState.value.selectedSchedule ?: ""]

    init {
        viewModelScope.launch {
            settingsRepository.settingsFlow.collect { settings ->
                _uiState.update { currentState ->
                    currentState.copy(
                        hourHeight = HourHeight.valueOf(settings.hourHeight),
                        showAddSchedule = settings.addScheduleInFab,
                        selectedSchedule = settings.defaultSchedule
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

    fun addNewSchedule(name: String, isPrivate: Boolean) {
        viewModelScope.launch {
            if (isPrivate) {
                scheduleRepository.saveSchedule(name, Schedule())
                if (uiState.value.selectedSchedule == null) {
                    settingsRepository.setDefaultSchedule(name)
                }
                _uiState.update {
                    it.copy(
                        selectedSchedule = name
                    )
                }
            }
        }
    }

    fun setCurrentSchedule(name: String) {
        _uiState.update {
            it.copy(
                selectedSchedule = name
            )
        }
    }
}