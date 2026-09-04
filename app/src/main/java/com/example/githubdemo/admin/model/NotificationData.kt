package com.example.githubdemo.admin.model

data class NotificationData(
    val id: Int,
    val title: String,
    val message: String,
    val date: String,
    val isRead: Boolean,
    val timestamp: String = ""
)