package com.wem.snoozy.presentation.screen

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.wem.snoozy.data.local.UserPreferencesManager
import com.wem.snoozy.domain.entity.AlarmItem
import com.wem.snoozy.presentation.itemCard.CycleItemCard
import com.wem.snoozy.presentation.itemCard.myTypeFamily
import com.wem.snoozy.presentation.utils.DatePickerDialog
import com.wem.snoozy.presentation.utils.SwipeToDeleteAlarmItem
import com.wem.snoozy.presentation.utils.TimePickerDialog
import com.wem.snoozy.presentation.utils.formatDateWithRelative
import com.wem.snoozy.presentation.viewModel.MainViewModel
import java.time.LocalDate
import java.time.LocalTime
import kotlin.random.Random

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    context: Context = LocalContext.current.applicationContext,
    viewModel: MainViewModel = viewModel {
        MainViewModel(UserPreferencesManager(context))
    }
) {

    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    var showBottomSheet by remember { mutableStateOf(false) }
    val alarms = viewModel.alarms.collectAsState()

    Box(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .padding(top = 16.dp)
    ) {
        AlarmsList(
            alarms = alarms.value,
            onDeleteSwipe = { alarmId ->
                viewModel.swipeToDelete(alarmId)
            },
            onToggleAlarm = { alarmItem ->
                viewModel.toggleAlarm(alarmItem)
            }
        )
        BottomGradientShadow(
            shadowHeight = 160.dp
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 16.dp, horizontal = 32.dp),
            contentAlignment = Alignment.BottomEnd
        ) {
            AddButton {
                showBottomSheet = true
                Log.d("Add", "click")
            }
        }
//        Box(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(all = 16.dp),
//            contentAlignment = Alignment.BottomEnd
//        ) {
//            IconButton({
//                onSettingsClick()
//            }) {
//                Icon(
//                    imageVector = ImageVector.vectorResource(R.drawable.ic_settings),
//                    "settings icon",
//                    tint = MaterialTheme.colorScheme.tertiary
//                )
//            }
//        }
        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = {
                    showBottomSheet = false
                    viewModel.resetStates()
                },
                sheetState = sheetState,
                dragHandle = {
                    Box(modifier = Modifier.height(0.dp))
                },
                sheetGesturesEnabled = false,
                containerColor = MaterialTheme.colorScheme.surface,
                scrimColor = Color.Black.copy(.85f)
            ) {
                BottomSheetContent(
                    viewModel = viewModel
                ) {
                    showBottomSheet = false
                    viewModel.resetStates()
                }
            }
        }
    }
}

@Composable
fun AddButton(
    modifier: Modifier = Modifier,
    onAddClick: () -> Unit
) {
    Box(
        modifier = modifier
            .size(48.dp)
            .shadow(1.dp, RoundedCornerShape(50))
            .clip(RoundedCornerShape(50))
            .background(MaterialTheme.colorScheme.onTertiary)
            .clickable {
                onAddClick()
            },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            Icons.Default.Add,
            "Add button",
            modifier = Modifier.size(70.dp),
            tint = MaterialTheme.colorScheme.onTertiaryContainer
        )
    }
}

@Composable
fun BottomGradientShadow(
    modifier: Modifier = Modifier,
    shadowHeight: Dp = 100.dp
) {
    Box(
        modifier = modifier
            .fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        0.0f to MaterialTheme.colorScheme.background.copy(alpha = 0f),
                        0.3f to MaterialTheme.colorScheme.background.copy(alpha = 0.5f),
                        1f to MaterialTheme.colorScheme.background.copy(alpha = 1f),
                    )
                )
                .height(shadowHeight),
            contentAlignment = Alignment.Center
        ) {}
    }
}


