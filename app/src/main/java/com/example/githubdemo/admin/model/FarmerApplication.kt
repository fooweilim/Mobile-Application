package com.example.githubdemo.admin.model

import kotlinx.serialization.Serializable

@Serializable
data class FarmerApplication(
    val id: String,
    val user_id: String? = null,
    val name: String = "",
    val email: String = "",
    val phone: String? = null,
    val farm_name: String? = null,
    val state: String? = null,
    val farm_type: String? = null,
    val documents: List<String> =
        emptyList(),
    val status: String = "Pending",
    val applied_date: String = ""
)