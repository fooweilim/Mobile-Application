package com.example.githubdemo.model.farmer

import kotlinx.serialization.Serializable

@Serializable
data class Order(
    val id: String? = null,

    val farmer_id: String? = null,

    val customer_id: String? = null,

    val cart_item_id: String? = null,

    val product_id: String? = null,

    val customer_name: String = "",

    val product_name: String = "",

    val quantity: Int = 0,

    val price: Double = 0.0,

    val status: String = "Pending",

    val payment_method: String = "",

    val created_at: String? = null
)