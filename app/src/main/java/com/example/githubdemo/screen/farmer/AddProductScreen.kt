package com.example.githubdemo.screen.farmer


import android.graphics.Bitmap
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.githubdemo.model.market.Product
import com.example.githubdemo.supabase.FarmerProductRepository
import kotlinx.coroutines.launch

private val AddProductGreen =
    Color(0xFF28785B)

private val AddProductBackground =
    Color(0xFFF8F5ED)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddProductScreen(
    onBack: () -> Unit,
    onProductAdded: () -> Unit
) {
    val repository = remember {
        FarmerProductRepository()
    }

    val scope =
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

            if (bitmap != null) {
                imageBitmap = bitmap
                errorMessage = ""
            }
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
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(20.dp)
                .fillMaxSize()
                .verticalScroll(
                    rememberScrollState()
                ),

            verticalArrangement =
                Arrangement.spacedBy(15.dp)
        ) {
            Card(
                onClick = {
                    cameraLauncher.launch(null)
                },

                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp),

                shape =
                    RoundedCornerShape(20.dp)
            ) {
                Box(
                    modifier =
                        Modifier.fillMaxSize(),

                    contentAlignment =
                        Alignment.Center
                ) {
                    val selectedImage =
                        imageBitmap

                    if (selectedImage != null) {
                        Image(
                            bitmap =
                                selectedImage
                                    .asImageBitmap(),

                            contentDescription =
                                "Selected product photo",

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
                                    null,

                                tint =
                                    AddProductGreen,

                                modifier =
                                    Modifier.size(45.dp)
                            )

                            Spacer(
                                modifier =
                                    Modifier.height(6.dp)
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
                    Text("Product Name")
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
                    Text("Category")
                },

                modifier =
                    Modifier.fillMaxWidth(),

                singleLine = true
            )

            OutlinedTextField(
                value = price,

                onValueChange = {
                        newValue ->

                    if (
                        newValue.matches(
                            Regex(
                                "^\\d*(\\.\\d{0,2})?$"
                            )
                        )
                    ) {
                        price = newValue
                        errorMessage = ""
                    }
                },

                label = {
                    Text("Price")
                },

                placeholder = {
                    Text("Example: 2.50")
                },

                modifier =
                    Modifier.fillMaxWidth(),

                singleLine = true
            )

            OutlinedTextField(
                value = stock,

                onValueChange = {
                        newValue ->

                    if (
                        newValue.isEmpty() ||
                        newValue.all {
                                character ->

                            character.isDigit()
                        }
                    ) {
                        stock = newValue
                        errorMessage = ""
                    }
                },

                label = {
                    Text("Stock")
                },

                placeholder = {
                    Text("Example: 100")
                },

                modifier =
                    Modifier.fillMaxWidth(),

                singleLine = true
            )

            OutlinedTextField(
                value = description,

                onValueChange = {
                    description = it
                },

                label = {
                    Text(
                        "Description (Optional)"
                    )
                },

                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
            )

            if (errorMessage.isNotEmpty()) {
                Text(
                    text = errorMessage,

                    color =
                        Color(0xFFB3261E),

                    fontSize = 13.sp
                )
            }

            Button(
                enabled = !isLoading,

                onClick = {
                    val parsedPrice =
                        price.toDoubleOrNull()

                    val parsedStock =
                        stock.toIntOrNull()

                    when {
                        productName.isBlank() ||
                                category.isBlank() ||
                                price.isBlank() ||
                                stock.isBlank() -> {

                            errorMessage =
                                "Please fill in all required fields."
                        }

                        parsedPrice == null ||
                                parsedPrice <= 0.0 -> {

                            errorMessage =
                                "Enter a price greater than 0."
                        }

                        parsedStock == null ||
                                parsedStock < 0 -> {

                            errorMessage =
                                "Enter a valid whole-number stock."
                        }

                        imageBitmap == null -> {
                            errorMessage =
                                "Please add a product photo."
                        }

                        else -> {
                            scope.launch {
                                isLoading = true
                                errorMessage = ""

                                try {
                                    val imageUrl =
                                        repository
                                            .uploadProductImage(
                                                bitmap =
                                                    imageBitmap!!,

                                                fileName =
                                                    "product_${System.currentTimeMillis()}.jpg"
                                            )

                                    repository
                                        .addProduct(
                                            Product(
                                                farmer_id =
                                                    null,

                                                name =
                                                    productName
                                                        .trim(),

                                                category =
                                                    category
                                                        .trim(),

                                                price =
                                                    parsedPrice,

                                                stock =
                                                    parsedStock,

                                                description =
                                                    description
                                                        .trim(),

                                                image_url =
                                                    imageUrl,

                                                status =
                                                    "Available"
                                            )
                                        )

                                    onProductAdded()
                                } catch (
                                    exception:
                                    Exception
                                ) {
                                    errorMessage =
                                        exception
                                            .message
                                            ?: "Unable to add product."
                                } finally {
                                    isLoading = false
                                }
                            }
                        }
                    }
                },

                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp),

                shape =
                    RoundedCornerShape(30.dp),

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
                        strokeWidth = 2.dp,

                        modifier =
                            Modifier.size(22.dp)
                    )
                } else {
                    Text(
                        text = "List Product",
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}