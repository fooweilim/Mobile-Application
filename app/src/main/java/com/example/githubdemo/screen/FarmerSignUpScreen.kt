package com.example.githubdemo.screen

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.layout.offset
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.outlined.Badge
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Eco
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.FileUpload
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material.icons.outlined.VerifiedUser
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.githubdemo.data.AccountStorage
import com.example.githubdemo.data.AuthValidation
import com.example.githubdemo.data.UserRole
import com.example.githubdemo.model.UserAccount
import com.example.githubdemo.ui.theme.ForestGreen
import com.example.githubdemo.ui.theme.GithubDemoTheme
import com.example.githubdemo.ui.theme.MainText
import com.example.githubdemo.ui.theme.PageBackground
import com.example.githubdemo.ui.theme.PrimaryGreen
import com.example.githubdemo.ui.theme.SecondaryText

@Composable
fun FarmerSignUpScreen(
    onSignUpSuccess: (String) -> Unit,
    onLoginClick: (String) -> Unit,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current

    var currentStep by rememberSaveable {
        mutableIntStateOf(1)
    }

    var formSubmitted by rememberSaveable {
        mutableStateOf(false)
    }

    var formMessage by rememberSaveable {
        mutableStateOf("")
    }

    var fullName by rememberSaveable {
        mutableStateOf("")
    }

    var email by rememberSaveable {
        mutableStateOf("")
    }

    var phoneNumber by rememberSaveable {
        mutableStateOf("")
    }

    var icNumber by rememberSaveable {
        mutableStateOf("")
    }

    var password by rememberSaveable {
        mutableStateOf("")
    }

    var confirmPassword by rememberSaveable {
        mutableStateOf("")
    }

    var passwordVisible by rememberSaveable {
        mutableStateOf(false)
    }

    var confirmPasswordVisible by rememberSaveable {
        mutableStateOf(false)
    }

    var farmName by rememberSaveable {
        mutableStateOf("")
    }

    var stateName by rememberSaveable {
        mutableStateOf("")
    }

    var farmType by rememberSaveable {
        mutableStateOf("")
    }

    var icFrontUri by rememberSaveable {
        mutableStateOf("")
    }

    var icBackUri by rememberSaveable {
        mutableStateOf("")
    }

    var farmCertificateUri by rememberSaveable {
        mutableStateOf("")
    }

    val icFrontLauncher =
        rememberLauncherForActivityResult(
            contract =
                ActivityResultContracts
                    .OpenDocument()
        ) { selectedUri ->

            if (selectedUri != null) {
                saveDocumentPermission(
                    context = context,
                    selectedUri = selectedUri
                )

                icFrontUri =
                    selectedUri.toString()

                formMessage = ""
            }
        }

    val icBackLauncher =
        rememberLauncherForActivityResult(
            contract =
                ActivityResultContracts
                    .OpenDocument()
        ) { selectedUri ->

            if (selectedUri != null) {
                saveDocumentPermission(
                    context = context,
                    selectedUri = selectedUri
                )

                icBackUri =
                    selectedUri.toString()

                formMessage = ""
            }
        }

    val farmCertificateLauncher =
        rememberLauncherForActivityResult(
            contract =
                ActivityResultContracts
                    .OpenDocument()
        ) { selectedUri ->

            if (selectedUri != null) {
                saveDocumentPermission(
                    context = context,
                    selectedUri = selectedUri
                )

                farmCertificateUri =
                    selectedUri.toString()

                formMessage = ""
            }
        }

    val validIcNumber =
        icNumber
            .filter { character ->
                character.isDigit()
            }
            .length == 12

    val accountInformationValid =
        AuthValidation.isValidName(
            fullName
        ) &&
                AuthValidation.isValidEmail(
                    email
                ) &&
                AuthValidation.isValidPhoneNumber(
                    phoneNumber
                ) &&
                validIcNumber &&
                AuthValidation.isValidPassword(
                    password
                ) &&
                AuthValidation.passwordsMatch(
                    password,
                    confirmPassword
                )

    val farmInformationValid =
        farmName.trim().length >= 2 &&
                stateName.trim().length >= 2 &&
                farmType.isNotBlank()

    val documentInformationValid =
        icFrontUri.isNotBlank() &&
                icBackUri.isNotBlank() &&
                farmCertificateUri.isNotBlank()

    val handleBackClick = {
        if (currentStep > 1) {
            currentStep -= 1
            formSubmitted = false
            formMessage = ""
        } else {
            onBackClick()
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(PageBackground),
        contentPadding =
            PaddingValues(bottom = 30.dp),
        horizontalAlignment =
            Alignment.CenterHorizontally
    ) {
        item {
            FarmerRegistrationHeader(
                currentStep = currentStep,
                onBackClick =
                    handleBackClick
            )
        }

        when (currentStep) {
            1 -> {
                item {
                    FarmerStepCard {
                        FarmerAccountStep(
                            fullName =
                                fullName,

                            onFullNameChange = {
                                fullName =
                                    it.take(50)

                                formMessage = ""
                            },

                            email = email,

                            onEmailChange = {
                                email =
                                    it.take(60)

                                formMessage = ""
                            },

                            phoneNumber =
                                phoneNumber,

                            onPhoneNumberChange = {
                                    newPhoneNumber ->

                                if (
                                    newPhoneNumber
                                        .length <= 12 &&
                                    newPhoneNumber
                                        .all {
                                                character ->

                                            character
                                                .isDigit()
                                        }
                                ) {
                                    phoneNumber =
                                        newPhoneNumber

                                    formMessage = ""
                                }
                            },

                            icNumber =
                                icNumber,

                            onIcNumberChange = {
                                    newIcNumber ->

                                if (
                                    newIcNumber
                                        .length <= 14 &&
                                    newIcNumber
                                        .all {
                                                character ->

                                            character
                                                .isDigit() ||
                                                    character ==
                                                    '-'
                                        }
                                ) {
                                    icNumber =
                                        newIcNumber

                                    formMessage = ""
                                }
                            },

                            password =
                                password,

                            onPasswordChange = {
                                password =
                                    it.take(30)

                                formMessage = ""
                            },

                            confirmPassword =
                                confirmPassword,

                            onConfirmPasswordChange = {
                                confirmPassword =
                                    it.take(30)

                                formMessage = ""
                            },

                            passwordVisible =
                                passwordVisible,

                            onPasswordVisibilityChange = {
                                passwordVisible =
                                    !passwordVisible
                            },

                            confirmPasswordVisible =
                                confirmPasswordVisible,

                            onConfirmPasswordVisibilityChange = {
                                confirmPasswordVisible =
                                    !confirmPasswordVisible
                            },

                            formSubmitted =
                                formSubmitted,

                            validIcNumber =
                                validIcNumber
                        )
                    }
                }
            }

            2 -> {
                item {
                    FarmerStepCard {
                        FarmerDetailsStep(
                            farmName =
                                farmName,

                            onFarmNameChange = {
                                farmName =
                                    it.take(60)

                                formMessage = ""
                            },

                            stateName =
                                stateName,

                            onStateNameChange = {
                                stateName =
                                    it.take(50)

                                formMessage = ""
                            },

                            farmType =
                                farmType,

                            onFarmTypeSelected = {
                                    selectedFarmType ->

                                farmType =
                                    selectedFarmType

                                formMessage = ""
                            },

                            formSubmitted =
                                formSubmitted
                        )
                    }
                }
            }

            3 -> {
                item {
                    FarmerDocumentsStep(
                        icFrontUri =
                            icFrontUri,

                        onIcFrontUpload = {
                            icFrontLauncher.launch(
                                arrayOf(
                                    "image/*",
                                    "application/pdf"
                                )
                            )
                        },

                        icBackUri =
                            icBackUri,

                        onIcBackUpload = {
                            icBackLauncher.launch(
                                arrayOf(
                                    "image/*",
                                    "application/pdf"
                                )
                            )
                        },

                        farmCertificateUri =
                            farmCertificateUri,

                        onFarmCertificateUpload = {
                            farmCertificateLauncher
                                .launch(
                                    arrayOf(
                                        "image/*",
                                        "application/pdf"
                                    )
                                )
                        }
                    )
                }
            }

            4 -> {
                item {
                    FarmerReviewStep(
                        fullName =
                            fullName,

                        email = email,

                        phoneNumber =
                            phoneNumber,

                        farmName =
                            farmName,

                        stateName =
                            stateName,

                        farmType =
                            farmType,

                        documentsUploaded =
                            listOf(
                                icFrontUri,
                                icBackUri,
                                farmCertificateUri
                            ).count {
                                    documentUri ->

                                documentUri
                                    .isNotBlank()
                            }
                    )
                }
            }
        }

        if (
            formMessage.isNotEmpty()
        ) {
            item {
                Text(
                    text = formMessage,
                    modifier = Modifier
                        .widthIn(max = 600.dp)
                        .fillMaxWidth()
                        .padding(
                            horizontal = 28.dp,
                            vertical = 8.dp
                        ),
                    color = Color(0xFFB3261E),
                    fontSize = 13.sp,
                    textAlign =
                        TextAlign.Center
                )
            }
        }

        item {
            Button(
                onClick = {
                    formSubmitted = true
                    formMessage = ""

                    when (currentStep) {
                        1 -> {
                            if (
                                accountInformationValid
                            ) {
                                currentStep = 2
                                formSubmitted =
                                    false
                            } else {
                                formMessage =
                                    "Please complete all account information correctly."
                            }
                        }

                        2 -> {
                            if (
                                farmInformationValid
                            ) {
                                currentStep = 3
                                formSubmitted =
                                    false
                            } else {
                                formMessage =
                                    "Please enter the farm name, state and farm type."
                            }
                        }

                        3 -> {
                            if (
                                documentInformationValid
                            ) {
                                currentStep = 4
                                formSubmitted =
                                    false
                            } else {
                                formMessage =
                                    "Please upload all three required documents."
                            }
                        }

                        4 -> {
                            val additionalInformation =
                                """
                                IC Number: $icNumber
                                Farm Name: $farmName
                                State: $stateName
                                Farm Type: $farmType
                                IC Front: $icFrontUri
                                IC Back: $icBackUri
                                Farm Certificate: $farmCertificateUri
                                Verification Status: Pending
                                """.trimIndent()

                            val farmerAccount =
                                UserAccount(
                                    userRole =
                                        UserRole.FARMER,

                                    fullName =
                                        fullName.trim(),

                                    email =
                                        email.trim(),

                                    phoneNumber =
                                        phoneNumber.trim(),

                                    additionalInformation =
                                        additionalInformation,

                                    password =
                                        password
                                )

                            val accountSaved =
                                AccountStorage
                                    .saveAccount(
                                        context =
                                            context,

                                        userAccount =
                                            farmerAccount
                                    )

                            if (accountSaved) {
                                Toast.makeText(
                                    context,
                                    "Registration submitted for verification.",
                                    Toast.LENGTH_SHORT
                                ).show()

                                onSignUpSuccess(
                                    UserRole.FARMER
                                )
                            } else {
                                formMessage =
                                    "This email is already registered as a Farmer."
                            }
                        }
                    }
                },
                modifier = Modifier
                    .widthIn(max = 600.dp)
                    .fillMaxWidth()
                    .padding(
                        start = 27.dp,
                        top = 2.dp,
                        end = 27.dp
                    ),
                shape =
                    RoundedCornerShape(17.dp),
                colors =
                    ButtonDefaults.buttonColors(
                        containerColor =
                            ForestGreen
                    ),
                contentPadding =
                    PaddingValues(
                        vertical = 16.dp
                    )
            ) {
                Text(
                    text =
                        if (currentStep == 4) {
                            "Submit for Verification"
                        } else {
                            "Continue"
                        },
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight =
                        FontWeight.Bold
                )
            }
        }

        if (currentStep == 1) {
            item {
                Row(
                    modifier = Modifier
                        .widthIn(max = 600.dp)
                        .fillMaxWidth()
                        .padding(
                            start = 24.dp,
                            top = 8.dp,
                            end = 24.dp
                        ),
                    horizontalArrangement =
                        Arrangement.Center,
                    verticalAlignment =
                        Alignment.CenterVertically
                ) {
                    Text(
                        text =
                            "Already have an account?",
                        color = SecondaryText,
                        fontSize = 14.sp
                    )

                    TextButton(
                        onClick = {
                            onLoginClick(
                                UserRole.FARMER
                            )
                        }
                    ) {
                        Text(
                            text = "Sign In",
                            color = MainText,
                            fontSize = 16.sp,
                            fontFamily =
                                FontFamily.Serif,
                            fontWeight =
                                FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun FarmerRegistrationHeader(
    currentStep: Int,
    onBackClick: () -> Unit
) {
    val pageTitle =
        when (currentStep) {
            1 -> "Create Account"
            2 -> "Farm Details"
            3 -> "Upload Documents"
            4 -> "Review & Submit"
            else -> "Create Account"
        }

    Column(
        modifier = Modifier
            .widthIn(max = 600.dp)
            .fillMaxWidth()
            .background(
                color = ForestGreen,
                shape = RoundedCornerShape(
                    bottomStart = 42.dp,
                    bottomEnd = 42.dp
                )
            )
            .statusBarsPadding()
            .padding(
                start = 23.dp,
                top = 4.dp,
                end = 23.dp,
                bottom = 34.dp
            )
    ) {
        IconButton(
            onClick = onBackClick,
            modifier = Modifier
                .size(44.dp)
                .background(
                    color = Color(0xFF315C50),
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
            modifier = Modifier.height(17.dp)
        )

        Row(
            verticalAlignment =
                Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .background(
                        color =
                            Color(0xFF2D7459),
                        shape = CircleShape
                    ),
                contentAlignment =
                    Alignment.Center
            ) {
                Icon(
                    imageVector =
                        Icons.Outlined.Eco,
                    contentDescription =
                        "Farmer Registration",
                    modifier =
                        Modifier.size(22.dp),
                    tint =
                        Color(0xFF55D69B)
                )
            }

            Text(
                text =
                    "FARMER REGISTRATION",
                modifier =
                    Modifier.padding(
                        start = 10.dp
                    ),
                color = Color(0xFF55D69B),
                fontSize = 14.sp,
                fontWeight =
                    FontWeight.Bold
            )
        }

        Spacer(
            modifier = Modifier.height(14.dp)
        )

        Text(
            text = pageTitle,
            color = Color.White,
            fontSize = 29.sp,
            lineHeight = 34.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Serif
        )

        Spacer(
            modifier = Modifier.height(17.dp)
        )

        FarmerRegistrationProgress(
            currentStep = currentStep
        )
    }
}

@Composable
fun FarmerRegistrationProgress(
    currentStep: Int
) {
    val stepNames = listOf(
        "Account",
        "Farm Info",
        "Documents",
        "Review"
    )

    Row(
        modifier =
            Modifier.fillMaxWidth(),
        verticalAlignment =
            Alignment.CenterVertically
    ) {
        for (stepNumber in 1..4) {

            val stepCompleted =
                stepNumber < currentStep

            val current =
                stepNumber == currentStep

            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(28.dp),
                contentAlignment =
                    Alignment.Center
            ) {
                if (stepNumber > 1) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.5f)
                            .height(2.dp)
                            .align(
                                Alignment.CenterStart
                            )
                            .background(
                                color =
                                    if (
                                        stepNumber <=
                                        currentStep
                                    ) {
                                        Color(
                                            0xFF55C995
                                        )
                                    } else {
                                        Color(
                                            0xFF577269
                                        )
                                    }
                            )
                    )
                }

                if (stepNumber < 4) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.5f)
                            .height(2.dp)
                            .align(
                                Alignment.CenterEnd
                            )
                            .background(
                                color =
                                    if (
                                        stepNumber <
                                        currentStep
                                    ) {
                                        Color(
                                            0xFF55C995
                                        )
                                    } else {
                                        Color(
                                            0xFF577269
                                        )
                                    }
                            )
                    )
                }

                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .background(
                            color = when {
                                current ->
                                    Color.White

                                stepCompleted ->
                                    Color(
                                        0xFF55C995
                                    )

                                else ->
                                    Color(
                                        0xFF577269
                                    )
                            },
                            shape = CircleShape
                        ),
                    contentAlignment =
                        Alignment.Center
                ) {
                    if (stepCompleted) {
                        Icon(
                            imageVector =
                                Icons.Filled.Check,
                            contentDescription =
                                "Completed",
                            modifier =
                                Modifier.size(
                                    16.dp
                                ),
                            tint = Color.White
                        )
                    } else {
                        Text(
                            text =
                                stepNumber
                                    .toString(),
                            color =
                                if (current) {
                                    ForestGreen
                                } else {
                                    Color(
                                        0xFFB9C7C2
                                    )
                                },
                            fontSize = 12.sp,
                            fontWeight =
                                FontWeight.Bold,
                            textAlign =
                                TextAlign.Center
                        )
                    }
                }
            }
        }
    }

    Spacer(
        modifier = Modifier.height(7.dp)
    )

    Row(
        modifier =
            Modifier.fillMaxWidth(),
        verticalAlignment =
            Alignment.Top
    ) {
        stepNames.forEachIndexed {
                index,
                stepName ->

            val stepNumber = index + 1

            Text(
                text = stepName,
                modifier = Modifier
                    .weight(1f)
                    .padding(
                        horizontal = 2.dp
                    ),
                color = when {
                    stepNumber ==
                            currentStep ->
                        Color.White

                    stepNumber <
                            currentStep ->
                        Color(0xFF55C995)

                    else ->
                        Color(0xFFB9C7C2)
                },
                fontSize = 10.sp,
                lineHeight = 13.sp,
                fontWeight =
                    if (
                        stepNumber ==
                        currentStep
                    ) {
                        FontWeight.SemiBold
                    } else {
                        FontWeight.Normal
                    },
                textAlign =
                    TextAlign.Center,
                maxLines = 1
            )
        }
    }
}

