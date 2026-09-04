package com.example.githubdemo.repository.farmer

import android.graphics.Bitmap
import com.example.githubdemo.model.farmer.Product
import com.example.githubdemo.supabase.SupabaseConnection
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.storage.storage
import java.io.ByteArrayOutputStream

class ProductFarmerRepository {

    private val supabase =
        SupabaseConnection.supabase

    private fun getFarmerId(): String {
        return supabase
            .auth
            .currentUserOrNull()
            ?.id
            ?: throw IllegalStateException(
                "Please sign in as a farmer first."
            )
    }

    suspend fun getProducts():
            List<Product> {
        val farmerId =
            getFarmerId()

        return supabase
            .from(PRODUCT_TABLE)
            .select {
                filter {
                    eq(
                        "farmer_id",
                        farmerId
                    )
                }
            }
            .decodeList<Product>()
    }

    suspend fun addProduct(
        product: Product
    ) {
        val productWithFarmer =
            product.copy(
                farmer_id =
                    getFarmerId()
            )

        supabase
            .from(PRODUCT_TABLE)
            .insert(
                productWithFarmer
            )
    }

    suspend fun getProductById(
        id: String
    ): Product? {
        val farmerId =
            getFarmerId()

        return supabase
            .from(PRODUCT_TABLE)
            .select {
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
            .decodeSingleOrNull<Product>()
    }

    suspend fun updateProduct(
        id: String,
        product: Product
    ) {
        val farmerId =
            getFarmerId()

        val updatedProduct =
            product.copy(
                farmer_id =
                    farmerId
            )

        supabase
            .from(PRODUCT_TABLE)
            .update(
                updatedProduct
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

    suspend fun deleteProduct(
        id: String
    ) {
        val farmerId =
            getFarmerId()

        supabase
            .from(PRODUCT_TABLE)
            .delete {
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

    suspend fun uploadProductImage(
        bitmap: Bitmap,
        fileName: String
    ): String {
        val outputStream =
            ByteArrayOutputStream()

        bitmap.compress(
            Bitmap.CompressFormat.JPEG,
            IMAGE_QUALITY,
            outputStream
        )

        val imagePath =
            "${getFarmerId()}/$fileName"

        val bucket =
            supabase
                .storage
                .from(
                    PRODUCT_IMAGE_BUCKET
                )

        bucket.upload(
            path = imagePath,

            data =
                outputStream
                    .toByteArray()
        ) {
            upsert = true
        }

        return bucket.publicUrl(
            imagePath
        )
    }

    private companion object {

        const val PRODUCT_TABLE =
            "products"

        const val PRODUCT_IMAGE_BUCKET =
            "product-images"

        const val IMAGE_QUALITY =
            90
    }
}