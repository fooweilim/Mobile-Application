package com.example.githubdemo.viewmodel.userprofile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.githubdemo.model.farmer.Order
import com.example.githubdemo.supabase.CustomerOrderRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CustomerOrderViewModel :
    ViewModel() {

    private val repository =
        CustomerOrderRepository()

    private val _orders =
        MutableStateFlow<List<Order>>(
            emptyList()
        )

    val orders: StateFlow<List<Order>> =
        _orders.asStateFlow()

    private val _isLoading =
        MutableStateFlow(false)

    val isLoading: StateFlow<Boolean> =
        _isLoading.asStateFlow()

    private val _errorMessage =
        MutableStateFlow("")

    val errorMessage: StateFlow<String> =
        _errorMessage.asStateFlow()

    fun loadOrders() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = ""

            try {
                _orders.value =
                    repository.getMyOrders()
            } catch (exception: Exception) {
                _errorMessage.value =
                    exception.message
                        ?: "Unable to load your orders."
            } finally {
                _isLoading.value = false
            }
        }
    }
}