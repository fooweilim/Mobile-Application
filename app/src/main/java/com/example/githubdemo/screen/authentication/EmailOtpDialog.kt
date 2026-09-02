package com.example.githubdemo.screen.authentication

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.githubdemo.ui.theme.PrimaryGreen
import com.example.githubdemo.viewmodel.authentication.AuthViewModel

@Composable
fun EmailOtpDialog(
    email: String,
    authViewModel: AuthViewModel,
    onVerifyOtp: (String) -> Unit,
    onResendOtp: () -> Unit,
    onDismiss: () -> Unit
) {
    var otp by rememberSaveable {
        mutableStateOf("")
    }

    AlertDialog(
        onDismissRequest = {
            if (!authViewModel.isLoading) {
                authViewModel.clearMessage()
                onDismiss()
            }
        },

        title = {
            Text("Verify Email")
        },

        text = {
            Column(
                verticalArrangement =
                    Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text =
                        "A 8-digit OTP was sent to $email.",

                    fontSize = 14.sp
                )

                OutlinedTextField(
                    value = otp,

                    onValueChange = { newOtp ->
                        otp = newOtp
                            .filter { character ->
                                character.isDigit()
                            }
                            .take(8)

                        authViewModel.clearMessage()
                    },

                    label = {
                        Text("Email OTP")
                    },

                    placeholder = {
                        Text("12345678")
                    },

                    singleLine = true,

                    keyboardOptions =
                        KeyboardOptions(
                            keyboardType =
                                KeyboardType
                                    .NumberPassword
                        )
                )

                if (
                    authViewModel.message
                        .isNotEmpty()
                ) {
                    Text(
                        text =
                            authViewModel.message,

                        color =
                            if (
                                authViewModel
                                    .messageIsError
                            ) {
                                Color(0xFFB3261E)
                            } else {
                                PrimaryGreen
                            },

                        fontSize = 13.sp
                    )
                }

                TextButton(
                    onClick = onResendOtp,

                    enabled =
                        !authViewModel.isLoading
                ) {
                    Text("Resend OTP")
                }
            }
        },

        confirmButton = {
            TextButton(
                onClick = {
                    if (otp.length != 8) {
                        authViewModel
                            .showErrorMessage(
                                "Enter the 8-digit OTP."
                            )
                    } else {
                        onVerifyOtp(otp)
                    }
                },

                enabled =
                    !authViewModel.isLoading
            ) {
                Text(
                    text =
                        if (
                            authViewModel
                                .isLoading
                        ) {
                            "Please wait..."
                        } else {
                            "Verify"
                        }
                )
            }
        },

        dismissButton = {
            TextButton(
                onClick = {
                    authViewModel.clearMessage()
                    onDismiss()
                },

                enabled =
                    !authViewModel.isLoading
            ) {
                Text("Cancel")
            }
        }
    )
}