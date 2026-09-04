package com.example.githubdemo.admin.repository

import com.example.githubdemo.admin.model.FarmerApplication
import com.example.githubdemo.supabase.SupabaseConnection
import io.github.jan.supabase.postgrest.from

private const val FARMER_APPLICATION_TABLE =
    "farmer_applications"

class FarmerVerificationRepository {

    private val supabase =
        SupabaseConnection.supabase

    suspend fun getFarmers():
            List<FarmerApplication> {
        return supabase
            .from(FARMER_APPLICATION_TABLE)
            .select()
            .decodeList<FarmerApplication>()
    }

    suspend fun updateStatus(
        id: String,
        status: String
    ) {
        supabase
            .from(FARMER_APPLICATION_TABLE)
            .update(
                {
                    set(
                        column = "status",
                        value = status
                    )
                }
            ) {
                filter {
                    eq(
                        column = "id",
                        value = id
                    )
                }
            }
    }
}