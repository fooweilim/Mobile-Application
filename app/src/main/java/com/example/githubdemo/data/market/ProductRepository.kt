package com.example.githubdemo.data.market

import com.example.githubdemo.model.market.Product
import com.example.githubdemo.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest

class ProductRepository {


    suspend fun getProducts():List<Product>{


        return SupabaseClient.client
            .postgrest["products"]
            .select()
            .decodeList<Product>()


    }


}