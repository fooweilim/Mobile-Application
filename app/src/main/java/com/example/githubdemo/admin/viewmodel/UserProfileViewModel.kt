package com.example.githubdemo.admin.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.githubdemo.admin.model.ProfileDto
import com.example.githubdemo.admin.repository.UserProfileRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class UserProfileViewModel :
    ViewModel() {

    private val repository =
        UserProfileRepository()

    private val _profile =
        MutableStateFlow<ProfileDto?>(
            null
        )

    val profile:
            StateFlow<ProfileDto?> =
        _profile.asStateFlow()

    private val _isLoading =
        MutableStateFlow(false)

    val isLoading:
            StateFlow<Boolean> =
        _isLoading.asStateFlow()

    private val _errorMessage =
        MutableStateFlow<String?>(null)

    val errorMessage:
            StateFlow<String?> =
        _errorMessage.asStateFlow()

    fun loadProfile(
        userId: String
    ) {
        if (userId.isBlank()) {
            _errorMessage.value =
                "Invalid user ID."
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            try {
                _profile.value =
                    repository.getUserById(
                        userId
                    )

                if (_profile.value == null) {
                    _errorMessage.value =
                        "User profile was not found."
                }
            } catch (exception: Exception) {
                _errorMessage.value =
                    exception.message
                        ?: "Unable to load user profile."
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun clearProfile() {
        _profile.value = null
        _errorMessage.value = null
    }
}