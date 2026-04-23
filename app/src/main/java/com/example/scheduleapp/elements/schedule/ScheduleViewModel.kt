package com.example.scheduleapp.elements.schedule

import android.content.Context
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.scheduleapp.data.classes.Lesson
import com.example.scheduleapp.data.classes.Schedule
import com.example.scheduleapp.data.classes.ScheduleMap
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
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalTime

data class ScheduleUiState(
    val hourHeight: HourHeight = HourHeight.MEDIUM,
    val lessonBlockDisplayStyle: LessonBlockDisplayStyle = LessonBlockDisplayStyle.NORMAL,
    val schedules: ScheduleMap = ScheduleMap(),
    val selectedSchedule: String? = null,
    val showAddSchedule: Boolean = false,
    val preferences: UserPreferences? = null,
    val startTime: LocalTime? = null,
    val textButtons: Boolean = false,
    val bigButton: Boolean = false
)

class ScheduleViewModel(
    val navController: NavController,
    val settingsRepository: SettingsRepository,
    val scheduleRepository: ScheduleRepository,
    val preferenceRepository: PreferenceRepository
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
                        textButtons = settings.textButtons,
                        bigButton = settings.bigButton
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
                        preferences = preferences
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
        _uiState.update {
            it.copy(
                selectedSchedule = name
            )
        }
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

    suspend fun getLessonsByTime(
        scheduleName: String,
        dayOfWeek: DayOfWeek,
        startTime: LocalTime,
        endTime: LocalTime
    ): List<Lesson> {
        return scheduleRepository.getLessonsByTime(
            scheduleName,
            dayOfWeek,
            startTime,
            endTime
        )
    }

    fun groupOverlappingLessons(lessons: List<Lesson>): List<List<Lesson>> {
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
            } else {
                groups.add(mutableListOf(lesson))
            }
        }
        return groups
    }
}