@Composable
fun AlarmsList(
    modifier: Modifier = Modifier,
    alarms: List<AlarmItem>,
    onDeleteSwipe: (Int) -> Unit,
    onToggleAlarm: (AlarmItem) -> Unit
) {
    LazyColumn(
        modifier = modifier
    ) {
        items(
            alarms,
            key = { it.id }
        ) { alarm ->
            Box(
                modifier = Modifier.animateItem()
            ) {
                SwipeToDeleteAlarmItem(
                    alarmItem = alarm,
                    onDelete = { onDeleteSwipe(alarm.id) },
                    onToggle = { onToggleAlarm(alarm) }
                )
            }
        }
        item {
            Box(
                modifier = Modifier
                    .height(200.dp)
                    .background(Color.Transparent)
            )
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetContent(
    viewModel: MainViewModel,
    onCancelClick: () -> Unit
) {

    var showTimePicker by remember { mutableStateOf(false) }
    var selectedTime by remember { mutableStateOf(LocalTime.now()) }

    var showDatePicker by remember { mutableStateOf(false) }
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }

    val cycles = viewModel.cycles.collectAsState()

    val selectedCycleId = viewModel.selectedCycleId.collectAsState()


    if (showTimePicker) {
        TimePickerDialog(
            onDismiss = { showTimePicker = false },
            onConfirm = { hour, minute ->
                selectedTime = LocalTime.of(hour, minute)
                showTimePicker = false
            },
            onCancelClick = {
                showTimePicker = false
            }
        )
    }

    if (showDatePicker) {
        DatePickerDialog(
            initialDate = selectedDate,
            onDismiss = { showDatePicker = false },
            onConfirm = { date ->
                selectedDate = date
                showDatePicker = false
            },
            onCancelClick = {
                showDatePicker = false
            }
        )
    }

    viewModel.applyCyclesList(selectedTime)

    Column(
        modifier = Modifier
            .padding(horizontal = 24.dp)
            .padding(bottom = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AlarmTime(
            selectedTime = selectedTime,
            onTimeClick = {
                showTimePicker = true
            }
        )
        CycleTable(
            viewModel = viewModel
        )
        WeekDaysRow(
            viewModel = viewModel
        )
        AlarmDate(
            selectedDate = selectedDate,
            onApplyDateClick = {
                showDatePicker = true
            }
        )
        BottomSheetCancelAndSave(
            onSaveClick = {
                val alarmItem = AlarmItem(
                    id = Random.nextInt(0, 10000),
                    ringDay = formatDateWithRelative(selectedDate),
                    ringHours = selectedTime.hour.toString()
                        .padStart(2, '0') + ":" + selectedTime.minute.toString()
                        .padStart(2, '0'),
                    timeToBed = if (selectedCycleId.value != -1) cycles.value.first().time else "",
                    checked = true,
                    repeatDays = ""
                )
                viewModel.addNewAlarm(alarmItem)
            },
            onCancelClick = onCancelClick
        )
    }
}

@Composable
fun BottomSheetCancelAndSave(
    modifier: Modifier = Modifier,
    onCancelClick: () -> Unit,
    onSaveClick: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Button(
            onClick = { onCancelClick() },
            colors = ButtonDefaults.buttonColors().copy(
                containerColor = MaterialTheme.colorScheme.onSurface
            )
        ) {
            Text(
                "Cancel",
                fontSize = 20.sp,
                fontFamily = myTypeFamily,
                fontWeight = FontWeight(900),
                color = MaterialTheme.colorScheme.tertiary
            )
        }

        Button(
            onClick = {
                onSaveClick()
                onCancelClick()
            },
            colors = ButtonDefaults.buttonColors().copy(
                containerColor = MaterialTheme.colorScheme.onBackground
            )
        ) {
            Text(
                "Save",
                fontSize = 20.sp,
                fontFamily = myTypeFamily,
                fontWeight = FontWeight(900),
                color = Color.Black
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AlarmDate(
    modifier: Modifier = Modifier,
    selectedDate: LocalDate,
    onApplyDateClick: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 35.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(
                text = "Alarm rings",
                fontSize = 20.sp,
                fontFamily = myTypeFamily,
                fontWeight = FontWeight(200),
                color = MaterialTheme.colorScheme.tertiary
            )
            Text(
                text = formatDateWithRelative(selectedDate),
                fontSize = 20.sp,
                fontFamily = myTypeFamily,
                fontWeight = FontWeight(900),
                color = MaterialTheme.colorScheme.tertiary
            )
        }
        Button({ onApplyDateClick() }) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.AccessTime,
                    ""
                )
                Text(
                    "Schedule",
                    fontSize = 20.sp,
                    fontFamily = myTypeFamily,
                    fontWeight = FontWeight(200),
                    color = MaterialTheme.colorScheme.tertiary
                )
            }
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AlarmTime(
    modifier: Modifier = Modifier,
    selectedTime: LocalTime,
    onTimeClick: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 35.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                onTimeClick()
            },
        horizontalArrangement = Arrangement.Center
    ) {
        TimeCard(
            time = selectedTime.hour.toString().padStart(2, '0')
        )
        Text(
            ":",
            fontSize = 60.sp,
            fontFamily = myTypeFamily,
            fontWeight = FontWeight(900),
            color = MaterialTheme.colorScheme.tertiary,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        TimeCard(
            time = selectedTime.minute.toString().padStart(2, '0')
        )
    }
}

@Composable
fun TimeCard(
    modifier: Modifier = Modifier,
    containerSize: Dp = 90.dp,
    fontSize: TextUnit = 60.sp,
    time: String
) {
    Card(
        modifier = modifier.size(containerSize),
        colors = CardDefaults.cardColors().copy(
            containerColor = MaterialTheme.colorScheme.onSurface
        ),
        shape = CircleShape.copy(CornerSize(20))
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = time,
                fontSize = fontSize,
                fontFamily = myTypeFamily,
                fontWeight = FontWeight(900),
                color = MaterialTheme.colorScheme.tertiary,
            )
        }
    }
}

@Composable
fun WeekDaysRow(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel
) {

    val daysState = viewModel.days.collectAsState()

    LazyRow(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 35.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        items(
            items = daysState.value,
            key = { it.id.toString() + it.name }
        ) {
            Box(
                Modifier
                    .size(40.dp)
                    .then(
                        if (it.checked) Modifier
                            .shadow(2.dp, RoundedCornerShape(20)) else Modifier
                    )
                    .clip(RoundedCornerShape(20))
                    .background(if (it.checked) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onSurface)
                    .padding(3.dp)
                    .clickable {
                        viewModel.toggleDay(it.id)
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    it.name,
                    fontSize = 25.sp,
                    fontFamily = myTypeFamily,
                    fontWeight = FontWeight(900),
                    color = MaterialTheme.colorScheme.tertiary,
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CycleTable(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel
) {

    val cyclesListState = viewModel.cycles.collectAsState()
    val listState = rememberLazyListState()

    LaunchedEffect(cyclesListState.value) {
        if (cyclesListState.value.firstOrNull()?.checked == true) {
            listState.animateScrollToItem(0)
        }
    }

    Box(
        modifier = modifier
            .size(340.dp, 240.dp)
            .clip(RoundedCornerShape(10))
            .background(MaterialTheme.colorScheme.onSurface)
    ) {
        LazyColumn(
            modifier = Modifier,
            contentPadding = PaddingValues(vertical = 16.dp),
            state = listState
        ) {
            items(
                cyclesListState.value,
                key = { it.id.toString() + it.time }
            ) {
                Box(
                    modifier = Modifier.animateItem()
                ) {
                    CycleItemCard(it) { viewModel.toggleCycle(it.id) }
                }
            }
        }
    }
}