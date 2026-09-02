package com.example.githubdemo.screen.authentication

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.AdminPanelSettings
import androidx.compose.material.icons.outlined.Eco
import androidx.compose.material.icons.outlined.ShoppingBag
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.githubdemo.data.UserRole
import com.example.githubdemo.ui.theme.ForestGreen
import com.example.githubdemo.ui.theme.MainText
import com.example.githubdemo.ui.theme.PrimaryGreen
import com.example.githubdemo.ui.theme.SecondaryText

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
                top = 8.dp,
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
            modifier = Modifier.height(30.dp)
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
                    Modifier.padding(start = 12.dp),

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

            modifier =
                Modifier.fillMaxWidth(),

            placeholder = {
                Text(
                    text = placeholder,
                    color = Color(0xFF9AA8A2)
                )
            },

            leadingIcon = {
                Icon(
                    imageVector = leadingIcon,
                    contentDescription = label,
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
                                if (passwordVisible) {
                                    Icons.Outlined
                                        .VisibilityOff
                                } else {
                                    Icons.Outlined
                                        .Visibility
                                },

                            contentDescription =
                                if (passwordVisible) {
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
                OutlinedTextFieldDefaults
                    .colors(
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

fun getRoleIcon(
    userRole: String
): ImageVector {
    return when (userRole) {
        UserRole.BUYER ->
            Icons.Outlined.ShoppingBag

        UserRole.FARMER ->
            Icons.Outlined.Eco

        UserRole.ADMIN ->
            Icons.Outlined
                .AdminPanelSettings

        else ->
            Icons.Outlined.ShoppingBag
    }
}