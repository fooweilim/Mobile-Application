package com.example.githubdemo.admin.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Agriculture
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.githubdemo.ui.theme.HarvestGreen
import com.example.githubdemo.ui.theme.LightGreen


private data class AdminNavigationItem(
    val route: String,
    val label: String,
    val icon: ImageVector
)

private val adminNavigationItems =
    listOf(
        AdminNavigationItem(
            route = "home",
            label = "Home",
            icon = Icons.Default.Home
        ),
        AdminNavigationItem(
            route = "user",
            label = "User",
            icon = Icons.Default.Person
        ),
        AdminNavigationItem(
            route = "farmer",
            label = "Farmer",
            icon = Icons.Default.Agriculture
        ),
        AdminNavigationItem(
            route = "foodbox",
            label = "Food Box",
            icon = Icons.Default.Inventory
        ),
        AdminNavigationItem(
            route = "announcement",
            label = "Announcement",
            icon = Icons.Default.Campaign
        )
    )

@Composable
fun AdminBottomBar(
    currentRoute: String,
    onNavigate: (String) -> Unit
) {
    NavigationBar(
        containerColor = Color.White,
        tonalElevation = 5.dp
    ) {
        adminNavigationItems.forEach { item ->
            NavigationBarItem(
                selected =
                    isAdminRouteSelected(
                        currentRoute =
                            currentRoute,
                        itemRoute =
                            item.route
                    ),
                onClick = {
                    onNavigate(item.route)
                },
                icon = {
                    Icon(
                        imageVector =
                            item.icon,
                        contentDescription =
                            item.label
                    )
                },
                label = {
                    Text(
                        text = item.label,
                        fontSize = 9.sp,
                        maxLines = 1,
                        softWrap = false
                    )
                },
                colors =
                    NavigationBarItemDefaults
                        .colors(
                            selectedIconColor =
                                HarvestGreen,
                            selectedTextColor =
                                HarvestGreen,
                            indicatorColor =
                                LightGreen,
                            unselectedIconColor =
                                Color.Gray,
                            unselectedTextColor =
                                Color.Gray
                        )
            )
        }
    }
}

private fun isAdminRouteSelected(
    currentRoute: String,
    itemRoute: String
): Boolean {
    return when (itemRoute) {
        "user" ->
            currentRoute == "user" ||
                    currentRoute.startsWith(
                        "profile"
                    )

        "foodbox" ->
            currentRoute == "foodbox" ||
                    currentRoute.startsWith(
                        "foodboxDetail"
                    ) ||
                    currentRoute.startsWith(
                        "foodItemDetailEdit"
                    ) ||
                    currentRoute == "foodItems"

        "announcement" ->
            currentRoute ==
                    "announcement" ||
                    currentRoute.startsWith(
                        "announcementDetail"
                    )

        else ->
            currentRoute == itemRoute
    }
}