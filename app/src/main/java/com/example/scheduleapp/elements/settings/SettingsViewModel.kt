package com.example.scheduleapp.elements.settings

import Destination
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scheduleapp.data.classes.Schedule
import com.example.scheduleapp.data.classes.ScheduleMap
import com.example.scheduleapp.data.repository.PreferenceRepository
import com.example.scheduleapp.data.repository.ScheduleRepository
import com.example.scheduleapp.data.repository.SettingsRepository
import com.example.scheduleapp.elements.schedule.timetable.HourHeight
import com.example.scheduleapp.elements.schedule.timetable.LessonBlockDisplayStyle
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalTime


data class Settings(
    val hourHeight: HourHeight = HourHeight.MEDIUM,
    val lessonBlockDisplayStyle: LessonBlockDisplayStyle = LessonBlockDisplayStyle.NORMAL,
    val schedules: ScheduleMap = ScheduleMap(),
    var addScheduleInFab: Boolean = false,
    var defaultSchedule: String? = null,
    var recentChatId: Long? = null,
    var startHour: LocalTime? = null,
    var selectedStartPage: Destination? = null,
    var textButtons: Boolean = false
)

class SettingsViewModel(
    private val repository: SettingsRepository,
    private val scheduleRepository: ScheduleRepository,
    private val preferenceRepository: PreferenceRepository
) : ViewModel() {
    val _uiState = MutableStateFlow(Settings())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.settingsFlow.collect { settings ->
                _uiState.update { currentState ->
                    currentState.copy(
                        hourHeight = HourHeight.valueOf(settings.hourHeight),
                        lessonBlockDisplayStyle = LessonBlockDisplayStyle.valueOf(settings.lessonBlockDisplayStyle),
                        addScheduleInFab = settings.addScheduleInFab,
                        defaultSchedule = settings.defaultSchedule,
                        startHour = LocalTime.parse(settings.startTime ?: "00:00"),
                        selectedStartPage = Destination.main.firstOrNull { it.id == settings.startPage },
                        textButtons = settings.textButtons
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
        viewModelScope.launch {
            preferenceRepository.preferences.collect { preferences ->
                _uiState.update { currentState ->
                    currentState.copy(
                        recentChatId = preferences.recentChatId
                    )
                }
            }
        }
    }

    fun onTextButtonsChange(value: Boolean) {
        viewModelScope.launch {
            repository.setTextButtons(value);
        }
    }

    fun onSelectedStartPageChange(value: Int?) {
        viewModelScope.launch {
            repository.setStartPage(value)
        }
    }

    fun onHourHeightChange(hourHeight: HourHeight) {
        viewModelScope.launch {
            repository.setHourHeight(hourHeight)
        }
    }

    fun onLessonBlockDisplayStyleChange(style: LessonBlockDisplayStyle) {
        viewModelScope.launch {
            repository.setLessonBlockDisplayStyle(style)
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

    fun onStartHourChange(value: LocalTime) {
        viewModelScope.launch {
            repository.setStartHour(value)
        }
    }

    suspend fun addNewSchedule(name: String, schedule: Schedule = Schedule()): String? {
        val error = scheduleRepository.saveSchedule(name, schedule)
        error?.let {
            return it
        }
        return null
    }

    suspend fun importSchedules(chatId: Long, list: List<String>): String? {
        val error = scheduleRepository.importSchedules(chatId, list)
        error?.let {
            return it
        }
        return null
    }

    suspend fun getSchedules(chatId: Long): Pair<List<String>?, String?> {
        return scheduleRepository.getAvailableSchedules(chatId)
    }

    suspend fun removeSchedule(name: String, schedule: Schedule, localOnly: Boolean): String? {
        val error = scheduleRepository.removeSchedule(name, schedule, localOnly)
        error?.let { return it }
        if (uiState.value.schedules.count() < 1) {
            onDefaultScheduleChange(null)
        }
        return null
    }

    suspend fun editSchedule(oldName: String, newName: String, schedule: Schedule): String? {
        val error = scheduleRepository.editSchedule(oldName, newName, schedule)
        return error
    }

    fun setScheduleFormPreferences(chatId: Long? = null) {
        viewModelScope.launch {
            preferenceRepository.setRecentChatId(chatId)
        }
    }
}