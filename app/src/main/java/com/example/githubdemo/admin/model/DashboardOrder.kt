package com.example.githubdemo.admin.model

import kotlinx.serialization.Serializable

@Serializable
data class DashboardOrder(
    val id: String,
    val total_amount: Double? = 0.0
)