package com.example.githubdemo.admin.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.githubdemo.admin.model.Announcement
import com.example.githubdemo.admin.repository.AnnouncementRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AnnouncementViewModel :
    ViewModel() {

    private val repository =
        AnnouncementRepository()

    private val _announcements =
        MutableStateFlow<
                List<Announcement>
                >(
            emptyList()
        )

    val announcements:
            StateFlow<List<Announcement>> =
        _announcements.asStateFlow()

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

    init {
        loadAnnouncements()
    }

    fun loadAnnouncements() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            try {
                _announcements.value =
                    repository
                        .getAnnouncements()
            } catch (exception: Exception) {
                _errorMessage.value =
                    exception.message
                        ?: "Unable to load announcements."
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun addAnnouncement(
        title: String,
        content: String
    ) {
        if (
            title.isBlank() ||
            content.isBlank()
        ) {
            _errorMessage.value =
                "Title and content are required."
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            try {
                repository.addAnnouncement(
                    title = title.trim(),
                    content = content.trim()
                )

                _announcements.value =
                    repository
                        .getAnnouncements()
            } catch (exception: Exception) {
                _errorMessage.value =
                    exception.message
                        ?: "Unable to add announcement."
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun updateAnnouncement(
        id: String,
        title: String,
        content: String
    ) {
        if (
            id.isBlank() ||
            title.isBlank() ||
            content.isBlank()
        ) {
            _errorMessage.value =
                "Title and content are required."
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            try {
                repository.updateAnnouncement(
                    id = id,
                    title = title.trim(),
                    content = content.trim()
                )

                _announcements.value =
                    repository
                        .getAnnouncements()
            } catch (exception: Exception) {
                _errorMessage.value =
                    exception.message
                        ?: "Unable to update announcement."
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun deleteAnnouncement(
        id: String
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            try {
                repository
                    .deleteAnnouncement(id)

                _announcements.value =
                    repository
                        .getAnnouncements()
            } catch (exception: Exception) {
                _errorMessage.value =
                    exception.message
                        ?: "Unable to delete announcement."
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun getAnnouncement(
        id: String
    ): Announcement? {
        return announcements.value
            .find { announcement ->
                announcement.id == id
            }
    }

    fun clearError() {
        _errorMessage.value = null
    }
}