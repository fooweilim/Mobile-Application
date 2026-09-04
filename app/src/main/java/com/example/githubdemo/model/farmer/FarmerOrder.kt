package com.example.githubdemo.model.farmer


import kotlinx.serialization.Serializable


@Serializable
data class FarmerOrder(


    val id: String? = null,


    // buyer profile id
    val customer_id: String? = null,


    // farmer profile id
    val farmer_id: String? = null,


    // product
    val product_id: String? = null,


    val product_name: String = "",


    val quantity: Int = 0,


    val price: Double = 0.0,


    val status: String = "Pending",


    val created_at: String? = null


)