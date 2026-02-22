package com.wem.snoozy.presentation.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wem.snoozy.data.local.UserPreferencesManager
import com.wem.snoozy.data.repository.AlarmRepositoryImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val userPreferencesManager: UserPreferencesManager
) : ViewModel() {

    private val _state = MutableStateFlow<SettingsState>(SettingsState.Initial)
    val state = _state.asStateFlow()

    val themeState = MutableStateFlow(false)

    private val repository = AlarmRepositoryImpl()

    fun processCommand(command: SettingsCommand) {
        when (command) {
            is SettingsCommand.InputCycleLength -> {
                _state.update { prevState ->
                    if (prevState is SettingsState.Content) {
                        prevState.copy(cycleLength = command.newValue).also {
                            viewModelScope.launch {
                                userPreferencesManager.saveCycleLength(command.newValue)
                            }
                        }
                    } else {
                        prevState
                    }
                }
            }

            is SettingsCommand.InputSleepStartTime -> {
                _state.update { prevState ->
                    if (prevState is SettingsState.Content) {
                        prevState.copy(sleepStartTime = command.newValue).also {
                            viewModelScope.launch {
                                userPreferencesManager.saveSleepStartTime(command.newValue)
                            }
                        }
                    } else {
                        prevState
                    }
                }
            }

            is SettingsCommand.ToggleTheme -> {
                _state.update { previousState ->
                    if (previousState is SettingsState.Content) {
                        previousState.copy(isDarkTheme = command.isDarkTheme).also {
                            themeState.value = command.isDarkTheme
                            viewModelScope.launch {
                                userPreferencesManager.saveTheme(command.isDarkTheme)
                            }
                        }
                    } else {
                        previousState
                    }
                }
            }
        }
    }


    init {
        observePreferences()
    }

    private fun observePreferences() {
        combine(
            userPreferencesManager.darkThemeFlow,
            userPreferencesManager.cycleLengthFlow,
            userPreferencesManager.sleepStartTimeFlow
        ) { theme, cycleLength, sleepStartTime ->

            themeState.value = theme

            SettingsState.Content(
                cycleLength = cycleLength ?: "",
                sleepStartTime = sleepStartTime ?: "",
                isDarkTheme = theme
            )
        }.onEach { settingsState ->
            _state.value = settingsState
        }.launchIn(viewModelScope)
    }
}

sealed interface SettingsState {

    data object Initial : SettingsState

    data class Content(
        val cycleLength: String,
        val sleepStartTime: String,
        val isDarkTheme: Boolean
    ) : SettingsState
}

sealed interface SettingsCommand {

    data class InputCycleLength(
        val newValue: String
    ) : SettingsCommand

    data class InputSleepStartTime(
        val newValue: String
    ) : SettingsCommand

    data class ToggleTheme(
        val isDarkTheme: Boolean
    ) : SettingsCommand

}
