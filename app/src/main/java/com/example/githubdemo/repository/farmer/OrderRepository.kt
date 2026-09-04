package com.example.githubdemo.repository.farmer

import com.example.githubdemo.model.farmer.Order
import com.example.githubdemo.supabase.SupabaseConnection
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.from

class OrderRepository {

    private val supabase =
        SupabaseConnection.supabase

    suspend fun getOrders():
            List<Order> {
        val farmerId =
            getFarmerId()

        return supabase
            .from(ORDER_TABLE)
            .select {
                filter {
                    eq(
                        "farmer_id",
                        farmerId
                    )
                }
            }
            .decodeList<Order>()
    }

    suspend fun updateStatus(
        id: String,
        status: String
    ) {
        val farmerId =
            getFarmerId()

        supabase
            .from(ORDER_TABLE)
            .update(
                {
                    set(
                        "status",
                        status
                    )
                }
            ) {
                filter {
                    eq(
                        "id",
                        id
                    )

                    eq(
                        "farmer_id",
                        farmerId
                    )
                }
            }
    }

    private fun getFarmerId(): String {
        return supabase
            .auth
            .currentUserOrNull()
            ?.id
            ?: throw IllegalStateException(
                "Please sign in as a farmer first."
            )
    }

    private companion object {

        const val ORDER_TABLE =
            "orders"
    }
}