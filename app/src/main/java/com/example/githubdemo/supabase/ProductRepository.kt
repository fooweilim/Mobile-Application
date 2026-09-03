package com.example.githubdemo.supabase

import com.example.githubdemo.model.market.Product
import io.github.jan.supabase.postgrest.postgrest

class ProductRepository {


    suspend fun getProducts():List<Product>{


        return SupabaseConnection.supabase
            .postgrest["products"]
            .select()
            .decodeList<Product>()


    }


}