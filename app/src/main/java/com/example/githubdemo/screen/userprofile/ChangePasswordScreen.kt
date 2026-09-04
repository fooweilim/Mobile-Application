package com.example.githubdemo.screen.userprofile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.githubdemo.data.AuthValidation
import com.example.githubdemo.data.local.LocalAccountStorage
import com.example.githubdemo.supabase.SupabaseConnection
import com.example.githubdemo.ui.theme.PrimaryGreen
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import kotlinx.coroutines.launch

@Composable
fun ChangePasswordScreen(
    onBack: () -> Unit = {}
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var currentPassword by remember {
        mutableStateOf("")
    }

    var newPassword by remember {
        mutableStateOf("")
    }

    var confirmPassword by remember {
        mutableStateOf("")
    }

    var message by remember {
        mutableStateOf("")
    }

    var messageIsError by remember {
        mutableStateOf(false)
    }

    var isLoading by remember {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Color(0xFFF4F6EE)
            )
    ) {
        BuyerProfilePageHeader(
            title = "Change Password",
            onBack = onBack
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(
                    rememberScrollState()
                )
                .padding(16.dp)
        ) {
            PasswordField(
                value = currentPassword,
                label = "Current Password",
                onValueChange = {
                    currentPassword = it
                }
            )

            Spacer(Modifier.height(12.dp))

            PasswordField(
                value = newPassword,
                label = "New Password",
                onValueChange = {
                    newPassword = it
                }
            )

            Spacer(Modifier.height(12.dp))

            PasswordField(
                value = confirmPassword,
                label =
                    "Confirm New Password",
                onValueChange = {
                    confirmPassword = it
                }
            )

            Text(
                text =
                    "Use at least 8 characters with a letter and a number.",
                color = Color.Gray,
                style =
                    MaterialTheme.typography
                        .bodySmall,
                modifier =
                    Modifier.padding(top = 8.dp)
            )

            if (message.isNotBlank()) {
                Text(
                    text = message,
                    color =
                        if (messageIsError) {
                            MaterialTheme
                                .colorScheme.error
                        } else {
                            PrimaryGreen
                        },
                    modifier =
                        Modifier.padding(
                            top = 12.dp
                        )
                )
            }

            Spacer(Modifier.height(20.dp))

            Button(
                onClick = {
                    when {
                        currentPassword
                            .isBlank() -> {
                            message =
                                "Enter your current password."
                            messageIsError =
                                true
                        }

                        !AuthValidation
                            .isValidPassword(
                                newPassword
                            ) -> {
                            message =
                                "New password must have at least 8 characters, including a letter and a number."
                            messageIsError =
                                true
                        }

                        newPassword !=
                                confirmPassword -> {
                            message =
                                "New passwords do not match."
                            messageIsError =
                                true
                        }

                        currentPassword ==
                                newPassword -> {
                            message =
                                "New password must be different from the current password."
                            messageIsError =
                                true
                        }

                        else -> {
                            scope.launch {
                                val email =
                                    LocalAccountStorage
                                        .getProfile(
                                            context
                                        )
                                        ?.email

                                if (
                                    email.isNullOrBlank()
                                ) {
                                    message =
                                        "Unable to find your account email. Please sign in again."
                                    messageIsError =
                                        true

                                    return@launch
                                }

                                isLoading = true
                                message = ""

                                try {
                                    SupabaseConnection
                                        .supabase
                                        .auth
                                        .signInWith(
                                            Email
                                        ) {
                                            this.email =
                                                email

                                            password =
                                                currentPassword
                                        }

                                    SupabaseConnection
                                        .supabase
                                        .auth
                                        .updateUser {
                                            password =
                                                newPassword
                                        }

                                    currentPassword = ""
                                    newPassword = ""
                                    confirmPassword = ""

                                    message =
                                        "Password updated successfully."

                                    messageIsError =
                                        false
                                } catch (
                                    exception: Exception
                                ) {
                                    val rawMessage =
                                        exception
                                            .message
                                            .orEmpty()

                                    message =
                                        if (
                                            rawMessage
                                                .contains(
                                                    "invalid login credentials",
                                                    ignoreCase =
                                                        true
                                                )
                                        ) {
                                            "Current password is incorrect."
                                        } else {
                                            rawMessage
                                                .ifBlank {
                                                    "Unable to update your password."
                                                }
                                        }

                                    messageIsError =
                                        true
                                } finally {
                                    isLoading = false
                                }
                            }
                        }
                    }
                },
                enabled = !isLoading,
                modifier =
                    Modifier.fillMaxWidth(),
                colors =
                    ButtonDefaults.buttonColors(
                        containerColor =
                            PrimaryGreen
                    ),
                shape =
                    RoundedCornerShape(14.dp)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier =
                            Modifier.height(22.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("Update Password")
                }
            }
        }
    }
}

@Composable
private fun PasswordField(
    value: String,
    label: String,
    onValueChange: (String) -> Unit
) {
    var visible by remember {
        mutableStateOf(false)
    }

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = {
            Text(label)
        },
        singleLine = true,
        visualTransformation =
            if (visible) {
                VisualTransformation.None
            } else {
                PasswordVisualTransformation()
            },
        keyboardOptions =
            KeyboardOptions(
                keyboardType =
                    KeyboardType.Password
            ),
        trailingIcon = {
            IconButton(
                onClick = {
                    visible = !visible
                }
            ) {
                Icon(
                    imageVector =
                        if (visible) {
                            Icons.Default
                                .VisibilityOff
                        } else {
                            Icons.Default
                                .Visibility
                        },
                    contentDescription =
                        if (visible) {
                            "Hide password"
                        } else {
                            "Show password"
                        }
                )
            }
        },
        modifier = Modifier.fillMaxWidth()
    )
}