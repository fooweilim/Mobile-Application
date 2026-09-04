package com.example.githubdemo.admin.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.githubdemo.ui.theme.HarvestGreen
import com.example.githubdemo.ui.theme.LightGreen
import com.example.githubdemo.ui.theme.TextDark


@Composable
fun StatisticCard(
    title: String,
    value: String,
    icon: ImageVector
) {
    Card(
        modifier =
            Modifier
                .fillMaxWidth()
                .height(95.dp),
        shape =
            RoundedCornerShape(16.dp),
        colors =
            CardDefaults.cardColors(
                containerColor =
                    Color.White
            ),
        elevation =
            CardDefaults.cardElevation(
                defaultElevation =
                    3.dp
            )
    ) {
        Row(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(14.dp),
            verticalAlignment =
                Alignment.CenterVertically
        ) {
            Surface(
                modifier =
                    Modifier.size(42.dp),
                shape =
                    RoundedCornerShape(
                        12.dp
                    ),
                color =
                    LightGreen
            ) {
                Box(
                    contentAlignment =
                        Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription =
                            null,
                        tint =
                            HarvestGreen
                    )
                }
            }

            Spacer(
                modifier =
                    Modifier.width(12.dp)
            )

            Column {
                Text(
                    text = title,
                    style =
                        MaterialTheme
                            .typography
                            .bodySmall,
                    color =
                        Color.Gray
                )

                Spacer(
                    modifier =
                        Modifier.height(4.dp)
                )

                Text(
                    text = value,
                    style =
                        MaterialTheme
                            .typography
                            .titleLarge
                            .copy(
                                fontSize =
                                    22.sp
                            ),
                    color =
                        TextDark
                )
            }
        }
    }
}