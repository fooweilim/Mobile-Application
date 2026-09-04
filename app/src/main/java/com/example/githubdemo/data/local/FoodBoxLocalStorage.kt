package com.example.githubdemo.data.local

import android.content.Context
import com.example.githubdemo.model.FoodBoxReminderSettings
import com.example.githubdemo.model.FoodBoxSubscription
import com.example.githubdemo.model.FoodBoxUiState
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

object FoodBoxLocalStorage {

    private const val PREFERENCE_NAME =
        "harvestlink_food_box"

    private const val KEY_DRAFT =
        "food_box_draft"

    private const val KEY_SUBSCRIPTION =
        "food_box_subscription"

    private const val KEY_REMINDERS =
        "food_box_reminders"

    private val json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
    }

    fun saveDraft(
        context: Context,
        state: FoodBoxUiState
    ) {
        val draft = state.copy(
            activeSubscription = null,
            itemBeingSwappedId = null,
            isLoading = false,
            message = null
        )

        preferences(context)
            .edit()
            .putString(
                KEY_DRAFT,
                json.encodeToString(draft)
            )
            .apply()
    }

    fun loadDraft(
        context: Context
    ): FoodBoxUiState? {
        val savedValue =
            preferences(context)
                .getString(
                    KEY_DRAFT,
                    null
                )

        return decode(savedValue)
    }

    fun saveSubscription(
        context: Context,
        subscription: FoodBoxSubscription
    ) {
        preferences(context)
            .edit()
            .putString(
                KEY_SUBSCRIPTION,
                json.encodeToString(
                    subscription
                )
            )
            .apply()
    }

    fun loadSubscription(
        context: Context
    ): FoodBoxSubscription? {
        val savedValue =
            preferences(context)
                .getString(
                    KEY_SUBSCRIPTION,
                    null
                )

        return decode(savedValue)
    }

    fun clearSubscription(
        context: Context
    ) {
        preferences(context)
            .edit()
            .remove(KEY_SUBSCRIPTION)
            .apply()
    }

    fun saveReminderSettings(
        context: Context,
        settings: FoodBoxReminderSettings
    ) {
        preferences(context)
            .edit()
            .putString(
                KEY_REMINDERS,
                json.encodeToString(settings)
            )
            .apply()
    }

    fun loadReminderSettings(
        context: Context
    ): FoodBoxReminderSettings {
        val savedValue =
            preferences(context)
                .getString(
                    KEY_REMINDERS,
                    null
                )

        return decode<FoodBoxReminderSettings>(
            savedValue
        ) ?: FoodBoxReminderSettings()
    }

    private inline fun <reified T> decode(
        value: String?
    ): T? {
        if (value.isNullOrBlank()) {
            return null
        }

        return runCatching {
            json.decodeFromString<T>(value)
        }.getOrNull()
    }

    private fun preferences(
        context: Context
    ) = context.getSharedPreferences(
        PREFERENCE_NAME,
        Context.MODE_PRIVATE
    )
}