package com.wem.snoozy.presentation.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wem.snoozy.data.local.UserPreferencesManager
import com.wem.snoozy.data.repository.AlarmRepositoryImpl
import com.wem.snoozy.domain.entity.AlarmItem
import com.wem.snoozy.domain.entity.CycleItem
import com.wem.snoozy.domain.entity.DayItem
import com.wem.snoozy.domain.entity.DaysName
import com.wem.snoozy.domain.usecase.AddNewAlarmUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime

/**
 * ViewModel for add alarm bottom sheet
 *
 * @param userPreferencesManager manager for working with local storage
 */
class AddAlarmViewModel(
    private val userPreferencesManager: UserPreferencesManager
) : ViewModel() {

    private val repository = AlarmRepositoryImpl()
    private val addNewAlarmUseCase = AddNewAlarmUseCase(repository)

    // Add alarm screen state
    private val _state = MutableStateFlow<AddAlarmState>(AddAlarmState.Initial)
    val state = _state.asStateFlow()

    private val _cyclesList = MutableStateFlow<List<CycleItem>>(emptyList())

    private val initDaysList = listOf(
        DayItem(0, DaysName.SUNDAY.getDisplayName(), false),
        DayItem(1, DaysName.MONDAY.getDisplayName(), false),
        DayItem(2, DaysName.TUESDAY.getDisplayName(), false),
        DayItem(3, DaysName.WEDNESDAY.getDisplayName(), false),
        DayItem(4, DaysName.THURSDAY.getDisplayName(), false),
        DayItem(5, DaysName.FRIDAY.getDisplayName(), false),
        DayItem(6, DaysName.SATURDAY.getDisplayName(), false),
    )
    private val _daysList = MutableStateFlow(initDaysList)
    val daysList = _daysList.asStateFlow()


    val cycleLength = userPreferencesManager.cycleLengthFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = "90"
    )
    val sleepStartTime = userPreferencesManager.sleepStartTimeFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = "0"
    )

    fun processCommand(command: AddAlarmCommand) {
        when (command) {
            is AddAlarmCommand.SaveAlarm -> {
                viewModelScope.launch {
                    addNewAlarmUseCase(command.alarmItem)
                }
            }

            is AddAlarmCommand.SelectCycle -> {
                _state.update { prevState ->
                    toggleCycle(command.cycleId)
                    if (prevState is AddAlarmState.Content) {
                        prevState.copy(
                            cyclesList = _cyclesList.value
                        )
                    } else {
                        prevState
                    }
                }
            }

            is AddAlarmCommand.SelectTime -> {
                _state.update { prevState ->
                    applyCyclesList(command.time)
                    if (prevState is AddAlarmState.Content) {
                        prevState.copy(
                            selectedTime = command.time,
                            cyclesList = _cyclesList.value
                        )
                    } else {
                        prevState
                    }
                }
            }

            is AddAlarmCommand.SelectDay -> {
                _state.update { prevState ->
                    toggleDay(command.id)
                    if (prevState is AddAlarmState.Content) {
                        prevState.copy(
                            daysList = _daysList.value
                        )
                    } else {
                        prevState
                    }
                }
            }

            is AddAlarmCommand.SelectDate -> {
                _state.update { prevState ->
                    if (prevState is AddAlarmState.Content) {
                        prevState.copy(
                            selectedDate = command.date,
                        )
                    } else {
                        prevState
                    }
                }
            }
        }
    }


    init {
        applyCyclesList(LocalTime.now())
        _state.value = AddAlarmState.Content(
            selectedTime = LocalTime.now(),
            cyclesList = _cyclesList.value,
            daysList = initDaysList,
            selectedDate = LocalDate.now()
        ).also {
            Log.d("AddAlarmViewModel", applyCyclesList(LocalTime.now()).toString())
        }
    }

    val selectedCycleId = MutableStateFlow(-1)

    fun toggleDay(id: Int) {
        val currentList = _daysList.value.toMutableList()
        currentList.replaceAll { if (it.id == id) it.copy(checked = !it.checked) else it }
        _daysList.value = currentList
    }

    fun toggleCycle(id: Int) {
        val currentList = _cyclesList.value.toMutableList()
        if (currentList.find { it.checked && it.id == id } != null) {
            currentList.replaceAll { it.copy(checked = false) }
            Log.d("Cycles", "All cycles unchecked")
            selectedCycleId.value = -1
        } else if (currentList.find { it.checked && it.id != id } != null) {
            currentList.replaceAll { it.copy(checked = false) }
            currentList.replaceAll { if (it.id == id) it.copy(checked = !it.checked) else it }
            selectedCycleId.value = id
        } else {
            currentList.replaceAll { if (it.id == id) it.copy(checked = !it.checked) else it }
            selectedCycleId.value = id
        }
        _cyclesList.value = currentList.sortedWith(
            compareByDescending<CycleItem> { it.checked }
                .thenByDescending { it.id }
        ).toMutableList()
    }

    fun applyCyclesList(selectedTime: LocalTime) {

        var selectedTime = selectedTime

        val newItems = mutableListOf<CycleItem>()

        for (i in 1..7) {
            val minusMinutes = selectedTime
                .minusMinutes(cycleLength.value!!.toLong())
                .minusMinutes(sleepStartTime.value!!.toLong())
            val hours = minusMinutes.hour.toString()
            val minutes = minusMinutes.minute.toString().padStart(2, '0')
            val cycleItem = CycleItem(i, "$hours:$minutes", i.toString(), checked = false)
            newItems.add(cycleItem)
            selectedTime = selectedTime.minusMinutes(cycleLength.value!!.toLong())
        }

        if (_cyclesList.value.isNotEmpty() && (newItems.toSet() == _cyclesList.value.map {
                it.copy(
                    checked = false
                )
            }
                .toSet())) {
            return
        }

        _cyclesList.value = newItems.sortedByDescending { it.id }.toMutableList()

    }
}


sealed interface AddAlarmCommand {

    data class SaveAlarm(
        val alarmItem: AlarmItem
    ) : AddAlarmCommand

    data class SelectCycle(
        val cycleId: Int
    ) : AddAlarmCommand

    data class SelectTime(
        val time: LocalTime
    ) : AddAlarmCommand

    data class SelectDay(
        val id: Int
    ) : AddAlarmCommand

    data class SelectDate(
        val date: LocalDate
    ) : AddAlarmCommand
}

sealed interface AddAlarmState {

    data object Initial : AddAlarmState

    data class Content(
        val selectedTime: LocalTime,
        val selectedDate: LocalDate,
        val daysList: List<DayItem>,
        val cyclesList: List<CycleItem>
    ) : AddAlarmState
}



