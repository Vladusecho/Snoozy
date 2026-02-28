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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
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
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.viewModelFactory
import com.wem.snoozy.data.local.UserPreferencesManager
import com.wem.snoozy.domain.entity.AlarmItem
import com.wem.snoozy.domain.entity.CycleItem
import com.wem.snoozy.presentation.itemCard.CycleItemCard
import com.wem.snoozy.presentation.itemCard.myTypeFamily
import com.wem.snoozy.presentation.utils.DatePickerDialog
import com.wem.snoozy.presentation.utils.SwipeToDeleteAlarmItem
import com.wem.snoozy.presentation.utils.TimePickerDialog
import com.wem.snoozy.presentation.utils.formatDateWithRelative
import com.wem.snoozy.presentation.viewModel.AddAlarmCommand
import com.wem.snoozy.presentation.viewModel.AddAlarmState
import com.wem.snoozy.presentation.viewModel.AddAlarmViewModel
import com.wem.snoozy.presentation.viewModel.AddAlarmViewModelFactory
import com.wem.snoozy.presentation.viewModel.MainCommand
import com.wem.snoozy.presentation.viewModel.MainState
import com.wem.snoozy.presentation.viewModel.MainViewModel
import com.wem.snoozy.presentation.viewModel.SettingsViewModel
import java.time.LocalDate
import java.time.LocalTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: MainViewModel = viewModel()
) {


    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    var showBottomSheet by remember { mutableStateOf(false) }

    val state = viewModel.state.collectAsState()
    val currentState = state.value

    Box(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .padding(top = 16.dp)
    ) {
        when (currentState) {
            MainState.Initial -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is MainState.Content -> {
                AlarmsList(
                    alarms = currentState.alarmList,
                    onDeleteSwipe = { alarmId ->
                        viewModel.processCommand(MainCommand.DeleteAlarm(alarmId))
                    },
                    onToggleAlarm = { alarmItem ->
                        viewModel.processCommand(MainCommand.SwitchAlarm(alarmItem))
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
                    }
                }
            }
        }
        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = {
                    showBottomSheet = false
//                    addAlarmViewModel.processCommand(AddAlarmCommand.ResetAddSheet)
                },
                sheetState = sheetState,
                dragHandle = {
                    Box(modifier = Modifier.height(0.dp))
                },
                sheetGesturesEnabled = false,
                containerColor = MaterialTheme.colorScheme.surface,
                scrimColor = Color.Black.copy(.85f)
            ) {
                BottomSheetContent {
                    showBottomSheet = false
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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetContent(
    onCancelClick: () -> Unit
) {

    val context = LocalContext.current.applicationContext

    val viewModelStore = remember { ViewModelStore() }

    val factory = remember(UserPreferencesManager(context)) {
        AddAlarmViewModelFactory(
            UserPreferencesManager(context)
        )
    }

    val addAlarmViewModel: AddAlarmViewModel = remember(viewModelStore) {
        ViewModelProvider(viewModelStore, factory)[AddAlarmViewModel::class.java]
    }


    DisposableEffect(Unit) {
        onDispose {
            viewModelStore.clear()
            Log.d("MainViewModel", "finished")
        }
    }

    var showTimePicker by remember { mutableStateOf(false) }

    var showDatePicker by remember { mutableStateOf(false) }

    val selectedCycleId = addAlarmViewModel.selectedCycleId.collectAsState()

    val state = addAlarmViewModel.state.collectAsState()
    val currentState = state.value


    if (showTimePicker) {
        TimePickerDialog(
            onDismiss = {
                showTimePicker = false
            },
            onConfirm = { time ->
                addAlarmViewModel.processCommand(AddAlarmCommand.SelectTime(time))
                showTimePicker = false
            },
            onCancelClick = {
                showTimePicker = false
            }
        )
    }

    if (showDatePicker) {
        DatePickerDialog(
            initialDate = LocalDate.now(),
            onDismiss = { showDatePicker = false },
            onConfirm = { date ->
                addAlarmViewModel.processCommand(AddAlarmCommand.SelectDate(date))
                showDatePicker = false
            },
            onCancelClick = {
                showDatePicker = false
            }
        )
    }


    Column(
        modifier = Modifier
            .padding(horizontal = 24.dp)
            .padding(bottom = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when (currentState) {
            AddAlarmState.Initial -> {
            }

            is AddAlarmState.Content -> {
                AlarmTime(
                    selectedTime = currentState.selectedTime,
                    onTimeClick = {
                        showTimePicker = true
                    }
                )
                CycleTable(
                    cycles = currentState.cyclesList,
                    addAlarmViewModel = addAlarmViewModel
                )
                WeekDaysRow(
                    viewModel = addAlarmViewModel
                )
                AlarmDate(
                    selectedDate = currentState.selectedDate,
                    onApplyDateClick = {
                        showDatePicker = true
                    }
                )
                BottomSheetCancelAndSave(
                    onSaveClick = {
                        addAlarmViewModel.processCommand(AddAlarmCommand.SaveAlarm(
                            AlarmItem(
                                id = 0,
                                ringDay = formatDateWithRelative(currentState.selectedDate),
                                ringHours = currentState.selectedTime.hour.toString()
                                    .padStart(2, '0')
                                        + ":" + currentState.selectedTime.minute.toString()
                                    .padStart(2, '0'),
                                timeToBed = if (selectedCycleId.value != -1) currentState.cyclesList.first().time else "",
                                checked = true,
                                repeatDays = ""
                            )
                        ))
                    },
                    onCancelClick = onCancelClick
                )
            }
        }
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
    viewModel: AddAlarmViewModel
) {

    val daysState = viewModel.daysList.collectAsState()

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

@Composable
fun CycleTable(
    modifier: Modifier = Modifier,
    cycles: List<CycleItem>,
    addAlarmViewModel: AddAlarmViewModel
) {

    val listState = rememberLazyListState()

    LaunchedEffect(cycles) {
        if (cycles.firstOrNull()?.checked == true) {
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
                cycles,
                key = { it.id.toString() + it.time }
            ) {
                Box(
                    modifier = Modifier.animateItem()
                ) {
                    CycleItemCard(it) {
                        addAlarmViewModel.processCommand(AddAlarmCommand.SelectCycle(it.id))
                    }
                }
            }
        }
    }
}