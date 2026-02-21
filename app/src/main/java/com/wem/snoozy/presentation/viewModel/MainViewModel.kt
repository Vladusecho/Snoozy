package com.wem.snoozy.presentation.viewModel

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wem.snoozy.data.local.UserPreferencesManager
import com.wem.snoozy.data.repository.AlarmRepositoryImpl
import com.wem.snoozy.domain.entity.AlarmItem
import com.wem.snoozy.domain.entity.CycleItem
import com.wem.snoozy.domain.entity.DayItem
import com.wem.snoozy.domain.entity.DaysName
import com.wem.snoozy.domain.usecase.AddNewAlarmUseCase
import com.wem.snoozy.domain.usecase.DeleteAlarmUseCase
import com.wem.snoozy.domain.usecase.GetAllAlarmsUseCase
import com.wem.snoozy.domain.usecase.ToggleAlarmStateUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalTime

class MainViewModel(
    private val userPreferencesManager: UserPreferencesManager
) : ViewModel() {

    private val repository = AlarmRepositoryImpl()
    private val getAllAlarmsUseCase = GetAllAlarmsUseCase(repository)
    private val toggleAlarmStateUseCase = ToggleAlarmStateUseCase(repository)
    private val addNewAlarmUseCase = AddNewAlarmUseCase(repository)
    private val deleteAlarmUseCase = DeleteAlarmUseCase(repository)

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

    val cycles = MutableStateFlow(listOf<CycleItem>())

    val selectedCycleId = MutableStateFlow(-1)

    val alarms = getAllAlarmsUseCase().stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = emptyList()
    )

    val days = MutableStateFlow(
        listOf(
            DayItem(0, DaysName.SUNDAY.getDisplayName(), false),
            DayItem(1, DaysName.MONDAY.getDisplayName(), false),
            DayItem(2, DaysName.TUESDAY.getDisplayName(), false),
            DayItem(3, DaysName.WEDNESDAY.getDisplayName(), false),
            DayItem(4, DaysName.THURSDAY.getDisplayName(), false),
            DayItem(5, DaysName.FRIDAY.getDisplayName(), false),
            DayItem(6, DaysName.SATURDAY.getDisplayName(), false),
        )
    )

    fun toggleDay(id: Int) {
        val currentList = days.value.toMutableList()
        currentList.replaceAll { if (it.id == id) it.copy(checked = !it.checked) else it }
        days.value = currentList
    }

    fun toggleAlarm(alarmItem: AlarmItem) {
        viewModelScope.launch {
            toggleAlarmStateUseCase(alarmItem)
        }
    }

    fun toggleCycle(id: Int) {
        val currentList = cycles.value.toMutableList()
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
        cycles.value = currentList.sortedWith(
            compareByDescending<CycleItem> { it.checked }
                .thenByDescending { it.id }
        ).toMutableList()
    }

    fun resetStates() {
        cycles.update {
            emptyList()
        }
        days.update { oldList ->
            val newList = oldList.map { dayItem ->
                if (dayItem.checked) dayItem.copy(checked = false) else dayItem
            }
            newList
        }
        selectedCycleId.update {
            DEFAULT_VALUE
        }
        Log.d("myreset", days.value.toString())
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun applyCyclesList(selectedTime: LocalTime) {

        var selectedTime = selectedTime

        val newItems = mutableListOf<CycleItem>()

        for (i in FIRST_CYCLE..LAST_CYCLE) {
            val minusMinutes = selectedTime.minusMinutes(cycleLength.value!!.toLong()).minusMinutes(sleepStartTime.value!!.toLong())
            val hours = minusMinutes.hour.toString()
            val minutes = minusMinutes.minute.toString().padStart(2, '0')
            val cycleItem = CycleItem(i, "$hours:$minutes", i.toString(), checked = false)
            newItems.add(cycleItem)
            selectedTime = selectedTime.minusMinutes(cycleLength.value!!.toLong())
        }

        if (cycles.value.isNotEmpty() && (newItems.toSet() == cycles.value.map { it.copy(checked = false) }
                .toSet())) {
            return
        }

        cycles.value = newItems.sortedByDescending { it.id }.toMutableList()

    }

    fun addNewAlarm(alarmItem: AlarmItem) {
        viewModelScope.launch {
            addNewAlarmUseCase(alarmItem)
        }
    }

    fun swipeToDelete(alarmId: Int) {
        viewModelScope.launch {
            deleteAlarmUseCase(alarmId)
        }
    }

    companion object {

        const val DEFAULT_VALUE = -1

        const val FIRST_CYCLE = 1
        const val LAST_CYCLE = 7
    }
}