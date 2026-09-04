package com.example.githubdemo.screen.foodbox

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.githubdemo.data.FoodBoxData
import com.example.githubdemo.viewmodel.foodbox.FoodBoxViewModel

@Composable
fun DeliveryScheduleScreen(
    foodBoxViewModel: FoodBoxViewModel,
    onBackClick: () -> Unit,
    onContinueClick: () -> Unit
) {
    val state by
    foodBoxViewModel.uiState

    val plan =
        foodBoxViewModel
            .getSelectedPlan()

    var showAddressDialog by
    rememberSaveable {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        FoodBoxFlowHeader(
            title =
                "Delivery Schedule",

            currentStep = 4,

            onBackClick =
                onBackClick
        )

        LazyColumn(
            modifier =
                Modifier.fillMaxSize(),

            contentPadding =
                PaddingValues(20.dp),

            verticalArrangement =
                Arrangement.spacedBy(
                    18.dp
                )
        ) {
            item {
                Text(
                    text =
                        "Setting up your delivery",

                    color =
                        FoodBoxSecondaryText,

                    fontSize = 17.sp
                )

                Text(
                    text =
                        plan?.name.orEmpty(),

                    color =
                        FoodBoxMainText,

                    fontSize = 26.sp,

                    fontWeight =
                        FontWeight.Bold
                )
            }

            item {
                ScheduleCard(
                    title =
                        "01   Choose Delivery Day"
                ) {
                    Text(
                        text =
                            "Select your preferred day each week.",

                        color =
                            FoodBoxSecondaryText
                    )

                    Spacer(
                        modifier =
                            Modifier.height(
                                16.dp
                            )
                    )

                    Row(
                        modifier =
                            Modifier.fillMaxWidth(),

                        horizontalArrangement =
                            Arrangement.spacedBy(
                                8.dp
                            )
                    ) {
                        FoodBoxData
                            .deliveryDays
                            .forEach { day ->

                                DayButton(
                                    day = day,

                                    selected =
                                        state.deliveryDay ==
                                                day,

                                    modifier =
                                        Modifier.weight(
                                            1f
                                        ),

                                    onClick = {
                                        foodBoxViewModel
                                            .selectDeliveryDay(
                                                day
                                            )
                                    }
                                )
                            }
                    }

                    if (
                        state.deliveryDay
                            .isNotBlank()
                    ) {
                        Spacer(
                            modifier =
                                Modifier.height(
                                    14.dp
                                )
                        )

                        Row(
                            verticalAlignment =
                                Alignment
                                    .CenterVertically
                        ) {
                            Icon(
                                imageVector =
                                    Icons.Default
                                        .CheckCircle,

                                contentDescription =
                                    null,

                                tint =
                                    FoodBoxPrimaryGreen
                            )

                            Spacer(
                                modifier =
                                    Modifier.width(
                                        8.dp
                                    )
                            )

                            Text(
                                text =
                                    "Delivering every " +
                                            state.deliveryDay,

                                color =
                                    FoodBoxPrimaryGreen,

                                fontWeight =
                                    FontWeight.Bold
                            )
                        }
                    }
                }
            }

            item {
                ScheduleCard(
                    title =
                        "02   Delivery Address"
                ) {
                    Row(
                        verticalAlignment =
                            Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector =
                                Icons.Outlined.LocationOn,

                            contentDescription =
                                null,

                            tint =
                                FoodBoxPrimaryGreen
                        )

                        Spacer(
                            modifier =
                                Modifier.width(
                                    12.dp
                                )
                        )

                        Text(
                            text =
                                state.deliveryAddress
                                    .ifBlank {
                                        "No delivery address added"
                                    },

                            modifier =
                                Modifier.weight(1f),

                            color =
                                FoodBoxMainText,

                            fontWeight =
                                FontWeight.SemiBold
                        )

                        TextButton(
                            onClick = {
                                showAddressDialog =
                                    true
                            }
                        ) {
                            Text(
                                text =
                                    if (
                                        state.deliveryAddress
                                            .isBlank()
                                    ) {
                                        "Add"
                                    } else {
                                        "Change"
                                    },

                                color =
                                    FoodBoxPrimaryGreen
                            )
                        }
                    }
                }
            }

            if (
                !state.message
                    .isNullOrBlank()
            ) {
                item {
                    FoodBoxMessage(
                        message =
                            state.message
                    )
                }
            }

            item {
                FoodBoxPrimaryButton(
                    text =
                        "Proceed to Checkout",

                    enabled =
                        state.deliveryDay
                            .isNotBlank() &&
                                state.deliveryAddress
                                    .isNotBlank(),

                    onClick =
                        onContinueClick
                )
            }
        }
    }

    if (showAddressDialog) {
        AddressEditDialog(
            currentAddress =
                state.deliveryAddress,

            onDismiss = {
                showAddressDialog =
                    false
            },

            onSave = {
                    address ->

                if (
                    foodBoxViewModel
                        .updateDeliveryAddress(
                            address
                        )
                ) {
                    showAddressDialog =
                        false
                }
            }
        )
    }
}

