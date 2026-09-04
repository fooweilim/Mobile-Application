package com.example.githubdemo.supabase

import android.graphics.Bitmap
import com.example.githubdemo.model.market.Product
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.storage.storage
import java.io.ByteArrayOutputStream

class FarmerProductRepository {

    private val supabase =
        SupabaseConnection.supabase


    suspend fun getProducts():List<Product>{

        return supabase
            .postgrest
            .from(PRODUCT_TABLE)
            .select {

                filter {

                    eq(
                        "farmer_id",
                        currentFarmerId()
                    )

                }

            }
            .decodeList<Product>()

    }


    suspend fun getProductById(
        productId:String
    ):Product?{

        return supabase
            .postgrest
            .from(PRODUCT_TABLE)
            .select {

                filter {

                    eq(
                        "id",
                        productId
                    )

                    eq(
                        "farmer_id",
                        currentFarmerId()
                    )

                }

            }
            .decodeSingleOrNull<Product>()

    }


    suspend fun addProduct(
        product:Product
    ){

        supabase
            .postgrest
            .from(PRODUCT_TABLE)
            .insert(

                product.copy(

                    id = null,

                    farmer_id =
                        currentFarmerId()

                )

            )

    }


    suspend fun updateProduct(
        productId:String,
        product:Product
    ){

        supabase
            .postgrest
            .from(PRODUCT_TABLE)
            .update({

                set(
                    "name",
                    product.name
                )

                set(
                    "category",
                    product.category
                )

                set(
                    "price",
                    product.price
                )

                set(
                    "stock",
                    product.stock
                )

                set(
                    "description",
                    product.description
                )

                set(
                    "image_url",
                    product.image_url
                )

                set(
                    "status",
                    product.status
                )

            }){

                filter {

                    eq(
                        "id",
                        productId
                    )

                    eq(
                        "farmer_id",
                        currentFarmerId()
                    )

                }

            }

    }


    suspend fun deleteProduct(
        productId:String
    ){

        supabase
            .postgrest
            .from(PRODUCT_TABLE)
            .delete {

                filter {

                    eq(
                        "id",
                        productId
                    )

                    eq(
                        "farmer_id",
                        currentFarmerId()
                    )

                }

            }

    }


    suspend fun uploadProductImage(
        bitmap:Bitmap,
        fileName:String
    ):String{

        val bytes =
            ByteArrayOutputStream()
                .use { outputStream ->

                    bitmap.compress(
                        Bitmap.CompressFormat.JPEG,
                        IMAGE_QUALITY,
                        outputStream
                    )

                    outputStream.toByteArray()

                }


        val imagePath =
            "${currentFarmerId()}/$fileName"


        val bucket =
            supabase
                .storage
                .from(PRODUCT_IMAGE_BUCKET)


        bucket.upload(
            path = imagePath,
            data = bytes
        ){

            upsert = true

        }


        return bucket.publicUrl(
            imagePath
        )

    }


    private suspend fun currentFarmerId():String{

        val authId =
            supabase
                .auth
                .currentUserOrNull()
                ?.id
                ?: throw IllegalStateException(
                    "Login session expired"
                )


        val profile =
            supabase
                .postgrest
                .from("profiles")
                .select {

                    filter {

                        eq(
                            "id",
                            authId
                        )

                        eq(
                            "user_role",
                            "farmer"
                        )

                    }

                }
                .decodeSingleOrNull<CloudProfile>()


        return profile?.id
            ?: throw IllegalStateException(
                "Farmer profile not found"
            )

    }


    private companion object{

        const val PRODUCT_TABLE =
            "products"

        const val PRODUCT_IMAGE_BUCKET =
            "product-images"

        const val IMAGE_QUALITY =
            90

    }

}