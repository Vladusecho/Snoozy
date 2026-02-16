package com.wem.snoozy.presentation.screen

import com.wem.snoozy.domain.entity.AlarmItem

sealed interface MainScreens {

    data class Content(val alarmList: List<AlarmItem>) : MainScreens

    object Initial : MainScreens
}