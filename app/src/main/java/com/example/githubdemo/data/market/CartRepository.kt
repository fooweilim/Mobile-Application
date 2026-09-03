package com.example.githubdemo.data.market

import com.example.githubdemo.model.market.CartItem
import com.example.githubdemo.model.market.CartProduct
import com.example.githubdemo.model.market.Product
import com.example.githubdemo.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.postgrest


class CartRepository {


    private suspend fun getUserId():String?{

        return SupabaseClient.client
            .auth
            .currentUserOrNull()
            ?.id

    }



    suspend fun getCartProducts():List<CartProduct>{


        val userId =
            getUserId()
                ?: return emptyList()



        val cartItems =

            SupabaseClient.client
                .postgrest["cart_items"]
                .select{

                    filter {

                        eq(
                            "user_id",
                            userId
                        )

                    }

                }
                .decodeList<CartItem>()





        val products =

            SupabaseClient.client
                .postgrest["products"]
                .select()
                .decodeList<Product>()





        return cartItems.mapNotNull { cart ->


            val product =

                products.find {

                    it.id == cart.product_id

                }



            if(product != null){


                CartProduct(

                    cartId = cart.id ?: "",

                    productId = cart.product_id,

                    product = product,

                    quantity = cart.quantity

                )


            }else{

                null

            }


        }


    }




    suspend fun addCart(

        productId:String

    ){


        val userId =
            getUserId()
                ?: return



        SupabaseClient.client
            .postgrest["cart_items"]
            .insert(

                CartItem(

                    user_id = userId,

                    product_id = productId,

                    quantity = 1

                )

            )


    }




    suspend fun updateQuantity(

        cartId:String,

        quantity:Int

    ){


        SupabaseClient.client
            .postgrest["cart_items"]
            .update(

                {

                    set(
                        "quantity",
                        quantity
                    )

                }

            ){

                filter {

                    eq(
                        "id",
                        cartId
                    )

                }

            }


    }




    suspend fun deleteCart(

        cartId:String

    ){


        SupabaseClient.client
            .postgrest["cart_items"]
            .delete {


                filter {

                    eq(
                        "id",
                        cartId
                    )

                }


            }


    }


}