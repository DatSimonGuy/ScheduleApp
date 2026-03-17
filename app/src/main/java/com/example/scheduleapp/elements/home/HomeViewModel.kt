package com.example.scheduleapp.elements.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scheduleapp.data.classes.Lesson
import com.example.scheduleapp.data.repository.PreferenceRepository
import com.example.scheduleapp.data.repository.ScheduleRepository
import com.example.scheduleapp.data.repository.SettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


data class HomeUiState(
    var selectedSchedule: String? = null
)

class HomeViewModel (
    private val settingsRepository: SettingsRepository,
    private val scheduleRepository: ScheduleRepository,
    private val preferenceRepository: PreferenceRepository
) : ViewModel() {
    val _uiState = MutableStateFlow(HomeUiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            settingsRepository.settingsFlow.collect { settings ->
                _uiState.update {
                    it.copy(
                        selectedSchedule = settings.defaultSchedule
                    )
                }
            }
        }
    }

    suspend fun getNextLesson(): Lesson? {
        uiState.value.selectedSchedule?.let {
            return scheduleRepository.getNextLesson(it)
        }
        return null
    }

    suspend fun getCurrentLesson(): Lesson? {
        uiState.value.selectedSchedule?.let {
            return scheduleRepository.getCurrentLesson(it)
        }
        return null
    }

}