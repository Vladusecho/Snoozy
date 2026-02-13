package com.wem.snoozy.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetDefaults
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.wem.snoozy.R
import com.wem.snoozy.presentation.entity.AlarmItemCard
import com.wem.snoozy.presentation.entity.CycleItem
import com.wem.snoozy.presentation.entity.CycleItemCard
import com.wem.snoozy.presentation.entity.myTypeFamily
import com.wem.snoozy.presentation.viewModel.MainViewModel
import com.wem.snoozy.ui.theme.SnoozyTheme
import androidx.compose.ui.input.nestedscroll.NestedScrollSource.Companion.UserInput
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.wear.compose.material.rememberSwipeableState

val myTypeFamily = FontFamily(
    Font(R.font.public_sans_regular, FontWeight(400)),
    Font(R.font.public_sans_semi_bold, FontWeight(600)),
    Font(R.font.public_sans_black, FontWeight(1000))
)

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
                            .height(150.dp)
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
                    FloatingActionButton(
                        { showBottomSheet = true },
                        modifier = Modifier.size(80.dp),
                        shape = RoundedCornerShape(50),
                        elevation = FloatingActionButtonDefaults.elevation(2.dp)
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.add_alarm_button),
                            "",
                            tint = Color.Unspecified,
                        )
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
                        onDismissRequest = { },
                        sheetState = sheetState,
                        dragHandle = {
                            // Пустой handle или кастомный, который не реагирует на свайп
                            Box(modifier = Modifier.height(0.dp))
                        }
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetContent(
    viewModel: MainViewModel,
    onCancelClick: () -> Unit
) {

    val timePickerState = rememberTimePickerState()

    Column(
        modifier = Modifier.padding(horizontal = 32.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Card(
                modifier = Modifier.size(90.dp),
                colors = CardDefaults.cardColors().copy(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                shape = CircleShape.copy(CornerSize(20))
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "0",
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
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                shape = CircleShape.copy(CornerSize(20))
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Row {
                        Text(
                            "0",
                            fontSize = 60.sp,
                            fontFamily = myTypeFamily,
                            fontWeight = FontWeight(900),
                            color = MaterialTheme.colorScheme.tertiary,
                        )
                        Text(
                            "0",
                            fontSize = 60.sp,
                            fontFamily = myTypeFamily,
                            fontWeight = FontWeight(900),
                            color = MaterialTheme.colorScheme.tertiary,
                        )
                    }
                }
            }
        }
        CycleTable(
            viewModel.cycles
        )
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            repeat(7) {
                Box(
                    Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(20))
                        .background(MaterialTheme.colorScheme.primary)
                        .padding(3.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "S",
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
                    "Upcoming alarm",
                    fontSize = 20.sp,
                    fontFamily = myTypeFamily,
                    fontWeight = FontWeight(200),
                    color = MaterialTheme.colorScheme.tertiary
                )
                Text(
                    "Tomorrow",
                    fontSize = 20.sp,
                    fontFamily = myTypeFamily,
                    fontWeight = FontWeight(900),
                    color = MaterialTheme.colorScheme.tertiary
                )
            }
            Button({}) {
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
                onClick = { onCancelClick() }
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

@Composable
fun CycleTable(
    cyclesList: List<CycleItem>
) {

//    val nestedScrollConnection = remember {
//        object : NestedScrollConnection {
//            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
//                // Блокируем вертикальный скролл для BottomSheet
//                return if (source == NestedScrollSource.UserInput) {
//                    Offset(0f, 0f) // Не передаем скролл дальше
//                } else {
//                    Offset.Zero
//                }
//            }
//        }
//    }

    Box(
        modifier = Modifier
            .padding(vertical = 16.dp)
            .size(340.dp, 240.dp)
            .clip(RoundedCornerShape(10))
            .background(MaterialTheme.colorScheme.primary)
            .border(1.dp, MaterialTheme.colorScheme.tertiary, RoundedCornerShape(10))
    ) {
        LazyColumn(
            modifier = Modifier
        ) {
            items(
                cyclesList
            ) {
                CycleItemCard(cycleItem = it)
            }
        }
    }
}