package com.example.githubdemo.screen.farmer

import android.graphics.Bitmap
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.githubdemo.model.farmer.Product
import com.example.githubdemo.repository.farmer.ProductFarmerRepository
import kotlinx.coroutines.launch

private val AddProductGreen =
    Color(0xFF28785B)

private val AddProductBackground =
    Color(0xFFF8F5ED)

private const val PRICE_PATTERN =
    "^\\d*(\\.\\d{0,2})?$"

private const val STOCK_PATTERN =
    "^\\d+$"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddProductScreen(
    onBack: () -> Unit,
    onProductAdded: () -> Unit
) {
    val repository = remember {
        ProductFarmerRepository()
    }

    val coroutineScope =
        rememberCoroutineScope()

    var productName by remember {
        mutableStateOf("")
    }

    var category by remember {
        mutableStateOf("")
    }

    var price by remember {
        mutableStateOf("")
    }

    var stock by remember {
        mutableStateOf("")
    }

    var description by remember {
        mutableStateOf("")
    }

    var imageBitmap by remember {
        mutableStateOf<Bitmap?>(null)
    }

    var isLoading by remember {
        mutableStateOf(false)
    }

    var errorMessage by remember {
        mutableStateOf("")
    }

    val cameraLauncher =
        rememberLauncherForActivityResult(
            contract =
                ActivityResultContracts
                    .TakePicturePreview()
        ) { bitmap ->
            imageBitmap = bitmap
        }

    Scaffold(
        containerColor =
            AddProductBackground,

        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Add Product",
                        fontWeight =
                            FontWeight.Bold
                    )
                },

                navigationIcon = {
                    IconButton(
                        onClick = onBack
                    ) {
                        Icon(
                            imageVector =
                                Icons.Default
                                    .ArrowBack,

                            contentDescription =
                                "Back"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(20.dp)
                .fillMaxSize()
                .verticalScroll(
                    rememberScrollState()
                ),

            verticalArrangement =
                Arrangement.spacedBy(
                    15.dp
                )
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp),

                shape =
                    RoundedCornerShape(
                        20.dp
                    ),

                onClick = {
                    cameraLauncher.launch(
                        null
                    )
                }
            ) {
                Box(
                    modifier =
                        Modifier.fillMaxSize(),

                    contentAlignment =
                        Alignment.Center
                ) {
                    val selectedBitmap =
                        imageBitmap

                    if (selectedBitmap != null) {
                        Image(
                            bitmap =
                                selectedBitmap
                                    .asImageBitmap(),

                            contentDescription =
                                "Selected product image",

                            modifier =
                                Modifier.fillMaxSize(),

                            contentScale =
                                ContentScale.Crop
                        )
                    } else {
                        Column(
                            horizontalAlignment =
                                Alignment
                                    .CenterHorizontally
                        ) {
                            Icon(
                                imageVector =
                                    Icons.Default
                                        .AddPhotoAlternate,

                                contentDescription =
                                    "Open camera",

                                tint =
                                    AddProductGreen,

                                modifier =
                                    Modifier.size(
                                        45.dp
                                    )
                            )

                            Text(
                                text =
                                    "Tap to Open Camera",

                                color =
                                    AddProductGreen
                            )
                        }
                    }
                }
            }

            OutlinedTextField(
                value = productName,

                onValueChange = {
                    productName = it
                    errorMessage = ""
                },

                label = {
                    Text(
                        text =
                            "Product Name"
                    )
                },

                modifier =
                    Modifier.fillMaxWidth(),

                singleLine = true
            )

            OutlinedTextField(
                value = category,

                onValueChange = {
                    category = it
                    errorMessage = ""
                },

                label = {
                    Text(
                        text = "Category"
                    )
                },

                modifier =
                    Modifier.fillMaxWidth(),

                singleLine = true
            )

            OutlinedTextField(
                value = price,

                onValueChange = {
                        newValue ->

                    when {
                        newValue.isEmpty() -> {
                            price = ""
                            errorMessage = ""
                        }

                        newValue.matches(
                            Regex(
                                PRICE_PATTERN
                            )
                        ) -> {
                            price = newValue
                            errorMessage = ""
                        }

                        else -> {
                            errorMessage =
                                "Price only allows numbers with a maximum of 2 decimal places."
                        }
                    }
                },

                label = {
                    Text(
                        text =
                            "Price (RM)"
                    )
                },

                placeholder = {
                    Text(
                        text =
                            "Example: 2.50"
                    )
                },

                modifier =
                    Modifier.fillMaxWidth(),

                singleLine = true
            )

            OutlinedTextField(
                value = stock,

                onValueChange = {
                        newValue ->

                    when {
                        newValue.isEmpty() -> {
                            stock = ""
                            errorMessage = ""
                        }

                        newValue.matches(
                            Regex(
                                STOCK_PATTERN
                            )
                        ) -> {
                            stock = newValue
                            errorMessage = ""
                        }

                        else -> {
                            errorMessage =
                                "Stock only allows whole numbers."
                        }
                    }
                },

                label = {
                    Text(
                        text = "Stock"
                    )
                },

                placeholder = {
                    Text(
                        text =
                            "Example: 100"
                    )
                },

                modifier =
                    Modifier.fillMaxWidth(),

                singleLine = true
            )

            OutlinedTextField(
                value = description,

                onValueChange = {
                    description = it
                    errorMessage = ""
                },

                label = {
                    Text(
                        text =
                            "Description (Optional)"
                    )
                },

                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
            )

            if (
                errorMessage.isNotEmpty()
            ) {
                Text(
                    text = errorMessage,
                    color = Color.Red,
                    fontSize = 13.sp
                )
            }

            Button(
                enabled = !isLoading,

                onClick = {
                    if (
                        productName.isBlank() ||
                        category.isBlank() ||
                        price.isBlank() ||
                        stock.isBlank()
                    ) {
                        errorMessage =
                            "Please fill in all required fields."

                        return@Button
                    }

                    val productPrice =
                        price.toDoubleOrNull()

                    if (
                        productPrice == null ||
                        productPrice <= 0.0
                    ) {
                        errorMessage =
                            "Please enter a valid price."

                        return@Button
                    }

                    val productStock =
                        stock.toIntOrNull()

                    if (
                        productStock == null ||
                        productStock < 0
                    ) {
                        errorMessage =
                            "Please enter a valid stock quantity."

                        return@Button
                    }

                    val selectedBitmap =
                        imageBitmap

                    if (
                        selectedBitmap == null
                    ) {
                        errorMessage =
                            "Please add a product photo."

                        return@Button
                    }

                    coroutineScope.launch {
                        isLoading = true
                        errorMessage = ""

                        try {
                            val imageUrl =
                                repository
                                    .uploadProductImage(
                                        bitmap =
                                            selectedBitmap,

                                        fileName =
                                            "product_${System.currentTimeMillis()}.jpg"
                                    )

                            repository.addProduct(
                                Product(
                                    name =
                                        productName
                                            .trim(),

                                    category =
                                        category
                                            .trim(),

                                    price =
                                        productPrice,

                                    stock =
                                        productStock,

                                    description =
                                        description
                                            .trim(),

                                    image_url =
                                        imageUrl,

                                    status =
                                        "ACTIVE"
                                )
                            )

                            onProductAdded()
                        } catch (
                            exception: Exception
                        ) {
                            errorMessage =
                                exception.message
                                    ?: "Unable to add product."
                        } finally {
                            isLoading = false
                        }
                    }
                },

                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp),

                shape =
                    RoundedCornerShape(
                        30.dp
                    ),

                colors =
                    ButtonDefaults
                        .buttonColors(
                            containerColor =
                                AddProductGreen
                        )
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        color = Color.White,

                        modifier =
                            Modifier.size(
                                22.dp
                            )
                    )
                } else {
                    Text(
                        text =
                            "List Product",

                        fontSize =
                            16.sp
                    )
                }
            }
        }
    }
}