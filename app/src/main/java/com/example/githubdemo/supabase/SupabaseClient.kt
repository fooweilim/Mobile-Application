package com.example.githubdemo.supabase

import android.net.http.HttpResponseCache.install
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.postgrest.Postgrest

object SupabaseClient {

    val client = createSupabaseClient(
        supabaseUrl = "https://eixrhkrftwdfynuuytxx.supabase.co",
        supabaseKey = "sb_publishable_5EU08AT0AFENgMQBI4LUrQ_xwuR3Fmt"
    ){
        install(Auth)
        install(Postgrest)
    }

}