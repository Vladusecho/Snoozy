package com.wem.snoozy.presentation.utils

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wem.snoozy.presentation.itemCard.myTypeFamily
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerDialog(
    initialDate: LocalDate,
    onDismiss: () -> Unit,
    onConfirm: (LocalDate) -> Unit,
    onCancelClick: () -> Unit
) {
    val todayStart = LocalDate.now().toEpochDay() * 24 * 60 * 60 * 1000

    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = initialDate.toEpochDay() * 24 * 60 * 60 * 1000,
        selectableDates = object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                return utcTimeMillis >= todayStart
            }
        }
    )

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(
                onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        val date = LocalDate.ofEpochDay(millis / (24 * 60 * 60 * 1000))
                        onConfirm(date)
                    }
                },
                colors = ButtonDefaults.buttonColors().copy(
                    containerColor = MaterialTheme.colorScheme.onBackground
                ),
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    "Apply",
                    fontSize = 20.sp,
                    fontFamily = myTypeFamily,
                    fontWeight = FontWeight(900),
                    color = Color.Black
                )
            }
        },
        dismissButton = {
            Button(
                onClick = { onCancelClick() },
                colors = ButtonDefaults.buttonColors().copy(
                    containerColor = MaterialTheme.colorScheme.onSurface
                ),
                modifier = Modifier.padding(vertical = 16.dp)
            ) {
                Text(
                    "Cancel",
                    fontSize = 20.sp,
                    fontFamily = myTypeFamily,
                    fontWeight = FontWeight(900),
                    color = MaterialTheme.colorScheme.tertiary
                )
            }
        },
        colors = DatePickerDefaults.colors().copy(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        DatePicker(
            showModeToggle = false,
            state = datePickerState,
            colors = DatePickerDefaults.colors().copy(
                containerColor = MaterialTheme.colorScheme.surface,
                titleContentColor = MaterialTheme.colorScheme.tertiary,
                headlineContentColor = MaterialTheme.colorScheme.tertiary,
                weekdayContentColor = MaterialTheme.colorScheme.tertiary,
                navigationContentColor = MaterialTheme.colorScheme.tertiary,
                yearContentColor = MaterialTheme.colorScheme.tertiary,
                dayContentColor = MaterialTheme.colorScheme.tertiary,
                todayContentColor = MaterialTheme.colorScheme.tertiary
            )
        )
    }
}

fun formatDateWithRelative(date: LocalDate): String {
    val today = LocalDate.now()
    val formatter = DateTimeFormatter.ofPattern("MM.dd.yyyy").withLocale(Locale.ENGLISH)

    return when (date) {
        today -> "Today"
        today.plusDays(1) -> "Tomorrow"
        today.plusDays(2) -> "Next day"
        else -> date.format(formatter)
    }
}

fun String.formatStringToDate(): LocalDate {
    val today = LocalDate.now()
    val formatter = DateTimeFormatter.ofPattern("MM.dd.yyyy").withLocale(Locale.ENGLISH)

    return when (this) {
        "Today" -> today
        "Tomorrow" -> today.plusDays(1)
        "Next day" -> today.plusDays(2)
        else -> LocalDate.parse(this, formatter)
    }
}