@Composable
fun FarmerStepCard(
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier
            .widthIn(max = 600.dp)
            .fillMaxWidth()
            .padding(horizontal = 26.dp)
            .offset(y = (-12).dp),
        shape =
            RoundedCornerShape(27.dp),
        colors =
            CardDefaults.cardColors(
                containerColor = Color.White
            ),
        elevation =
            CardDefaults.cardElevation(
                defaultElevation = 3.dp
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 23.dp,
                    top = 24.dp,
                    end = 23.dp,
                    bottom = 24.dp
                ),
            verticalArrangement =
                Arrangement.spacedBy(18.dp)
        ) {
            content()
        }
    }
}

@Composable
fun FarmerAccountStep(
    fullName: String,
    onFullNameChange: (String) -> Unit,
    email: String,
    onEmailChange: (String) -> Unit,
    phoneNumber: String,
    onPhoneNumberChange: (String) -> Unit,
    icNumber: String,
    onIcNumberChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    confirmPassword: String,
    onConfirmPasswordChange: (String) -> Unit,
    passwordVisible: Boolean,
    onPasswordVisibilityChange: () -> Unit,
    confirmPasswordVisible: Boolean,
    onConfirmPasswordVisibilityChange: () -> Unit,
    formSubmitted: Boolean,
    validIcNumber: Boolean
) {
    FarmerRegistrationTextField(
        value = fullName,
        onValueChange =
            onFullNameChange,
        label =
            "Full Name (as per IC)",
        placeholder =
            "e.g. Ali bin Hassan",
        leadingIcon =
            Icons.Outlined.Person,
        keyboardType =
            KeyboardType.Text,
        imeAction =
            ImeAction.Next,
        isError =
            formSubmitted &&
                    !AuthValidation
                        .isValidName(
                            fullName
                        ),
        errorMessage =
            "Enter your full name."
    )

    FarmerRegistrationTextField(
        value = email,
        onValueChange =
            onEmailChange,
        label = "Email Address",
        placeholder =
            "farmer@email.com",
        leadingIcon =
            Icons.Outlined.Email,
        keyboardType =
            KeyboardType.Email,
        imeAction =
            ImeAction.Next,
        isError =
            formSubmitted &&
                    !AuthValidation
                        .isValidEmail(
                            email
                        ),
        errorMessage =
            "Enter a valid email address."
    )

    FarmerRegistrationTextField(
        value = phoneNumber,
        onValueChange =
            onPhoneNumberChange,
        label = "Phone Number",
        placeholder =
            "0123456789",
        leadingIcon =
            Icons.Outlined.Phone,
        keyboardType =
            KeyboardType.Phone,
        imeAction =
            ImeAction.Next,
        isError =
            formSubmitted &&
                    !AuthValidation
                        .isValidPhoneNumber(
                            phoneNumber
                        ),
        errorMessage =
            "Enter 9 to 12 numbers."
    )

    FarmerRegistrationTextField(
        value = icNumber,
        onValueChange =
            onIcNumberChange,
        label = "IC Number",
        placeholder =
            "820101-14-1234",
        leadingIcon =
            Icons.Outlined.Badge,
        keyboardType =
            KeyboardType.Number,
        imeAction =
            ImeAction.Next,
        isError =
            formSubmitted &&
                    !validIcNumber,
        errorMessage =
            "Enter a valid 12-digit IC number."
    )

    FarmerRegistrationTextField(
        value = password,
        onValueChange =
            onPasswordChange,
        label = "Password",
        placeholder =
            "Minimum 8 characters",
        leadingIcon =
            Icons.Outlined.Lock,
        keyboardType =
            KeyboardType.Password,
        imeAction =
            ImeAction.Next,
        isPassword = true,
        passwordVisible =
            passwordVisible,
        onPasswordVisibilityChange =
            onPasswordVisibilityChange,
        isError =
            formSubmitted &&
                    !AuthValidation
                        .isValidPassword(
                            password
                        ),
        errorMessage =
            "Use at least 8 characters, one letter and one number."
    )

    FarmerRegistrationTextField(
        value = confirmPassword,
        onValueChange =
            onConfirmPasswordChange,
        label = "Confirm Password",
        placeholder =
            "Re-enter password",
        leadingIcon =
            Icons.Outlined.Lock,
        keyboardType =
            KeyboardType.Password,
        imeAction =
            ImeAction.Done,
        isPassword = true,
        passwordVisible =
            confirmPasswordVisible,
        onPasswordVisibilityChange =
            onConfirmPasswordVisibilityChange,
        isError =
            formSubmitted &&
                    !AuthValidation
                        .passwordsMatch(
                            password,
                            confirmPassword
                        ),
        errorMessage =
            "Passwords do not match."
    )
}

