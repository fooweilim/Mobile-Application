package com.example.githubdemo.model.market

data class CartProduct(

    val cartId:String,

    val productId:String,

    val product:Product,

    val quantity:Int

)