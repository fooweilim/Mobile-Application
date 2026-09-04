package com.example.githubdemo.model.market

import com.example.githubdemo.model.PaymentMethod
import kotlinx.serialization.Serializable

@Serializable
data class MarketOrderInsert(
    val customer_id: String,
    val cart_item_id: String,
    val product_id: String,
    val quantity: Int,
    val payment_method: String
)

data class MarketOrderSummary(
    val itemCount: Int,
    val totalPrice: Double,
    val paymentMethod: PaymentMethod
)

data class MarketPaymentUiState(
    val selectedMethod: PaymentMethod? = null,

    val phoneNumber: String = "",
    val eWalletPassword: String = "",

    val accountNumber: String = "",
    val bankingPassword: String = "",

    val cardNumber: String = "",
    val cardPassword: String = "",

    val phoneError: String? = null,
    val eWalletPasswordError: String? = null,

    val accountError: String? = null,
    val bankingPasswordError: String? = null,

    val cardError: String? = null,
    val cardPasswordError: String? = null,

    val message: String? = null,
    val isLoading: Boolean = false,

    val orderSummary: MarketOrderSummary? = null
)