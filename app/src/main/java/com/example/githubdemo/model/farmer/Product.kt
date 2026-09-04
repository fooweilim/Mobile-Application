package com.example.githubdemo.model.farmer

import kotlinx.serialization.Serializable

@Serializable
data class Product(
    val id: String? = null,

    val farmer_id: String? = null,

    val name: String = "",

    val category: String = "",

    val price: Double = 0.0,

    val stock: Int = 0,

    val sold: Int = 0,

    val description: String? = null,

    val image_url: String? = null,

    val status: String = "ACTIVE"
)