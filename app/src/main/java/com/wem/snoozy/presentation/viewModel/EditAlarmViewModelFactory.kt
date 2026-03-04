package com.wem.snoozy.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.wem.snoozy.data.local.UserPreferencesManager
import com.wem.snoozy.domain.entity.AlarmItem

class EditAlarmViewModelFactory(
    private val alarmItem: AlarmItem,
    private val userPreferencesManager: UserPreferencesManager
) : ViewModelProvider.Factory{

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EditAlarmViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return EditAlarmViewModel(
                alarmItem = alarmItem,
                userPreferencesManager = userPreferencesManager
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}