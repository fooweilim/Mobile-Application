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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.githubdemo.ui.theme.PrimaryGreen
import com.example.githubdemo.viewmodel.userprofile.ProfileViewModel

@Composable
fun EditProfileScreen(
    onBack: () -> Unit = {},
    onSave: () -> Unit = {},
    profileViewModel: ProfileViewModel
) {
    val profile by
    profileViewModel.profile
        .collectAsState()

    val isLoading by
    profileViewModel.isLoading
        .collectAsState()

    val message by
    profileViewModel.message
        .collectAsState()

    val messageIsError by
    profileViewModel.messageIsError
        .collectAsState()

    var name by remember {
        mutableStateOf("")
    }

    var phone by remember {
        mutableStateOf("")
    }

    var address by remember {
        mutableStateOf("")
    }

    LaunchedEffect(profile?.id) {
        name =
            profile?.fullName.orEmpty()

        phone =
            profile?.phoneNumber.orEmpty()

        address =
            profile
                ?.additionalInformation
                .orEmpty()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Color(0xFFF4F6EE)
            )
    ) {
        BuyerProfilePageHeader(
            title = "Edit Profile",
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
            OutlinedTextField(
                value = name,
                onValueChange = {
                    name = it
                },
                label = {
                    Text("Full Name")
                },
                singleLine = true,
                modifier =
                    Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value =
                    profile?.email.orEmpty(),
                onValueChange = {},
                label = {
                    Text("Email")
                },
                enabled = false,
                singleLine = true,
                modifier =
                    Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = phone,
                onValueChange = { value ->
                    if (
                        value.length <= 12 &&
                        value.all {
                            it.isDigit()
                        }
                    ) {
                        phone = value
                    }
                },
                label = {
                    Text("Phone Number")
                },
                supportingText = {
                    Text(
                        "Enter 9 to 12 digits"
                    )
                },
                keyboardOptions =
                    KeyboardOptions(
                        keyboardType =
                            KeyboardType.Phone
                    ),
                singleLine = true,
                modifier =
                    Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = address,
                onValueChange = {
                    address = it
                },
                label = {
                    Text("Address")
                },
                minLines = 3,
                modifier =
                    Modifier.fillMaxWidth()
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
                    profileViewModel
                        .updateProfile(
                            fullName = name,
                            phoneNumber = phone,
                            address = address,
                            onSuccess = onSave
                        )
                },
                enabled =
                    !isLoading &&
                            profile != null,
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
                    Text("Save Changes")
                }
            }
        }
    }
}