package com.wem.snoozy.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.wem.snoozy.domain.entity.DaysName

@Entity("alarms")
data class AlarmItemModel(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val ringDay: String,
    val ringHours: String,
    val timeToBed: String,
    val checked: Boolean,
    val repeatDays: String
)