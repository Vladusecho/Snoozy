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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.wem.snoozy.presentation.itemCard.myTypeFamily
import com.wem.snoozy.presentation.viewModel.SettingsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
    navHostController: NavHostController
) {
    val uiState by viewModel.uiState.collectAsState()

    val textFieldCycle = remember { mutableStateOf(uiState.cycleLength) }
    val textFieldSleep = remember { mutableStateOf(uiState.sleepStartTime) }

    val sleepState = remember { mutableStateOf(uiState.sleepStartTime != "0") }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Settings",
                        fontSize = 25.sp,
                        fontFamily = myTypeFamily,
                        fontWeight = FontWeight(700),
                        color = MaterialTheme.colorScheme.tertiary,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        navHostController.popBackStack()
                        viewModel.updateCycleLength(textFieldCycle.value)
                        viewModel.updateSleepStartTime(textFieldSleep.value)
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.tertiary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors().copy(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            HorizontalDivider(Modifier, 1.dp, MaterialTheme.colorScheme.tertiary)
            Column {
                Row(
                    modifier = Modifier
                        .padding(horizontal = 30.dp)
                        .fillMaxWidth(),
                    verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        "Dark mode",
                        fontSize = 25.sp,
                        fontFamily = myTypeFamily,
                        fontWeight = FontWeight(500),
                        color = MaterialTheme.colorScheme.tertiary,
                    )
                    Switch(
                        checked = uiState.isDarkTheme,
                        onCheckedChange = {
                            viewModel.updateDarkTheme(it)
                        },
                        enabled = true,
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color(0xff8CD39C),
                            uncheckedThumbColor = Color(0xff79747E),
                            checkedTrackColor = Color(0xffAAFFBD),
                            uncheckedTrackColor = Color(0xffE6E0E9),
                            checkedBorderColor = Color(0xff8CD39C),
                            uncheckedBorderColor = Color(0xff79747E),
                            disabledCheckedTrackColor = Color.Gray,
                            disabledUncheckedTrackColor = Color.LightGray,
                            disabledCheckedThumbColor = Color.DarkGray,
                            disabledUncheckedThumbColor = Color.Gray,
                        )
                    )
                }
                HorizontalDivider(Modifier, 1.dp, MaterialTheme.colorScheme.tertiary)
                Row(
                    modifier = Modifier
                        .padding(horizontal = 30.dp, vertical = 8.dp)
                        .fillMaxWidth(),
                    verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        "Cycle length",
                        fontSize = 25.sp,
                        fontFamily = myTypeFamily,
                        fontWeight = FontWeight(500),
                        color = MaterialTheme.colorScheme.tertiary,
                    )
                    Row {
                        BasicTextField(
                            cursorBrush = Brush.verticalGradient(
                                listOf(
                                    Color.Transparent,
                                    Color.Transparent
                                )
                            ),
                            value = textFieldCycle.value,
                            onValueChange = {
                                val truncated = it.take(3)
                                textFieldCycle.value = truncated
                            },
                            modifier = Modifier
                                .width(80.dp)
                                .height(30.dp)
                                .background(
                                    color = MaterialTheme.colorScheme.surfaceVariant,
                                    shape = RoundedCornerShape(4.dp)
                                ),
                            textStyle = TextStyle(
                                fontSize = 25.sp,
                                textAlign = TextAlign.Center,
                                fontFamily = myTypeFamily,
                                fontWeight = FontWeight(500),
                                color = MaterialTheme.colorScheme.tertiary,
                            ),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            decorationBox = { innerTextField ->
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(0.dp)
                                        .clip(RoundedCornerShape(20))
                                        .background(MaterialTheme.colorScheme.surface)
                                        .border(
                                            1.dp,
                                            MaterialTheme.colorScheme.tertiary,
                                            RoundedCornerShape(20)
                                        )
                                ) {
                                    innerTextField()
                                }
                            }
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            "min.",
                            fontSize = 25.sp,
                            fontFamily = myTypeFamily,
                            fontWeight = FontWeight(500),
                            color = MaterialTheme.colorScheme.tertiary,
                        )
                    }
                }
                HorizontalDivider(Modifier, 1.dp, MaterialTheme.colorScheme.tertiary)
                Row(
                    modifier = Modifier
                        .padding(horizontal = 30.dp, vertical = 8.dp)
                        .fillMaxWidth(),
                    verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        "Sleep head-start",
                        fontSize = 25.sp,
                        fontFamily = myTypeFamily,
                        fontWeight = FontWeight(500),
                        color = MaterialTheme.colorScheme.tertiary,
                    )
                    Switch(
                        checked = sleepState.value,
                        onCheckedChange = {
                            if (!it) {
                                textFieldSleep.value = "0"
                            }
                            sleepState.value = it
                        },
                        enabled = true,
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color(0xff8CD39C),
                            uncheckedThumbColor = Color(0xff79747E),
                            checkedTrackColor = Color(0xffAAFFBD),
                            uncheckedTrackColor = Color(0xffE6E0E9),
                            checkedBorderColor = Color(0xff8CD39C),
                            uncheckedBorderColor = Color(0xff79747E),
                            disabledCheckedTrackColor = Color.Gray,
                            disabledUncheckedTrackColor = Color.LightGray,
                            disabledCheckedThumbColor = Color.DarkGray,
                            disabledUncheckedThumbColor = Color.Gray,
                        )
                    )
                }
                if (sleepState.value) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        BasicTextField(
                            cursorBrush = Brush.verticalGradient(
                                listOf(
                                    Color.Transparent,
                                    Color.Transparent
                                )
                            ),
                            value = textFieldSleep.value,
                            onValueChange = { string ->
                                textFieldSleep.value = string
                            },
                            modifier = Modifier
                                .width(80.dp)
                                .height(30.dp)
                                .background(
                                    color = MaterialTheme.colorScheme.surfaceVariant,
                                    shape = RoundedCornerShape(4.dp)
                                ),
                            textStyle = TextStyle(
                                fontSize = 25.sp,
                                textAlign = TextAlign.Center,
                                fontFamily = myTypeFamily,
                                fontWeight = FontWeight(500),
                                color = MaterialTheme.colorScheme.tertiary,
                            ),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            decorationBox = { innerTextField ->
                                // Контейнер без дополнительных отступов
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(0.dp)
                                        .clip(RoundedCornerShape(20))
                                        .background(MaterialTheme.colorScheme.surface)
                                        .border(
                                            1.dp,
                                            MaterialTheme.colorScheme.tertiary,
                                            RoundedCornerShape(20)
                                        )
                                ) {
                                    innerTextField()
                                }
                            }
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            "min.",
                            fontSize = 25.sp,
                            fontFamily = myTypeFamily,
                            fontWeight = FontWeight(500),
                            color = MaterialTheme.colorScheme.tertiary,
                        )
                    }
                }
                HorizontalDivider(Modifier, 1.dp, MaterialTheme.colorScheme.tertiary)
            }
        }
    }
}


