package com.wem.snoozy.presentation.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.wem.snoozy.R
import com.wem.snoozy.presentation.entity.AlarmItemCard
import com.wem.snoozy.presentation.entity.CycleItemCard
import com.wem.snoozy.presentation.entity.myTypeFamily
import com.wem.snoozy.presentation.utils.DatePickerDialog
import com.wem.snoozy.presentation.utils.TimePickerDialog
import com.wem.snoozy.presentation.utils.formatDateWithRelative
import com.wem.snoozy.presentation.viewModel.MainViewModel
import java.time.LocalDate
import java.time.LocalTime

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = viewModel()
) {

    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    var showBottomSheet by remember { mutableStateOf(false) }
    val alarms = viewModel.alarms.collectAsState()

    Scaffold { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
                .padding(top = 16.dp)
        ) {
            LazyColumn {
                items(
                    alarms.value,
                    key = { it.id }
                ) { alarm ->
                    AlarmItemCard(
                        alarmItem = alarm
                    ) {
                        viewModel.toggleAlarm(alarm.id)
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
            Box(
                modifier = Modifier.fillMaxSize(),
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
                        .padding(bottom = 60.dp, top = 100.dp),
                    contentAlignment = Alignment.Center
                ) {
                    AddButton {
                        showBottomSheet = true
                    }
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    contentAlignment = Alignment.BottomEnd
                ) {
                    IconButton({}) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.ic_settings),
                            "",
                            tint = MaterialTheme.colorScheme.tertiary
                        )
                    }
                }
                if (showBottomSheet) {
                    ModalBottomSheet(
                        onDismissRequest = { showBottomSheet = false },
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
                        }
                    }
                }
            }
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

    var showDialog by remember { mutableStateOf(false) }
    var selectedTime by remember { mutableStateOf(LocalTime.now()) }

    var showDatePicker by remember { mutableStateOf(false) }
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }

    val daysState = viewModel.days.collectAsState()


    if (showDialog) {
        TimePickerDialog(
            onDismiss = { showDialog = false },
            onConfirm = { hour, minute ->
                selectedTime = LocalTime.of(hour, minute)
                showDialog = false
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
            }
        )
    }

    viewModel.applyCyclesList(selectedTime)

    Column(
        modifier = Modifier.padding(horizontal = 24.dp, vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) {
                    showDialog = true
                },
            horizontalArrangement = Arrangement.Center
        ) {
            Card(
                modifier = Modifier.size(90.dp),
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
                        selectedTime.hour.toString().padStart(2, '0'),
                        fontSize = 60.sp,
                        fontFamily = myTypeFamily,
                        fontWeight = FontWeight(900),
                        color = MaterialTheme.colorScheme.tertiary,
                    )
                }
            }
            Text(
                ":",
                fontSize = 60.sp,
                fontFamily = myTypeFamily,
                fontWeight = FontWeight(900),
                color = MaterialTheme.colorScheme.tertiary,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Card(
                modifier = Modifier.size(90.dp),
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
                        selectedTime.minute.toString().padStart(2, '0'),
                        fontSize = 60.sp,
                        fontFamily = myTypeFamily,
                        fontWeight = FontWeight(900),
                        color = MaterialTheme.colorScheme.tertiary,
                    )
                }
            }
        }
        CycleTable(
            viewModel = viewModel
        )
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            items(
                daysState.value
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
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 32.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    "Alarm rings",
                    fontSize = 20.sp,
                    fontFamily = myTypeFamily,
                    fontWeight = FontWeight(200),
                    color = MaterialTheme.colorScheme.tertiary
                )
                Text(
                    formatDateWithRelative(selectedDate),
                    fontSize = 20.sp,
                    fontFamily = myTypeFamily,
                    fontWeight = FontWeight(900),
                    color = MaterialTheme.colorScheme.tertiary
                )
            }
            Button(
                {
                    showDatePicker = true
                },
            ) {
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
        Row(
            modifier = Modifier
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
                onClick = {},
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
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CycleTable(
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
        modifier = Modifier
            .padding(vertical = 16.dp)
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

@Composable
fun AddButton(
    modifier: Modifier = Modifier,
    onAddClick: () -> Unit
) {
    Box(
        modifier = modifier
            .size(85.dp)
            .shadow(1.dp, RoundedCornerShape(50))
            .clip(RoundedCornerShape(50))
            .background(MaterialTheme.colorScheme.onTertiary)
            .clickable { onAddClick() },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            Icons.Default.Add,
            "",
            modifier = Modifier.size(70.dp),
            tint = MaterialTheme.colorScheme.onTertiaryContainer
        )
    }
}