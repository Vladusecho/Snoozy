package com.wem.snoozy.domain.usecase

import com.wem.snoozy.domain.repository.AlarmRepository

class DeleteAlarmUseCase(
    private val repository: AlarmRepository
) {

    suspend operator fun invoke(alarmId: Int) {
        repository.deleteAlarm(alarmId)
    }
}