@Composable
fun FarmerDetailsStep(
    farmName: String,
    onFarmNameChange: (String) -> Unit,
    stateName: String,
    onStateNameChange: (String) -> Unit,
    farmType: String,
    onFarmTypeSelected: (String) -> Unit,
    formSubmitted: Boolean
) {
    FarmerRegistrationTextField(
        value = farmName,
        onValueChange =
            onFarmNameChange,
        label =
            "Farm / Business Name",
        placeholder =
            "e.g. Ladang Pak Ali",
        leadingIcon =
            Icons.Outlined.Eco,
        keyboardType =
            KeyboardType.Text,
        imeAction =
            ImeAction.Next,
        isError =
            formSubmitted &&
                    farmName.trim().length < 2,
        errorMessage =
            "Enter your farm or business name."
    )

    FarmerRegistrationTextField(
        value = stateName,
        onValueChange =
            onStateNameChange,
        label = "State",
        placeholder =
            "e.g. Selangor",
        leadingIcon =
            Icons.Outlined.LocationOn,
        keyboardType =
            KeyboardType.Text,
        imeAction =
            ImeAction.Done,
        isError =
            formSubmitted &&
                    stateName.trim().length < 2,
        errorMessage =
            "Enter your state."
    )

    Column(
        modifier =
            Modifier.fillMaxWidth()
    ) {
        Text(
            text = buildAnnotatedString {
                append("Farm Type")

                withStyle(
                    style = SpanStyle(
                        color =
                            Color(0xFFB3261E)
                    )
                ) {
                    append(" *")
                }
            },
            color = MainText,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        Row(
            modifier =
                Modifier.fillMaxWidth(),
            horizontalArrangement =
                Arrangement.spacedBy(8.dp)
        ) {
            FarmTypeButton(
                text = "Vegetables",
                selected =
                    farmType ==
                            "Vegetables",
                onClick = {
                    onFarmTypeSelected(
                        "Vegetables"
                    )
                }
            )

            FarmTypeButton(
                text = "Fruits",
                selected =
                    farmType == "Fruits",
                onClick = {
                    onFarmTypeSelected(
                        "Fruits"
                    )
                }
            )

            FarmTypeButton(
                text = "Dairy",
                selected =
                    farmType == "Dairy",
                onClick = {
                    onFarmTypeSelected(
                        "Dairy"
                    )
                }
            )
        }

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        Row(
            modifier =
                Modifier.fillMaxWidth(),
            horizontalArrangement =
                Arrangement.spacedBy(8.dp)
        ) {
            FarmTypeButton(
                text = "Mixed",
                selected =
                    farmType == "Mixed",
                onClick = {
                    onFarmTypeSelected(
                        "Mixed"
                    )
                }
            )

            FarmTypeButton(
                text = "Poultry",
                selected =
                    farmType == "Poultry",
                onClick = {
                    onFarmTypeSelected(
                        "Poultry"
                    )
                }
            )
        }

        if (
            formSubmitted &&
            farmType.isBlank()
        ) {
            Text(
                text =
                    "Select one farm type.",
                modifier =
                    Modifier.padding(
                        start = 8.dp,
                        top = 6.dp
                    ),
                color = Color(0xFFB3261E),
                fontSize = 12.sp
            )
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = Color(0xFFD8F5DF),
                shape =
                    RoundedCornerShape(17.dp)
            )
            .padding(14.dp),
        verticalAlignment =
            Alignment.Top
    ) {
        Icon(
            imageVector =
                Icons.Outlined.VerifiedUser,
            contentDescription =
                "Verification information",
            modifier =
                Modifier.size(20.dp),
            tint = PrimaryGreen
        )

        Text(
            text =
                "Your farm details will be reviewed during the verification process.",
            modifier =
                Modifier.padding(
                    start = 10.dp
                ),
            color = MainText,
            fontSize = 13.sp,
            lineHeight = 18.sp
        )
    }
}

@Composable
fun FarmTypeButton(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        color =
            if (selected) {
                ForestGreen
            } else {
                Color(0xFFF3F0EA)
            },
        contentColor =
            if (selected) {
                Color.White
            } else {
                SecondaryText
            },
        shape = CircleShape
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(
                horizontal = 14.dp,
                vertical = 8.dp
            ),
            fontSize = 13.sp,
            fontWeight =
                if (selected) {
                    FontWeight.Bold
                } else {
                    FontWeight.Medium
                }
        )
    }
}

