package com.example.githubdemo.model.market

import kotlinx.serialization.Serializable

@Serializable
data class CartItem(

    val id:String? = null,

    val user_id:String? = null,

    val product_id:String,

    val quantity:Int

)