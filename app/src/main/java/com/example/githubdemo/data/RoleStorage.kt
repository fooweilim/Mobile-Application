package com.example.githubdemo.data

import android.content.Context

object RoleStorage {

    val preferenceName =
        "harvest_link_preference"

    val selectedRoleKey =
        "selected_role"

    fun saveSelectedRole(
        context: Context,
        userRole: String
    ) {
        if (UserRole.isValidRole(userRole)) {
            val sharedPreferences =
                context.getSharedPreferences(
                    preferenceName,
                    Context.MODE_PRIVATE
                )

            sharedPreferences.edit()
                .putString(
                    selectedRoleKey,
                    userRole
                )
                .apply()
        }
    }

    fun getSelectedRole(
        context: Context
    ): String? {
        val sharedPreferences =
            context.getSharedPreferences(
                preferenceName,
                Context.MODE_PRIVATE
            )

        val savedRole =
            sharedPreferences.getString(
                selectedRoleKey,
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

    fun clearSelectedRole(
        context: Context
    ) {
        val sharedPreferences =
            context.getSharedPreferences(
                preferenceName,
                Context.MODE_PRIVATE
            )

        sharedPreferences.edit()
            .remove(selectedRoleKey)
            .apply()
    }
}