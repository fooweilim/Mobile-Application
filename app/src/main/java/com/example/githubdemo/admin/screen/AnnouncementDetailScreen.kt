package com.example.githubdemo.admin.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.githubdemo.admin.viewmodel.AnnouncementViewModel

@Composable
fun AnnouncementDetailScreen(
    announcementId: String,
    onBack: () -> Unit,
    viewModel: AnnouncementViewModel
) {
    val announcements by
    viewModel
        .announcements
        .collectAsState()

    val errorMessage by
    viewModel
        .errorMessage
        .collectAsState()

    val announcement =
        announcements.find {
                currentAnnouncement ->
            currentAnnouncement.id ==
                    announcementId
        }

    var showDeleteDialog by remember {
        mutableStateOf(false)
    }

    var title by rememberSaveable(
        announcementId,
        announcement?.title
    ) {
        mutableStateOf(
            announcement
                ?.title
                .orEmpty()
        )
    }

    var content by rememberSaveable(
        announcementId,
        announcement?.content
    ) {
        mutableStateOf(
            announcement
                ?.content
                .orEmpty()
        )
    }

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(16.dp)
    ) {
        Row(
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
                text =
                    "Announcement Details",
                style =
                    MaterialTheme
                        .typography
                        .headlineSmall
            )
        }

        Spacer(
            modifier =
                Modifier.height(20.dp)
        )

        if (announcement == null) {
            Text(
                text =
                    "Announcement not found.",
                color =
                    MaterialTheme
                        .colorScheme
                        .error
            )

            Spacer(
                modifier =
                    Modifier.height(16.dp)
            )

            Button(
                onClick = onBack
            ) {
                Text("Back")
            }

            return@Column
        }

        OutlinedTextField(
            value = title,
            onValueChange = {
                title = it.take(100)
                viewModel.clearError()
            },
            modifier =
                Modifier.fillMaxWidth(),
            label = {
                Text("Title")
            },
            singleLine = true
        )

        Spacer(
            modifier =
                Modifier.height(12.dp)
        )

        OutlinedTextField(
            value = content,
            onValueChange = {
                content = it.take(1000)
                viewModel.clearError()
            },
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(160.dp),
            label = {
                Text("Content")
            }
        )

        if (!errorMessage.isNullOrBlank()) {
            Spacer(
                modifier =
                    Modifier.height(10.dp)
            )

            Text(
                text =
                    errorMessage.orEmpty(),
                color =
                    MaterialTheme
                        .colorScheme
                        .error
            )
        }

        Spacer(
            modifier =
                Modifier.height(20.dp)
        )

        Button(
            modifier =
                Modifier.fillMaxWidth(),
            enabled =
                title.isNotBlank() &&
                        content.isNotBlank(),
            onClick = {
                viewModel.updateAnnouncement(
                    id = announcementId,
                    title = title,
                    content = content
                )
            }
        ) {
            Text("Save Changes")
        }

        Spacer(
            modifier =
                Modifier.height(10.dp)
        )

        OutlinedButton(
            modifier =
                Modifier.fillMaxWidth(),
            onClick = {
                showDeleteDialog = true
            }
        ) {
            Text("Delete Announcement")
        }
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = {
                showDeleteDialog = false
            },
            title = {
                Text("Delete announcement?")
            },
            text = {
                Text(
                    "This announcement will be permanently deleted."
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDeleteDialog = false

                        viewModel
                            .deleteAnnouncement(
                                announcementId
                            )

                        onBack()
                    }
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showDeleteDialog = false
                    }
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}