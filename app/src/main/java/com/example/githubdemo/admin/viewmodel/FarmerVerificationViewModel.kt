package com.example.githubdemo.admin.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.githubdemo.admin.model.FarmerApplication
import com.example.githubdemo.admin.repository.FarmerVerificationRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

private const val FARMER_TAG =
    "ADMIN_FARMER"

class FarmerVerificationViewModel :
    ViewModel() {

    private val repository =
        FarmerVerificationRepository()

    private val _farmers =
        MutableStateFlow<
                List<FarmerApplication>
                >(
            emptyList()
        )

    val farmers:
            StateFlow<List<FarmerApplication>> =
        _farmers.asStateFlow()

    private val _errorMessage =
        MutableStateFlow<String?>(null)

    val errorMessage:
            StateFlow<String?> =
        _errorMessage.asStateFlow()

    init {
        loadFarmers()
    }

    fun loadFarmers() {
        viewModelScope.launch {
            try {
                _errorMessage.value = null

                _farmers.value =
                    repository.getFarmers()
            } catch (exception: Exception) {
                Log.e(
                    FARMER_TAG,
                    "Load error: " +
                            exception.message,
                    exception
                )

                _errorMessage.value =
                    exception.message
                        ?: "Unable to load farmer applications."
            }
        }
    }

    fun approve(
        farmer: FarmerApplication
    ) {
        updateStatus(
            farmer = farmer,
            status = "Approved"
        )
    }

    fun reject(
        farmer: FarmerApplication
    ) {
        updateStatus(
            farmer = farmer,
            status = "Rejected"
        )
    }

    private fun updateStatus(
        farmer: FarmerApplication,
        status: String
    ) {
        viewModelScope.launch {
            try {
                _errorMessage.value = null

                repository.updateStatus(
                    id = farmer.id,
                    status = status
                )

                _farmers.value =
                    repository.getFarmers()
            } catch (exception: Exception) {
                Log.e(
                    FARMER_TAG,
                    "Update error: " +
                            exception.message,
                    exception
                )

                _errorMessage.value =
                    exception.message
                        ?: "Unable to update farmer status."
            }
        }
    }

    fun getByStatus(
        status: String
    ): List<FarmerApplication> {
        return farmers.value.filter {
                farmer ->
            farmer.status.equals(
                other = status,
                ignoreCase = true
            )
        }
    }

    fun clearError() {
        _errorMessage.value = null
    }
}