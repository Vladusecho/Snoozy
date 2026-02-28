package com.wem.snoozy.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.wem.snoozy.data.local.UserPreferencesManager

class AddAlarmViewModelFactory(
    private val userPreferencesManager: UserPreferencesManager
) : ViewModelProvider.Factory{

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddAlarmViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AddAlarmViewModel(
                userPreferencesManager = userPreferencesManager
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}