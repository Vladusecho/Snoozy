package com.wem.snoozy.domain.navigation

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Alarm
import androidx.compose.material.icons.outlined.Group
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wem.snoozy.presentation.itemCard.myTypeFamily

sealed class MyNavItem(
    val screen: Screen,
    val icon: ImageVector,
    val title: String
) {

    data object Home : MyNavItem(
        screen = Screen.Home,
        icon = Icons.Outlined.Alarm,
        title = "Alarms"
    )

    data object Groups : MyNavItem(
        screen = Screen.Groups,
        icon = Icons.Outlined.Group,
        title = "Groups"
    )

    data object Profile : MyNavItem(
        screen = Screen.Profile,
        icon = Icons.Outlined.AccountCircle,
        title = "Profile"
    )

    data object Settings : MyNavItem(
        screen = Screen.Settings,
        icon = Icons.Outlined.Settings,
        title = "Settings"
    )
}

val tabs = listOf(
    MyNavItem.Home,
    MyNavItem.Groups,
    MyNavItem.Profile,
    MyNavItem.Settings,
)

@Composable
fun BottomBarTabs(
    tabs: List<MyNavItem>,
    selectedTab: Int,
    onTabSelected: (MyNavItem) -> Unit,
) {

    val navColorMain by animateColorAsState(
        targetValue = MaterialTheme.colorScheme.surface,
        animationSpec = tween(0),
        label = "nav_color_main"
    )

    Row(
        modifier = Modifier
            .shadow(1.dp, RoundedCornerShape(30))
            .clip(RoundedCornerShape(30))
            .background(MaterialTheme.colorScheme.surface,)
            .fillMaxSize(),
    ) {
        for (tab in tabs) {
            val navColor by animateColorAsState(
                targetValue = if (selectedTab == tabs.indexOf(tab)) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
                animationSpec = tween(0),
                label = "nav_color"
            )
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(horizontal = 8.dp, vertical = 6.dp)
                    .clip(RoundedCornerShape(26))
                    .background(if (selectedTab == tabs.indexOf(tab)) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface)
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier
                        .pointerInput(Unit) {
                            detectTapGestures {
                                onTabSelected(tab)
                            }
                        },
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    Icon(
                        imageVector = tab.icon,
                        contentDescription = "tab ${tab.title}",
                        tint = MaterialTheme.colorScheme.tertiary,
                        modifier = Modifier.size(32.dp)
                    )
                    Text(
                        text = tab.title,
                        color = MaterialTheme.colorScheme.tertiary,
                        fontSize = 10.sp,
                        fontFamily = myTypeFamily,
                        fontWeight = FontWeight(900),
                        lineHeight = 2.sp
                    )
                }
            }
        }
    }
}
