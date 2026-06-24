package com.example.scheduleapp.elements.settings

import Destination
import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import com.example.scheduleapp.data.classes.ColorTheme
import com.example.scheduleapp.data.classes.LessonType
import com.example.scheduleapp.data.classes.RefreshType
import com.example.scheduleapp.data.classes.Schedule
import com.example.scheduleapp.data.classes.ScheduleMap
import com.example.scheduleapp.data.classes.ScheduleSortMode
import com.example.scheduleapp.data.repository.PreferenceRepository
import com.example.scheduleapp.data.repository.ScheduleRepository
import com.example.scheduleapp.data.repository.SettingsRepository
import com.example.scheduleapp.elements.schedule.parts.timetable.HourHeight
import com.example.scheduleapp.elements.schedule.parts.timetable.LessonBlockDisplayStyle
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
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
    var bigButton: Boolean = false,
    var refreshType: RefreshType = RefreshType.AUTOMATIC,
    val customTheme: Map<LessonType, Color> = emptyMap(),
    val currentTheme: ColorTheme = ColorTheme.DEFAULT,
    val scheduleSortMode: ScheduleSortMode = ScheduleSortMode.ALPHABETICAL,
    val scheduleOrder: List<String> = emptyList(),
    val showWeekends: Boolean = true,
    val showTimeBar: Boolean = true
)

class SettingsViewModel(
    private val repository: SettingsRepository,
    private val scheduleRepository: ScheduleRepository,
    private val preferenceRepository: PreferenceRepository,
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
                        bigButton = settings.bigButton,
                        refreshType = RefreshType.valueOf(settings.refreshType),
                        currentTheme = ColorTheme.valueOf(settings.currentTheme),
                        scheduleSortMode = ScheduleSortMode.valueOf(settings.sortMode),
                        showWeekends = settings.showWeekends,
                        showTimeBar = settings.showTimeBar
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
                        recentChatId = preferences.recentChatId,
                        scheduleOrder = preferences.scheduleOrder?.let {
                            Json.decodeFromString(it)
                        } ?: emptyList()
                    )
                }
            }
        }
    }

    fun updateScheduleOrder() {
        _uiState.update { currentState ->
            val currentOrder = currentState.scheduleOrder.ifEmpty {
                currentState.schedules.schedules.keys.toList()
            }.filter { uiState.value.schedules[it] != null }
            val fullList = (currentOrder + currentState.schedules.schedules.keys).distinct()
            viewModelScope.launch {
                preferenceRepository.setScheduleOrder(Json.encodeToString(fullList))
            }
            currentState.copy(
                scheduleOrder = fullList,
            )
        }
    }

    fun onShowTimeBarChange(value: Boolean) {
        viewModelScope.launch {
            repository.setShowTimeBar(value)
        }
    }

    fun onShowWeekendsChange(value: Boolean) {
        viewModelScope.launch {
            repository.setShowWeekends(value)
        }
    }

    fun onScheduleSortModeChange(value: ScheduleSortMode) {
        viewModelScope.launch {
            repository.setScheduleSortMode(value)
        }
    }

    fun onCurrentThemeChange(value: ColorTheme) {
        viewModelScope.launch {
            repository.setCurrentTheme(value)
        }
    }

    fun onRefreshTypeChange(value: RefreshType) {
        viewModelScope.launch {
            repository.setRefreshType(value)
        }
    }

    fun onBigButtonChange(value: Boolean) {
        viewModelScope.launch {
            repository.setBigButton(value);
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

    suspend fun addNewSchedule(name: String, schedule: Schedule = Schedule(), context: Context): String? {
        val error = scheduleRepository.saveSchedule(name, schedule, context)
        updateScheduleOrder()
        error?.let {
            return it
        }
        return null
    }

    suspend fun importSchedules(chatId: Long, list: List<String>, context: Context): String? {
        val error = scheduleRepository.importSchedules(chatId, list, context)
        updateScheduleOrder()
        error?.let {
            return it
        }
        return null
    }

    suspend fun getSchedules(chatId: Long): Pair<List<String>?, String?> {
        return scheduleRepository.getAvailableSchedules(chatId)
    }

    @Composable
    fun sortedSchedules(): List<Pair<String, Schedule?>> {
        val ui by uiState.collectAsStateWithLifecycle()
        val displayList = remember(ui.schedules.schedules, ui.scheduleSortMode) {
            val list = ui.schedules.schedules.toList()
            when (ui.scheduleSortMode) {
                ScheduleSortMode.ALPHABETICAL -> list.sortedBy { it.first }
                ScheduleSortMode.ALPHABETICAL_DESC -> list.sortedByDescending { it.first }
                ScheduleSortMode.RECENTLY_USED -> ui.scheduleOrder.map { it to _uiState.value.schedules[it] }
            }
        }
        return displayList
    }

    suspend fun removeSchedule(name: String, schedule: Schedule, localOnly: Boolean): String? {
        val error = scheduleRepository.removeSchedule(name, schedule, localOnly)
        error?.let { return it }
        if (uiState.value.schedules.count() < 1) {
            onDefaultScheduleChange(null)
        }
        updateScheduleOrder()
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