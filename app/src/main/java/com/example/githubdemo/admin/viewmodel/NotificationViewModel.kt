package com.example.githubdemo.admin.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.githubdemo.admin.data.NotificationStorage
import com.example.githubdemo.admin.model.NotificationData
import com.example.githubdemo.admin.repository.NotificationRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class NotificationViewModel(
    context: Context
) : ViewModel() {

    private val repository =
        NotificationRepository()

    private val storage =
        NotificationStorage(context)

    private val _notifications =
        MutableStateFlow<
                List<NotificationData>
                >(
            emptyList()
        )

    val notifications:
            StateFlow<List<NotificationData>> =
        _notifications.asStateFlow()

    private val _errorMessage =
        MutableStateFlow<String?>(null)

    val errorMessage:
            StateFlow<String?> =
        _errorMessage.asStateFlow()

    init {
        loadNotifications()
    }

    fun loadNotifications() {
        viewModelScope.launch {
            try {
                _errorMessage.value = null

                val cloudNotifications =
                    repository
                        .getNotifications()

                _notifications.value =
                    cloudNotifications.map {
                            notification ->

                        notification.copy(
                            isRead =
                                storage.isRead(
                                    notification.id
                                )
                        )
                    }
            } catch (exception: Exception) {
                _errorMessage.value =
                    exception.message
                        ?: "Unable to load notifications."
            }
        }
    }

    fun markAsRead(
        notification: NotificationData
    ) {
        storage.setRead(
            notification.id
        )

        _notifications.value =
            _notifications.value.map {
                    currentNotification ->

                if (
                    currentNotification.id ==
                    notification.id
                ) {
                    currentNotification.copy(
                        isRead = true
                    )
                } else {
                    currentNotification
                }
            }
    }

    fun markAllAsRead() {
        notifications.value.forEach {
                notification ->
            storage.setRead(
                notification.id
            )
        }

        _notifications.value =
            notifications.value.map {
                    notification ->
                notification.copy(
                    isRead = true
                )
            }
    }

    fun unreadCount(): Int {
        return notifications.value.count {
                notification ->
            !notification.isRead
        }
    }

    fun clearError() {
        _errorMessage.value = null
    }
}