package com.wem.snoozy.data.mapper

import android.os.Build
import androidx.annotation.RequiresApi
import com.wem.snoozy.data.local.AlarmItemModel
import com.wem.snoozy.data.local.GroupItemModel
import com.wem.snoozy.data.remote.dto.CreateAlarmRequest
import com.wem.snoozy.data.remote.dto.GroupResponse
import com.wem.snoozy.data.remote.dto.MemberDto
import com.wem.snoozy.data.remote.dto.RemoteAlarmDto
import com.wem.snoozy.domain.entity.AlarmItem
import com.wem.snoozy.domain.entity.GroupItem
import com.wem.snoozy.domain.entity.Member
import com.wem.snoozy.presentation.utils.timeToMilli
import com.wem.snoozy.presentation.utils.formatStringToDate
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

fun AlarmItemModel.toAlarmItem() = AlarmItem(
    id = this.id,
    ringDay = this.ringDay,
    ringHours = this.ringHours,
    timeToBed = this.timeToBed,
    checked = this.checked,
    repeatDays = this.repeatDays,
    isOverslept = this.isOverslept,
    remoteId = this.remoteId
)

@RequiresApi(Build.VERSION_CODES.O)
fun AlarmItem.toAlarmItemModel() = AlarmItemModel(
    id = this.id,
    ringDay = this.ringDay,
    ringHours = this.ringHours,
    ringHoursMillis = timeToMilli(this.ringHours),
    timeToBed = this.timeToBed,
    checked = this.checked,
    repeatDays = this.repeatDays,
    isOverslept = this.isOverslept,
    remoteId = this.remoteId
)

fun List<AlarmItemModel>.toAlarmItems() = this.map { it.toAlarmItem() }
fun Flow<List<AlarmItemModel>>.toAlarmItemsFlow() = this.map { it.toAlarmItems() }

// Группы
fun GroupItemModel.toGroupItem() = GroupItem(
    id = this.id,
    name = this.name,
    membersCount = this.membersCount,
    contactIds = this.contactIds,
    avatarUri = this.avatarUri
)

fun GroupItem.toGroupItemModel() = GroupItemModel(
    id = this.id,
    name = this.name,
    membersCount = this.membersCount,
    contactIds = this.contactIds,
    avatarUri = this.avatarUri
)

fun List<GroupItemModel>.toGroupItems() = this.map { it.toGroupItem() }

fun GroupResponse.toGroupItem() = GroupItem(
    id = this.id,
    name = this.name,
    ownerId = this.ownerId,
    avatarUri = this.url,
    membersCount = this.members.size,
    members = this.members.map { Member(it.id, it.username, it.avatarUrl) }
)

// Будильники (Remote)
fun RemoteAlarmDto.toAlarmItem(): AlarmItem {
    // alarmTime приходит в формате "2026-04-05T07:30:00"
    // Извлекаем дату (первые 10 символов)
    val datePart = this.alarmTime.split("T").first()
    
    return AlarmItem(
        id = 0, 
        ringDay = datePart,
        ringHours = this.alarmTime.split("T").last().take(5),
        timeToBed = "00:00",
        checked = this.enabled,
        repeatDays = mapPatternToIds(this.repeatPattern),
        isOverslept = this.isOverslept,
        remoteId = this.id
    )
}

fun AlarmItem.toCreateRequest(): CreateAlarmRequest {
    val datePart = try {
        this.ringDay.formatStringToDate().toString()
    } catch (e: Exception) {
        "2026-01-01"
    }
    return CreateAlarmRequest(
        title = "Alarm",
        alarmTime = "${datePart}T${this.ringHours}:00",
        enabled = this.checked,
        repeatPattern = mapRepeatIdsToPattern(this.repeatDays),
        difficultyLevel = 1
    )
}

fun mapRepeatIdsToPattern(repeatDays: String): String {
    if (repeatDays.isBlank()) return "ONCE"
    val ids = repeatDays.split(",").map { it.trim() }
    if (ids.size == 7) return "DAILY"
    
    val idToPatternMap = mapOf(
        "1" to "MON", "2" to "TUE", "3" to "WED", "4" to "THU", "5" to "FRI", "6" to "SAT", "7" to "SUN"
    )
    return ids.mapNotNull { idToPatternMap[it] }.joinToString(",")
}

fun mapPatternToIds(pattern: String?): String {
    if (pattern == null || pattern == "ONCE") return ""
    if (pattern == "DAILY") return "1,2,3,4,5,6,7"
    
    val patternToIdMap = mapOf(
        "MON" to "1", "TUE" to "2", "WED" to "3", "THU" to "4", "FRI" to "5", "SAT" to "6", "SUN" to "7"
    )
    return pattern.split(",").mapNotNull { patternToIdMap[it.trim()] }.joinToString(",")
}

fun Flow<List<GroupItemModel>>.toGroupItemsFlow() = this.map { list -> list.map { it.toGroupItem() } }
