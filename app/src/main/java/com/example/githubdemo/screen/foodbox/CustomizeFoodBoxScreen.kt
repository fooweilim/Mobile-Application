package com.example.githubdemo.screen.foodbox

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Eco
import androidx.compose.material.icons.outlined.Sync
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.githubdemo.data.FoodBoxData
import com.example.githubdemo.model.FoodBoxAddOn
import com.example.githubdemo.viewmodel.foodbox.FoodBoxViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomizeFoodBoxScreen(
    foodBoxViewModel: FoodBoxViewModel,
    onBackClick: () -> Unit,
    onContinueClick: () -> Unit
) {
    val state by
    foodBoxViewModel.uiState

    val swappingItem =
        state.customizedItems
            .firstOrNull {
                it.id ==
                        state.itemBeingSwappedId
            }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        FoodBoxFlowHeader(
            title = "Customize Box",
            currentStep = 3,
            onBackClick = onBackClick
        )

        LazyColumn(
            modifier =
                Modifier.fillMaxSize(),

            contentPadding =
                PaddingValues(20.dp),

            verticalArrangement =
                Arrangement.spacedBy(
                    16.dp
                )
        ) {
            item {
                Card(
                    modifier =
                        Modifier.fillMaxWidth(),

                    shape =
                        RoundedCornerShape(
                            20.dp
                        ),

                    colors =
                        CardDefaults.cardColors(
                            containerColor =
                                FoodBoxSoftGreen
                        )
                ) {
                    Row(
                        modifier =
                            Modifier.padding(
                                18.dp
                            ),

                        verticalAlignment =
                            Alignment.Top
                    ) {
                        Icon(
                            imageVector =
                                Icons.Outlined.Sync,

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
                                "Tap any Swappable item to replace it. " +
                                        "The replacement keeps the same weight " +
                                        "as the original item.",

                            color =
                                FoodBoxPrimaryGreen,

                            lineHeight = 22.sp
                        )
                    }
                }
            }

            item {
                Text(
                    text = "Your Items",
                    color =
                        FoodBoxMainText,
                    fontSize = 22.sp,
                    fontWeight =
                        FontWeight.Bold
                )
            }

            item {
                Card(
                    modifier =
                        Modifier.fillMaxWidth(),

                    shape =
                        RoundedCornerShape(
                            22.dp
                        ),

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
                    Column {
                        state.customizedItems
                            .forEachIndexed {
                                    index,
                                    item ->

                                FoodBoxItemRow(
                                    item = item,

                                    showSwapButton =
                                        true,

                                    onSwapClick = {
                                        foodBoxViewModel
                                            .showSwapOptions(
                                                item.id
                                            )
                                    }
                                )

                                if (
                                    index <
                                    state.customizedItems
                                        .lastIndex
                                ) {
                                    HorizontalDivider(
                                        modifier =
                                            Modifier.padding(
                                                horizontal =
                                                    16.dp
                                            ),

                                        color =
                                            FoodBoxBorder
                                    )
                                }
                            }
                    }
                }
            }

            item {
                Text(
                    text =
                        "Optional Add-ons",

                    color =
                        FoodBoxMainText,

                    fontSize = 22.sp,

                    fontWeight =
                        FontWeight.Bold
                )
            }

            items(
                items =
                    FoodBoxData.addOns,

                key = {
                    it.id
                }
            ) { addOn ->
                AddOnRow(
                    addOn = addOn,

                    selected =
                        state.selectedAddOns
                            .any {
                                it.id ==
                                        addOn.id
                            },

                    onClick = {
                        foodBoxViewModel
                            .toggleAddOn(
                                addOn
                            )
                    }
                )
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
                        "Confirm & Set Schedule",

                    onClick =
                        onContinueClick
                )
            }
        }
    }

    if (swappingItem != null) {
        ModalBottomSheet(
            onDismissRequest =
                foodBoxViewModel::
                dismissSwapOptions
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Text(
                    text =
                        "Replace " +
                                swappingItem.name,

                    color =
                        FoodBoxMainText,

                    fontSize = 22.sp,

                    fontWeight =
                        FontWeight.Bold
                )

                Text(
                    text =
                        "Every choice will keep " +
                                swappingItem.quantity +
                                ".",

                    color =
                        FoodBoxSecondaryText
                )

                Spacer(
                    modifier =
                        Modifier.height(14.dp)
                )

                FoodBoxData
                    .replacementItems
                    .chunked(2)
                    .forEach {
                            rowItems ->

                        Row(
                            modifier =
                                Modifier.fillMaxWidth(),

                            horizontalArrangement =
                                Arrangement.spacedBy(
                                    10.dp
                                )
                        ) {
                            rowItems.forEach {
                                    replacement ->

                                OutlinedButton(
                                    onClick = {
                                        foodBoxViewModel
                                            .replaceItem(
                                                replacement
                                            )
                                    },

                                    modifier =
                                        Modifier.weight(
                                            1f
                                        ),

                                    shape =
                                        RoundedCornerShape(
                                            50
                                        ),

                                    border =
                                        BorderStroke(
                                            1.dp,
                                            FoodBoxBorder
                                        )
                                ) {
                                    Icon(
                                        imageVector =
                                            Icons.Outlined.Eco,

                                        contentDescription =
                                            null,

                                        modifier =
                                            Modifier.size(
                                                16.dp
                                            ),

                                        tint =
                                            FoodBoxPrimaryGreen
                                    )

                                    Spacer(
                                        modifier =
                                            Modifier.width(
                                                6.dp
                                            )
                                    )

                                    Text(
                                        text =
                                            replacement.name,

                                        color =
                                            FoodBoxMainText,

                                        fontSize =
                                            12.sp
                                    )
                                }
                            }

                            if (
                                rowItems.size ==
                                1
                            ) {
                                Spacer(
                                    modifier =
                                        Modifier.weight(
                                            1f
                                        )
                                )
                            }
                        }

                        Spacer(
                            modifier =
                                Modifier.height(
                                    8.dp
                                )
                        )
                    }

                Spacer(
                    modifier =
                        Modifier.height(20.dp)
                )
            }
        }
    }
}

@Composable
private fun AddOnRow(
    addOn: FoodBoxAddOn,
    selected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                onClick = onClick
            ),

        shape =
            RoundedCornerShape(18.dp),

        border =
            BorderStroke(
                1.dp,
                FoodBoxBorder
            ),

        colors =
            CardDefaults.cardColors(
                containerColor =
                    if (selected) {
                        FoodBoxSoftGreen
                    } else {
                        Color.White
                    }
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),

            verticalAlignment =
                Alignment.CenterVertically
        ) {
            Text(
                text = addOn.name,

                modifier =
                    Modifier.weight(1f),

                color =
                    FoodBoxMainText,

                fontWeight =
                    FontWeight.Bold
            )

            Text(
                text =
                    "+${formatMoney(addOn.price)}",

                color =
                    FoodBoxSecondaryText
            )

            Spacer(
                modifier =
                    Modifier.width(10.dp)
            )

            Icon(
                imageVector =
                    Icons.Default.Add,

                contentDescription =
                    null,

                tint =
                    FoodBoxPrimaryGreen
            )
        }
    }
}