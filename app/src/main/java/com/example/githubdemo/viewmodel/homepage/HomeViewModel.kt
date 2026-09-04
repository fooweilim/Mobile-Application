package com.example.githubdemo.viewmodel.homepage

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.githubdemo.data.local.LocalAccountStorage
import com.example.githubdemo.model.market.Product
import com.example.githubdemo.supabase.CloudAccountRepository
import com.example.githubdemo.supabase.ProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val productRepository =
        ProductRepository()

    private val _products =
        MutableStateFlow<List<Product>>(
            emptyList()
        )

    val products: StateFlow<List<Product>> =
        _products.asStateFlow()

    private val _userName =
        MutableStateFlow(
            LocalAccountStorage
                .getProfile(application)
                ?.fullName
                ?: "User"
        )

    val userName: StateFlow<String> =
        _userName.asStateFlow()

    fun refresh() {
        loadProducts()
        loadProfile()
    }

    private fun loadProducts() {
        viewModelScope.launch {
            _products.value = try {
                productRepository.getProducts()
            } catch (exception: Exception) {
                emptyList()
            }
        }
    }

    private fun loadProfile() {
        val localProfile =
            LocalAccountStorage.getProfile(
                getApplication()
            )

        if (localProfile != null) {
            _userName.value =
                localProfile.fullName
        }

        viewModelScope.launch {
            CloudAccountRepository
                .getCurrentProfile()
                .onSuccess { profile ->
                    _userName.value =
                        profile.fullName

                    LocalAccountStorage.saveProfile(
                        context = getApplication(),
                        profile = profile
                    )
                }
        }
    }
}