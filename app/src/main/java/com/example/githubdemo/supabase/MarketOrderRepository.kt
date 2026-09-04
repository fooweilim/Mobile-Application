package com.example.githubdemo.supabase

import com.example.githubdemo.model.PaymentMethod
import com.example.githubdemo.model.market.CartProduct
import com.example.githubdemo.model.market.MarketOrderInsert
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.postgrest

class MarketOrderRepository {

    suspend fun placeOrder(
        customerId: String,
        items: List<CartProduct>,
        paymentMethod: PaymentMethod
    ) {
        val signedInUserId =
            SupabaseConnection.supabase
                .auth
                .currentUserOrNull()
                ?.id
                ?: throw IllegalStateException(
                    "Please sign in before placing an order."
                )

        if (
            customerId.isBlank() ||
            customerId != signedInUserId
        ) {
            throw IllegalStateException(
                "Your local account does not match the " +
                        "signed-in account. Please sign in again."
            )
        }

        if (items.isEmpty()) {
            throw IllegalStateException(
                "Your checkout is empty."
            )
        }

        val orderRows =
            items.map { item ->

                val productId =
                    item.product.id
                        ?.takeIf {
                            it.isNotBlank()
                        }
                        ?: throw IllegalStateException(
                            "${item.product.name} has no product ID."
                        )

                if (
                    item.product.farmer_id
                        .isNullOrBlank()
                ) {
                    throw IllegalStateException(
                        "${item.product.name} is not linked " +
                                "to a farmer. Ask the farmer to " +
                                "save the product again."
                    )
                }

                if (item.quantity <= 0) {
                    throw IllegalStateException(
                        "${item.product.name} has an invalid quantity."
                    )
                }

                if (item.cartId.isBlank()) {
                    throw IllegalStateException(
                        "${item.product.name} has no cart item ID."
                    )
                }

                MarketOrderInsert(
                    customer_id = customerId,

                    cart_item_id =
                        item.cartId,

                    product_id =
                        productId,

                    quantity =
                        item.quantity,

                    payment_method =
                        paymentMethod
                            .toDatabaseValue()
                )
            }

        /*
         * Passwords, bank account numbers and card
         * numbers are intentionally not passed here.
         */
        SupabaseConnection.supabase
            .postgrest[ORDER_TABLE]
            .insert(orderRows)
    }

    private fun PaymentMethod
            .toDatabaseValue(): String {

        return when (this) {
            PaymentMethod.E_WALLET ->
                "Touch 'n Go"

            PaymentMethod.ONLINE_BANKING ->
                "Online Banking"

            PaymentMethod.CARD ->
                "Credit / Debit Card"
        }
    }

    private companion object {

        const val ORDER_TABLE =
            "orders"
    }
}