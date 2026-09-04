package com.example.githubdemo.admin.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.githubdemo.admin.model.NotificationData
import com.example.githubdemo.ui.theme.LightGreen


@Composable
fun NotificationCard(
    notification: NotificationData,
    onClick: () -> Unit
) {
    Card(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(
                    vertical = 6.dp
                ),
        shape =
            RoundedCornerShape(14.dp),
        colors =
            CardDefaults.cardColors(
                containerColor =
                    if (
                        notification.isRead
                    ) {
                        Color.White
                    } else {
                        LightGreen
                    }
            ),
        onClick = onClick
    ) {
        Column(
            modifier =
                Modifier.padding(16.dp)
        ) {
            Row(
                modifier =
                    Modifier.fillMaxWidth(),
                verticalAlignment =
                    Alignment.CenterVertically
            ) {
                Text(
                    text =
                        notification.title,
                    style =
                        MaterialTheme
                            .typography
                            .titleMedium,
                    modifier =
                        Modifier.weight(1f)
                )

                if (!notification.isRead) {
                    Spacer(
                        modifier =
                            Modifier.size(8.dp)
                    )

                    Spacer(
                        modifier =
                            Modifier
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

            Spacer(
                modifier =
                    Modifier.height(8.dp)
            )

            Text(
                text =
                    notification.message,
                style =
                    MaterialTheme
                        .typography
                        .bodyMedium
            )

            if (notification.date.isNotBlank()) {
                Spacer(
                    modifier =
                        Modifier.height(6.dp)
                )

                Text(
                    text =
                        notification.date,
                    style =
                        MaterialTheme
                            .typography
                            .bodySmall,
                    color =
                        Color.Gray
                )
            }
        }
    }
}