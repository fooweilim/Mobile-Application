package com.example.githubdemo.supabase

import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest

object SupabaseConnection {

    private const val SUPABASE_URL =
        "https://eixrhkrftwdfynuuytxx.supabase.co"

    private const val SUPABASE_KEY =
        "sb_publishable_5EU08AT0AFENgMQBI4LUrQ_xwuR3Fmt"

    val supabase = createSupabaseClient(
        supabaseUrl = SUPABASE_URL,
        supabaseKey = SUPABASE_KEY
    ) {
        install(Auth)
        install(Postgrest)
    }
}