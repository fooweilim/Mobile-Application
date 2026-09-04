package com.example.githubdemo.screen.farmer

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun RemoveProductDialog(
    productName: String,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,

        title = {
            Text("Remove Product")
        },

        text = {
            Text(
                "Are you sure you want to remove $productName?"
            )
        },

        confirmButton = {
            Button(
                onClick = onConfirm,

                colors =
                    ButtonDefaults.buttonColors(
                        containerColor =
                            Color(0xFFB3261E)
                    )
            ) {
                Text("Remove")
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