@Composable
private fun ScheduleCard(
    title: String,
    content:
    @Composable
    ColumnScope.() -> Unit
) {
    Card(
        modifier =
            Modifier.fillMaxWidth(),

        shape =
            RoundedCornerShape(22.dp),

        border =
            BorderStroke(
                1.dp,
                FoodBoxBorder
            ),

        colors =
            CardDefaults.cardColors(
                containerColor =
                    Color.White
            )
    ) {
        Column(
            modifier =
                Modifier.padding(20.dp)
        ) {
            Text(
                text = title,

                color =
                    FoodBoxMainText,

                fontSize = 20.sp,

                fontWeight =
                    FontWeight.Bold
            )

            Spacer(
                modifier =
                    Modifier.height(16.dp)
            )

            content()
        }
    }
}

@Composable
private fun DayButton(
    day: String,
    selected: Boolean,
    modifier: Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier =
            modifier.clickable(
                onClick = onClick
            ),

        shape =
            RoundedCornerShape(16.dp),

        border =
            BorderStroke(
                width = 1.dp,

                color =
                    if (selected) {
                        FoodBoxPrimaryGreen
                    } else {
                        FoodBoxBorder
                    }
            ),

        colors =
            CardDefaults.cardColors(
                containerColor =
                    if (selected) {
                        FoodBoxPrimaryGreen
                    } else {
                        FoodBoxPageBackground
                    }
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    vertical = 14.dp
                ),

            horizontalAlignment =
                Alignment.CenterHorizontally
        ) {
            Text(
                text = day.take(3),

                color =
                    if (selected) {
                        Color.White
                    } else {
                        FoodBoxSecondaryText
                    },

                fontWeight =
                    FontWeight.Bold
            )

            RadioButton(
                selected = selected,

                onClick = onClick,

                colors =
                    RadioButtonDefaults
                        .colors(
                            selectedColor =
                                Color.White,

                            unselectedColor =
                                FoodBoxSecondaryText
                        )
            )
        }
    }
}

@Composable
fun AddressEditDialog(
    currentAddress: String,
    onDismiss: () -> Unit,
    onSave: (String) -> Unit
) {
    var address by
    rememberSaveable(
        currentAddress
    ) {
        mutableStateOf(
            currentAddress
        )
    }

    AlertDialog(
        onDismissRequest =
            onDismiss,

        title = {
            Text(
                "Delivery Address"
            )
        },

        text = {
            OutlinedTextField(
                value = address,

                onValueChange = {
                    address = it
                },

                modifier =
                    Modifier.fillMaxWidth(),

                label = {
                    Text("Full address")
                },

                minLines = 3
            )
        },

        confirmButton = {
            TextButton(
                onClick = {
                    onSave(address)
                }
            ) {
                Text(
                    text = "Save",
                    color =
                        FoodBoxPrimaryGreen
                )
            }
        },

        dismissButton = {
            TextButton(
                onClick = onDismiss
            ) {
                Text("Cancel")
            }
        }
    )
}