package com.example.githubdemo.admin.model

import kotlinx.serialization.Serializable

@Serializable
data class Announcement(
    val id: String,
    val title: String,
    val content: String,
    val created_at: String? = null
)