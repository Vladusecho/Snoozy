package com.wem.snoozy.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.wem.snoozy.R
import com.wem.snoozy.presentation.entity.AlarmItemCard
import com.wem.snoozy.presentation.viewModel.MainViewModel

val myTypeFamily = FontFamily(
    Font(R.font.public_sans_regular, FontWeight(400)),
    Font(R.font.public_sans_black, FontWeight(1000))
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = viewModel()
) {
    Scaffold { paddingValues ->
        val alarms = viewModel.alarms
        Box(
            modifier = Modifier.padding(paddingValues)
        ) {
            LazyColumn {
                items(
                    alarms
                ) { alarm ->
                    AlarmItemCard(
                        alarmItem = alarm
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
                        .padding(vertical = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    IconButton(
                        {},
                        modifier = Modifier.size(80.dp)
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.add_alarm_button),
                            "",
                            tint = Color.Unspecified
                        )
                    }
                }
            }
        }
    }
}
