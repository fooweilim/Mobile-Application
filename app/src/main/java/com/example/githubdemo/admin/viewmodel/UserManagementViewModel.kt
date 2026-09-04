package com.example.githubdemo.admin.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.githubdemo.admin.model.UserData
import com.example.githubdemo.admin.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

private const val ALL_FILTER =
    "All"

private const val ACTIVE_STATUS =
    "Active"

private const val BANNED_STATUS =
    "Banned"

private const val USER_TAG =
    "ADMIN_USER"

class UserManagementViewModel :
    ViewModel() {

    private val repository =
        UserRepository()

    private val _users =
        MutableStateFlow<
                List<UserData>
                >(
            emptyList()
        )

    val users:
            StateFlow<List<UserData>> =
        _users.asStateFlow()

    private val _searchText =
        MutableStateFlow("")

    val searchText:
            StateFlow<String> =
        _searchText.asStateFlow()

    private val _selectedFilter =
        MutableStateFlow(
            ALL_FILTER
        )

    val selectedFilter:
            StateFlow<String> =
        _selectedFilter.asStateFlow()

    private val _errorMessage =
        MutableStateFlow<String?>(null)

    val errorMessage:
            StateFlow<String?> =
        _errorMessage.asStateFlow()

    init {
        loadUsers()
    }

    fun loadUsers() {
        viewModelScope.launch {
            try {
                _errorMessage.value = null

                val profiles =
                    repository.getUsers()

                _users.value =
                    profiles
                        .filter { profile ->
                            !profile.user_role
                                .equals(
                                    other = "admin",
                                    ignoreCase = true
                                )
                        }
                        .map { profile ->
                            UserData(
                                id =
                                    profile.id,
                                name =
                                    profile.full_name
                                        .ifBlank {
                                            "Unnamed User"
                                        },
                                email =
                                    profile.email,
                                phone =
                                    profile
                                        .phone_number,
                                role =
                                    formatRole(
                                        profile
                                            .user_role
                                    ),
                                status =
                                    if (
                                        profile.is_banned
                                    ) {
                                        BANNED_STATUS
                                    } else {
                                        ACTIVE_STATUS
                                    },
                                registeredDate =
                                    profile
                                        .created_at
                                        .substringBefore(
                                            "T"
                                        )
                            )
                        }
            } catch (exception: Exception) {
                Log.e(
                    USER_TAG,
                    "Load users error: " +
                            exception.message,
                    exception
                )

                _errorMessage.value =
                    exception.message
                        ?: "Unable to load users."
            }
        }
    }

    fun updateSearch(
        text: String
    ) {
        _searchText.value =
            text
    }

    fun changeFilter(
        filter: String
    ) {
        _selectedFilter.value =
            filter
    }

    fun filteredUsers():
            List<UserData> {
        val query =
            searchText.value.trim()

        return users.value.filter {
                user ->

            val searchMatches =
                query.isBlank() ||
                        user.name.contains(
                            other = query,
                            ignoreCase = true
                        ) ||
                        user.email.contains(
                            other = query,
                            ignoreCase = true
                        )

            val filterMatches =
                selectedFilter.value ==
                        ALL_FILTER ||
                        user.role.equals(
                            other =
                                selectedFilter.value,
                            ignoreCase = true
                        )

            searchMatches &&
                    filterMatches
        }
    }

    fun toggleBan(
        user: UserData
    ) {
        viewModelScope.launch {
            try {
                _errorMessage.value = null

                val shouldBan =
                    user.status ==
                            ACTIVE_STATUS

                repository.updateBanStatus(
                    id = user.id,
                    banned = shouldBan
                )

                _users.value =
                    users.value.map {
                            currentUser ->

                        if (
                            currentUser.id ==
                            user.id
                        ) {
                            currentUser.copy(
                                status =
                                    if (
                                        shouldBan
                                    ) {
                                        BANNED_STATUS
                                    } else {
                                        ACTIVE_STATUS
                                    }
                            )
                        } else {
                            currentUser
                        }
                    }
            } catch (exception: Exception) {
                Log.e(
                    USER_TAG,
                    "Ban error: " +
                            exception.message,
                    exception
                )

                _errorMessage.value =
                    exception.message
                        ?: "Unable to update user status."
            }
        }
    }

    fun deleteUser(
        user: UserData
    ) {
        viewModelScope.launch {
            try {
                _errorMessage.value = null

                repository.deleteUser(
                    user.id
                )

                _users.value =
                    users.value.filter {
                            currentUser ->
                        currentUser.id !=
                                user.id
                    }
            } catch (exception: Exception) {
                Log.e(
                    USER_TAG,
                    "Delete error: " +
                            exception.message,
                    exception
                )

                _errorMessage.value =
                    exception.message
                        ?: "Unable to delete user."
            }
        }
    }

    fun clearError() {
        _errorMessage.value = null
    }

    private fun formatRole(
        role: String
    ): String {
        val cleanRole =
            role
                .trim()
                .lowercase()

        if (cleanRole.isBlank()) {
            return "Buyer"
        }

        return cleanRole
            .replaceFirstChar {
                    character ->
                character.uppercase()
            }
    }
}