@Composable
fun FarmerDocumentsStep(
    icFrontUri: String,
    onIcFrontUpload: () -> Unit,
    icBackUri: String,
    onIcBackUpload: () -> Unit,
    farmCertificateUri: String,
    onFarmCertificateUpload: () -> Unit
) {
    Column(
        modifier = Modifier
            .widthIn(max = 600.dp)
            .fillMaxWidth()
            .offset(y = (-10).dp)
            .padding(horizontal = 27.dp),
        verticalArrangement =
            Arrangement.spacedBy(14.dp)
    ) {
        FarmerDocumentCard(
            title = "IC Photo (Front)",
            description =
                "Clear photo of your identity card front",
            leadingIcon =
                Icons.Outlined.Badge,
            documentSelected =
                icFrontUri.isNotBlank(),
            onUploadClick =
                onIcFrontUpload
        )

        FarmerDocumentCard(
            title = "IC Photo (Back)",
            description =
                "Clear photo of your identity card back",
            leadingIcon =
                Icons.Outlined.Badge,
            documentSelected =
                icBackUri.isNotBlank(),
            onUploadClick =
                onIcBackUpload
        )

        FarmerDocumentCard(
            title =
                "Farm Certificate / Letter",
            description =
                "Proof of farm ownership or registration",
            leadingIcon =
                Icons.Outlined.Description,
            documentSelected =
                farmCertificateUri.isNotBlank(),
            onUploadClick =
                onFarmCertificateUpload
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = Color(0xFFFFEFD6),
                    shape =
                        RoundedCornerShape(17.dp)
                )
                .padding(14.dp),
            verticalAlignment =
                Alignment.Top
        ) {
            Icon(
                imageVector =
                    Icons.Outlined.Info,
                contentDescription =
                    "Document information",
                modifier =
                    Modifier.size(19.dp),
                tint = Color(0xFFFF8A3D)
            )

            Text(
                text =
                    "Documents are reviewed within 1–2 business days. You will receive an SMS once approved.",
                modifier =
                    Modifier.padding(
                        start = 9.dp
                    ),
                color = Color(0xFF99500C),
                fontSize = 12.sp,
                lineHeight = 17.sp
            )
        }
    }
}

