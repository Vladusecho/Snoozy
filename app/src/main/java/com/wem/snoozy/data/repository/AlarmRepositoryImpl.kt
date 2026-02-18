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

    private val initAlarmsFlow = MutableStateFlow(listOf(
        AlarmItem(0, "Monday", "08:00", "12:00", checked = false, ""),
        AlarmItem(1, "Thursday", "08:00", "12:00", checked = true, ""),
        AlarmItem(2, "Saturday", "08:00", "12:00", checked = true, ""),
        AlarmItem(3, "Wednesday", "08:00", "12:00", checked = false, ""),
        AlarmItem(4, "Friday", "08:00", "12:00", checked = false, ""),
        AlarmItem(5, "Monday", "08:00", "12:00",checked = false, ""),
        AlarmItem(6, "Monday", "08:00", "12:00",checked = true, ""),
        AlarmItem(7, "Monday", "08:00", "12:00",checked = true, ""),
        AlarmItem(8, "Monday", "08:00", "12:00",checked = true, ""),
    ))

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun addNewAlarm(alarmItem: AlarmItem) {
        AlarmDatabase.getInstance(SnoozyApp.getContext()).dao().addAlarm(alarmItem.toAlarmItemModel())
    }

    override fun editAlarm(alarmId: Int) {
        TODO("Not yet implemented")
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