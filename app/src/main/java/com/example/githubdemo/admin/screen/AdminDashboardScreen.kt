package com.example.githubdemo.admin.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Agriculture
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.githubdemo.admin.components.SalesChart
import com.example.githubdemo.admin.components.StatisticCard
import com.example.githubdemo.admin.viewmodel.AdminDashboardViewModel
import com.example.githubdemo.ui.theme.AdminBackground
import com.example.githubdemo.ui.theme.TextDark

@Composable
fun AdminDashboardScreen(
    viewModel: AdminDashboardViewModel
) {
    val stats by
    viewModel.stats.collectAsState()

    val selectedMonth by
    viewModel
        .selectedMonth
        .collectAsState()

    LazyColumn(
        modifier =
            Modifier
                .fillMaxSize()
                .background(
                    AdminBackground
                ),
        contentPadding =
            PaddingValues(16.dp),
        verticalArrangement =
            Arrangement.spacedBy(
                14.dp
            )
    ) {
        item {
            Column {
                Text(
                    text = "Welcome back",
                    style =
                        MaterialTheme
                            .typography
                            .bodyMedium,
                    color = TextDark
                )

                Text(
                    text = "Administrator",
                    style =
                        MaterialTheme
                            .typography
                            .headlineSmall,
                    color = TextDark
                )
            }
        }

        item {
            Row(
                modifier =
                    Modifier.fillMaxWidth(),
                horizontalArrangement =
                    Arrangement.spacedBy(
                        10.dp
                    )
            ) {
                Box(
                    modifier =
                        Modifier.weight(1f)
                ) {
                    StatisticCard(
                        title =
                            "Monthly Sales",
                        value =
                            stats.monthlySales,
                        icon =
                            Icons.Default
                                .ShowChart
                    )
                }

                Box(
                    modifier =
                        Modifier.weight(1f)
                ) {
                    StatisticCard(
                        title = "Users",
                        value = stats.users,
                        icon =
                            Icons.Default.People
                    )
                }
            }
        }

        item {
            Row(
                modifier =
                    Modifier.fillMaxWidth(),
                horizontalArrangement =
                    Arrangement.spacedBy(
                        10.dp
                    )
            ) {
                Box(
                    modifier =
                        Modifier.weight(1f)
                ) {
                    StatisticCard(
                        title = "Farmers",
                        value =
                            stats.farmers,
                        icon =
                            Icons.Default
                                .Agriculture
                    )
                }

                Box(
                    modifier =
                        Modifier.weight(1f)
                ) {
                    StatisticCard(
                        title =
                            "Subscriptions",
                        value =
                            stats.subscriptions,
                        icon =
                            Icons.Default
                                .CardGiftcard
                    )
                }
            }
        }

        item {
            Spacer(
                modifier =
                    Modifier.height(2.dp)
            )

            Text(
                text =
                    "Monthly Sales Trend",
                style =
                    MaterialTheme
                        .typography
                        .titleMedium,
                color = TextDark
            )
        }

        item {
            Card(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(320.dp),
                colors =
                    CardDefaults.cardColors(),
                elevation =
                    CardDefaults
                        .cardElevation(
                            defaultElevation =
                                2.dp
                        )
            ) {
                SalesChart(
                    selectedMonth =
                        selectedMonth,
                    months =
                        viewModel
                            .salesData
                            .map {
                                    salesData ->
                                salesData.month
                            },
                    sales =
                        viewModel
                            .currentSales(),
                    onMonthChange = {
                            month ->
                        viewModel.changeMonth(
                            month
                        )
                    }
                )
            }
        }
    }
}