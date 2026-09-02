package com.example.githubdemo.viewmodel.authentication

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.githubdemo.data.local.LocalAccountStorage
import com.example.githubdemo.supabase.CloudAccountRepository
import kotlinx.coroutines.launch

class AuthViewModel(
    application: Application
) : AndroidViewModel(application) {

    var isLoading by mutableStateOf(false)
        private set

    var message by mutableStateOf("")
        private set

    var messageIsError by mutableStateOf(true)
        private set

    fun clearMessage() {
        message = ""
    }

    fun showSuccessMessage(
        successMessage: String
    ) {
        messageIsError = false
        message = successMessage
    }

    fun showErrorMessage(
        errorMessage: String
    ) {
        messageIsError = true
        message = errorMessage
    }

    fun login(
        email: String,
        password: String,
        selectedRole: String,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            isLoading = true
            message = ""

            val result =
                CloudAccountRepository.login(
                    email = email,
                    password = password,
                    selectedRole = selectedRole
                )

            if (result.isSuccess) {
                val profile =
                    result.getOrNull()

                if (profile != null) {
                    LocalAccountStorage
                        .saveProfile(
                            context =
                                getApplication(),

                            profile = profile
                        )

                    LocalAccountStorage
                        .saveSelectedRole(
                            context =
                                getApplication(),

                            userRole =
                                profile.userRole
                        )
                }

                onSuccess()
            } else {
                showErrorMessage(
                    result.exceptionOrNull()
                        ?.message
                        ?: "Unable to sign in."
                )
            }

            isLoading = false
        }
    }

    fun sendSignUpOtp(
        email: String,
        password: String,
        onOtpSent: () -> Unit
    ) {
        viewModelScope.launch {
            isLoading = true
            message = ""

            val result =
                CloudAccountRepository
                    .sendSignUpOtp(
                        email = email,
                        password = password
                    )

            if (result.isSuccess) {
                showSuccessMessage(
                    "OTP sent. Check your email."
                )

                onOtpSent()
            } else {
                showErrorMessage(
                    result.exceptionOrNull()
                        ?.message
                        ?: "Unable to send OTP."
                )
            }

            isLoading = false
        }
    }

    fun resendSignUpOtp(
        email: String
    ) {
        viewModelScope.launch {
            isLoading = true
            message = ""

            val result =
                CloudAccountRepository
                    .resendSignUpOtp(email)

            if (result.isSuccess) {
                showSuccessMessage(
                    "A new OTP was sent."
                )
            } else {
                showErrorMessage(
                    result.exceptionOrNull()
                        ?.message
                        ?: "Unable to resend OTP."
                )
            }

            isLoading = false
        }
    }

    fun verifySignUpOtpAndSaveProfile(
        email: String,
        otp: String,
        userRole: String,
        fullName: String,
        phoneNumber: String,
        additionalInformation: String,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            isLoading = true
            message = ""

            val result =
                CloudAccountRepository
                    .verifySignUpOtpAndSaveProfile(
                        email = email,
                        otp = otp,
                        userRole = userRole,
                        fullName = fullName,
                        phoneNumber = phoneNumber,

                        additionalInformation =
                            additionalInformation
                    )

            if (result.isSuccess) {
                showSuccessMessage(
                    "Email verified successfully."
                )

                onSuccess()
            } else {
                showErrorMessage(
                    result.exceptionOrNull()
                        ?.message
                        ?: "Invalid or expired OTP."
                )
            }

            isLoading = false
        }
    }

    fun sendResetOtp(
        email: String,
        onOtpSent: () -> Unit
    ) {
        viewModelScope.launch {
            isLoading = true
            message = ""

            val result =
                CloudAccountRepository
                    .sendResetOtp(email)

            if (result.isSuccess) {
                showSuccessMessage(
                    "OTP sent. Check your email."
                )

                onOtpSent()
            } else {
                showErrorMessage(
                    result.exceptionOrNull()
                        ?.message
                        ?: "Unable to send OTP."
                )
            }

            isLoading = false
        }
    }

    fun verifyResetOtp(
        email: String,
        otp: String,
        selectedRole: String,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            isLoading = true
            message = ""

            val result =
                CloudAccountRepository
                    .verifyResetOtp(
                        email = email,
                        otp = otp,
                        selectedRole = selectedRole
                    )

            if (result.isSuccess) {
                showSuccessMessage(
                    "Email verified successfully."
                )

                onSuccess()
            } else {
                showErrorMessage(
                    result.exceptionOrNull()
                        ?.message
                        ?: "Invalid or expired OTP."
                )
            }

            isLoading = false
        }
    }

    fun updatePassword(
        newPassword: String,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            isLoading = true
            message = ""

            val result =
                CloudAccountRepository
                    .updatePassword(newPassword)

            if (result.isSuccess) {
                showSuccessMessage(
                    "Password reset successfully."
                )

                onSuccess()
            } else {
                showErrorMessage(
                    result.exceptionOrNull()
                        ?.message
                        ?: "Unable to reset password."
                )
            }

            isLoading = false
        }
    }

    fun signOut(
        onComplete: () -> Unit
    ) {
        viewModelScope.launch {
            isLoading = true
            message = ""

            val result =
                CloudAccountRepository.signOut()

            if (result.isFailure) {
                showErrorMessage(
                    result.exceptionOrNull()
                        ?.message
                        ?: "Unable to sign out."
                )
            }

            LocalAccountStorage.clearAll(
                getApplication()
            )

            isLoading = false
            onComplete()
        }
    }
}