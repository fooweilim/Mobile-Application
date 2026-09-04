package com.example.githubdemo.repository

import com.example.githubdemo.model.farmer.FarmerOrder
import com.example.githubdemo.model.market.Product
import com.example.githubdemo.supabase.SupabaseConnection
import io.github.jan.supabase.postgrest.from
import kotlinx.serialization.Serializable


@Serializable
data class CreateOrderRequest(
    val customer_id:String,
    val farmer_id:String?,
    val product_id:String?,
    val product_name:String,
    val quantity:Int,
    val price:Double,
    val status:String
)


class FarmerOrderRepository {

    private val supabase =
        SupabaseConnection.supabase


    // Buyer checkout create order
    suspend fun createOrder(
        product: Product,
        quantity: Int,
        customerId: String
    ){

        val order =
            CreateOrderRequest(

                customer_id = customerId,

                farmer_id =
                    product.farmer_id,

                product_id =
                    product.id,

                product_name =
                    product.name,

                quantity =
                    quantity,

                price =
                    product.price * quantity,

                status =
                    "Pending"

            )


        supabase
            .from("orders")
            .insert(order)

    }



    // Farmer get own orders
    suspend fun getFarmerOrders(
        farmerId:String
    ):List<FarmerOrder>{

        return supabase
            .from("orders")
            .select {

                filter {

                    eq(
                        "farmer_id",
                        farmerId
                    )

                }

            }
            .decodeList<FarmerOrder>()

    }



    // Update order status
    suspend fun updateStatus(
        orderId:String,
        newStatus:String
    ){

        supabase
            .from("orders")
            .update({

                set(
                    "status",
                    newStatus
                )

            }){

                filter {

                    eq(
                        "id",
                        orderId
                    )

                }

            }

    }

}