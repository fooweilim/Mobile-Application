package com.example.githubdemo.admin.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.githubdemo.admin.components.NotificationCard
import com.example.githubdemo.admin.viewmodel.NotificationViewModel

@Composable
fun NotificationScreen(
    onBack: () -> Unit,
    viewModel: NotificationViewModel
) {
    val notifications by
    viewModel
        .notifications
        .collectAsState()

    val errorMessage by
    viewModel
        .errorMessage
        .collectAsState()

    LazyColumn(
        modifier =
            Modifier.fillMaxSize(),
        contentPadding =
            PaddingValues(16.dp),
        verticalArrangement =
            Arrangement.spacedBy(
                8.dp
            )
    ) {
        item {
            Row(
                modifier =
                    Modifier.fillMaxWidth(),
                verticalAlignment =
                    Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBack
                ) {
                    Icon(
                        imageVector =
                            Icons.Default
                                .ArrowBack,
                        contentDescription =
                            "Back"
                    )
                }

                Text(
                    text = "Notifications",
                    style =
                        MaterialTheme
                            .typography
                            .titleLarge,
                    modifier =
                        Modifier.weight(1f)
                )

                if (
                    notifications.any {
                            notification ->
                        !notification.isRead
                    }
                ) {
                    TextButton(
                        onClick = {
                            viewModel
                                .markAllAsRead()
                        }
                    ) {
                        Text("Read all")
                    }
                }
            }
        }

        if (!errorMessage.isNullOrBlank()) {
            item {
                Text(
                    text =
                        errorMessage.orEmpty(),
                    color =
                        MaterialTheme
                            .colorScheme
                            .error
                )
            }
        }

        if (notifications.isEmpty()) {
            item {
                Text(
                    text =
                        "No notifications available."
                )
            }
        }

        items(
            items = notifications,
            key = {
                    notification ->
                notification.id
            }
        ) { notification ->
            NotificationCard(
                notification =
                    notification,
                onClick = {
                    viewModel.markAsRead(
                        notification
                    )
                }
            )
        }
    }
}