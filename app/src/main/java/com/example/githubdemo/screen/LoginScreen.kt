package com.example.githubdemo.screen

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.AdminPanelSettings
import androidx.compose.material.icons.outlined.Eco
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.ShoppingBag
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.githubdemo.data.AccountStorage
import com.example.githubdemo.data.AuthValidation
import com.example.githubdemo.data.UserRole
import com.example.githubdemo.ui.theme.ForestGreen
import com.example.githubdemo.ui.theme.GithubDemoTheme
import com.example.githubdemo.ui.theme.MainText
import com.example.githubdemo.ui.theme.PageBackground
import com.example.githubdemo.ui.theme.PrimaryGreen
import com.example.githubdemo.ui.theme.SecondaryText

@Composable
fun LoginScreen(
    userRole: String,
    onLoginSuccess: (String) -> Unit,
    onSignUpClick: (String) -> Unit,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current

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

    var loginMessage by rememberSaveable {
        mutableStateOf("")
    }

    var messageIsError by rememberSaveable {
        mutableStateOf(true)
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

    val emailPlaceholder =
        if (
            userRole == UserRole.ADMIN
        ) {
            AccountStorage.adminEmail
        } else {
            "$userRole@email.com"
        }

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
                    title = "Welcome back",
                    subtitle =
                        "Sign in to your HarvestLink account",
                    onBackClick = onBackClick
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
                        CardDefaults.cardElevation(
                            defaultElevation = 4.dp
                        )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(28.dp),
                        verticalArrangement =
                            Arrangement.spacedBy(
                                18.dp
                            )
                    ) {
                        AuthenticationTextField(
                            value = email,
                            onValueChange = {
                                email = it.take(60)
                                loginMessage = ""
                            },
                            label = "Email Address",
                            placeholder =
                                emailPlaceholder,
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
                                password = it.take(30)
                                loginMessage = ""
                            },
                            label = "Password",
                            placeholder =
                                "Enter your password",
                            leadingIcon =
                                Icons.Outlined.Lock,
                            isError = passwordError,
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

                        /*
                         * Buyer and Farmer can
                         * reset their passwords.
                         * Admin cannot change the
                         * prepared password here.
                         */
                        if (
                            userRole != UserRole.ADMIN
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
                                        FontWeight.SemiBold
                                )
                            }
                        }

                        if (
                            userRole == UserRole.ADMIN
                        ) {
                            Text(
                                text =
                                    "Admin accounts are provided by the system.",
                                modifier =
                                    Modifier.fillMaxWidth(),
                                color = SecondaryText,
                                fontSize = 13.sp,
                                textAlign =
                                    TextAlign.Center
                            )
                        }

                        if (
                            loginMessage.isNotEmpty()
                        ) {
                            Text(
                                text = loginMessage,
                                modifier =
                                    Modifier.fillMaxWidth(),
                                color =
                                    if (messageIsError) {
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
                                loginMessage = ""

                                val inputIsValid =
                                    AuthValidation
                                        .isValidEmail(
                                            email
                                        ) &&
                                            AuthValidation
                                                .isValidPassword(
                                                    password
                                                )

                                if (inputIsValid) {
                                    val loginSuccessful =
                                        AccountStorage
                                            .validateLogin(
                                                context =
                                                    context,
                                                userRole =
                                                    userRole,
                                                email =
                                                    email,
                                                password =
                                                    password
                                            )

                                    if (
                                        loginSuccessful
                                    ) {
                                        onLoginSuccess(
                                            userRole
                                        )
                                    } else {
                                        messageIsError =
                                            true

                                        loginMessage =
                                            "Incorrect email, password, or user role."
                                    }
                                }
                            },
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
                                text = "Sign In",
                                fontSize = 18.sp,
                                fontWeight =
                                    FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        /*
         * Sign Up is displayed only
         * for Buyer and Farmer.
         */
        if (
            UserRole.canSignUp(userRole)
        ) {
            item {
                Row(
                    modifier = Modifier
                        .widthIn(max = 600.dp)
                        .fillMaxWidth()
                        .padding(
                            start = 24.dp,
                            top = 40.dp,
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

    if (
        showResetDialog &&
        userRole != UserRole.ADMIN
    ) {
        ResetPasswordDialog(
            userRole = userRole,
            initialEmail = email,
            onDismiss = {
                showResetDialog = false
            },
            onPasswordReset = {
                showResetDialog = false
                messageIsError = false
                loginMessage =
                    "Password reset successfully."
            }
        )
    }
}

@Composable
fun AuthenticationHeader(
    userRole: String,
    title: String,
    subtitle: String,
    onBackClick: () -> Unit
) {
    val roleName =
        UserRole.getRoleName(userRole)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(430.dp)
            .background(
                color = ForestGreen,
                shape = RoundedCornerShape(
                    bottomStart = 56.dp,
                    bottomEnd = 56.dp
                )
            )
            .statusBarsPadding()
            .padding(
                start = 24.dp,

                /*
                 * Previously 18.dp.
                 * A smaller value moves the
                 * back arrow upward.
                 */
                top = 6.dp,

                end = 24.dp,
                bottom = 40.dp
            )
    ) {
        IconButton(
            onClick = onBackClick,
            modifier = Modifier
                .size(52.dp)
                .background(
                    color = Color(0xFF3F8269),
                    shape = CircleShape
                )
        ) {
            Icon(
                imageVector =
                    Icons.AutoMirrored
                        .Filled.ArrowBack,
                contentDescription = "Back",
                tint = Color.White
            )
        }

        Spacer(
            modifier = Modifier.height(32.dp)
        )

        Row(
            verticalAlignment =
                Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .background(
                        color =
                            Color(0xFF4F947A),
                        shape = CircleShape
                    ),
                contentAlignment =
                    Alignment.Center
            ) {
                Icon(
                    imageVector =
                        getRoleIcon(userRole),
                    contentDescription =
                        roleName,
                    tint = Color.White,
                    modifier =
                        Modifier.size(25.dp)
                )
            }

            Text(
                text =
                    "${roleName.uppercase()} ACCOUNT",
                modifier =
                    Modifier.padding(
                        start = 12.dp
                    ),
                color = Color.White,
                fontSize = 16.sp,
                fontWeight =
                    FontWeight.Bold
            )
        }

        Spacer(
            modifier = Modifier.height(24.dp)
        )

        Text(
            text = title,
            color = Color.White,
            fontSize = 42.sp,
            lineHeight = 48.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Serif
        )

        Spacer(
            modifier = Modifier.height(6.dp)
        )

        Text(
            text = subtitle,
            color = Color(0xFFC6D9D1),
            fontSize = 17.sp
        )
    }
}

@Composable
fun AuthenticationTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
    leadingIcon: ImageVector,
    isError: Boolean,
    errorMessage: String,
    keyboardType: KeyboardType,
    imeAction: ImeAction,
    isPassword: Boolean = false,
    passwordVisible: Boolean = false,
    onPasswordVisibilityChange:
        () -> Unit = {}
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = label,
            color = MainText,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                Text(
                    text = placeholder,
                    color = Color(0xFF9AA8A2)
                )
            },
            leadingIcon = {
                Icon(
                    imageVector =
                        leadingIcon,
                    contentDescription =
                        label,
                    tint = SecondaryText
                )
            },
            trailingIcon = {
                if (isPassword) {
                    IconButton(
                        onClick =
                            onPasswordVisibilityChange
                    ) {
                        Icon(
                            imageVector =
                                if (
                                    passwordVisible
                                ) {
                                    Icons.Outlined
                                        .VisibilityOff
                                } else {
                                    Icons.Outlined
                                        .Visibility
                                },
                            contentDescription =
                                if (
                                    passwordVisible
                                ) {
                                    "Hide password"
                                } else {
                                    "Show password"
                                },
                            tint = SecondaryText
                        )
                    }
                }
            },
            visualTransformation =
                if (
                    isPassword &&
                    !passwordVisible
                ) {
                    PasswordVisualTransformation()
                } else {
                    VisualTransformation.None
                },
            isError = isError,
            singleLine = true,
            keyboardOptions =
                KeyboardOptions(
                    keyboardType =
                        keyboardType,
                    imeAction = imeAction
                ),
            shape =
                RoundedCornerShape(18.dp),
            colors =
                OutlinedTextFieldDefaults.colors(
                    focusedContainerColor =
                        Color(0xFFF7F4EE),
                    unfocusedContainerColor =
                        Color(0xFFF7F4EE),
                    errorContainerColor =
                        Color(0xFFFFF2F0),
                    focusedBorderColor =
                        PrimaryGreen,
                    unfocusedBorderColor =
                        Color.Transparent
                )
        )

        if (isError) {
            Text(
                text = errorMessage,
                modifier = Modifier.padding(
                    start = 12.dp,
                    top = 5.dp
                ),
                color = Color(0xFFB3261E),
                fontSize = 12.sp
            )
        }
    }
}

