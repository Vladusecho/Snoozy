package com.wem.snoozy.data.remote.dto

import com.google.gson.annotations.SerializedName

data class RemoteAlarmDto(
    @SerializedName("id")
    val id: Long,
    @SerializedName("ownerId")
    val ownerId: Long,
    @SerializedName("title")
    val title: String,
    @SerializedName("alarmTime")
    val alarmTime: String,
    @SerializedName("enabled")
    val enabled: Boolean,
    @SerializedName("repeatPattern")
    val repeatPattern: String?,
    @SerializedName("difficultyLevel")
    val difficultyLevel: Int,
    @SerializedName("isOverslept")
    val isOverslept: Boolean = false
)
