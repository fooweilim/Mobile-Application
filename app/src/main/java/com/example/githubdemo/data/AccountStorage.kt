package com.example.githubdemo.data

import android.content.Context
import com.example.githubdemo.model.UserAccount
import java.security.MessageDigest
import org.json.JSONObject

object AccountStorage {

    val preferenceName =
        "harvest_link_accounts"

    val adminEmail =
        "admin@harvestlink.com"

    val adminPassword =
        "Admin123"

    fun saveAccount(
        context: Context,
        userAccount: UserAccount
    ): Boolean {

        /*
         * Admin registration is not allowed.
         * Admin uses the account prepared
         * by the system.
         */
        if (
            userAccount.userRole ==
            UserRole.ADMIN
        ) {
            return false
        }

        if (
            !UserRole.canSignUp(
                userAccount.userRole
            )
        ) {
            return false
        }

        val normalizedEmail =
            normalizeEmail(
                userAccount.email
            )

        if (
            accountExists(
                context = context,
                userRole =
                    userAccount.userRole,
                email = normalizedEmail
            )
        ) {
            return false
        }

        val accountData =
            JSONObject().apply {

                put(
                    "userRole",
                    userAccount.userRole
                )

                put(
                    "fullName",
                    userAccount.fullName.trim()
                )

                put(
                    "email",
                    normalizedEmail
                )

                put(
                    "phoneNumber",
                    userAccount
                        .phoneNumber
                        .trim()
                )

                put(
                    "additionalInformation",
                    userAccount
                        .additionalInformation
                        .trim()
                )

                put(
                    "passwordHash",
                    hashPassword(
                        normalizedEmail +
                                userAccount.password
                    )
                )
            }

        val sharedPreferences =
            context.getSharedPreferences(
                preferenceName,
                Context.MODE_PRIVATE
            )

        sharedPreferences
            .edit()
            .putString(
                createAccountKey(
                    userRole =
                        userAccount.userRole,
                    email =
                        normalizedEmail
                ),
                accountData.toString()
            )
            .apply()

        return true
    }

    fun accountExists(
        context: Context,
        userRole: String,
        email: String
    ): Boolean {
        val sharedPreferences =
            context.getSharedPreferences(
                preferenceName,
                Context.MODE_PRIVATE
            )

        return sharedPreferences.contains(
            createAccountKey(
                userRole = userRole,
                email =
                    normalizeEmail(email)
            )
        )
    }

    fun validateLogin(
        context: Context,
        userRole: String,
        email: String,
        password: String
    ): Boolean {

        /*
         * Admin account is prepared by
         * the application.
         */
        if (
            userRole == UserRole.ADMIN
        ) {
            return normalizeEmail(email) ==
                    normalizeEmail(adminEmail) &&
                    password == adminPassword
        }

        val normalizedEmail =
            normalizeEmail(email)

        val sharedPreferences =
            context.getSharedPreferences(
                preferenceName,
                Context.MODE_PRIVATE
            )

        val savedAccount =
            sharedPreferences.getString(
                createAccountKey(
                    userRole = userRole,
                    email = normalizedEmail
                ),
                null
            ) ?: return false

        return try {
            val accountData =
                JSONObject(savedAccount)

            val savedPasswordHash =
                accountData.getString(
                    "passwordHash"
                )

            val enteredPasswordHash =
                hashPassword(
                    normalizedEmail +
                            password
                )

            savedPasswordHash ==
                    enteredPasswordHash

        } catch (exception: Exception) {
            false
        }
    }

    fun updatePassword(
        context: Context,
        userRole: String,
        email: String,
        newPassword: String
    ): Boolean {

        /*
         * Admin password cannot be changed
         * from the application.
         */
        if (
            userRole == UserRole.ADMIN
        ) {
            return false
        }

        val normalizedEmail =
            normalizeEmail(email)

        val accountKey =
            createAccountKey(
                userRole = userRole,
                email = normalizedEmail
            )

        val sharedPreferences =
            context.getSharedPreferences(
                preferenceName,
                Context.MODE_PRIVATE
            )

        val savedAccount =
            sharedPreferences.getString(
                accountKey,
                null
            ) ?: return false

        return try {
            val accountData =
                JSONObject(savedAccount)

            accountData.put(
                "passwordHash",
                hashPassword(
                    normalizedEmail +
                            newPassword
                )
            )

            sharedPreferences
                .edit()
                .putString(
                    accountKey,
                    accountData.toString()
                )
                .apply()

            true

        } catch (exception: Exception) {
            false
        }
    }

    fun createAccountKey(
        userRole: String,
        email: String
    ): String {
        return userRole +
                "_" +
                normalizeEmail(email)
    }

    fun normalizeEmail(
        email: String
    ): String {
        return email
            .trim()
            .lowercase()
    }

    fun hashPassword(
        value: String
    ): String {
        val messageDigest =
            MessageDigest.getInstance(
                "SHA-256"
            )

        val hashBytes =
            messageDigest.digest(
                value.toByteArray()
            )

        return hashBytes.joinToString(
            separator = ""
        ) { byte ->
            "%02x".format(byte)
        }
    }
}