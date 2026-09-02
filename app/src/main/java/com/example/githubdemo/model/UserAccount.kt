package com.example.githubdemo.model

data class UserAccount(
    val userRole: String,
    val fullName: String,
    val email: String,
    val phoneNumber: String,
    val additionalInformation: String,
    val password: String
)