package com.example.githubdemo.supabase

import com.example.githubdemo.model.market.CartItem
import com.example.githubdemo.model.market.CartProduct
import com.example.githubdemo.model.market.Product
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.postgrest


class CartRepository {


    private suspend fun getUserId():String?{

        return SupabaseConnection.supabase
            .auth
            .currentUserOrNull()
            ?.id

    }



    suspend fun getCartProducts():List<CartProduct>{


        val userId =
            getUserId()
                ?: return emptyList()



        val cartItems =

            SupabaseConnection.supabase
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

            SupabaseConnection.supabase
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

        val userId = getUserId()
            ?: return


        val existingCart =
            SupabaseConnection.supabase
                .postgrest["cart_items"]
                .select{
                    filter {
                        eq(
                            "user_id",
                            userId
                        )

                        eq(
                            "product_id",
                            productId
                        )
                    }
                }
                .decodeList<CartItem>()


        if(existingCart.isNotEmpty()){

            val cart = existingCart.first()

            cart.id?.let { cartId ->

                SupabaseConnection.supabase
                    .postgrest["cart_items"]
                    .update(
                        {
                            set(
                                "quantity",
                                cart.quantity + 1
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

        }else{


            SupabaseConnection.supabase
                .postgrest["cart_items"]
                .insert(

                    CartItem(

                        user_id = userId,

                        product_id = productId,

                        quantity = 1

                    )

                )

        }

    }




    suspend fun updateQuantity(

        cartId:String,

        quantity:Int

    ){


        SupabaseConnection.supabase
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


        SupabaseConnection.supabase
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