package com.wem.snoozy.presentation.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wem.snoozy.data.local.UserPreferencesManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val userPreferencesManager: UserPreferencesManager
) : ViewModel() {

    data class UiState(
        val cycleLength: String = "90",
        val sleepStartTime: String = "0",
        val isDarkTheme: Boolean = true,
    )

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    init {
        loadUserPreferences()
    }

    private fun loadUserPreferences() {
        viewModelScope.launch {
            userPreferencesManager.darkThemeFlow.collect { theme ->
                Log.d("mysett", theme.toString())
                _uiState.update { it.copy(isDarkTheme = theme) }
            }
        }
        viewModelScope.launch {
            userPreferencesManager.cycleLengthFlow.collect { value ->
                _uiState.update { it.copy(
                    cycleLength = value ?: ""
                ) }
            }
        }
        viewModelScope.launch {
            userPreferencesManager.sleepStartTimeFlow.collect { time ->
                _uiState.update { it.copy(
                    sleepStartTime = time ?: ""
                ) }
            }
        }
    }

    fun updateSleepStartTime(time: String) {
        viewModelScope.launch {
            userPreferencesManager.saveSleepStartTime(time)
        }
    }

    fun updateCycleLength(value: String) {
        viewModelScope.launch {
            userPreferencesManager.saveCycleLength(value)
        }
    }

    fun updateDarkTheme(isDark: Boolean) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(isDarkTheme = isDark)
            }
            userPreferencesManager.saveTheme(isDark)
        }
    }

    fun resetSleepStartTime() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(sleepStartTime = "0")
            }
            userPreferencesManager.saveSleepStartTime("0")
        }
    }

    fun clearPreferences() {
        viewModelScope.launch {
            userPreferencesManager.clearAll()
        }
    }
}