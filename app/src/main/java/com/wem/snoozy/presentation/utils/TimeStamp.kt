package com.wem.snoozy.presentation.utils

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId

@RequiresApi(Build.VERSION_CODES.O)
fun timeToTimestamp(timeString: String): Long {
    val localTime = LocalTime.parse(timeString)

    val now = LocalDateTime.now()
    val dateTime = LocalDateTime.of(
        now.year,
        now.month,
        now.dayOfMonth,
        localTime.hour,
        localTime.minute
    )
    return dateTime
        .atZone(ZoneId.systemDefault())
        .toInstant()
        .toEpochMilli()
}