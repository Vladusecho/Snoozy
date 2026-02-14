package com.wem.snoozy.presentation.entity

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wem.snoozy.ui.theme.SnoozyTheme

@Composable
fun CycleItemCard(
    cycleItem: CycleItem,
    onCycleClick: () -> Unit
) {

    val cardColor = if (cycleItem.checked) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.surface
    val icon = if (cycleItem.checked) Icons.Default.Notifications else Icons.Default.NotificationsNone

    Card(
        modifier = Modifier
            .padding(horizontal = 24.dp, vertical = 4.dp)
            .clickable { onCycleClick() },
        colors = CardDefaults.cardColors().copy(
            containerColor = cardColor
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.tertiary),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(8.dp)
        ) {
            Icon(
                icon,
                "",
                tint = MaterialTheme.colorScheme.tertiary
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                cycleItem.time,
                fontSize = 35.sp,
                fontFamily = myTypeFamily,
                fontWeight = FontWeight(900),
                color = MaterialTheme.colorScheme.tertiary,
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                cycleItem.cycleCount + " cycles",
                fontSize = 15.sp,
                fontFamily = myTypeFamily,
                fontWeight = FontWeight(400),
                color = MaterialTheme.colorScheme.tertiary,
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun CyclesPreview() {
    SnoozyTheme {
        CycleItemCard(CycleItem(0, "22:00", "7", false)) {}
    }
}