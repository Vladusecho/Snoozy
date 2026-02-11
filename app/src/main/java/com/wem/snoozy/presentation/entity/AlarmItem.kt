package com.wem.snoozy.presentation.entity

data class AlarmItem(
    val id: Int,
    val dayOfWeek: String,
    val hours: String,
    val timeToBed: String,
    val checked: Boolean
)