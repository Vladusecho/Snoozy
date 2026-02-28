package com.wem.snoozy.presentation.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.wem.snoozy.presentation.itemCard.myTypeFamily

/** Navigation object
 *
 * @param screen Navigation object provide a unique screen
 * @param icon Navigation object's icon
 * @param title Navigation object's title
 */
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


@Composable
fun BottomBarTabs(
    navController: NavController
) {

    // list of all navigation objects
    val tabs = listOf(
        MyNavItem.Home,
        MyNavItem.Groups,
        MyNavItem.Profile,
        MyNavItem.Settings,
    )

    // navigation BackStack and current route
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // bottom bar
    Row(
        modifier = Modifier
            .shadow(1.dp, RoundedCornerShape(30))
            .clip(RoundedCornerShape(30))
            .background(MaterialTheme.colorScheme.surface)
            .fillMaxSize(),
    ) {
        for (tab in tabs) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(horizontal = 8.dp, vertical = 6.dp)
                    .clip(RoundedCornerShape(26))
                    .weight(1f)
                    // if current route == tab route -> checked background
                    .background(if (currentRoute == tab.screen.route) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface)
                    .clickable {
                        navController.navigate(tab.screen.route) {
                            // only one copy of screen we can use
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            // only one same screen at the top
                            launchSingleTop = true
                            // save screen state after another screen
                            restoreState = true
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier,
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
