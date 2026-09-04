package com.example.githubdemo.admin.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.githubdemo.admin.model.SalesData
import com.example.githubdemo.admin.repository.AdminDashboardRepository
import java.util.Locale
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

private const val DEFAULT_MONTH =
    "July 2026"

private const val DASHBOARD_TAG =
    "ADMIN_DASHBOARD"

data class DashboardStats(
    val monthlySales: String =
        "RM 0.00",
    val users: String =
        "0",
    val farmers: String =
        "0",
    val subscriptions: String =
        "0"
)

class AdminDashboardViewModel :
    ViewModel() {

    private val repository =
        AdminDashboardRepository()

    private val _stats =
        MutableStateFlow(
            DashboardStats()
        )

    val stats:
            StateFlow<DashboardStats> =
        _stats.asStateFlow()

    private val _selectedMonth =
        MutableStateFlow(
            DEFAULT_MONTH
        )

    val selectedMonth:
            StateFlow<String> =
        _selectedMonth.asStateFlow()

    val salesData =
        listOf(
            "January 2026",
            "February 2026",
            "March 2026",
            "April 2026",
            "May 2026",
            "June 2026",
            "July 2026",
            "August 2026"
        ).map { month ->
            SalesData(
                month = month,
                sales = listOf(
                    0f,
                    0f,
                    0f,
                    0f
                )
            )
        }

    init {
        loadDashboard()
    }

    fun loadDashboard() {
        viewModelScope.launch {
            try {
                val users =
                    repository
                        .getTotalUsers()

                val farmers =
                    repository
                        .getTotalFarmers()

                val subscriptions =
                    repository
                        .getTotalSubscriptions()

                val sales =
                    repository
                        .getMonthlySales()

                _stats.value =
                    DashboardStats(
                        monthlySales =
                            String.format(
                                Locale.US,
                                "RM %.2f",
                                sales
                            ),
                        users =
                            users.toString(),
                        farmers =
                            farmers.toString(),
                        subscriptions =
                            subscriptions
                                .toString()
                    )
            } catch (exception: Exception) {
                Log.e(
                    DASHBOARD_TAG,
                    exception.message
                        ?: "Unable to load dashboard.",
                    exception
                )
            }
        }
    }

    fun changeMonth(
        month: String
    ) {
        _selectedMonth.value =
            month
    }

    fun currentSales():
            List<Float> {
        return salesData
            .find { data ->
                data.month ==
                        selectedMonth.value
            }
            ?.sales
            ?: emptyList()
    }
}