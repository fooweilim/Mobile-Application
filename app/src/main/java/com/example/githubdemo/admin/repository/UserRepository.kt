package com.example.githubdemo.admin.repository

import com.example.githubdemo.admin.model.ProfileDto
import com.example.githubdemo.supabase.SupabaseConnection
import io.github.jan.supabase.postgrest.from

private const val PROFILE_TABLE =
    "profiles"

class UserRepository {

    private val supabase =
        SupabaseConnection.supabase

    suspend fun getUsers():
            List<ProfileDto> {
        return supabase
            .from(PROFILE_TABLE)
            .select()
            .decodeList<ProfileDto>()
    }

    suspend fun updateBanStatus(
        id: String,
        banned: Boolean
    ) {
        supabase
            .from(PROFILE_TABLE)
            .update(
                {
                    set(
                        column = "is_banned",
                        value = banned
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

    suspend fun deleteUser(
        id: String
    ) {
        supabase
            .from(PROFILE_TABLE)
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