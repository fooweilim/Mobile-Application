package com.example.githubdemo.viewmodel.userprofile

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.githubdemo.data.local.LocalAccountStorage
import com.example.githubdemo.supabase.CloudAccountRepository
import com.example.githubdemo.supabase.CloudProfile
import kotlinx.coroutines.launch

class ProfileViewModel(
    application: Application
) : AndroidViewModel(application) {

    var profile by
    mutableStateOf<CloudProfile?>(null)
        private set

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf("")
        private set

    var isUsingLocalData by
    mutableStateOf(false)
        private set

    fun loadProfile() {
        val localProfile =
            LocalAccountStorage.getProfile(
                getApplication()
            )

        if (localProfile != null) {
            profile = localProfile
            isUsingLocalData = true
        }

        viewModelScope.launch {
            isLoading = true
            errorMessage = ""

            val result =
                CloudAccountRepository
                    .getCurrentProfile()

            if (result.isSuccess) {
                profile = result.getOrNull()

                if (profile != null) {
                    LocalAccountStorage
                        .saveProfile(
                            context =
                                getApplication(),

                            profile = profile!!
                        )
                }

                isUsingLocalData = false
            } else {
                if (localProfile != null) {
                    errorMessage =
                        "No internet connection. Showing locally saved profile."
                } else {
                    errorMessage =
                        result.exceptionOrNull()
                            ?.message
                            ?: "Unable to load your profile."
                }
            }

            isLoading = false
        }
    }
}