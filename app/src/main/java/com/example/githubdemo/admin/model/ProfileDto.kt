package com.example.githubdemo.admin.model

import kotlinx.serialization.Serializable

@Serializable
data class ProfileDto(
    val id: String,
    val user_role: String = "",
    val full_name: String = "",
    val email: String = "",
    val phone_number: String = "",
    val additional_information: String = "",
    val email_verified: Boolean = false,
    val created_at: String = "",
    val is_banned: Boolean = false
)