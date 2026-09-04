package com.example.githubdemo.admin.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.githubdemo.admin.model.UserData
import com.example.githubdemo.ui.theme.HarvestGreen
import com.example.githubdemo.ui.theme.LightGreen
import com.example.githubdemo.ui.theme.TextDark


@Composable
fun UserCard(
    user: UserData,
    onViewProfile: () -> Unit,
    onBanUser: () -> Unit,
    onDeleteUser: () -> Unit
) {
    var expanded by remember {
        mutableStateOf(false)
    }

    Card(
        modifier =
            Modifier.fillMaxWidth(),
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
                    2.dp
            )
    ) {
        Row(
            modifier =
                Modifier.padding(16.dp),
            verticalAlignment =
                Alignment.CenterVertically
        ) {
            Column(
                modifier =
                    Modifier.weight(1f)
            ) {
                Text(
                    text = user.name,
                    style =
                        MaterialTheme
                            .typography
                            .titleMedium,
                    color =
                        TextDark
                )

                Spacer(
                    modifier =
                        Modifier.height(4.dp)
                )

                Text(
                    text = user.email,
                    style =
                        MaterialTheme
                            .typography
                            .bodySmall,
                    color =
                        Color.Gray
                )

                Spacer(
                    modifier =
                        Modifier.height(8.dp)
                )

                Row(
                    horizontalArrangement =
                        Arrangement.spacedBy(
                            8.dp
                        )
                ) {
                    AssistChip(
                        onClick = {},
                        label = {
                            Text(user.role)
                        },
                        colors =
                            AssistChipDefaults
                                .assistChipColors(
                                    containerColor =
                                        LightGreen,
                                    labelColor =
                                        HarvestGreen
                                )
                    )

                    AssistChip(
                        onClick = {},
                        label = {
                            Text(user.status)
                        },
                        colors =
                            AssistChipDefaults
                                .assistChipColors(
                                    containerColor =
                                        if (
                                            user.status ==
                                            "Active"
                                        ) {
                                            LightGreen
                                        } else {
                                            Color(
                                                0xFFFFE0E0
                                            )
                                        },
                                    labelColor =
                                        if (
                                            user.status ==
                                            "Active"
                                        ) {
                                            HarvestGreen
                                        } else {
                                            Color.Red
                                        }
                                )
                    )
                }
            }

            Box {
                IconButton(
                    onClick = {
                        expanded = true
                    }
                ) {
                    Icon(
                        imageVector =
                            Icons.Default
                                .MoreVert,
                        contentDescription =
                            "User options"
                    )
                }

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = {
                        expanded = false
                    }
                ) {
                    DropdownMenuItem(
                        text = {
                            Text(
                                "View Profile"
                            )
                        },
                        onClick = {
                            expanded = false
                            onViewProfile()
                        }
                    )

                    DropdownMenuItem(
                        text = {
                            Text(
                                if (
                                    user.status ==
                                    "Active"
                                ) {
                                    "Ban User"
                                } else {
                                    "Unban User"
                                }
                            )
                        },
                        onClick = {
                            expanded = false
                            onBanUser()
                        }
                    )

                    DropdownMenuItem(
                        text = {
                            Text(
                                "Delete User"
                            )
                        },
                        onClick = {
                            expanded = false
                            onDeleteUser()
                        }
                    )
                }
            }
        }
    }
}