@Composable
fun FarmerDocumentCard(
    title: String,
    description: String,
    leadingIcon: ImageVector,
    documentSelected: Boolean,
    onUploadClick: () -> Unit
) {
    val borderColor =
        Color(0xFFCAD8D3)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .drawBehind {
                drawRoundRect(
                    color = borderColor,
                    cornerRadius =
                        CornerRadius(
                            x = 20.dp.toPx(),
                            y = 20.dp.toPx()
                        ),
                    style = Stroke(
                        width =
                            1.5.dp.toPx(),
                        pathEffect =
                            PathEffect
                                .dashPathEffect(
                                    intervals =
                                        floatArrayOf(
                                            10f,
                                            8f
                                        )
                                )
                    )
                )
            },
        shape =
            RoundedCornerShape(20.dp),
        colors =
            CardDefaults.cardColors(
                containerColor =
                    Color.White
            ),
        elevation =
            CardDefaults.cardElevation(
                defaultElevation = 2.dp
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(23.dp)
        ) {
            Row(
                verticalAlignment =
                    Alignment.Top
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(
                            color =
                                Color(
                                    0xFFD8F5DF
                                ),
                            shape =
                                RoundedCornerShape(
                                    16.dp
                                )
                        ),
                    contentAlignment =
                        Alignment.Center
                ) {
                    Icon(
                        imageVector =
                            if (
                                documentSelected
                            ) {
                                Icons.Outlined
                                    .CheckCircle
                            } else {
                                leadingIcon
                            },
                        contentDescription =
                            title,
                        tint = PrimaryGreen,
                        modifier =
                            Modifier.size(24.dp)
                    )
                }

                Column(
                    modifier =
                        Modifier.padding(
                            start = 14.dp
                        )
                ) {
                    Text(
                        text = title,
                        color = MainText,
                        fontSize = 16.sp,
                        fontWeight =
                            FontWeight.Bold
                    )

                    Spacer(
                        modifier =
                            Modifier.height(
                                3.dp
                            )
                    )

                    Text(
                        text =
                            if (
                                documentSelected
                            ) {
                                "File selected successfully"
                            } else {
                                description
                            },
                        color =
                            if (
                                documentSelected
                            ) {
                                PrimaryGreen
                            } else {
                                SecondaryText
                            },
                        fontSize = 13.sp,
                        lineHeight = 18.sp
                    )
                }
            }

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            Button(
                onClick = onUploadClick,
                modifier =
                    Modifier.fillMaxWidth(),
                shape =
                    RoundedCornerShape(17.dp),
                colors =
                    ButtonDefaults.buttonColors(
                        containerColor =
                            Color(0xFFD8F5DF),
                        contentColor =
                            MainText
                    ),
                elevation =
                    ButtonDefaults.buttonElevation(
                        defaultElevation = 0.dp
                    ),
                contentPadding =
                    PaddingValues(
                        vertical = 11.dp
                    )
            ) {
                Icon(
                    imageVector =
                        if (
                            documentSelected
                        ) {
                            Icons.Outlined
                                .CheckCircle
                        } else {
                            Icons.Outlined
                                .FileUpload
                        },
                    contentDescription = null,
                    modifier =
                        Modifier.size(18.dp)
                )

                Text(
                    text =
                        if (
                            documentSelected
                        ) {
                            "Change File"
                        } else {
                            "Upload File"
                        },
                    modifier =
                        Modifier.padding(
                            start = 8.dp
                        ),
                    fontSize = 14.sp,
                    fontWeight =
                        FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
fun FarmerReviewStep(
    fullName: String,
    email: String,
    phoneNumber: String,
    farmName: String,
    stateName: String,
    farmType: String,
    documentsUploaded: Int
) {
    Column(
        modifier = Modifier
            .widthIn(max = 600.dp)
            .fillMaxWidth()
            .offset(y = (-10).dp)
            .padding(horizontal = 31.dp),
        verticalArrangement =
            Arrangement.spacedBy(13.dp)
    ) {
        FarmerReviewCard(
            title = "Account Details"
        ) {
            FarmerReviewRow(
                label = "Name",
                value = fullName
            )

            FarmerReviewRow(
                label = "Email",
                value = email
            )

            FarmerReviewRow(
                label = "Phone",
                value = phoneNumber
            )
        }

        FarmerReviewCard(
            title = "Farm Details"
        ) {
            FarmerReviewRow(
                label = "Farm Name",
                value = farmName
            )

            FarmerReviewRow(
                label = "State",
                value = stateName
            )

            FarmerReviewRow(
                label = "Farm Type",
                value = farmType
            )
        }

        Card(
            modifier =
                Modifier.fillMaxWidth(),
            shape =
                RoundedCornerShape(18.dp),
            colors =
                CardDefaults.cardColors(
                    containerColor =
                        Color.White
                ),
            elevation =
                CardDefaults.cardElevation(
                    defaultElevation = 2.dp
                )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(19.dp),
                verticalAlignment =
                    Alignment.Top
            ) {
                Icon(
                    imageVector =
                        Icons.Outlined
                            .CheckCircle,
                    contentDescription =
                        "Documents uploaded",
                    tint = PrimaryGreen,
                    modifier =
                        Modifier.size(24.dp)
                )

                Column(
                    modifier =
                        Modifier.padding(
                            start = 13.dp
                        )
                ) {
                    Text(
                        text =
                            "$documentsUploaded Documents Uploaded",
                        color = MainText,
                        fontSize = 16.sp,
                        fontWeight =
                            FontWeight.Bold
                    )

                    Text(
                        text =
                            "IC Front, IC Back, Farm Certificate",
                        color = SecondaryText,
                        fontSize = 13.sp
                    )
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = ForestGreen,
                    shape =
                        RoundedCornerShape(
                            18.dp
                        )
                )
                .padding(18.dp),
            verticalAlignment =
                Alignment.Top
        ) {
            Icon(
                imageVector =
                    Icons.Outlined
                        .VerifiedUser,
                contentDescription =
                    "Confirmation",
                tint =
                    Color(0xFF55D69B),
                modifier =
                    Modifier.size(23.dp)
            )

            Text(
                text =
                    "By submitting, you confirm all information is accurate and agree to HarvestLink's Farmer Terms of Service.",
                modifier =
                    Modifier.padding(
                        start = 12.dp
                    ),
                color =
                    Color(0xFFD8F5DF),
                fontSize = 13.sp,
                lineHeight = 19.sp
            )
        }
    }
}

@Composable
fun FarmerReviewCard(
    title: String,
    content: @Composable () -> Unit
) {
    Card(
        modifier =
            Modifier.fillMaxWidth(),
        shape =
            RoundedCornerShape(18.dp),
        colors =
            CardDefaults.cardColors(
                containerColor = Color.White
            ),
        elevation =
            CardDefaults.cardElevation(
                defaultElevation = 2.dp
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(22.dp)
        ) {
            Text(
                text = title,
                color = MainText,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(
                modifier =
                    Modifier.height(15.dp)
            )

            content()
        }
    }
}

@Composable
fun FarmerReviewRow(
    label: String,
    value: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement =
            Arrangement.SpaceBetween,
        verticalAlignment =
            Alignment.CenterVertically
    ) {
        Text(
            text = label,
            color = SecondaryText,
            fontSize = 13.sp
        )

        Text(
            text = value,
            modifier =
                Modifier.weight(1f),
            color = MainText,
            fontSize = 13.sp,
            fontWeight =
                FontWeight.Bold,
            textAlign = TextAlign.End
        )
    }
}

@Composable
fun FarmerRegistrationTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
    leadingIcon: ImageVector,
    keyboardType: KeyboardType,
    imeAction: ImeAction,
    isError: Boolean,
    errorMessage: String,
    isPassword: Boolean = false,
    passwordVisible: Boolean = false,
    onPasswordVisibilityChange:
        () -> Unit = {}
) {
    Column(
        modifier =
            Modifier.fillMaxWidth()
    ) {
        Text(
            text = buildAnnotatedString {
                append(label)

                withStyle(
                    style = SpanStyle(
                        color =
                            Color(0xFFB3261E)
                    )
                ) {
                    append(" *")
                }
            },
            color = MainText,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        OutlinedTextField(
            value = value,
            onValueChange =
                onValueChange,
            modifier =
                Modifier.fillMaxWidth(),
            placeholder = {
                Text(
                    text = placeholder,
                    color =
                        Color(0xFF9AA8A2),
                    fontSize = 14.sp
                )
            },
            leadingIcon = {
                Icon(
                    imageVector =
                        leadingIcon,
                    contentDescription =
                        label,
                    tint =
                        Color(0xFF71827A)
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
                            tint =
                                Color(0xFF71827A)
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
            keyboardOptions =
                KeyboardOptions(
                    keyboardType =
                        keyboardType,
                    imeAction =
                        imeAction
                ),
            isError = isError,
            singleLine = true,
            shape =
                RoundedCornerShape(17.dp),
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
                modifier =
                    Modifier.padding(
                        start = 12.dp,
                        top = 5.dp
                    ),
                color = Color(0xFFB3261E),
                fontSize = 12.sp
            )
        }
    }
}

fun saveDocumentPermission(
    context: Context,
    selectedUri: Uri
) {
    try {
        context.contentResolver
            .takePersistableUriPermission(
                selectedUri,
                Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
    } catch (exception: Exception) {
    }
}

