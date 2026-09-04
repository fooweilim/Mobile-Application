package com.example.githubdemo.supabase

import com.example.githubdemo.model.farmer.Order
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.postgrest

class CustomerOrderRepository {

    suspend fun getMyOrders():
            List<Order> {

        val userId =
            SupabaseConnection
                .supabase
                .auth
                .currentUserOrNull()
                ?.id
                ?: throw IllegalStateException(
                    "Please sign in again."
                )

        val orders =
            SupabaseConnection
                .supabase
                .postgrest
                .from("orders")
                .select {
                    filter {
                        eq(
                            "customer_id",
                            userId
                        )
                    }
                }
                .decodeList<Order>()

        val products = try {
            ProductRepository()
                .getProducts()
        } catch (exception: Exception) {
            emptyList()
        }

        return orders.map { order ->
            val product =
                products.firstOrNull {
                    it.id ==
                            order.product_id
                }

            order.copy(
                farmer_id =
                    order.farmer_id
                        .orEmpty()
                        .ifBlank {
                            product
                                ?.farmer_id
                                .orEmpty()
                        },
                product_name =
                    order.product_name
                        .ifBlank {
                            product?.name
                                ?: "Product"
                        },
                price =
                    if (order.price > 0.0) {
                        order.price
                    } else {
                        product?.price ?: 0.0
                    }
            )
        }
    }
}