package com.example.githubdemo.viewmodel.userprofile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.githubdemo.data.AuthValidation
import com.example.githubdemo.data.local.LocalAccountStorage
import com.example.githubdemo.supabase.CloudAccountRepository
import com.example.githubdemo.supabase.CloudProfile
import com.example.githubdemo.supabase.SupabaseConnection
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val _profile =
        MutableStateFlow(
            LocalAccountStorage.getProfile(
                application
            )
        )

    val profile: StateFlow<CloudProfile?> =
        _profile.asStateFlow()

    private val _isLoading =
        MutableStateFlow(false)

    val isLoading: StateFlow<Boolean> =
        _isLoading.asStateFlow()

    private val _message =
        MutableStateFlow("")

    val message: StateFlow<String> =
        _message.asStateFlow()

    private val _messageIsError =
        MutableStateFlow(false)

    val messageIsError: StateFlow<Boolean> =
        _messageIsError.asStateFlow()

    fun loadProfile() {
        viewModelScope.launch {
            _isLoading.value = true

            CloudAccountRepository
                .getCurrentProfile()
                .onSuccess { cloudProfile ->
                    _profile.value =
                        cloudProfile

                    LocalAccountStorage
                        .saveProfile(
                            context =
                                getApplication(),
                            profile =
                                cloudProfile
                        )

                    clearMessage()
                }
                .onFailure { exception ->
                    if (_profile.value != null) {
                        showMessage(
                            text =
                                "No internet connection. Showing the saved profile.",
                            isError = false
                        )
                    } else {
                        showMessage(
                            text =
                                exception.message
                                    ?: "Unable to load your profile.",
                            isError = true
                        )
                    }
                }

            _isLoading.value = false
        }
    }

    fun updateProfile(
        fullName: String,
        phoneNumber: String,
        address: String,
        onSuccess: () -> Unit = {}
    ) {
        val currentProfile =
            _profile.value

        when {
            currentProfile == null -> {
                showMessage(
                    "Unable to find your profile.",
                    true
                )
                return
            }

            !AuthValidation.isValidName(
                fullName
            ) -> {
                showMessage(
                    "Enter a valid full name.",
                    true
                )
                return
            }

            !AuthValidation
                .isValidPhoneNumber(
                    phoneNumber
                ) -> {
                showMessage(
                    "Phone number must contain 9 to 12 digits.",
                    true
                )
                return
            }

            !AuthValidation
                .isValidAdditionalInformation(
                    address
                ) -> {
                showMessage(
                    "Enter a valid address.",
                    true
                )
                return
            }
        }

        viewModelScope.launch {
            _isLoading.value = true
            clearMessage()

            try {
                val signedInUserId =
                    SupabaseConnection
                        .supabase
                        .auth
                        .currentUserOrNull()
                        ?.id
                        ?: throw IllegalStateException(
                            "Please sign in again."
                        )

                if (
                    signedInUserId !=
                    currentProfile.id
                ) {
                    throw IllegalStateException(
                        "The signed-in account does not match this profile."
                    )
                }

                val updatedProfile =
                    currentProfile.copy(
                        fullName =
                            fullName.trim(),
                        phoneNumber =
                            phoneNumber.trim(),
                        additionalInformation =
                            address.trim()
                    )

                SupabaseConnection
                    .supabase
                    .postgrest
                    .from("profiles")
                    .update({
                        set(
                            "full_name",
                            updatedProfile.fullName
                        )

                        set(
                            "phone_number",
                            updatedProfile
                                .phoneNumber
                        )

                        set(
                            "additional_information",
                            updatedProfile
                                .additionalInformation
                        )
                    }) {
                        filter {
                            eq(
                                "id",
                                signedInUserId
                            )
                        }
                    }

                _profile.value =
                    updatedProfile

                LocalAccountStorage.saveProfile(
                    context = getApplication(),
                    profile = updatedProfile
                )

                showMessage(
                    "Profile updated successfully.",
                    false
                )

                onSuccess()
            } catch (exception: Exception) {
                showMessage(
                    exception.message
                        ?: "Unable to update your profile.",
                    true
                )
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun clearMessage() {
        _message.value = ""
        _messageIsError.value = false
    }

    private fun showMessage(
        text: String,
        isError: Boolean
    ) {
        _message.value = text
        _messageIsError.value = isError
    }
}