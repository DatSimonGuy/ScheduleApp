package com.example.scheduleapp.elements.schedule

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.scheduleapp.data.classes.ColorTheme
import com.example.scheduleapp.data.classes.Lesson
import com.example.scheduleapp.data.classes.LessonType
import com.example.scheduleapp.data.classes.RefreshType
import com.example.scheduleapp.data.classes.Schedule
import com.example.scheduleapp.data.classes.ScheduleMap
import com.example.scheduleapp.data.classes.ScheduleSortMode
import com.example.scheduleapp.data.repository.PreferenceRepository
import com.example.scheduleapp.data.repository.ScheduleRepository
import com.example.scheduleapp.data.repository.SettingsRepository
import com.example.scheduleapp.data.repository.UserPreferences
import com.example.scheduleapp.elements.schedule.timetable.HourHeight
import com.example.scheduleapp.elements.schedule.timetable.LessonBlockDisplayStyle
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime

data class ScheduleUiState(
    val hourHeight: HourHeight = HourHeight.MEDIUM,
    val lessonBlockDisplayStyle: LessonBlockDisplayStyle = LessonBlockDisplayStyle.NORMAL,
    val schedules: ScheduleMap = ScheduleMap(),
    val selectedSchedule: String? = null,
    val showAddSchedule: Boolean = false,
    val preferences: UserPreferences? = null,
    val startTime: LocalTime? = null,
    val bigButton: Boolean = false,
    val refreshType: RefreshType = RefreshType.AUTOMATIC,
    val customTheme: Map<LessonType, Color> = emptyMap(),
    val currentTheme: ColorTheme = ColorTheme.DEFAULT,
    val sortMode: ScheduleSortMode = ScheduleSortMode.ALPHABETICAL,
    val scheduleOrder: List<String> = emptyList()
)

class ScheduleViewModel(
    val navController: NavController,
    val settingsRepository: SettingsRepository,
    val scheduleRepository: ScheduleRepository,
    val preferenceRepository: PreferenceRepository,
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
                        lessonBlockDisplayStyle = LessonBlockDisplayStyle.valueOf(settings.lessonBlockDisplayStyle),
                        showAddSchedule = settings.addScheduleInFab,
                        selectedSchedule = settings.defaultSchedule,
                        startTime = LocalTime.parse(settings.startTime ?: "00:00"),
                        bigButton = settings.bigButton,
                        refreshType = RefreshType.valueOf(settings.refreshType),
                        currentTheme = ColorTheme.valueOf(settings.currentTheme),
                        sortMode = ScheduleSortMode.valueOf(settings.sortMode)
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
                        preferences = preferences,
                        scheduleOrder = preferences.scheduleOrder?.let {
                            Json.decodeFromString(it)
                        } ?: emptyList()
                    )
                }
            }
        }
    }

    suspend fun addSchedule(scheduleName: String, schedule: Schedule, context: Context): String? {
        val error = scheduleRepository.saveSchedule(scheduleName, schedule, context)
        error?.let {
            return it
        }
        setCurrentSchedule(scheduleName)
        return null
    }

    suspend fun updateSchedule(scheduleName: String, schedule: Schedule): String? {
        return scheduleRepository.updateSchedule(scheduleName, schedule)
    }

    fun setCurrentSchedule(name: String) {
        _uiState.update { currentState ->
            val currentOrder = currentState.scheduleOrder.ifEmpty {
                currentState.schedules.schedules.keys.toList()
            }
            val fullList = (currentOrder + currentState.schedules.schedules.keys).distinct()
            val newOrder = fullList.toMutableList().apply {
                remove(name)
                addFirst(name)
            }
            viewModelScope.launch {
                preferenceRepository.setScheduleOrder(Json.encodeToString(newOrder))
            }
            currentState.copy(
                scheduleOrder = newOrder,
                selectedSchedule = name
            )
        }
    }

    @Composable
    fun sortedSchedules(): List<Pair<String, Schedule>> {
        val ui by uiState.collectAsStateWithLifecycle()
        val displayList = remember(ui.schedules.schedules, ui.sortMode) {
            val list = ui.schedules.schedules.toList()
            when (ui.sortMode) {
                ScheduleSortMode.ALPHABETICAL -> list.sortedBy { it.first }
                ScheduleSortMode.ALPHABETICAL_DESC -> list.sortedByDescending { it.first }
                ScheduleSortMode.RECENTLY_USED -> ui.scheduleOrder.map { it to ui.schedules[it]!! }
            }
        }
        return displayList
    }

    fun setDefaultSchedule(name: String) {
        viewModelScope.launch {
            settingsRepository.setDefaultSchedule(name)
        }
    }

    suspend fun addNewLesson(dayOfWeek: DayOfWeek, lesson: Lesson, context: Context): String? {
        return scheduleRepository.addLesson(
            uiState.value.selectedSchedule ?: "",
            dayOfWeek,
            lesson,
            context
        )
    }

    fun updateLesson(scheduleName: String, lesson: Lesson, oldDay: DayOfWeek, newDay: DayOfWeek, context: Context) {
        viewModelScope.launch {
            scheduleRepository.updateLesson(scheduleName, lesson, oldDay, newDay, context)
        }
    }

    fun removeLesson(scheduleName: String, lessonId: String, dayOfWeek: DayOfWeek, context: Context) {
        viewModelScope.launch {
            scheduleRepository.removeLesson(scheduleName, lessonId, dayOfWeek, context)
        }
    }

    suspend fun getLesson(scheduleName: String, id: String): Lesson? {
        return scheduleRepository.getLesson(scheduleName, id)
    }

    fun setRecentChatId(chatId: Long?) {
        viewModelScope.launch {
            preferenceRepository.setRecentChatId(chatId)
        }
    }

    fun groupOverlappingLessons(lessons: List<Lesson>, date: LocalDate): List<List<Lesson>> {
        if (lessons.isEmpty()) return emptyList()

        val sorted = lessons.sortedBy { it.startTime }
        val groups = mutableListOf<MutableList<Lesson>>()

        for (lesson in sorted) {
            val group = groups.find { g ->
                g.any {
                    (lesson.startTime < it.endTime && lesson.endTime > it.startTime)
                }
            }
            if (group != null) {
                group.add(lesson)
                group.sortBy { !it.isActive(date) }
            } else {
                groups.add(mutableListOf(lesson))
            }
        }
        return groups
    }
}