package com.example.githubdemo.admin.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Description
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.githubdemo.admin.model.FarmerApplication
import com.example.githubdemo.ui.theme.HarvestGreen
import com.example.githubdemo.ui.theme.LightGreen


private val FarmerTextGray =
    Color(0xFF7A7A7A)

@Composable
fun FarmerVerificationCard(
    farmer: FarmerApplication,
    onDocumentClick: (String) -> Unit,
    onApprove: () -> Unit,
    onReject: () -> Unit
) {
    Card(
        modifier =
            Modifier.fillMaxWidth(),
        shape =
            RoundedCornerShape(16.dp),
        colors =
            CardDefaults.cardColors(
                containerColor =
                    Color.White
            ),
        elevation =
            CardDefaults.cardElevation(
                defaultElevation =
                    2.dp
            )
    ) {
        Column(
            modifier =
                Modifier.padding(16.dp)
        ) {
            FarmerHeader(
                farmer = farmer
            )

            Spacer(
                modifier =
                    Modifier.height(16.dp)
            )

            Text(
                text = "Documents",
                style =
                    MaterialTheme
                        .typography
                        .titleSmall
            )

            Spacer(
                modifier =
                    Modifier.height(6.dp)
            )

            if (farmer.documents.isEmpty()) {
                Text(
                    text =
                        "No document uploaded.",
                    style =
                        MaterialTheme
                            .typography
                            .bodyMedium,
                    color =
                        FarmerTextGray
                )
            } else {
                farmer.documents.forEach {
                        document ->

                    FarmerDocumentRow(
                        document =
                            document,
                        onClick = {
                            onDocumentClick(
                                document
                            )
                        }
                    )
                }
            }

            if (
                farmer.status.equals(
                    other = "Pending",
                    ignoreCase = true
                )
            ) {
                Spacer(
                    modifier =
                        Modifier.height(16.dp)
                )

                Row(
                    modifier =
                        Modifier.fillMaxWidth(),
                    horizontalArrangement =
                        Arrangement.spacedBy(
                            10.dp
                        )
                ) {
                    Button(
                        modifier =
                            Modifier.weight(1f),
                        onClick = onApprove,
                        colors =
                            ButtonDefaults
                                .buttonColors(
                                    containerColor =
                                        HarvestGreen
                                )
                    ) {
                        Text("Approve")
                    }

                    OutlinedButton(
                        modifier =
                            Modifier.weight(1f),
                        onClick = onReject
                    ) {
                        Text("Reject")
                    }
                }
            }
        }
    }
}

@Composable
private fun FarmerHeader(
    farmer: FarmerApplication
) {
    Row(
        verticalAlignment =
            Alignment.CenterVertically
    ) {
        Box(
            modifier =
                Modifier
                    .size(45.dp)
                    .background(
                        color = LightGreen,
                        shape = CircleShape
                    ),
            contentAlignment =
                Alignment.Center
        ) {
            Text(
                text =
                    farmer.name
                        .firstOrNull()
                        ?.uppercase()
                        ?: "F",
                color =
                    HarvestGreen,
                style =
                    MaterialTheme
                        .typography
                        .titleMedium
            )
        }

        Spacer(
            modifier =
                Modifier.width(12.dp)
        )

        Column(
            modifier =
                Modifier.weight(1f)
        ) {
            Text(
                text =
                    farmer.name.ifBlank {
                        "Unnamed Farmer"
                    },
                style =
                    MaterialTheme
                        .typography
                        .titleMedium
            )

            Text(
                text = farmer.email,
                style =
                    MaterialTheme
                        .typography
                        .bodySmall,
                color =
                    FarmerTextGray
            )

            Text(
                text =
                    "Applied: " +
                            farmer.applied_date
                                .substringBefore(
                                    "T"
                                )
                                .ifBlank {
                                    "Unknown"
                                },
                style =
                    MaterialTheme
                        .typography
                        .bodySmall,
                color =
                    FarmerTextGray
            )
        }

        AssistChip(
            onClick = {},
            label = {
                Text(farmer.status)
            },
            colors =
                AssistChipDefaults
                    .assistChipColors(
                        containerColor =
                            getStatusBackground(
                                farmer.status
                            ),
                        labelColor =
                            getStatusColour(
                                farmer.status
                            )
                    )
        )
    }
}

@Composable
private fun FarmerDocumentRow(
    document: String,
    onClick: () -> Unit
) {
    Column {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .clickable(
                        onClick = onClick
                    )
                    .padding(
                        vertical = 12.dp
                    ),
            verticalAlignment =
                Alignment.CenterVertically
        ) {
            Icon(
                imageVector =
                    Icons.Default
                        .Description,
                contentDescription =
                    "Document",
                tint =
                    HarvestGreen
            )

            Spacer(
                modifier =
                    Modifier.width(10.dp)
            )

            Text(
                text =
                    document.substringAfterLast(
                        "/"
                    ),
                modifier =
                    Modifier.weight(1f),
                maxLines = 1
            )

            Text(
                text = "Open",
                color =
                    HarvestGreen
            )
        }

        HorizontalDivider(
            color =
                Color(0xFFE0E0E0)
        )
    }
}

private fun getStatusBackground(
    status: String
): Color {
    return when {
        status.equals(
            other = "Rejected",
            ignoreCase = true
        ) ->
            Color(0xFFFFE5E5)

        status.equals(
            other = "Approved",
            ignoreCase = true
        ) ->
            LightGreen

        else ->
            Color(0xFFFFF1D6)
    }
}

private fun getStatusColour(
    status: String
): Color {
    return when {
        status.equals(
            other = "Rejected",
            ignoreCase = true
        ) ->
            Color(0xFFB3261E)

        status.equals(
            other = "Approved",
            ignoreCase = true
        ) ->
            HarvestGreen

        else ->
            Color(0xFF9A6700)
    }
}