@Composable
fun ResetPasswordDialog(
    userRole: String,
    initialEmail: String,
    onDismiss: () -> Unit,
    onPasswordReset: () -> Unit
) {
    val context = LocalContext.current

    var email by rememberSaveable {
        mutableStateOf(initialEmail)
    }

    var newPassword by rememberSaveable {
        mutableStateOf("")
    }

    var confirmPassword by rememberSaveable {
        mutableStateOf("")
    }

    var resetMessage by rememberSaveable {
        mutableStateOf("")
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Reset Password"
            )
        },
        text = {
            Column(
                verticalArrangement =
                    Arrangement.spacedBy(
                        12.dp
                    )
            ) {
                OutlinedTextField(
                    value = email,
                    onValueChange = {
                        email = it.take(60)
                        resetMessage = ""
                    },
                    label = {
                        Text(
                            text =
                                "Email Address"
                        )
                    },
                    keyboardOptions =
                        KeyboardOptions(
                            keyboardType =
                                KeyboardType.Email
                        ),
                    singleLine = true
                )

                OutlinedTextField(
                    value = newPassword,
                    onValueChange = {
                        newPassword =
                            it.take(30)

                        resetMessage = ""
                    },
                    label = {
                        Text(
                            text =
                                "New Password"
                        )
                    },
                    visualTransformation =
                        PasswordVisualTransformation(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = {
                        confirmPassword =
                            it.take(30)

                        resetMessage = ""
                    },
                    label = {
                        Text(
                            text =
                                "Confirm Password"
                        )
                    },
                    visualTransformation =
                        PasswordVisualTransformation(),
                    singleLine = true
                )

                if (
                    resetMessage.isNotEmpty()
                ) {
                    Text(
                        text = resetMessage,
                        color =
                            Color(0xFFB3261E),
                        fontSize = 13.sp
                    )
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    when {
                        !AuthValidation
                            .isValidEmail(email) -> {
                            resetMessage =
                                "Enter a valid email address."
                        }

                        !AuthValidation
                            .isValidPassword(
                                newPassword
                            ) -> {
                            resetMessage =
                                "Use at least 8 characters, one letter and one number."
                        }

                        !AuthValidation
                            .passwordsMatch(
                                newPassword,
                                confirmPassword
                            ) -> {
                            resetMessage =
                                "Passwords do not match."
                        }

                        else -> {
                            val updated =
                                AccountStorage
                                    .updatePassword(
                                        context =
                                            context,
                                        userRole =
                                            userRole,
                                        email =
                                            email,
                                        newPassword =
                                            newPassword
                                    )

                            if (updated) {
                                onPasswordReset()
                            } else {
                                resetMessage =
                                    "No $userRole account uses this email."
                            }
                        }
                    }
                }
            ) {
                Text(
                    text = "Reset"
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss
            ) {
                Text(
                    text = "Cancel"
                )
            }
        }
    )
}

fun getRoleIcon(
    userRole: String
): ImageVector {
    return when (userRole) {
        UserRole.BUYER ->
            Icons.Outlined.ShoppingBag

        UserRole.FARMER ->
            Icons.Outlined.Eco

        UserRole.ADMIN ->
            Icons.Outlined.AdminPanelSettings

        else ->
            Icons.Outlined.ShoppingBag
    }
}

@Preview(
    showBackground = true,
    widthDp = 412,
    heightDp = 915
)
@Composable
fun AdminLoginScreenPreview() {
    GithubDemoTheme(
        darkTheme = false,
        dynamicColor = false
    ) {
        LoginScreen(
            userRole = UserRole.ADMIN,
            onLoginSuccess = {},
            onSignUpClick = {},
            onBackClick = {}
        )
    }
}