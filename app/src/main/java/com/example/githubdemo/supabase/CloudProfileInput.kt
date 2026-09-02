package com.example.githubdemo.supabase

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CloudProfileInput(
    val id: String,

    @SerialName("user_role")
    val userRole: String,

    @SerialName("full_name")
    val fullName: String,

    val email: String,

    @SerialName("phone_number")
    val phoneNumber: String,

    @SerialName("additional_information")
    val additionalInformation: String,

    @SerialName("email_verified")
    val emailVerified: Boolean = true
)