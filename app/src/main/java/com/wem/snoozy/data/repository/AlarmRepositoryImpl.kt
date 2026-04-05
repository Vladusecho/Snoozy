package com.wem.snoozy.data.repository

import android.util.Log
import com.wem.snoozy.data.alarm.AlarmScheduler
import com.wem.snoozy.data.local.Dao
import com.wem.snoozy.data.mapper.*
import com.wem.snoozy.data.remote.ApiService
import com.wem.snoozy.domain.entity.AlarmItem
import com.wem.snoozy.domain.entity.GroupItem
import com.wem.snoozy.domain.repository.AlarmRepository
import com.wem.snoozy.presentation.utils.formatStringToDate
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AlarmRepositoryImpl @Inject constructor(
    private val dao: Dao,
    private val apiService: ApiService,
    private val alarmScheduler: AlarmScheduler
) : AlarmRepository {

    override suspend fun addNewAlarm(alarmItem: AlarmItem) {
        // 1. Сохраняем локально (без remoteId)
        val localId = dao.addAlarm(alarmItem.toAlarmItemModel()).toInt()
        var savedAlarm = alarmItem.copy(id = localId)
        
        // Планируем и получаем обновленный объект (с возможной корректировкой даты)
        if (savedAlarm.checked) {
            savedAlarm = alarmScheduler.schedule(savedAlarm)
            // Обновляем в БД, если дата изменилась
            if (savedAlarm.ringDay != alarmItem.ringDay) {
                dao.addAlarm(savedAlarm.toAlarmItemModel())
            }
        }

        // 2. Отправляем в API
        try {
            val response = apiService.createAlarm(savedAlarm.toCreateRequest())
            if (response.isSuccessful) {
                val remoteAlarm = response.body()
                remoteAlarm?.let {
                    // Обновляем локальную запись, добавляя remoteId
                    dao.addAlarm(savedAlarm.copy(remoteId = it.id).toAlarmItemModel())
                }
            }
        } catch (e: Exception) {
            Log.e("AlarmRepo", "Error creating remote alarm", e)
        }
    }

    override suspend fun editAlarm(alarmItem: AlarmItem) {
        var updatedAlarm = alarmItem
        if (alarmItem.checked) {
            updatedAlarm = alarmScheduler.schedule(alarmItem)
        } else {
            alarmScheduler.cancelAlarm(alarmItem.id)
            alarmScheduler.cancelBedtimeNotification(alarmItem.id)
        }

        dao.addAlarm(updatedAlarm.toAlarmItemModel())
        
        updatedAlarm.remoteId?.let { remoteId ->
            try {
                val datePart = try {
                    updatedAlarm.ringDay.formatStringToDate().toString()
                } catch (e: Exception) {
                    "2026-01-01"
                }

                val updateMap = mutableMapOf<String, Any>(
                    "title" to "Alarm",
                    "alarmTime" to "${datePart}T${updatedAlarm.ringHours}:00",
                    "enabled" to updatedAlarm.checked,
                    "repeatPattern" to mapRepeatIdsToPattern(updatedAlarm.repeatDays)
                )
                apiService.updateAlarm(remoteId, updateMap)
            } catch (e: Exception) {
                Log.e("AlarmRepo", "Error updating remote alarm", e)
            }
        }
    }

    override fun getAllAlarms(): Flow<List<AlarmItem>> {
        return dao.getAlarms().toAlarmItemsFlow()
    }

    override suspend fun toggleAlarmState(alarmItem: AlarmItem) {
        val newCheckedState = !alarmItem.checked
        var updatedAlarm = alarmItem.copy(checked = newCheckedState)
        
        if (newCheckedState) {
            updatedAlarm = alarmScheduler.schedule(updatedAlarm)
        } else {
            alarmScheduler.cancelAlarm(updatedAlarm.id)
            alarmScheduler.cancelBedtimeNotification(updatedAlarm.id)
        }

        // Обновляем локальную БД (включая возможную новую дату)
        dao.addAlarm(updatedAlarm.toAlarmItemModel())
        
        updatedAlarm.remoteId?.let { remoteId ->
            try {
                val datePart = try {
                    updatedAlarm.ringDay.formatStringToDate().toString()
                } catch (e: Exception) {
                    "2026-01-01"
                }
                apiService.updateAlarm(remoteId, mapOf(
                    "enabled" to newCheckedState,
                    "alarmTime" to "${datePart}T${updatedAlarm.ringHours}:00"
                ))
            } catch (e: Exception) {
                Log.e("AlarmRepo", "Error toggling remote alarm", e)
            }
        }
    }

    override suspend fun deleteAlarm(alarmId: Int) {
        val localAlarm = dao.getAlarmById(alarmId)
        localAlarm?.remoteId?.let { remoteId ->
            try {
                apiService.deleteAlarm(remoteId)
            } catch (e: Exception) {
                Log.e("AlarmRepo", "Error deleting remote alarm", e)
            }
        }
        
        dao.deleteAlarm(alarmId)
        alarmScheduler.cancelAlarm(alarmId)
        alarmScheduler.cancelBedtimeNotification(alarmId)
    }

    // Группы
    override suspend fun addGroup(groupItem: GroupItem) {
        dao.addGroup(groupItem.toGroupItemModel())
    }

    override fun getGroups(): Flow<List<GroupItem>> {
        return dao.getGroups().toGroupItemsFlow()
    }

    override suspend fun deleteGroup(groupId: Int) {
        dao.deleteGroup(groupId)
    }

    suspend fun syncWithRemote() {
        try {
            val response = apiService.getAlarms()
            if (response.isSuccessful) {
                val remoteAlarms = response.body() ?: emptyList()
                remoteAlarms.forEach { remote ->
                    // Проверяем, есть ли уже такой будильник локально
                    val existingLocal = dao.getAlarmByRemoteId(remote.id)
                    
                    val alarmItem = remote.toAlarmItem()
                    val model = if (existingLocal != null) {
                        // Если есть, обновляем существующий ID, чтобы не плодить дубликаты
                        alarmItem.copy(id = existingLocal.id).toAlarmItemModel()
                    } else {
                        // Если нет, создаем новый
                        alarmItem.toAlarmItemModel()
                    }
                    dao.addAlarm(model)
                }
            }
        } catch (e: Exception) {
            Log.e("AlarmRepo", "Sync failed", e)
        }
    }
}
