package com.wem.snoozy.presentation.viewModel

import androidx.lifecycle.ViewModel
import com.wem.snoozy.presentation.entity.AlarmItem

class MainViewModel : ViewModel() {

    val alarms = listOf(
        AlarmItem(0, "Monday", "8:00", "12:00"),
        AlarmItem(0, "Thursday", "8:00", "12:00"),
        AlarmItem(0, "Saturday", "8:00", "12:00"),
        AlarmItem(0, "Wednesday", "8:00", "12:00"),
        AlarmItem(0, "Friday", "8:00", "12:00"),
        AlarmItem(0, "Monday", "8:00", "12:00"),
        AlarmItem(0, "Monday", "8:00", "12:00"),
        AlarmItem(0, "Monday", "8:00", "12:00"),
        AlarmItem(0, "Monday", "8:00", "12:00"),
    )
}