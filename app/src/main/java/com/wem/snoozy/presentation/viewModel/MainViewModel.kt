package com.wem.snoozy.presentation.viewModel

import androidx.lifecycle.ViewModel
import com.wem.snoozy.presentation.entity.AlarmItem
import kotlinx.coroutines.flow.MutableStateFlow

class MainViewModel : ViewModel() {

    val alarms = MutableStateFlow(listOf(
        AlarmItem(0, "Monday", "8:00", "12:00", checked = false),
        AlarmItem(1, "Thursday", "8:00", "12:00", checked = true),
        AlarmItem(2, "Saturday", "8:00", "12:00", checked = true),
        AlarmItem(3, "Wednesday", "8:00", "12:00", checked = false),
        AlarmItem(4, "Friday", "8:00", "12:00", checked = false),
        AlarmItem(5, "Monday", "8:00", "12:00",checked = false),
        AlarmItem(6, "Monday", "8:00", "12:00",checked = true),
        AlarmItem(7, "Monday", "8:00", "12:00",checked = true),
        AlarmItem(8, "Monday", "8:00", "12:00",checked = true),
    ))

    fun toggleAlarm(id: Int) {
        val currentList = alarms.value.toMutableList()
        val index = currentList.indexOfFirst { it.id == id }
        if (index != -1) {
            currentList[index] = currentList[index].copy(
                checked = !currentList[index].checked
            )
            alarms.value = currentList
        }
    }
}