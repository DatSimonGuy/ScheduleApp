package com.example.scheduleapp.elements.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scheduleapp.data.classes.Lesson
import com.example.scheduleapp.data.repository.PreferenceRepository
import com.example.scheduleapp.data.repository.ScheduleRepository
import com.example.scheduleapp.data.repository.SettingsRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalTime


data class HomeUiState(
    val selectedSchedule: String? = null,
    val currentLesson: Lesson? = null,
    val nextLesson: Lesson? = null,
    val currentTime: LocalTime = LocalTime.now()
)

class HomeViewModel (
    private val settingsRepository: SettingsRepository,
    private val scheduleRepository: ScheduleRepository,
    private val preferenceRepository: PreferenceRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            while (true) {
                _uiState.update {
                    it.copy(currentTime = LocalTime.now())
                }
                delay(1000)
            }
        }

        viewModelScope.launch {
            settingsRepository.settingsFlow.collect { settings ->
                _uiState.update {
                    it.copy(selectedSchedule = settings.defaultSchedule)
                }
            }
        }
    }

    suspend fun updateScheduleInfo(): Unit {
        val selectedSchedule = settingsRepository.settingsFlow.firstOrNull()?.defaultSchedule
        val currentLesson = selectedSchedule?.let {
            scheduleRepository.getCurrentLesson(it)
        }
        val nextLesson = selectedSchedule?.let {
            scheduleRepository.getNextLesson(it)
        }
        _uiState.update {
            it.copy(
                selectedSchedule = selectedSchedule,
                currentLesson = currentLesson,
                nextLesson = nextLesson
            )
        }
    }
}