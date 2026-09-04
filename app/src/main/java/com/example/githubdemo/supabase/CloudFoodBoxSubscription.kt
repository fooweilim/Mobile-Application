package com.example.githubdemo.supabase

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CloudFoodBoxSubscription(
    val id: String,

    @SerialName("user_id")
    val userId: String,

    @SerialName("plan_id")
    val planId: String,

    @SerialName("plan_name")
    val planName: String,

    @SerialName("billing_cycle")
    val billingCycle: String,

    @SerialName("customized_items")
    val customizedItems: String,

    @SerialName("selected_add_ons")
    val selectedAddOns: String,

    @SerialName("delivery_day")
    val deliveryDay: String,

    @SerialName("delivery_address")
    val deliveryAddress: String,

    @SerialName("payment_method")
    val paymentMethod: String,

    @SerialName("total_price")
    val totalPrice: Double,

    val status: String,

    @SerialName("created_at")
    val createdAt: Long
)