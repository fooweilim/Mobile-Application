package com.example.githubdemo.admin.repository

import com.example.githubdemo.admin.model.NotificationData
import com.example.githubdemo.supabase.SupabaseConnection
import io.github.jan.supabase.postgrest.from
import kotlinx.serialization.Serializable

private const val ANNOUNCEMENT_TABLE =
    "announcements"

private const val FARMER_APPLICATION_TABLE =
    "farmer_applications"

private const val PROFILE_TABLE =
    "profiles"

class NotificationRepository {

    private val supabase =
        SupabaseConnection.supabase

    suspend fun getNotifications():
            List<NotificationData> {
        val notifications =
            mutableListOf<
                    NotificationData
                    >()

        loadAnnouncementNotifications(
            notifications
        )

        loadFarmerNotifications(
            notifications
        )

        loadUserNotifications(
            notifications
        )

        return notifications
            .sortedByDescending {
                    notification ->
                notification.timestamp
            }
    }

    private suspend fun loadAnnouncementNotifications(
        notifications:
        MutableList<NotificationData>
    ) {
        try {
            val announcements =
                supabase
                    .from(
                        ANNOUNCEMENT_TABLE
                    )
                    .select()
                    .decodeList<
                            AnnouncementNotificationDto
                            >()

            announcements.forEach {
                    announcement ->

                notifications.add(
                    NotificationData(
                        id =
                            (
                                    "announcement_" +
                                            announcement.id
                                    ).hashCode(),
                        title =
                            "New Announcement",
                        message =
                            announcement.title,
                        date =
                            announcement
                                .created_at
                                ?.substringBefore(
                                    "T"
                                )
                                .orEmpty(),
                        isRead = false,
                        timestamp =
                            announcement
                                .created_at
                                .orEmpty()
                    )
                )
            }
        } catch (_: Exception) {
            // Continue loading other notifications.
        }
    }

    private suspend fun loadFarmerNotifications(
        notifications:
        MutableList<NotificationData>
    ) {
        try {
            val applications =
                supabase
                    .from(
                        FARMER_APPLICATION_TABLE
                    )
                    .select()
                    .decodeList<
                            FarmerNotificationDto
                            >()

            applications.forEach {
                    application ->

                notifications.add(
                    NotificationData(
                        id =
                            (
                                    "farmer_" +
                                            application.id
                                    ).hashCode(),
                        title =
                            "New Farmer Application",
                        message =
                            application.name +
                                    " submitted a farmer " +
                                    "verification request.",
                        date =
                            application
                                .applied_date
                                ?.substringBefore(
                                    "T"
                                )
                                .orEmpty(),
                        isRead = false,
                        timestamp =
                            application
                                .applied_date
                                .orEmpty()
                    )
                )
            }
        } catch (_: Exception) {
            // Continue loading other notifications.
        }
    }

    private suspend fun loadUserNotifications(
        notifications:
        MutableList<NotificationData>
    ) {
        try {
            val profiles =
                supabase
                    .from(
                        PROFILE_TABLE
                    )
                    .select()
                    .decodeList<
                            ProfileNotificationDto
                            >()

            profiles.forEach { profile ->
                notifications.add(
                    NotificationData(
                        id =
                            (
                                    "profile_" +
                                            profile.id
                                    ).hashCode(),
                        title =
                            "New User Registered",
                        message =
                            profile.full_name +
                                    " created an account.",
                        date =
                            profile
                                .created_at
                                ?.substringBefore(
                                    "T"
                                )
                                .orEmpty(),
                        isRead = false,
                        timestamp =
                            profile
                                .created_at
                                .orEmpty()
                    )
                )
            }
        } catch (_: Exception) {
            // Continue loading other notifications.
        }
    }
}

@Serializable
private data class AnnouncementNotificationDto(
    val id: String,
    val title: String = "",
    val created_at: String? = null
)

@Serializable
private data class FarmerNotificationDto(
    val id: String,
    val name: String = "",
    val applied_date: String? = null
)

@Serializable
private data class ProfileNotificationDto(
    val id: String,
    val full_name: String = "User",
    val created_at: String? = null
)