package com.example.githubdemo.data.local

import android.content.Context
import com.example.githubdemo.data.UserRole
import com.example.githubdemo.supabase.CloudProfile

object LocalAccountStorage {

    private const val PREFERENCE_NAME =
        "harvestlink_local_account"

    private const val KEY_SELECTED_ROLE =
        "selected_role"

    private const val KEY_USER_ID =
        "user_id"

    private const val KEY_USER_ROLE =
        "user_role"

    private const val KEY_FULL_NAME =
        "full_name"

    private const val KEY_EMAIL =
        "email"

    private const val KEY_PHONE_NUMBER =
        "phone_number"

    private const val KEY_ADDITIONAL_INFORMATION =
        "additional_information"

    private const val KEY_EMAIL_VERIFIED =
        "email_verified"

    fun saveSelectedRole(
        context: Context,
        userRole: String
    ) {
        if (!UserRole.isValidRole(userRole)) {
            return
        }

        getPreferences(context)
            .edit()
            .putString(
                KEY_SELECTED_ROLE,
                userRole
            )
            .apply()
    }

    fun getSelectedRole(
        context: Context
    ): String? {
        val savedRole =
            getPreferences(context)
                .getString(
                    KEY_SELECTED_ROLE,
                    null
                )

        return if (
            savedRole != null &&
            UserRole.isValidRole(savedRole)
        ) {
            savedRole
        } else {
            null
        }
    }

    fun saveProfile(
        context: Context,
        profile: CloudProfile
    ) {
        getPreferences(context)
            .edit()
            .putString(
                KEY_USER_ID,
                profile.id
            )
            .putString(
                KEY_USER_ROLE,
                profile.userRole
            )
            .putString(
                KEY_FULL_NAME,
                profile.fullName
            )
            .putString(
                KEY_EMAIL,
                profile.email
            )
            .putString(
                KEY_PHONE_NUMBER,
                profile.phoneNumber
            )
            .putString(
                KEY_ADDITIONAL_INFORMATION,
                profile.additionalInformation
            )
            .putBoolean(
                KEY_EMAIL_VERIFIED,
                profile.emailVerified
            )
            .apply()
    }

    fun getProfile(
        context: Context
    ): CloudProfile? {
        val preferences =
            getPreferences(context)

        val userId =
            preferences.getString(
                KEY_USER_ID,
                null
            ) ?: return null

        val userRole =
            preferences.getString(
                KEY_USER_ROLE,
                null
            ) ?: return null

        val fullName =
            preferences.getString(
                KEY_FULL_NAME,
                null
            ) ?: return null

        val email =
            preferences.getString(
                KEY_EMAIL,
                null
            ) ?: return null

        return CloudProfile(
            id = userId,

            userRole = userRole,

            fullName = fullName,

            email = email,

            phoneNumber =
                preferences.getString(
                    KEY_PHONE_NUMBER,
                    ""
                ).orEmpty(),

            additionalInformation =
                preferences.getString(
                    KEY_ADDITIONAL_INFORMATION,
                    ""
                ).orEmpty(),

            emailVerified =
                preferences.getBoolean(
                    KEY_EMAIL_VERIFIED,
                    false
                )
        )
    }

    fun clearAll(
        context: Context
    ) {
        getPreferences(context)
            .edit()
            .clear()
            .apply()
    }

    private fun getPreferences(
        context: Context
    ) = context.getSharedPreferences(
        PREFERENCE_NAME,
        Context.MODE_PRIVATE
    )
}