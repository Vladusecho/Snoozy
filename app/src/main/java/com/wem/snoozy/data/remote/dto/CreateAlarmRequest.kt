package com.wem.snoozy.data.remote.dto

import com.google.gson.annotations.SerializedName

data class CreateAlarmRequest(
    @SerializedName("title")
    val title: String,
    @SerializedName("alarmTime")
    val alarmTime: String,
    @SerializedName("enabled")
    val enabled: Boolean,
    @SerializedName("repeatPattern")
    val repeatPattern: String,
    @SerializedName("difficultyLevel")
    val difficultyLevel: Int
)
