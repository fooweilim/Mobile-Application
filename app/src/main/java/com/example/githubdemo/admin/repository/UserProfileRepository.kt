package com.example.githubdemo.admin.repository

import com.example.githubdemo.admin.model.ProfileDto
import com.example.githubdemo.supabase.SupabaseConnection
import io.github.jan.supabase.postgrest.from

private const val PROFILE_TABLE =
    "profiles"

class UserProfileRepository {

    private val supabase =
        SupabaseConnection.supabase

    suspend fun getUserById(
        id: String
    ): ProfileDto? {
        return supabase
            .from(PROFILE_TABLE)
            .select {
                filter {
                    eq(
                        column = "id",
                        value = id
                    )
                }

                limit(1)
            }
            .decodeList<ProfileDto>()
            .firstOrNull()
    }
}