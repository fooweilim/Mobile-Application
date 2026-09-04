package com.example.githubdemo.admin.model

import kotlinx.serialization.Serializable

@Serializable
data class FoodItem(
    val id: String,
    val food_box_id: String? = null,
    var name: String = "",
    var weight: String? = null,
    var price: Double = 0.0
)