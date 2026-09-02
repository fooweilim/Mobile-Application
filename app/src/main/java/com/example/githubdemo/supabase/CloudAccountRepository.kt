package com.example.githubdemo.supabase

import io.github.jan.supabase.auth.OtpType
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.auth.providers.builtin.OTP
import io.github.jan.supabase.postgrest.postgrest

object CloudAccountRepository {

    private const val PROFILE_TABLE =
        "profiles"

    suspend fun sendSignUpOtp(
        email: String,
        password: String
    ): Result<Unit> {
        return try {
            val cleanEmail =
                email.trim().lowercase()

            if (
                SupabaseConnection
                    .supabase
                    .auth
                    .currentUserOrNull() != null
            ) {
                SupabaseConnection
                    .supabase
                    .auth
                    .signOut()
            }

            SupabaseConnection
                .supabase
                .auth
                .signUpWith(Email) {
                    this.email = cleanEmail
                    this.password = password
                }

            Result.success(Unit)
        } catch (exception: Exception) {
            signOutAfterError()

            Result.failure(exception)
        }
    }

    suspend fun resendSignUpOtp(
        email: String
    ): Result<Unit> {
        return try {
            SupabaseConnection
                .supabase
                .auth
                .resendEmail(
                    type =
                        OtpType.Email.SIGNUP,

                    email =
                        email
                            .trim()
                            .lowercase()
                )

            Result.success(Unit)
        } catch (exception: Exception) {
            Result.failure(exception)
        }
    }

    suspend fun verifySignUpOtpAndSaveProfile(
        email: String,
        otp: String,
        userRole: String,
        fullName: String,
        phoneNumber: String,
        additionalInformation: String
    ): Result<Unit> {
        return try {
            SupabaseConnection
                .supabase
                .auth
                .verifyEmailOtp(
                    type =
                        OtpType.Email.SIGNUP,

                    email =
                        email
                            .trim()
                            .lowercase(),

                    token = otp.trim()
                )

            val currentUser =
                SupabaseConnection
                    .supabase
                    .auth
                    .currentUserOrNull()
                    ?: throw Exception(
                        "Email was verified, but the session was not created."
                    )

            val cloudProfile =
                CloudProfileInput(
                    id = currentUser.id,

                    userRole = userRole,

                    fullName =
                        fullName.trim(),

                    email =
                        email
                            .trim()
                            .lowercase(),

                    phoneNumber =
                        phoneNumber.trim(),

                    additionalInformation =
                        additionalInformation
                            .trim(),

                    emailVerified = true
                )

            SupabaseConnection
                .supabase
                .postgrest
                .from(PROFILE_TABLE)
                .insert(cloudProfile)

            SupabaseConnection
                .supabase
                .auth
                .signOut()

            Result.success(Unit)
        } catch (exception: Exception) {
            signOutAfterError()

            Result.failure(exception)
        }
    }

    suspend fun login(
        email: String,
        password: String,
        selectedRole: String
    ): Result<CloudProfile> {
        return try {
            /*
             * Supabase Authentication checks:
             *
             * 1. Whether the email exists.
             * 2. Whether the password is correct.
             * 3. Whether the email is confirmed.
             */
            SupabaseConnection
                .supabase
                .auth
                .signInWith(Email) {
                    this.email =
                        email
                            .trim()
                            .lowercase()

                    this.password = password
                }

            val currentUser =
                SupabaseConnection
                    .supabase
                    .auth
                    .currentUserOrNull()
                    ?: throw Exception(
                        "Login session was not created."
                    )

            /*
             * Load the user's application profile
             * from the cloud database.
             */
            val cloudProfile =
                getProfileById(
                    currentUser.id
                )

            /*
             * Make sure the user selected the
             * correct role on the role screen.
             */
            if (
                cloudProfile.userRole !=
                selectedRole
            ) {
                SupabaseConnection
                    .supabase
                    .auth
                    .signOut()

                throw Exception(
                    "This email is registered as a " +
                            "${cloudProfile.userRole}."
                )
            }

            /*
             * Do not check:
             *
             * cloudProfile.emailVerified
             *
             * Supabase Authentication has already
             * checked the real email confirmation
             * before creating the login session.
             */
            Result.success(cloudProfile)
        } catch (exception: Exception) {
            signOutAfterError()

            val originalMessage =
                exception.message.orEmpty()

            val lowercaseMessage =
                originalMessage.lowercase()

            val userMessage = when {
                lowercaseMessage.contains(
                    "email_not_confirmed"
                ) ||
                        lowercaseMessage.contains(
                            "email not confirmed"
                        ) -> {
                    "Verify your email before signing in."
                }

                lowercaseMessage.contains(
                    "invalid login credentials"
                ) -> {
                    "Incorrect email or password."
                }

                originalMessage.isBlank() -> {
                    "Unable to sign in."
                }

                else -> originalMessage
            }

            Result.failure(
                Exception(userMessage)
            )
        }
    }

