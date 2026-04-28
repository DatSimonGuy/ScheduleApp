package com.example.scheduleapp.elements.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.scheduleapp.data.repository.PreferenceRepository
import com.example.scheduleapp.data.repository.ScheduleRepository
import com.example.scheduleapp.data.repository.SettingsRepository

class HomeViewModelFactory(
    private val settingsRepository: SettingsRepository,
    private val scheduleRepository: ScheduleRepository,
    private val preferenceRepository: PreferenceRepository,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HomeViewModel(settingsRepository, scheduleRepository, preferenceRepository) as T
    }
}