package com.wem.snoozy.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.wem.snoozy.R
import com.wem.snoozy.presentation.entity.AlarmItemCard
import com.wem.snoozy.presentation.viewModel.MainViewModel

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

    val sheetState = rememberModalBottomSheetState()
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
                        shape = RoundedCornerShape(50)
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
                        onDismissRequest = { showBottomSheet = false },
                        sheetState = sheetState
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Button(
                                onClick = { showBottomSheet = false }
                            ) {
                                Text("Отмена")
                            }
                            Button(
                                onClick = {}
                            ) {
                                Text("Установить")
                            }
                        }
                    }
                }
            }
        }
    }
}

