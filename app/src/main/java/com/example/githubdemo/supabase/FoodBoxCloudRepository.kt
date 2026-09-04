package com.example.githubdemo.supabase

import com.example.githubdemo.model.BillingCycle
import com.example.githubdemo.model.FoodBoxAddOn
import com.example.githubdemo.model.FoodBoxItem
import com.example.githubdemo.model.FoodBoxSubscription
import com.example.githubdemo.model.PaymentMethod
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

object FoodBoxCloudRepository {

    private const val TABLE_NAME =
        "food_box_subscriptions"

    private const val ACTIVE_STATUS =
        "active"

    private val json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
    }

    suspend fun createSubscription(
        subscription: FoodBoxSubscription
    ): Result<Unit> {
        return try {
            if (subscription.userId.isBlank()) {
                return Result.failure(
                    Exception(
                        "Please sign in before subscribing."
                    )
                )
            }

            val existingSubscription =
                getActiveSubscription(
                    subscription.userId
                ).getOrThrow()

            if (existingSubscription != null) {
                return Result.failure(
                    Exception(
                        "You already have an active Food Box " +
                                "subscription. Cancel it before " +
                                "subscribing to another box."
                    )
                )
            }

            SupabaseConnection
                .supabase
                .postgrest
                .from(TABLE_NAME)
                .insert(
                    subscription.toCloud()
                )

            Result.success(Unit)
        } catch (exception: Exception) {
            Result.failure(
                Exception(
                    toUserMessage(exception)
                )
            )
        }
    }

    suspend fun getActiveSubscription(
        userId: String
    ): Result<FoodBoxSubscription?> {
        return try {
            if (userId.isBlank()) {
                return Result.success(null)
            }

            val currentUserId =
                SupabaseConnection
                    .supabase
                    .auth
                    .currentUserOrNull()
                    ?.id
                    ?: return Result.failure(
                        Exception(
                            "Please sign in again."
                        )
                    )

            if (currentUserId != userId) {
                return Result.failure(
                    Exception(
                        "The signed-in user does not match " +
                                "this subscription."
                    )
                )
            }

            val rows =
                SupabaseConnection
                    .supabase
                    .postgrest
                    .from(TABLE_NAME)
                    .select {
                        filter {
                            eq(
                                "user_id",
                                userId
                            )

                            eq(
                                "status",
                                ACTIVE_STATUS
                            )
                        }

                        limit(1)
                    }
                    .decodeList<
                            CloudFoodBoxSubscription
                            >()

            Result.success(
                rows.firstOrNull()
                    ?.toModel()
            )
        } catch (exception: Exception) {
            Result.failure(exception)
        }
    }

    suspend fun updateSubscription(
        subscription: FoodBoxSubscription
    ): Result<Unit> {
        return try {
            SupabaseConnection
                .supabase
                .postgrest
                .from(TABLE_NAME)
                .update(
                    subscription.toCloud()
                ) {
                    filter {
                        eq(
                            "id",
                            subscription.id
                        )

                        eq(
                            "user_id",
                            subscription.userId
                        )
                    }
                }

            Result.success(Unit)
        } catch (exception: Exception) {
            Result.failure(exception)
        }
    }

    suspend fun deleteSubscription(
        subscriptionId: String
    ): Result<Unit> {
        return try {
            SupabaseConnection
                .supabase
                .postgrest
                .from(TABLE_NAME)
                .delete {
                    filter {
                        eq(
                            "id",
                            subscriptionId
                        )
                    }
                }

            Result.success(Unit)
        } catch (exception: Exception) {
            Result.failure(exception)
        }
    }

    private fun FoodBoxSubscription.toCloud():
            CloudFoodBoxSubscription {

        return CloudFoodBoxSubscription(
            id = id,
            userId = userId,
            planId = planId,
            planName = planName,
            billingCycle =
                billingCycle.name.lowercase(),
            customizedItems =
                json.encodeToString(
                    customizedItems
                ),
            selectedAddOns =
                json.encodeToString(
                    selectedAddOns
                ),
            deliveryDay = deliveryDay,
            deliveryAddress =
                deliveryAddress,
            paymentMethod =
                paymentMethod.name.lowercase(),
            totalPrice = totalPrice,
            status = status,
            createdAt = createdAt
        )
    }

    private fun CloudFoodBoxSubscription.toModel():
            FoodBoxSubscription {

        return FoodBoxSubscription(
            id = id,
            userId = userId,
            planId = planId,
            planName = planName,
            billingCycle =
                BillingCycle.valueOf(
                    billingCycle.uppercase()
                ),
            customizedItems =
                json.decodeFromString<
                        List<FoodBoxItem>
                        >(customizedItems),
            selectedAddOns =
                json.decodeFromString<
                        List<FoodBoxAddOn>
                        >(selectedAddOns),
            deliveryDay = deliveryDay,
            deliveryAddress =
                deliveryAddress,
            paymentMethod =
                PaymentMethod.valueOf(
                    paymentMethod.uppercase()
                ),
            totalPrice = totalPrice,
            status = status,
            createdAt = createdAt
        )
    }

    private fun toUserMessage(
        exception: Exception
    ): String {
        val message =
            exception.message.orEmpty()

        val normalized =
            message.lowercase()

        return if (
            normalized.contains("23505") ||
            normalized.contains(
                "duplicate key"
            ) ||
            normalized.contains(
                "one_active_food_box_per_user"
            )
        ) {
            "You already have an active Food Box " +
                    "subscription. Cancel it before " +
                    "subscribing to another box."
        } else {
            message.ifBlank {
                "Unable to save the Food Box subscription."
            }
        }
    }
}