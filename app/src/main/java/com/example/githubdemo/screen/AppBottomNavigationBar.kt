package com.example.githubdemo.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.githubdemo.data.AppData
import com.example.githubdemo.ui.theme.DividerColor
import com.example.githubdemo.ui.theme.PrimaryGreen
import com.example.githubdemo.ui.theme.SecondaryText

@Composable
fun AppBottomNavigationBar(
    currentRoute: String?,
    onItemClick: (String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        HorizontalDivider(
            thickness = 1.dp,
            color = DividerColor
        )

        NavigationBar(
            containerColor = Color.White,
            tonalElevation = 0.dp
        ) {
            AppData.bottomNavigationItems.forEach { item ->

                val selected = if (
                    item.route == AppData.FOOD_BOX_ROUTE
                ) {
                    currentRoute in AppData.foodBoxRoutes
                } else {
                    currentRoute == item.route
                }

                NavigationBarItem(
                    selected = selected,
                    onClick = {
                        if (!selected) {
                            onItemClick(item.route)
                        }
                    },
                    icon = {
                        Box(
                            modifier = Modifier
                                .size(46.dp)
                                .background(
                                    color = if (selected) {
                                        PrimaryGreen
                                    } else {
                                        Color.Transparent
                                    },
                                    shape = RoundedCornerShape(15.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (selected) {
                                    item.selectedIcon
                                } else {
                                    item.unselectedIcon
                                },
                                contentDescription = item.label,
                                tint = if (selected) {
                                    Color.White
                                } else {
                                    SecondaryText
                                },
                                modifier = Modifier.size(26.dp)
                            )
                        }
                    },
                    label = {
                        Text(
                            text = item.label,
                            fontSize = 11.sp,
                            fontWeight = if (selected) {
                                FontWeight.SemiBold
                            } else {
                                FontWeight.Normal
                            }
                        )
                    },
                    alwaysShowLabel = true,
                    colors = NavigationBarItemDefaults.colors(
                        selectedTextColor = PrimaryGreen,
                        unselectedTextColor = SecondaryText,
                        indicatorColor = Color.Transparent
                    )
                )
            }
        }
    }
}