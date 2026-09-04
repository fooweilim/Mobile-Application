package com.example.githubdemo.admin.repository

import com.example.githubdemo.admin.model.FoodBox
import com.example.githubdemo.admin.model.FoodItem
import com.example.githubdemo.supabase.SupabaseConnection
import io.github.jan.supabase.postgrest.from

private const val FOOD_BOX_TABLE =
    "food_boxes"

private const val PRODUCT_TABLE =
    "products"

class FoodBoxRepository {

    private val supabase =
        SupabaseConnection.supabase

    suspend fun getFoodBoxes():
            List<FoodBox> {
        return supabase
            .from(FOOD_BOX_TABLE)
            .select()
            .decodeList<FoodBox>()
    }

    suspend fun getFoodItems(
        foodBoxId: String
    ): List<FoodItem> {
        return supabase
            .from(PRODUCT_TABLE)
            .select {
                filter {
                    eq(
                        column = "food_box_id",
                        value = foodBoxId
                    )
                }
            }
            .decodeList<FoodItem>()
    }

    suspend fun deleteFoodItem(
        id: String
    ) {
        supabase
            .from(PRODUCT_TABLE)
            .delete {
                filter {
                    eq(
                        column = "id",
                        value = id
                    )
                }
            }
    }
}