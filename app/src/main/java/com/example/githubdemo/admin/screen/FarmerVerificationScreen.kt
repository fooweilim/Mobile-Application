package com.example.githubdemo.admin.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.githubdemo.admin.components.FarmerVerificationCard
import com.example.githubdemo.admin.viewmodel.FarmerVerificationViewModel

private val farmerTabs =
    listOf(
        "Pending",
        "Approved",
        "Rejected"
    )

@Suppress("UNUSED_PARAMETER")
@Composable
fun FarmerVerificationScreen(
    navController: NavHostController,
    viewModel:
    FarmerVerificationViewModel
) {
    var selectedTab by remember {
        mutableStateOf(
            farmerTabs.first()
        )
    }

    val farmers by
    viewModel
        .farmers
        .collectAsState()

    val errorMessage by
    viewModel
        .errorMessage
        .collectAsState()

    val uriHandler =
        LocalUriHandler.current

    val filteredFarmers =
        farmers.filter { farmer ->
            farmer.status.equals(
                other = selectedTab,
                ignoreCase = true
            )
        }

    LazyColumn(
        modifier =
            Modifier.fillMaxSize(),
        contentPadding =
            PaddingValues(
                start = 16.dp,
                top = 16.dp,
                end = 16.dp,
                bottom = 90.dp
            ),
        verticalArrangement =
            Arrangement.spacedBy(
                12.dp
            )
    ) {
        item {
            Text(
                text =
                    "Farmer Verification",
                style =
                    MaterialTheme
                        .typography
                        .titleLarge
            )
        }

        item {
            TabRow(
                selectedTabIndex =
                    farmerTabs.indexOf(
                        selectedTab
                    )
            ) {
                farmerTabs.forEach { tab ->
                    Tab(
                        selected =
                            selectedTab == tab,
                        onClick = {
                            selectedTab = tab
                        },
                        text = {
                            Text(tab)
                        }
                    )
                }
            }
        }

        if (!errorMessage.isNullOrBlank()) {
            item {
                Text(
                    text =
                        errorMessage.orEmpty(),
                    color =
                        MaterialTheme
                            .colorScheme
                            .error
                )
            }
        }

        if (filteredFarmers.isEmpty()) {
            item {
                Box(
                    modifier =
                        Modifier.padding(
                            vertical = 28.dp
                        )
                ) {
                    Text(
                        text =
                            "No $selectedTab farmer applications."
                    )
                }
            }
        }

        items(
            items = filteredFarmers,
            key = { farmer ->
                farmer.id
            }
        ) { farmer ->
            FarmerVerificationCard(
                farmer = farmer,
                onDocumentClick = {
                        document ->

                    if (
                        document.startsWith(
                            "http"
                        )
                    ) {
                        runCatching {
                            uriHandler.openUri(
                                document
                            )
                        }
                    }
                },
                onApprove = {
                    viewModel.approve(
                        farmer
                    )
                },
                onReject = {
                    viewModel.reject(
                        farmer
                    )
                }
            )
        }
    }
}