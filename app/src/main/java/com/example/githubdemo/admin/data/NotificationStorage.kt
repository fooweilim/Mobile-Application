package com.example.githubdemo.admin.data

import android.content.Context

private const val NOTIFICATION_STORAGE_NAME =
    "admin_notification_read"

class NotificationStorage(
    context: Context
) {
    private val preferences =
        context.getSharedPreferences(
            NOTIFICATION_STORAGE_NAME,
            Context.MODE_PRIVATE
        )

    fun isRead(
        notificationId: Int
    ): Boolean {
        return preferences.getBoolean(
            notificationId.toString(),
            false
        )
    }

    fun setRead(
        notificationId: Int
    ) {
        preferences
            .edit()
            .putBoolean(
                notificationId.toString(),
                true
            )
            .apply()
    }
}