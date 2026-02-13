package com.wem.snoozy.presentation.entity

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
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
    Font(R.font.public_sans_semi_bold, FontWeight(600)),
    Font(R.font.public_sans_black, FontWeight(1000))
)

@Composable
fun AlarmItemCard(
    modifier: Modifier = Modifier,
    alarmItem: AlarmItem,
    onToggle: () -> Unit = {}
) {

    val checked = alarmItem.checked

    val cardColor =
        if (checked) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
    val textCardColor =
        if (checked) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSecondary
    val timeTextColor = if (checked) MaterialTheme.colorScheme.tertiary else Color.White.copy(alpha = .6f)
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .then(
                if (checked) {
                    Modifier.shadow(5.dp, RoundedCornerShape(20))
                } else {
                    Modifier
                }
            ),
        colors = CardDefaults.cardColors().copy(
            containerColor = cardColor
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
                    color = textCardColor
                )
                Text(
                    text = "Time to bed: " + alarmItem.timeToBed,
                    fontSize = 20.sp,
                    fontFamily = myTypeFamily,
                    color = textCardColor
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
                    fontWeight = if (checked) FontWeight(900) else FontWeight(600),
                    color = timeTextColor
                )
                Switch(
                    checked = checked,
                    onCheckedChange = {
                        onToggle()
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
        }
    }
}

@Preview
@Composable
fun AlarmItemCardPreview() {
    SnoozyTheme {
        AlarmItemCard(alarmItem = AlarmItem(0, "Monday", "7:00", "22:00", checked = false))
    }
}