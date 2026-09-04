package com.example.githubdemo.admin.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.githubdemo.admin.components.UserCard
import com.example.githubdemo.admin.model.UserData
import com.example.githubdemo.admin.viewmodel.UserManagementViewModel

private val userFilters =
    listOf(
        "All",
        "Buyer",
        "Farmer"
    )

@Suppress("UNUSED_PARAMETER")
@Composable
fun UserManagementScreen(
    navController: NavHostController,
    onSelectUser: (UserData) -> Unit,
    viewModel: UserManagementViewModel
) {
    val allUsers by
    viewModel.users.collectAsState()

    val searchText by
    viewModel
        .searchText
        .collectAsState()

    val selectedFilter by
    viewModel
        .selectedFilter
        .collectAsState()

    val errorMessage by
    viewModel
        .errorMessage
        .collectAsState()

    val displayedUsers =
        viewModel.filteredUsers()

    var userToDelete by remember {
        mutableStateOf<UserData?>(null)
    }

    LazyColumn(
        modifier =
            Modifier.fillMaxSize(),
        contentPadding =
            PaddingValues(
                start = 12.dp,
                top = 12.dp,
                end = 12.dp,
                bottom = 90.dp
            ),
        verticalArrangement =
            Arrangement.spacedBy(
                12.dp
            )
    ) {
        item {
            Text(
                text =
                    "User Management",
                style =
                    MaterialTheme
                        .typography
                        .titleLarge
            )
        }

        item {
            TabRow(
                selectedTabIndex =
                    userFilters
                        .indexOf(
                            selectedFilter
                        )
                        .coerceAtLeast(0)
            ) {
                userFilters.forEach {
                        filter ->

                    Tab(
                        selected =
                            selectedFilter ==
                                    filter,
                        onClick = {
                            viewModel
                                .changeFilter(
                                    filter
                                )
                        },
                        text = {
                            Text(filter)
                        }
                    )
                }
            }
        }

        item {
            OutlinedTextField(
                value = searchText,
                onValueChange = {
                    viewModel.updateSearch(
                        it
                    )
                },
                modifier =
                    Modifier.fillMaxWidth(),
                placeholder = {
                    Text(
                        "Search name or email..."
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector =
                            Icons.Default
                                .Search,
                        contentDescription =
                            null
                    )
                },
                singleLine = true
            )
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

        if (
            displayedUsers.isEmpty() &&
            allUsers.isNotEmpty()
        ) {
            item {
                Text(
                    text =
                        "No user matches your search."
                )
            }
        } else if (allUsers.isEmpty()) {
            item {
                Text(
                    text =
                        "No registered users available."
                )
            }
        }

        items(
            items = displayedUsers,
            key = { user ->
                user.id
            }
        ) { user ->
            UserCard(
                user = user,
                onViewProfile = {
                    onSelectUser(user)
                },
                onBanUser = {
                    viewModel.toggleBan(
                        user
                    )
                },
                onDeleteUser = {
                    userToDelete = user
                }
            )
        }
    }

    userToDelete?.let { selectedUser ->
        AlertDialog(
            onDismissRequest = {
                userToDelete = null
            },
            title = {
                Text("Delete user?")
            },
            text = {
                Text(
                    "${selectedUser.name}'s profile will be deleted."
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.deleteUser(
                            selectedUser
                        )

                        userToDelete = null
                    }
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        userToDelete = null
                    }
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}