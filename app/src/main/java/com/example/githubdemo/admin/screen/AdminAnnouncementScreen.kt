package com.example.githubdemo.admin.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.githubdemo.admin.components.AnnouncementCard
import com.example.githubdemo.admin.viewmodel.AnnouncementViewModel

@Composable
fun AdminAnnouncementScreen(
    navController: NavHostController,
    viewModel: AnnouncementViewModel
) {
    val announcements by viewModel.announcements.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    var showAddDialog by remember { mutableStateOf(false) }
    var newTitle by remember { mutableStateOf("") }
    var newContent by remember { mutableStateOf("") }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                start = 16.dp,
                top = 16.dp,
                end = 16.dp,
                bottom = 80.dp
            ),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Announcements",
                        style = MaterialTheme.typography.titleLarge
                    )

                    Button(
                        onClick = {
                            newTitle = ""
                            newContent = ""
                            viewModel.clearError()
                            showAddDialog = true
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Create Announcement"
                        )
                        Spacer(modifier = Modifier.padding(horizontal = 4.dp))
                        Text("Create")
                    }
                }
            }

            if (!errorMessage.isNullOrBlank()) {
                item {
                    Text(
                        text = errorMessage.orEmpty(),
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }

            if (isLoading && announcements.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            } else if (announcements.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier.padding(vertical = 28.dp)
                    ) {
                        Text(text = "No announcements available.")
                    }
                }
            }

            items(
                items = announcements,
                key = { announcement -> announcement.id }
            ) { announcement ->
                AnnouncementCard(
                    announcement = announcement,
                    onClick = {
                        navController.navigate("announcementDetail/${announcement.id}")
                    }
                )
            }
        }

        if (showAddDialog) {
            AlertDialog(
                onDismissRequest = {
                    showAddDialog = false
                    viewModel.clearError()
                },
                title = {
                    Text("New Announcement")
                },
                text = {
                    Column {
                        OutlinedTextField(
                            value = newTitle,
                            onValueChange = {
                                newTitle = it.take(100)
                                viewModel.clearError()
                            },
                            label = { Text("Title") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedTextField(
                            value = newContent,
                            onValueChange = {
                                newContent = it.take(1000)
                                viewModel.clearError()
                            },
                            label = { Text("Content") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(140.dp)
                        )

                        if (!errorMessage.isNullOrBlank()) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = errorMessage.orEmpty(),
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                },
                confirmButton = {
                    Button(
                        enabled = newTitle.isNotBlank() && newContent.isNotBlank() && !isLoading,
                        onClick = {
                            viewModel.addAnnouncement(
                                title = newTitle,
                                content = newContent
                            )
                            showAddDialog = false
                        }
                    ) {
                        Text("Create")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            showAddDialog = false
                            viewModel.clearError()
                        }
                    ) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}
