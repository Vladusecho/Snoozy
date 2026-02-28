package com.wem.snoozy.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wem.snoozy.data.repository.AlarmRepositoryImpl
import com.wem.snoozy.domain.entity.AlarmItem
import com.wem.snoozy.domain.usecase.DeleteAlarmUseCase
import com.wem.snoozy.domain.usecase.GetAllAlarmsUseCase
import com.wem.snoozy.domain.usecase.ToggleAlarmStateUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel for main screen
 *
 */
class MainViewModel() : ViewModel() {

    private val repository = AlarmRepositoryImpl()
    private val getAllAlarmsUseCase = GetAllAlarmsUseCase(repository)
    private val toggleAlarmStateUseCase = ToggleAlarmStateUseCase(repository)
    private val deleteAlarmUseCase = DeleteAlarmUseCase(repository)

    private val _state = MutableStateFlow<MainState>(MainState.Initial)
    val state = _state.asStateFlow()


    fun processCommand(command: MainCommand) {
        when (command) {
            is MainCommand.SwitchAlarm -> {
                viewModelScope.launch {
                    if (_state.value is MainState.Content) {
                        toggleAlarmStateUseCase(command.alarmItem)
                    }
                }
            }

            is MainCommand.DeleteAlarm -> {
                viewModelScope.launch {
                    if (_state.value is MainState.Content) {
                        deleteAlarmUseCase(command.alarmId)
                    }
                }
            }
        }
    }

    init {
        viewModelScope.launch {
            getAllAlarmsUseCase().collect {
                _state.value = MainState.Content(it)
            }
        }
    }
}

sealed interface MainState {

    data object Initial : MainState

    data class Content(
        val alarmList: List<AlarmItem>
    ) : MainState
}

sealed interface MainCommand {

    data class DeleteAlarm(
        val alarmId: Int
    ) : MainCommand

    data class SwitchAlarm(
        val alarmItem: AlarmItem
    ) : MainCommand
}