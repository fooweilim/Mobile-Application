package com.example.githubdemo.admin.model

import kotlinx.serialization.Serializable

@Serializable
data class DashboardProfile(
    val id: String,
    val user_role: String
)