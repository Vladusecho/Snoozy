package com.wem.snoozy.presentation.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.Text
import coil3.compose.AsyncImage
import com.wem.snoozy.presentation.itemCard.myTypeFamily

@Composable
fun ProfileScreen() {
    val load = remember {  mutableStateOf(true) }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (load.value) {
                CircularProgressIndicator()
            }
            AsyncImage(
                model = "https://img.icons8.com/?size=600&id=5tH5sHqq0t2q&format=png",
                contentDescription = "",
                onLoading = {
                    load.value = true
                },
                onSuccess = {
                    load.value = false
                }
            )
            Text(
                "Ведутся технические работы",
                color = MaterialTheme.colorScheme.tertiary,
                fontSize = 20.sp,
                fontFamily = myTypeFamily,
                fontWeight = FontWeight(900),
                lineHeight = 2.sp
            )
        }
    }
}