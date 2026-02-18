package com.wem.snoozy.presentation.utils

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId

@RequiresApi(Build.VERSION_CODES.O)
fun timeToMilli(timeString: String): Int {
    return timeString.split(":")[0].toInt() * 60 + timeString.split(":")[1].toInt()
}