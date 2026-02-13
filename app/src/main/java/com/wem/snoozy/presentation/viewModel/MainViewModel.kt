package com.wem.snoozy.presentation.viewModel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.wem.snoozy.presentation.entity.AlarmItem
import com.wem.snoozy.presentation.entity.CycleItem
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

    val cycles = mutableListOf(
        CycleItem(0, "22:00", "7"),
        CycleItem(1, "20:30", "7"),
        CycleItem(2, "19:00", "7"),
        CycleItem(3, "17:30", "7"),
        CycleItem(4, "16:00", "7")
    )

    fun toggleAlarm(id: Int) {
        val currentList = alarms.value.toMutableList()
        currentList.replaceAll { if (it.id == id) it.copy(checked = !it.checked) else it}
        alarms.value = currentList
    }
}