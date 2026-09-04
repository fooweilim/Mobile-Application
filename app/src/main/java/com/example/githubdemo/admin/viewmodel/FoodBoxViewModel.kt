package com.example.githubdemo.admin.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.githubdemo.admin.model.FoodBox
import com.example.githubdemo.admin.model.FoodItem
import com.example.githubdemo.admin.repository.FoodBoxRepository
import kotlinx.coroutines.launch

class FoodBoxViewModel :
    ViewModel() {

    private val repository =
        FoodBoxRepository()

    var foodBoxes by mutableStateOf(
        emptyList<FoodBox>()
    )
        private set

    var foodItems by mutableStateOf(
        emptyList<FoodItem>()
    )
        private set

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    init {
        loadFoodBoxes()
    }

    fun loadFoodBoxes() {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null

            try {
                foodBoxes =
                    repository
                        .getFoodBoxes()
            } catch (exception: Exception) {
                errorMessage =
                    exception.message
                        ?: "Unable to load food boxes."
            } finally {
                isLoading = false
            }
        }
    }

    fun loadFoodItems(
        foodBoxId: String
    ) {
        if (foodBoxId.isBlank()) {
            errorMessage =
                "Invalid food box."
            return
        }

        viewModelScope.launch {
            isLoading = true
            errorMessage = null

            try {
                foodItems =
                    repository.getFoodItems(
                        foodBoxId
                    )
            } catch (exception: Exception) {
                errorMessage =
                    exception.message
                        ?: "Unable to load food items."
            } finally {
                isLoading = false
            }
        }
    }

    fun deleteFoodItem(
        id: String
    ) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null

            try {
                repository
                    .deleteFoodItem(id)

                foodItems =
                    foodItems.filter {
                            item ->
                        item.id != id
                    }
            } catch (exception: Exception) {
                errorMessage =
                    exception.message
                        ?: "Unable to delete food item."
            } finally {
                isLoading = false
            }
        }
    }

    fun clearError() {
        errorMessage = null
    }
}