    suspend fun sendResetOtp(
        email: String
    ): Result<Unit> {
        return try {
            SupabaseConnection
                .supabase
                .auth
                .signInWith(OTP) {
                    this.email =
                        email
                            .trim()
                            .lowercase()

                    createUser = false
                }

            Result.success(Unit)
        } catch (exception: Exception) {
            Result.failure(exception)
        }
    }

    suspend fun verifyResetOtp(
        email: String,
        otp: String,
        selectedRole: String
    ): Result<Unit> {
        return try {
            SupabaseConnection
                .supabase
                .auth
                .verifyEmailOtp(
                    type =
                        OtpType.Email.EMAIL,

                    email =
                        email
                            .trim()
                            .lowercase(),

                    token = otp.trim()
                )

            val currentUser =
                SupabaseConnection
                    .supabase
                    .auth
                    .currentUserOrNull()
                    ?: throw Exception(
                        "Email verification session was not created."
                    )

            val cloudProfile =
                getProfileById(
                    currentUser.id
                )

            if (
                cloudProfile.userRole !=
                selectedRole
            ) {
                SupabaseConnection
                    .supabase
                    .auth
                    .signOut()

                throw Exception(
                    "This email is not registered as a $selectedRole."
                )
            }

            Result.success(Unit)
        } catch (exception: Exception) {
            signOutAfterError()

            Result.failure(exception)
        }
    }

    suspend fun updatePassword(
        newPassword: String
    ): Result<Unit> {
        return try {
            SupabaseConnection
                .supabase
                .auth
                .updateUser {
                    password = newPassword
                }

            SupabaseConnection
                .supabase
                .auth
                .signOut()

            Result.success(Unit)
        } catch (exception: Exception) {
            Result.failure(exception)
        }
    }

    suspend fun getCurrentProfile():
            Result<CloudProfile> {

        return try {
            val currentUser =
                SupabaseConnection
                    .supabase
                    .auth
                    .currentUserOrNull()
                    ?: throw Exception(
                        "Please sign in again."
                    )

            val cloudProfile =
                getProfileById(
                    currentUser.id
                )

            Result.success(cloudProfile)
        } catch (exception: Exception) {
            Result.failure(exception)
        }
    }

    suspend fun signOut():
            Result<Unit> {

        return try {
            SupabaseConnection
                .supabase
                .auth
                .signOut()

            Result.success(Unit)
        } catch (exception: Exception) {
            Result.failure(exception)
        }
    }

    private suspend fun getProfileById(
        userId: String
    ): CloudProfile {
        return SupabaseConnection
            .supabase
            .postgrest
            .from(PROFILE_TABLE)
            .select {
                filter {
                    eq(
                        column = "id",
                        value = userId
                    )
                }
            }
            .decodeSingle<CloudProfile>()
    }

    private suspend fun signOutAfterError() {
        try {
            if (
                SupabaseConnection
                    .supabase
                    .auth
                    .currentUserOrNull() != null
            ) {
                SupabaseConnection
                    .supabase
                    .auth
                    .signOut()
            }
        } catch (exception: Exception) {
            exception.printStackTrace()
        }
    }
}