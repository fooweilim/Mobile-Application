package com.example.githubdemo.admin.repository

import android.util.Log
import com.example.githubdemo.admin.model.DashboardOrder
import com.example.githubdemo.admin.model.DashboardProfile
import com.example.githubdemo.admin.model.DashboardSubscription
import com.example.githubdemo.supabase.SupabaseConnection
import io.github.jan.supabase.postgrest.from

private const val PROFILES_TABLE =
    "profiles"

private const val ORDERS_TABLE =
    "orders"

private const val SUBSCRIPTIONS_TABLE =
    "food_box_subscriptions"

private const val DASHBOARD_TAG =
    "ADMIN_DASHBOARD"

class AdminDashboardRepository {

    private val supabase =
        SupabaseConnection.supabase

    suspend fun getTotalUsers(): Int {
        val profiles =
            supabase
                .from(PROFILES_TABLE)
                .select()
                .decodeList<
                        DashboardProfile
                        >()

        return profiles.count {
                profile ->
            !profile.user_role.equals(
                other = "admin",
                ignoreCase = true
            )
        }
    }

    suspend fun getTotalFarmers(): Int {
        val profiles =
            supabase
                .from(PROFILES_TABLE)
                .select()
                .decodeList<
                        DashboardProfile
                        >()

        return profiles.count {
                profile ->
            profile.user_role.equals(
                other = "farmer",
                ignoreCase = true
            )
        }
    }

    suspend fun getTotalSubscriptions(): Int {
        val subscriptions =
            supabase
                .from(SUBSCRIPTIONS_TABLE)
                .select()
                .decodeList<
                        DashboardSubscription
                        >()

        return subscriptions.size
    }

    suspend fun getMonthlySales(): Double {
        return try {
            val orders =
                supabase
                    .from(ORDERS_TABLE)
                    .select()
                    .decodeList<
                            DashboardOrder
                            >()

            orders.sumOf { order ->
                order.total_amount ?: 0.0
            }
        } catch (exception: Exception) {
            Log.e(
                DASHBOARD_TAG,
                "Unable to load sales: " +
                        exception.message,
                exception
            )

            0.0
        }
    }
}