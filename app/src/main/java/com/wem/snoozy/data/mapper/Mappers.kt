package com.wem.snoozy.data.mapper

import com.wem.snoozy.data.local.AlarmItemModel
import com.wem.snoozy.domain.entity.AlarmItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

fun AlarmItemModel.toAlarmItem() = AlarmItem(
    this.id,
    this.ringDay,
    this.ringHours,
    this.timeToBed,
    this.checked,
    this.repeatDays
)

fun AlarmItem.toAlarmItemModel() = AlarmItemModel(
    this.id,
    this.ringDay,
    this.ringHours,
    this.timeToBed,
    this.checked,
    this.repeatDays
)



fun List<AlarmItemModel>.toAlarmItems() = this.map {
    it.toAlarmItem()
}

fun Flow<List<AlarmItemModel>>.toAlarmItemsFlow() = this.map { it.toAlarmItems() }