package com.example.githubdemo.data.meals


import androidx.annotation.DrawableRes


data class Meal(

    val id: Int,

    val name: String,

    val price: Double,

    val pax: Int,

    val supportedPax: List<Int>,


    val protein: String,

    val calories: String,

    val vitamins: String,


    @DrawableRes
    val imageRes: Int,


    val ingredients: List<String>,


    val seasonings: List<String>

)