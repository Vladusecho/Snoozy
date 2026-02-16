package com.wem.snoozy.domain.usecase

import com.wem.snoozy.domain.repository.AlarmRepository

class EditAlarmUseCase(
    private val repository: AlarmRepository
) {

    operator fun invoke(alarmId: Int) {
        repository.editAlarm(alarmId)
    }
}