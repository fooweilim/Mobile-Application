package com.example.githubdemo.screen.authentication

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
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Badge
import androidx.compose.material.icons.outlined.Business
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Eco
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material.icons.outlined.UploadFile
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.githubdemo.data.AuthValidation
import com.example.githubdemo.data.UserRole
import com.example.githubdemo.ui.theme.ForestGreen
import com.example.githubdemo.ui.theme.MainText
import com.example.githubdemo.ui.theme.PageBackground
import com.example.githubdemo.ui.theme.PrimaryGreen
import com.example.githubdemo.ui.theme.SecondaryText
import com.example.githubdemo.viewmodel.authentication.AuthViewModel

private const val ACCOUNT_STEP = 1
private const val FARM_STEP = 2
private const val DOCUMENT_STEP = 3
private const val REVIEW_STEP = 4

@Composable
fun FarmerSignUpScreen(
    onSignUpSuccess: (String) -> Unit,
    onLoginClick: (String) -> Unit,
    onBackClick: () -> Unit,
    authViewModel: AuthViewModel
) {
    val context = LocalContext.current

    var currentStep by rememberSaveable {
        mutableIntStateOf(ACCOUNT_STEP)
    }

    var formSubmitted by rememberSaveable {
        mutableStateOf(false)
    }

    var showOtpDialog by rememberSaveable {
        mutableStateOf(false)
    }

    // Account details
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

    // Farm details
    var farmName by rememberSaveable {
        mutableStateOf("")
    }

    var stateName by rememberSaveable {
        mutableStateOf("")
    }

    var farmType by rememberSaveable {
        mutableStateOf("")
    }

    // Selected documents
    var icFrontUri by rememberSaveable {
        mutableStateOf("")
    }

    var icBackUri by rememberSaveable {
        mutableStateOf("")
    }

    var farmDocumentUri by rememberSaveable {
        mutableStateOf("")
    }

    val icFrontLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent()
        ) { uri ->
            if (uri != null) {
                icFrontUri = uri.toString()
                authViewModel.clearMessage()
            }
        }

    val icBackLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent()
        ) { uri ->
            if (uri != null) {
                icBackUri = uri.toString()
                authViewModel.clearMessage()
            }
        }

    val farmDocumentLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent()
        ) { uri ->
            if (uri != null) {
                farmDocumentUri = uri.toString()
                authViewModel.clearMessage()
            }
        }

    val accountStepIsValid =
        AuthValidation.isValidName(fullName) &&
                AuthValidation.isValidEmail(email) &&
                AuthValidation.isValidPhoneNumber(phoneNumber) &&
                isValidMalaysianIc(icNumber) &&
                isValidFarmerPassword(password) &&
                AuthValidation.passwordsMatch(
                    password,
                    confirmPassword
                )

    val farmStepIsValid =
        farmName.trim().length >= 2 &&
                stateName.trim().length >= 2 &&
                farmType.isNotEmpty()

    val documentStepIsValid =
        icFrontUri.isNotEmpty() &&
                icBackUri.isNotEmpty() &&
                farmDocumentUri.isNotEmpty()

    fun moveToPreviousStep() {
        authViewModel.clearMessage()
        formSubmitted = false

        if (currentStep > ACCOUNT_STEP) {
            currentStep--
        } else {
            onBackClick()
        }
    }

    fun moveToNextStep() {
        formSubmitted = true
        authViewModel.clearMessage()

        val currentStepIsValid = when (currentStep) {
            ACCOUNT_STEP -> accountStepIsValid
            FARM_STEP -> farmStepIsValid
            DOCUMENT_STEP -> documentStepIsValid
            else -> true
        }

        if (
            currentStepIsValid &&
            currentStep < REVIEW_STEP
        ) {
            currentStep++
            formSubmitted = false
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(PageBackground),
        contentPadding = PaddingValues(
            bottom = 30.dp
        ),
        horizontalAlignment =
            Alignment.CenterHorizontally
    ) {
        item {
            Box(
                modifier = Modifier
                    .widthIn(max = 600.dp)
                    .fillMaxWidth()
            ) {
                FarmerRegistrationHeader(
                    currentStep = currentStep,
                    onBackClick = {
                        moveToPreviousStep()
                    }
                )
            }
        }

        item {
            Card(
                modifier = Modifier
                    .widthIn(max = 600.dp)
                    .fillMaxWidth()
                    .padding(
                        start = 24.dp,
                        top = 18.dp,
                        end = 24.dp
                    ),
                shape = RoundedCornerShape(26.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 3.dp
                )
            ) {
                when (currentStep) {
                    ACCOUNT_STEP -> {
                        FarmerAccountStep(
                            fullName = fullName,
                            onFullNameChange = {
                                fullName = it.take(50)
                                authViewModel.clearMessage()
                            },
                            email = email,
                            onEmailChange = {
                                email = it.take(60)
                                authViewModel.clearMessage()
                            },
                            phoneNumber = phoneNumber,
                            onPhoneNumberChange = {
                                if (
                                    it.length <= 12 &&
                                    it.all { character ->
                                        character.isDigit()
                                    }
                                ) {
                                    phoneNumber = it
                                    authViewModel.clearMessage()
                                }
                            },
                            icNumber = icNumber,
                            onIcNumberChange = {
                                icNumber = formatIcNumber(it)
                                authViewModel.clearMessage()
                            },
                            password = password,
                            onPasswordChange = {
                                password = it.take(30)
                                authViewModel.clearMessage()
                            },
                            confirmPassword =
                                confirmPassword,
                            onConfirmPasswordChange = {
                                confirmPassword = it.take(30)
                                authViewModel.clearMessage()
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
                            formSubmitted = formSubmitted
                        )
                    }

                    FARM_STEP -> {
                        FarmerFarmDetailsStep(
                            farmName = farmName,
                            onFarmNameChange = {
                                farmName = it.take(80)
                                authViewModel.clearMessage()
                            },
                            stateName = stateName,
                            onStateNameChange = {
                                stateName = it.take(40)
                                authViewModel.clearMessage()
                            },
                            farmType = farmType,
                            onFarmTypeChange = {
                                farmType = it
                                authViewModel.clearMessage()
                            },
                            formSubmitted = formSubmitted
                        )
                    }

                    DOCUMENT_STEP -> {
                        FarmerDocumentStep(
                            icFrontUri = icFrontUri,
                            icBackUri = icBackUri,
                            farmDocumentUri =
                                farmDocumentUri,
                            onSelectIcFront = {
                                icFrontLauncher.launch(
                                    "image/*"
                                )
                            },
                            onSelectIcBack = {
                                icBackLauncher.launch(
                                    "image/*"
                                )
                            },
                            onSelectFarmDocument = {
                                farmDocumentLauncher.launch(
                                    "*/*"
                                )
                            },
                            formSubmitted = formSubmitted
                        )
                    }

                    REVIEW_STEP -> {
                        FarmerReviewStep(
                            fullName = fullName,
                            email = email,
                            phoneNumber = phoneNumber,
                            icNumber = icNumber,
                            farmName = farmName,
                            stateName = stateName,
                            farmType = farmType,
                            documentsSelected = listOf(
                                icFrontUri,
                                icBackUri,
                                farmDocumentUri
                            ).count {
                                it.isNotEmpty()
                            }
                        )
                    }
                }
            }
        }

        if (authViewModel.message.isNotEmpty()) {
            item {
                Text(
                    text = authViewModel.message,
                    modifier = Modifier
                        .widthIn(max = 600.dp)
                        .fillMaxWidth()
                        .padding(
                            start = 28.dp,
                            top = 14.dp,
                            end = 28.dp
                        ),
                    color =
                        if (authViewModel.messageIsError) {
                            Color(0xFFB3261E)
                        } else {
                            PrimaryGreen
                        },
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center
                )
            }
        }

        item {
            FarmerRegistrationActions(
                currentStep = currentStep,
                isLoading = authViewModel.isLoading,
                onBackClick = {
                    moveToPreviousStep()
                },
                onContinueClick = {
                    moveToNextStep()
                },
                onSubmitClick = {
                    authViewModel.clearMessage()

                    if (
                        accountStepIsValid &&
                        farmStepIsValid &&
                        documentStepIsValid
                    ) {
                        authViewModel.sendSignUpOtp(
                            email = email,
                            password = password,
                            onOtpSent = {
                                showOtpDialog = true
                            }
                        )
                    } else {
                        authViewModel.showErrorMessage(
                            "Please complete every registration step."
                        )
                    }
                }
            )
        }

        item {
            Row(
                modifier = Modifier
                    .widthIn(max = 600.dp)
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement =
                    Arrangement.Center,
                verticalAlignment =
                    Alignment.CenterVertically
            ) {
                Text(
                    text = "Already have an account?",
                    color = SecondaryText,
                    fontSize = 14.sp
                )

                TextButton(
                    onClick = {
                        onLoginClick(UserRole.FARMER)
                    }
                ) {
                    Text(
                        text = "Sign In",
                        color = MainText,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }

    if (showOtpDialog) {
        EmailOtpDialog(
            email = email.trim().lowercase(),
            authViewModel = authViewModel,
            onVerifyOtp = { enteredOtp ->
                authViewModel
                    .verifySignUpOtpAndSaveProfile(
                        email = email,
                        otp = enteredOtp,
                        userRole = UserRole.FARMER,
                        fullName = fullName,
                        phoneNumber = phoneNumber,
                        additionalInformation =
                            "$farmName | $stateName | $farmType",
                        onSuccess = {
                            showOtpDialog = false

                            Toast.makeText(
                                context,
                                "Farmer account created.",
                                Toast.LENGTH_SHORT
                            ).show()

                            onSignUpSuccess(
                                UserRole.FARMER
                            )
                        }
                    )
            },
            onResendOtp = {
                authViewModel.resendSignUpOtp(
                    email
                )
            },
            onDismiss = {
                showOtpDialog = false
            }
        )
    }
}

@Composable
fun FarmerRegistrationHeader(
    currentStep: Int,
    onBackClick: () -> Unit
) {
    val stepTitle = when (currentStep) {
        ACCOUNT_STEP -> "Create Account"
        FARM_STEP -> "Farm Details"
        DOCUMENT_STEP -> "Upload Documents"
        else -> "Review & Submit"
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = ForestGreen,
                shape = RoundedCornerShape(
                    bottomStart = 38.dp,
                    bottomEnd = 38.dp
                )
            )
            .statusBarsPadding()
            .padding(
                start = 24.dp,
                top = 8.dp,
                end = 24.dp,
                bottom = 28.dp
            )
    ) {
        IconButton(
            onClick = onBackClick,
            modifier = Modifier
                .size(44.dp)
                .background(
                    color = Color(0xFF315E50),
                    shape = CircleShape
                )
        ) {
            Icon(
                imageVector =
                    Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = Color.White
            )
        }

        Spacer(
            modifier = Modifier.height(18.dp)
        )

        Row(
            verticalAlignment =
                Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .background(
                        color = Color(0xFF2E775C),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.Eco,
                    contentDescription = "Farmer",
                    tint = Color(0xFF50D69A),
                    modifier = Modifier.size(22.dp)
                )
            }

            Text(
                text = "FARMER REGISTRATION",
                modifier = Modifier.padding(
                    start = 10.dp
                ),
                color = Color(0xFF50D69A),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        Text(
            text = stepTitle,
            color = Color.White,
            fontSize = 30.sp,
            lineHeight = 36.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Serif
        )

        Spacer(
            modifier = Modifier.height(18.dp)
        )

        FarmerStepIndicator(
            currentStep = currentStep
        )
    }
}

@Composable
fun FarmerStepIndicator(
    currentStep: Int
) {
    val stepNames = listOf(
        "Account",
        "Farm Info",
        "Documents",
        "Review"
    )

    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 38.dp,
                    top = 14.dp,
                    end = 38.dp
                )
                .height(2.dp)
                .background(
                    Color(0xFF617B72)
                )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(
                        (
                                (currentStep - 1) / 3f
                                ).coerceIn(
                                minimumValue = 0f,
                                maximumValue = 1f
                            )
                    )
                    .background(
                        Color(0xFF50D69A)
                    )
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            stepNames.forEachIndexed {
                    index,
                    name ->

                val stepNumber = index + 1
                val completed =
                    stepNumber < currentStep
                val selected =
                    stepNumber == currentStep

                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment =
                        Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(29.dp)
                            .background(
                                color = when {
                                    completed ->
                                        Color(
                                            0xFF50C99A
                                        )

                                    selected ->
                                        Color.White

                                    else ->
                                        Color(
                                            0xFF58736A
                                        )
                                },
                                shape = CircleShape
                            ),
                        contentAlignment =
                            Alignment.Center
                    ) {
                        if (completed) {
                            Icon(
                                imageVector =
                                    Icons.Outlined.Check,
                                contentDescription =
                                    "Completed",
                                tint = Color.White,
                                modifier =
                                    Modifier.size(18.dp)
                            )
                        } else {
                            Text(
                                text =
                                    stepNumber.toString(),
                                color =
                                    if (selected) {
                                        ForestGreen
                                    } else {
                                        Color(
                                            0xFFC5D4CF
                                        )
                                    },
                                fontSize = 12.sp,
                                fontWeight =
                                    FontWeight.Bold
                            )
                        }
                    }

                    Spacer(
                        modifier =
                            Modifier.height(6.dp)
                    )

                    Text(
                        text = name,
                        color =
                            if (selected || completed) {
                                Color(0xFFD7E5E0)
                            } else {
                                Color(0xFF9BB0A9)
                            },
                        fontSize = 10.sp,
                        maxLines = 1
                    )
                }
            }
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
    formSubmitted: Boolean
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(22.dp),
        verticalArrangement =
            Arrangement.spacedBy(17.dp)
    ) {
        AuthenticationTextField(
            value = fullName,
            onValueChange = onFullNameChange,
            label = "Full Name (as per IC) *",
            placeholder = "e.g. Ali bin Hassan",
            leadingIcon = Icons.Outlined.Person,
            isError = formSubmitted &&
                    !AuthValidation.isValidName(
                        fullName
                    ),
            errorMessage = "Enter your full name.",
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Next
        )

        AuthenticationTextField(
            value = email,
            onValueChange = onEmailChange,
            label = "Email Address *",
            placeholder = "farmer@email.com",
            leadingIcon = Icons.Outlined.Email,
            isError = formSubmitted &&
                    !AuthValidation.isValidEmail(
                        email
                    ),
            errorMessage =
                "Enter a valid email address.",
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Next
        )

        AuthenticationTextField(
            value = phoneNumber,
            onValueChange = onPhoneNumberChange,
            label = "Phone Number *",
            placeholder = "0123456789",
            leadingIcon = Icons.Outlined.Phone,
            isError = formSubmitted &&
                    !AuthValidation.isValidPhoneNumber(
                        phoneNumber
                    ),
            errorMessage =
                "Enter 9 to 12 numbers.",
            keyboardType = KeyboardType.Phone,
            imeAction = ImeAction.Next
        )

        AuthenticationTextField(
            value = icNumber,
            onValueChange = onIcNumberChange,
            label = "IC Number *",
            placeholder = "820101-14-1234",
            leadingIcon = Icons.Outlined.Badge,
            isError = formSubmitted &&
                    !isValidMalaysianIc(icNumber),
            errorMessage =
                "Enter a valid 12-digit IC number.",
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Next
        )

        AuthenticationTextField(
            value = password,
            onValueChange = onPasswordChange,
            label = "Password *",
            placeholder = "Minimum 8 characters",
            leadingIcon = Icons.Outlined.Lock,
            isError = formSubmitted &&
                    !isValidFarmerPassword(password),
            errorMessage =
                "Use 8 characters with a letter and number.",
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Next,
            isPassword = true,
            passwordVisible = passwordVisible,
            onPasswordVisibilityChange =
                onPasswordVisibilityChange
        )

        AuthenticationTextField(
            value = confirmPassword,
            onValueChange =
                onConfirmPasswordChange,
            label = "Confirm Password *",
            placeholder = "Re-enter password",
            leadingIcon = Icons.Outlined.Lock,
            isError = formSubmitted &&
                    !AuthValidation.passwordsMatch(
                        password,
                        confirmPassword
                    ),
            errorMessage =
                "Passwords do not match.",
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done,
            isPassword = true,
            passwordVisible =
                confirmPasswordVisible,
            onPasswordVisibilityChange =
                onConfirmPasswordVisibilityChange
        )
    }
}

@Composable
fun FarmerFarmDetailsStep(
    farmName: String,
    onFarmNameChange: (String) -> Unit,
    stateName: String,
    onStateNameChange: (String) -> Unit,
    farmType: String,
    onFarmTypeChange: (String) -> Unit,
    formSubmitted: Boolean
) {
    val farmTypes = listOf(
        "Vegetables",
        "Fruits",
        "Dairy",
        "Mixed",
        "Poultry"
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(22.dp),
        verticalArrangement =
            Arrangement.spacedBy(18.dp)
    ) {
        AuthenticationTextField(
            value = farmName,
            onValueChange = onFarmNameChange,
            label = "Farm / Business Name *",
            placeholder = "e.g. Ladang Pak Ali",
            leadingIcon = Icons.Outlined.Business,
            isError = formSubmitted &&
                    farmName.trim().length < 2,
            errorMessage =
                "Enter your farm or business name.",
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Next
        )

        AuthenticationTextField(
            value = stateName,
            onValueChange = onStateNameChange,
            label = "State *",
            placeholder = "e.g. Selangor",
            leadingIcon =
                Icons.Outlined.LocationOn,
            isError = formSubmitted &&
                    stateName.trim().length < 2,
            errorMessage =
                "Enter the farm state.",
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Done
        )

        Text(
            text = "Farm Type *",
            color = MainText,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement =
                Arrangement.spacedBy(8.dp)
        ) {
            farmTypes.take(3).forEach { type ->
                FarmerFarmTypeChip(
                    text = type,
                    selected = farmType == type,
                    onClick = {
                        onFarmTypeChange(type)
                    }
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement =
                Arrangement.spacedBy(8.dp)
        ) {
            farmTypes.drop(3).forEach { type ->
                FarmerFarmTypeChip(
                    text = type,
                    selected = farmType == type,
                    onClick = {
                        onFarmTypeChange(type)
                    }
                )
            }
        }

        if (
            formSubmitted &&
            farmType.isEmpty()
        ) {
            Text(
                text = "Select one farm type.",
                color = Color(0xFFB3261E),
                fontSize = 12.sp
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = Color(0xFFD7F5DF),
                    shape =
                        RoundedCornerShape(16.dp)
                )
                .padding(15.dp),
            verticalAlignment =
                Alignment.CenterVertically
        ) {
            Icon(
                imageVector =
                    Icons.Outlined.Info,
                contentDescription = null,
                tint = PrimaryGreen
            )

            Text(
                text =
                    "Your farm details will be reviewed during verification.",
                modifier = Modifier.padding(
                    start = 10.dp
                ),
                color = MainText,
                fontSize = 13.sp,
                lineHeight = 18.sp
            )
        }
    }
}

@Composable
fun FarmerFarmTypeChip(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    FilterChip(
        selected = selected,
        onClick = onClick,
        label = {
            Text(
                text = text,
                fontWeight =
                    if (selected) {
                        FontWeight.Bold
                    } else {
                        FontWeight.Normal
                    }
            )
        },
        colors =
            FilterChipDefaults.filterChipColors(
                selectedContainerColor =
                    ForestGreen,
                selectedLabelColor =
                    Color.White,
                containerColor =
                    Color(0xFFF4F0E9),
                labelColor = SecondaryText
            )
    )
}

@Composable
fun FarmerDocumentStep(
    icFrontUri: String,
    icBackUri: String,
    farmDocumentUri: String,
    onSelectIcFront: () -> Unit,
    onSelectIcBack: () -> Unit,
    onSelectFarmDocument: () -> Unit,
    formSubmitted: Boolean
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
        verticalArrangement =
            Arrangement.spacedBy(14.dp)
    ) {
        FarmerDocumentCard(
            title = "IC Photo (Front)",
            description =
                "Clear photo of your identity card front",
            selectedUri = icFrontUri,
            icon = Icons.Outlined.Badge,
            onUploadClick = onSelectIcFront
        )

        FarmerDocumentCard(
            title = "IC Photo (Back)",
            description =
                "Clear photo of your identity card back",
            selectedUri = icBackUri,
            icon = Icons.Outlined.Badge,
            onUploadClick = onSelectIcBack
        )

        FarmerDocumentCard(
            title = "Farm Certificate / Letter",
            description =
                "Proof of farm ownership or registration",
            selectedUri = farmDocumentUri,
            icon = Icons.Outlined.Description,
            onUploadClick =
                onSelectFarmDocument
        )

        if (
            formSubmitted &&
            (
                    icFrontUri.isEmpty() ||
                            icBackUri.isEmpty() ||
                            farmDocumentUri.isEmpty()
                    )
        ) {
            Text(
                text =
                    "Select all three required documents.",
                color = Color(0xFFB3261E),
                fontSize = 12.sp
            )
        }

        Text(
            text =
                "Documents are reviewed within 1–2 business days.",
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = Color(0xFFFFF0D6),
                    shape =
                        RoundedCornerShape(15.dp)
                )
                .padding(14.dp),
            color = Color(0xFF9A5A00),
            fontSize = 13.sp,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun FarmerDocumentCard(
    title: String,
    description: String,
    selectedUri: String,
    icon: ImageVector,
    onUploadClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment =
                    Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(
                            color =
                                Color(0xFFD7F5DF),
                            shape =
                                RoundedCornerShape(
                                    14.dp
                                )
                        ),
                    contentAlignment =
                        Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = title,
                        tint = PrimaryGreen
                    )
                }

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 14.dp)
                ) {
                    Text(
                        text = title,
                        color = MainText,
                        fontSize = 15.sp,
                        fontWeight =
                            FontWeight.Bold
                    )

                    Text(
                        text = description,
                        color = SecondaryText,
                        fontSize = 13.sp,
                        lineHeight = 17.sp
                    )
                }
            }

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            Button(
                onClick = onUploadClick,
                modifier = Modifier.fillMaxWidth(),
                shape =
                    RoundedCornerShape(14.dp),
                colors =
                    ButtonDefaults.buttonColors(
                        containerColor =
                            Color(0xFFD7F5DF),
                        contentColor = MainText
                    )
            ) {
                Icon(
                    imageVector =
                        if (selectedUri.isEmpty()) {
                            Icons.Outlined.UploadFile
                        } else {
                            Icons.Outlined.Check
                        },
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )

                Text(
                    text =
                        if (selectedUri.isEmpty()) {
                            "Select File"
                        } else {
                            getSelectedFileName(
                                selectedUri
                            )
                        },
                    modifier = Modifier.padding(
                        start = 8.dp
                    ),
                    maxLines = 1,
                    overflow =
                        TextOverflow.Ellipsis
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
    icNumber: String,
    farmName: String,
    stateName: String,
    farmType: String,
    documentsSelected: Int
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
        verticalArrangement =
            Arrangement.spacedBy(14.dp)
    ) {
        FarmerReviewCard(
            title = "Account Details",
            details = listOf(
                "Name" to fullName,
                "Email" to email,
                "Phone" to phoneNumber,
                "IC Number" to icNumber
            )
        )

        FarmerReviewCard(
            title = "Farm Details",
            details = listOf(
                "Farm Name" to farmName,
                "State" to stateName,
                "Farm Type" to farmType
            )
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = Color(0xFFF7F4EE),
                    shape =
                        RoundedCornerShape(17.dp)
                )
                .padding(16.dp),
            verticalAlignment =
                Alignment.CenterVertically
        ) {
            Icon(
                imageVector =
                    Icons.Outlined.Check,
                contentDescription = null,
                tint = PrimaryGreen
            )

            Column(
                modifier = Modifier.padding(
                    start = 12.dp
                )
            ) {
                Text(
                    text =
                        "$documentsSelected Documents Selected",
                    color = MainText,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text =
                        "IC Front, IC Back, Farm Certificate",
                    color = SecondaryText,
                    fontSize = 12.sp
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = ForestGreen,
                    shape =
                        RoundedCornerShape(17.dp)
                )
                .padding(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            Icon(
                imageVector =
                    Icons.Outlined.Info,
                contentDescription = null,
                tint = Color(0xFF50D69A)
            )

            Text(
                text =
                    "By submitting, you confirm that all information is accurate and agree to HarvestLink's Farmer Terms of Service.",
                modifier = Modifier.padding(
                    start = 12.dp
                ),
                color = Color.White,
                fontSize = 13.sp,
                lineHeight = 18.sp
            )
        }
    }
}

@Composable
fun FarmerReviewCard(
    title: String,
    details: List<Pair<String, String>>
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalArrangement =
                Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = title,
                color = MainText,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold
            )

            details.forEach { detail ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement =
                        Arrangement.SpaceBetween
                ) {
                    Text(
                        text = detail.first,
                        color = SecondaryText,
                        fontSize = 13.sp
                    )

                    Text(
                        text = detail.second,
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 18.dp),
                        color = MainText,
                        fontSize = 13.sp,
                        fontWeight =
                            FontWeight.SemiBold,
                        textAlign = TextAlign.End,
                        maxLines = 2,
                        overflow =
                            TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

@Composable
fun FarmerRegistrationActions(
    currentStep: Int,
    isLoading: Boolean,
    onBackClick: () -> Unit,
    onContinueClick: () -> Unit,
    onSubmitClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .widthIn(max = 600.dp)
            .fillMaxWidth()
            .padding(
                start = 24.dp,
                top = 18.dp,
                end = 24.dp
            )
    ) {
        Button(
            onClick =
                if (currentStep == REVIEW_STEP) {
                    onSubmitClick
                } else {
                    onContinueClick
                },
            enabled = !isLoading,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(17.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = ForestGreen
            ),
            contentPadding = PaddingValues(
                vertical = 15.dp
            )
        ) {
            Text(
                text = when {
                    isLoading ->
                        "Please wait..."

                    currentStep == REVIEW_STEP ->
                        "Submit for Verification"

                    else ->
                        "Continue"
                },
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }

        if (currentStep > ACCOUNT_STEP) {
            TextButton(
                onClick = onBackClick,
                enabled = !isLoading,
                modifier = Modifier.align(
                    Alignment.CenterHorizontally
                )
            ) {
                Text(
                    text = "Back to Previous Step",
                    color = PrimaryGreen
                )
            }
        }
    }
}

fun isValidMalaysianIc(
    icNumber: String
): Boolean {
    val digits = icNumber.filter {
        it.isDigit()
    }

    return digits.length == 12
}

fun isValidFarmerPassword(
    password: String
): Boolean {
    return password.length >= 8 &&
            password.any {
                it.isLetter()
            } &&
            password.any {
                it.isDigit()
            }
}

fun formatIcNumber(
    value: String
): String {
    val digits = value
        .filter {
            it.isDigit()
        }
        .take(12)

    return buildString {
        digits.forEachIndexed {
                index,
                character ->

            if (
                index == 6 ||
                index == 8
            ) {
                append('-')
            }

            append(character)
        }
    }
}

fun getSelectedFileName(
    uriValue: String
): String {
    val fileName =
        Uri.parse(uriValue).lastPathSegment

    return if (fileName.isNullOrBlank()) {
        "File Selected"
    } else {
        fileName.substringAfterLast('/')
    }
}