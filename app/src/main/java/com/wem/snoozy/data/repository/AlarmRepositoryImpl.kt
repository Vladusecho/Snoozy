package com.wem.snoozy.data.repository

import android.os.Build
import androidx.annotation.RequiresApi
import com.wem.snoozy.SnoozyApp
import com.wem.snoozy.data.local.AlarmDatabase
import com.wem.snoozy.data.mapper.toAlarmItemModel
import com.wem.snoozy.data.mapper.toAlarmItemsFlow
import com.wem.snoozy.domain.entity.AlarmItem
import com.wem.snoozy.domain.repository.AlarmRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class AlarmRepositoryImpl : AlarmRepository {

    override suspend fun addNewAlarm(alarmItem: AlarmItem) {
        AlarmDatabase.getInstance(SnoozyApp.getContext()).dao().addAlarm(alarmItem.toAlarmItemModel())
    }

    override suspend fun editAlarm(alarmItem: AlarmItem) {
        AlarmDatabase.getInstance(SnoozyApp.getContext()).dao().addAlarm(alarmItem.toAlarmItemModel())
    }

    override fun getAllAlarms(): Flow<List<AlarmItem>> {
        return AlarmDatabase.getInstance(SnoozyApp.getContext()).dao().getAlarms().toAlarmItemsFlow()
    }

    override suspend fun toggleAlarmState(alarmItem: AlarmItem) {
        AlarmDatabase.getInstance(SnoozyApp.getContext()).dao().updateCheckedStatus(alarmItem.id, !alarmItem.checked)
    }

    override suspend fun deleteAlarm(alarmId: Int) {
        AlarmDatabase.getInstance(SnoozyApp.getContext()).dao().deleteAlarm(alarmId)
    }
}