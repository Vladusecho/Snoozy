package com.wem.snoozy.presentation.viewModel

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.wem.snoozy.presentation.entity.AlarmItem
import com.wem.snoozy.presentation.entity.CycleItem
import kotlinx.coroutines.flow.MutableStateFlow
import java.time.LocalTime
import java.time.format.DateTimeFormatter

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

    val cycles = MutableStateFlow(mutableListOf<CycleItem>())

    @RequiresApi(Build.VERSION_CODES.O)
    val savedTime = MutableStateFlow<LocalTime>(LocalTime.now())

    fun toggleAlarm(id: Int) {
        val currentList = alarms.value.toMutableList()
        currentList.replaceAll { if (it.id == id) it.copy(checked = !it.checked) else it}
        alarms.value = currentList
    }

    fun toggleCycle(id: Int) {
        val currentList = cycles.value.toMutableList()
        if (currentList.find { it.checked && it.id == id } != null) {
            currentList.replaceAll { it.copy(checked = false) }
            Log.d("Cycles", "All cycles unchecked")
        } else if (currentList.find { it.checked && it.id != id } != null){
            currentList.replaceAll { it.copy(checked = false) }
            currentList.replaceAll { if (it.id == id) it.copy(checked = !it.checked) else it }
//            Log.d("Cycles", "Cycle $id checked")
        } else {
            currentList.replaceAll { if (it.id == id) it.copy(checked = !it.checked) else it }
        }
        cycles.value = currentList.sortedWith(
            compareByDescending<CycleItem> { it.checked }
                .thenByDescending { it.id }
        ).toMutableList()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun applyCyclesList(selectedTime: LocalTime) {

        var selectedTime = selectedTime

        val newItems = mutableListOf<CycleItem>()

        for (i in 1..7) {
            val minusMinutes = selectedTime.minusMinutes(90)
            val hours = minusMinutes.hour.toString()
            val minutes = minusMinutes.minute.toString().padStart(2, '0')
            val cycleItem = CycleItem(i, "$hours:$minutes", i.toString(), checked = false)
            newItems.add(cycleItem)
            selectedTime = selectedTime.minusMinutes(90)
        }

        if (cycles.value.isNotEmpty() && (newItems.toSet() == cycles.value.map { it.copy(checked = false) }.toSet())) {
            return
        }

        cycles.value.clear()

        cycles.value = newItems.sortedByDescending { it.id }.toMutableList()

    }
}