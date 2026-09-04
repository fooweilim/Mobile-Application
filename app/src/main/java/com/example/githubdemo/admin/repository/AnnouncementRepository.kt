package com.example.githubdemo.admin.repository

import android.util.Log
import com.example.githubdemo.admin.model.Announcement
import com.example.githubdemo.supabase.SupabaseConnection
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Order
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import kotlinx.serialization.Serializable

private const val ANNOUNCEMENT_TABLE =
    "announcements"

private const val CREATED_AT_COLUMN =
    "created_at"

private const val ANNOUNCEMENT_TAG =
    "ADMIN_ANNOUNCEMENT"

@Serializable
private data class AnnouncementInput(
    val title: String,
    val content: String
)

class AnnouncementRepository {

    private val supabase =
        SupabaseConnection.supabase

    suspend fun getAnnouncements():
            List<Announcement> {
        val announcements =
            supabase
                .from(ANNOUNCEMENT_TABLE)
                .select {
                    order(
                        column =
                            CREATED_AT_COLUMN,
                        order =
                            Order.DESCENDING
                    )
                }
                .decodeList<Announcement>()

        return announcements.map {
                announcement ->
            announcement.copy(
                created_at =
                    formatDate(
                        announcement.created_at
                    )
            )
        }
    }

    suspend fun addAnnouncement(
        title: String,
        content: String
    ) {
        Log.d(
            ANNOUNCEMENT_TAG,
            "Adding announcement: $title"
        )

        supabase
            .from(ANNOUNCEMENT_TABLE)
            .insert(
                AnnouncementInput(
                    title = title,
                    content = content
                )
            )
    }

    suspend fun updateAnnouncement(
        id: String,
        title: String,
        content: String
    ) {
        supabase
            .from(ANNOUNCEMENT_TABLE)
            .update(
                {
                    set(
                        column = "title",
                        value = title
                    )

                    set(
                        column = "content",
                        value = content
                    )
                }
            ) {
                filter {
                    eq(
                        column = "id",
                        value = id
                    )
                }
            }
    }

    suspend fun deleteAnnouncement(
        id: String
    ) {
        supabase
            .from(ANNOUNCEMENT_TABLE)
            .delete {
                filter {
                    eq(
                        column = "id",
                        value = id
                    )
                }
            }
    }

    private fun formatDate(
        date: String?
    ): String {
        if (date.isNullOrBlank()) {
            return ""
        }

        val outputFormatter =
            DateTimeFormatter.ofPattern(
                "dd MMM yyyy, HH:mm"
            )

        return try {
            OffsetDateTime
                .parse(date)
                .format(outputFormatter)
        } catch (_: Exception) {
            try {
                LocalDateTime
                    .parse(
                        date
                            .replace("Z", "")
                            .substringBefore("+")
                    )
                    .format(outputFormatter)
            } catch (_: Exception) {
                date
            }
        }
    }
}