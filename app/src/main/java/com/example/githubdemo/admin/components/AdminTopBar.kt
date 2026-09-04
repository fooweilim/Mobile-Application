package com.example.githubdemo.admin.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.githubdemo.ui.theme.HarvestGreen


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminTopBar(
    title: String,
    onLogout: () -> Unit,
    onNotificationClick: () -> Unit,
    unreadCount: Int = 0
) {
    TopAppBar(
        colors =
            TopAppBarDefaults
                .topAppBarColors(
                    containerColor =
                        HarvestGreen,
                    titleContentColor =
                        Color.White,
                    navigationIconContentColor =
                        Color.White,
                    actionIconContentColor =
                        Color.White
                ),
        title = {
            Text(
                text = title
            )
        },
        navigationIcon = {
            IconButton(
                onClick = onLogout
            ) {
                Icon(
                    imageVector =
                        Icons.Default.ExitToApp,
                    contentDescription =
                        "Logout"
                )
            }
        },
        actions = {
            Box(
                contentAlignment =
                    Alignment.TopEnd
            ) {
                IconButton(
                    onClick =
                        onNotificationClick
                ) {
                    Icon(
                        imageVector =
                            Icons.Default
                                .Notifications,
                        contentDescription =
                            "Notifications"
                    )
                }

                if (unreadCount > 0) {
                    Box(
                        modifier =
                            Modifier
                                .offset(
                                    x = (-8).dp,
                                    y = 8.dp
                                )
                                .size(10.dp)
                                .background(
                                    color =
                                        Color.Red,
                                    shape =
                                        CircleShape
                                )
                    )
                }
            }
        }
    )
}