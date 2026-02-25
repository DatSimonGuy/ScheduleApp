package com.example.scheduleapp.elements.schedule

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.scheduleapp.data.classes.Lesson
import com.example.scheduleapp.data.classes.Schedule
import com.example.scheduleapp.data.classes.ScheduleMap
import com.example.scheduleapp.data.repository.ScheduleRepository
import com.example.scheduleapp.data.repository.SettingsRepository
import com.example.scheduleapp.elements.timetable.HourHeight
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.DayOfWeek

data class ScheduleUiState(
    val hourHeight: HourHeight = HourHeight.MEDIUM,
    val schedules: ScheduleMap = ScheduleMap(),
    val selectedSchedule: String? = null,
    val showAddSchedule: Boolean = false,
)

class ScheduleViewModel(
    val navController: NavController,
    val settingsRepository: SettingsRepository,
    val scheduleRepository: ScheduleRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(ScheduleUiState())
    val uiState = _uiState.asStateFlow()
    val currentScheduleFlow = combine(
        _uiState.map { it.selectedSchedule },
        scheduleRepository.scheduleMap
    ) { selectedName, map ->
        map[selectedName ?: ""]
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

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

    fun addNewLesson(dayOfWeek: DayOfWeek, lesson: Lesson) {
        viewModelScope.launch {
            scheduleRepository.addLesson(uiState.value.selectedSchedule ?: "", dayOfWeek, lesson)
        }
    }

    fun updateLesson(scheduleName: String, lesson: Lesson, dayOfWeek: DayOfWeek) {
        viewModelScope.launch {
            scheduleRepository.updateLesson(scheduleName, lesson, dayOfWeek)
        }
    }

    fun removeLesson(scheduleName: String, lessonId: String) {
        viewModelScope.launch {
            scheduleRepository.removeLesson(scheduleName, lessonId)
        }
    }

    suspend fun getLesson(scheduleName: String, id: String): Lesson? {
        return scheduleRepository.getLesson(scheduleName, id)
    }
}