package com.wem.snoozy.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface Dao {

    @Insert
    suspend fun addAlarm(alarmItemModel: AlarmItemModel)

    @Query("SELECT * FROM alarms")
    fun getAlarms(): Flow<List<AlarmItemModel>>

    @Query("UPDATE alarms SET checked = :isChecked WHERE id = :alarmId")
    suspend fun updateCheckedStatus(alarmId: Int, isChecked: Boolean)

    @Query("DELETE FROM alarms WHERE id= :alarmId")
    suspend fun deleteAlarm(alarmId: Int)
}