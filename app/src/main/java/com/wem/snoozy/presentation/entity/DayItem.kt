package com.wem.snoozy.presentation.entity

data class DayItem(
    val id: Int,
    val name: String,
    val checked: Boolean
)

enum class DaysName {

    SUNDAY,
    MONDAY,
    TUESDAY,
    WEDNESDAY,
    THURSDAY,
    FRIDAY,
    SATURDAY;

    fun getDisplayName(): String {
        return when (this) {
            MONDAY -> "M"
            TUESDAY -> "T"
            WEDNESDAY -> "W"
            THURSDAY -> "T"
            FRIDAY -> "F"
            SATURDAY -> "S"
            SUNDAY -> "S"
        }
    }
}
