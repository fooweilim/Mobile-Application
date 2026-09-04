package com.example.githubdemo.admin.model

import kotlinx.serialization.Serializable

@Serializable
data class FoodBox(
    val id: String,
    val name: String = "",
    val price: Double = 0.0,
    val duration: String = "",
    val description: String = ""
)