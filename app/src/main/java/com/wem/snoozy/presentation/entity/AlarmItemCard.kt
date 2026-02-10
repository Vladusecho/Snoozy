package com.wem.snoozy.presentation.entity

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wem.snoozy.R
import com.wem.snoozy.ui.theme.SnoozyTheme

val myTypeFamily = FontFamily(
    Font(R.font.public_sans_regular, FontWeight(400)),
    Font(R.font.public_sans_black, FontWeight(1000))
)

@Composable
fun AlarmItemCard(
    modifier: Modifier = Modifier,
    alarmItem: AlarmItem
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors().copy(
            containerColor = MaterialTheme.colorScheme.primary
        ),
        shape = CircleShape.copy(CornerSize(20))
    ) {
        Column(
            modifier = modifier
                .padding(horizontal = 32.dp, vertical = 16.dp)
        ) {
            Row(
                modifier = modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = alarmItem.dayOfWeek,
                    fontSize = 20.sp,
                    fontFamily = myTypeFamily,
                    color = MaterialTheme.colorScheme.onPrimary
                )
                Text(
                    text = "Time to bed: " + alarmItem.timeToBed,
                    fontSize = 20.sp,
                    fontFamily = myTypeFamily,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
            Row(
                modifier = modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = alarmItem.hours,
                    fontSize = 60.sp,
                    fontFamily = myTypeFamily,
                    fontWeight = FontWeight(900),
                    color = Color.Black
                )
                Switch(
                    checked = false,
                    {}
                )
            }
        }
    }
}

@Preview
@Composable
fun AlarmItemCardPreview() {
    SnoozyTheme {
        AlarmItemCard(alarmItem =  AlarmItem(0, "Monday", "7:00", "22:00"))
    }
}