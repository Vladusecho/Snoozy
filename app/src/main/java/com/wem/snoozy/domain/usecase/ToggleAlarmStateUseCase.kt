package com.wem.snoozy.domain.usecase

import com.wem.snoozy.domain.entity.AlarmItem
import com.wem.snoozy.domain.repository.AlarmRepository

class ToggleAlarmStateUseCase(
    private val repository: AlarmRepository
) {

    suspend operator fun invoke(alarmItem: AlarmItem) {
        repository.toggleAlarmState(alarmItem)
    }
}