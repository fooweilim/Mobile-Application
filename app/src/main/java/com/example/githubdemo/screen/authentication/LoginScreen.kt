package com.example.githubdemo.screen.authentication

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.githubdemo.data.AuthValidation
import com.example.githubdemo.data.UserRole
import com.example.githubdemo.ui.theme.MainText
import com.example.githubdemo.ui.theme.PageBackground
import com.example.githubdemo.ui.theme.PrimaryGreen
import com.example.githubdemo.ui.theme.SecondaryText
import com.example.githubdemo.viewmodel.authentication.AuthViewModel

@Composable
fun LoginScreen(
    userRole: String,
    onLoginSuccess: (String) -> Unit,
    onSignUpClick: (String) -> Unit,
    onBackClick: () -> Unit,
    authViewModel: AuthViewModel
) {
    var email by rememberSaveable {
        mutableStateOf("")
    }

    var password by rememberSaveable {
        mutableStateOf("")
    }

    var passwordVisible by rememberSaveable {
        mutableStateOf(false)
    }

    var formSubmitted by rememberSaveable {
        mutableStateOf(false)
    }

    var showResetDialog by rememberSaveable {
        mutableStateOf(false)
    }

    val emailError =
        formSubmitted &&
                !AuthValidation.isValidEmail(
                    email
                )

    val passwordError =
        formSubmitted &&
                !AuthValidation.isValidPassword(
                    password
                )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(PageBackground),

        contentPadding =
            PaddingValues(bottom = 36.dp),

        horizontalAlignment =
            Alignment.CenterHorizontally
    ) {
        item {
            Box(
                modifier = Modifier
                    .widthIn(max = 600.dp)
                    .fillMaxWidth()
            ) {
                AuthenticationHeader(
                    userRole = userRole,

                    title =
                        "Welcome back",

                    subtitle =
                        "Sign in to your HarvestLink account",

                    onBackClick =
                        onBackClick
                )

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            start = 24.dp,
                            top = 350.dp,
                            end = 24.dp
                        ),

                    shape =
                        RoundedCornerShape(30.dp),

                    colors =
                        CardDefaults.cardColors(
                            containerColor =
                                Color.White
                        ),

                    elevation =
                        CardDefaults
                            .cardElevation(
                                defaultElevation =
                                    4.dp
                            )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(28.dp),

                        verticalArrangement =
                            Arrangement
                                .spacedBy(18.dp)
                    ) {
                        AuthenticationTextField(
                            value = email,

                            onValueChange = {
                                    newEmail ->

                                if (
                                    newEmail.length <= 60
                                ) {
                                    email = newEmail

                                    authViewModel
                                        .clearMessage()
                                }
                            },

                            label =
                                "Email Address",

                            placeholder =
                                "$userRole@email.com",

                            leadingIcon =
                                Icons.Outlined.Email,

                            isError = emailError,

                            errorMessage =
                                "Enter a valid email address.",

                            keyboardType =
                                KeyboardType.Email,

                            imeAction =
                                ImeAction.Next
                        )

                        AuthenticationTextField(
                            value = password,

                            onValueChange = {
                                    newPassword ->

                                if (
                                    newPassword.length <= 30
                                ) {
                                    password =
                                        newPassword

                                    authViewModel
                                        .clearMessage()
                                }
                            },

                            label = "Password",

                            placeholder =
                                "Enter your password",

                            leadingIcon =
                                Icons.Outlined.Lock,

                            isError =
                                passwordError,

                            errorMessage =
                                "Use at least 8 characters, one letter and one number.",

                            keyboardType =
                                KeyboardType.Password,

                            imeAction =
                                ImeAction.Done,

                            isPassword = true,

                            passwordVisible =
                                passwordVisible,

                            onPasswordVisibilityChange = {
                                passwordVisible =
                                    !passwordVisible
                            }
                        )

                        if (
                            userRole !=
                            UserRole.ADMIN
                        ) {
                            TextButton(
                                onClick = {
                                    showResetDialog =
                                        true
                                },

                                modifier =
                                    Modifier.align(
                                        Alignment.End
                                    )
                            ) {
                                Text(
                                    text =
                                        "Forgot password?",

                                    color =
                                        PrimaryGreen,

                                    fontSize = 15.sp,

                                    fontWeight =
                                        FontWeight
                                            .SemiBold
                                )
                            }
                        }

                        if (
                            authViewModel.message
                                .isNotEmpty()
                        ) {
                            Text(
                                text =
                                    authViewModel
                                        .message,

                                modifier =
                                    Modifier
                                        .fillMaxWidth(),

                                color =
                                    if (
                                        authViewModel
                                            .messageIsError
                                    ) {
                                        Color(
                                            0xFFB3261E
                                        )
                                    } else {
                                        PrimaryGreen
                                    },

                                fontSize = 14.sp,

                                textAlign =
                                    TextAlign.Center
                            )
                        }

                        Button(
                            onClick = {
                                formSubmitted = true

                                authViewModel
                                    .clearMessage()

                                val formIsValid =
                                    AuthValidation
                                        .isValidEmail(
                                            email
                                        ) &&
                                            AuthValidation
                                                .isValidPassword(
                                                    password
                                                )

                                if (formIsValid) {
                                    authViewModel
                                        .login(
                                            email =
                                                email,

                                            password =
                                                password,

                                            selectedRole =
                                                userRole,

                                            onSuccess = {
                                                onLoginSuccess(
                                                    userRole
                                                )
                                            }
                                        )
                                }
                            },

                            enabled =
                                !authViewModel
                                    .isLoading,

                            modifier =
                                Modifier.fillMaxWidth(),

                            shape =
                                RoundedCornerShape(
                                    18.dp
                                ),

                            colors =
                                ButtonDefaults
                                    .buttonColors(
                                        containerColor =
                                            PrimaryGreen
                                    ),

                            contentPadding =
                                PaddingValues(
                                    vertical = 16.dp
                                )
                        ) {
                            Text(
                                text =
                                    if (
                                        authViewModel
                                            .isLoading
                                    ) {
                                        "Signing In..."
                                    } else {
                                        "Sign In"
                                    },

                                fontSize = 18.sp,

                                fontWeight =
                                    FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        if (userRole != UserRole.ADMIN) {
            item {
                Row(
                    modifier = Modifier
                        .widthIn(max = 600.dp)
                        .fillMaxWidth()
                        .padding(
                            start = 24.dp,
                            top = 60.dp,
                            end = 24.dp
                        ),

                    horizontalArrangement =
                        Arrangement.Center,

                    verticalAlignment =
                        Alignment.CenterVertically
                ) {
                    Text(
                        text =
                            "Don't have an account?",

                        color = SecondaryText,

                        fontSize = 15.sp
                    )

                    TextButton(
                        onClick = {
                            onSignUpClick(userRole)
                        }
                    ) {
                        Text(
                            text = "Sign Up",
                            color = MainText,
                            fontSize = 16.sp,
                            fontWeight =
                                FontWeight.Bold
                        )
                    }
                }
            }
        }
    }

    if (showResetDialog) {
        ResetPasswordDialog(
            userRole = userRole,

            initialEmail = email,

            authViewModel =
                authViewModel,

            onDismiss = {
                showResetDialog = false
            },

            onPasswordReset = {
                showResetDialog = false
            }
        )
    }
}