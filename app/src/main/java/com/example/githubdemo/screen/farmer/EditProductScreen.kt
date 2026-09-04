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
import coil.compose.AsyncImage
import com.example.githubdemo.model.market.Product
import com.example.githubdemo.supabase.FarmerProductRepository
import kotlinx.coroutines.launch

private val EditProductGreen =
    Color(0xFF28785B)

private val EditProductBackground =
    Color(0xFFF8F5ED)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProductScreen(
    productId: String,
    onBack: () -> Unit,
    onUpdated: () -> Unit
) {
    val repository = remember {
        FarmerProductRepository()
    }

    val scope =
        rememberCoroutineScope()

    var product by remember {
        mutableStateOf<Product?>(null)
    }

    var imageBitmap by remember {
        mutableStateOf<Bitmap?>(null)
    }

    var name by remember {
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

    var isLoading by remember {
        mutableStateOf(true)
    }

    var isSaving by remember {
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

    LaunchedEffect(productId) {
        isLoading = true

        try {
            val loadedProduct =
                repository
                    .getProductById(
                        productId
                    )

            if (loadedProduct == null) {
                errorMessage =
                    "This product could not be found."
            } else {
                product = loadedProduct
                name = loadedProduct.name
                category =
                    loadedProduct.category

                price =
                    loadedProduct
                        .price
                        .toString()

                stock =
                    loadedProduct
                        .stock
                        .toString()

                description =
                    loadedProduct
                        .description
                        .orEmpty()
            }
        } catch (exception: Exception) {
            errorMessage =
                exception.message
                    ?: "Unable to load product."
        } finally {
            isLoading = false
        }
    }

    Scaffold(
        containerColor =
            EditProductBackground,

        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Edit Product",

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

        when {
            isLoading -> {
                Box(
                    modifier = Modifier
                        .padding(paddingValues)
                        .fillMaxSize(),

                    contentAlignment =
                        Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color =
                            EditProductGreen
                    )
                }
            }

            product == null -> {
                Box(
                    modifier = Modifier
                        .padding(paddingValues)
                        .fillMaxSize()
                        .padding(24.dp),

                    contentAlignment =
                        Alignment.Center
                ) {
                    Text(
                        text = errorMessage,

                        color =
                            Color(0xFFB3261E)
                    )
                }
            }

            else -> {
                val currentProduct =
                    product!!

                Column(
                    modifier = Modifier
                        .padding(paddingValues)
                        .padding(20.dp)
                        .fillMaxSize()
                        .verticalScroll(
                            rememberScrollState()
                        ),

                    verticalArrangement =
                        Arrangement
                            .spacedBy(15.dp)
                ) {
                    Card(
                        onClick = {
                            cameraLauncher
                                .launch(null)
                        },

                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp),

                        shape =
                            RoundedCornerShape(
                                20.dp
                            )
                    ) {
                        Box(
                            modifier =
                                Modifier
                                    .fillMaxSize(),

                            contentAlignment =
                                Alignment.Center
                        ) {
                            val selectedImage =
                                imageBitmap

                            when {
                                selectedImage !=
                                        null -> {

                                    Image(
                                        bitmap =
                                            selectedImage
                                                .asImageBitmap(),

                                        contentDescription =
                                            "New product photo",

                                        modifier =
                                            Modifier
                                                .fillMaxSize(),

                                        contentScale =
                                            ContentScale
                                                .Crop
                                    )
                                }

                                !currentProduct
                                    .image_url
                                    .isNullOrBlank() -> {

                                    AsyncImage(
                                        model =
                                            currentProduct
                                                .image_url,

                                        contentDescription =
                                            "Product photo",

                                        modifier =
                                            Modifier
                                                .fillMaxSize(),

                                        contentScale =
                                            ContentScale
                                                .Crop
                                    )
                                }

                                else -> {
                                    Icon(
                                        imageVector =
                                            Icons.Default
                                                .AddPhotoAlternate,

                                        contentDescription =
                                            "Add product photo",

                                        tint =
                                            EditProductGreen,

                                        modifier =
                                            Modifier
                                                .size(50.dp)
                                    )
                                }
                            }
                        }
                    }

                    OutlinedTextField(
                        value = name,

                        onValueChange = {
                            name = it
                            errorMessage = ""
                        },

                        label = {
                            Text(
                                "Product Name"
                            )
                        },

                        modifier =
                            Modifier
                                .fillMaxWidth(),

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
                            Modifier
                                .fillMaxWidth(),

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

                        modifier =
                            Modifier
                                .fillMaxWidth(),

                        singleLine = true
                    )

                    OutlinedTextField(
                        value = stock,

                        onValueChange = {
                                newValue ->

                            if (
                                newValue
                                    .isEmpty() ||
                                newValue.all {
                                        character ->

                                    character
                                        .isDigit()
                                }
                            ) {
                                stock = newValue
                                errorMessage = ""
                            }
                        },

                        label = {
                            Text("Stock")
                        },

                        modifier =
                            Modifier
                                .fillMaxWidth(),

                        singleLine = true
                    )

                    OutlinedTextField(
                        value = description,

                        onValueChange = {
                            description = it
                        },

                        label = {
                            Text("Description")
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

                            color =
                                Color(0xFFB3261E),

                            fontSize = 13.sp
                        )
                    }

                    Button(
                        enabled = !isSaving,

                        onClick = {
                            val parsedPrice =
                                price
                                    .toDoubleOrNull()

                            val parsedStock =
                                stock
                                    .toIntOrNull()

                            when {
                                name.isBlank() ||
                                        category
                                            .isBlank() -> {

                                    errorMessage =
                                        "Product name and category are required."
                                }

                                parsedPrice ==
                                        null ||
                                        parsedPrice <=
                                        0.0 -> {

                                    errorMessage =
                                        "Enter a price greater than 0."
                                }

                                parsedStock ==
                                        null ||
                                        parsedStock <
                                        0 -> {

                                    errorMessage =
                                        "Enter a valid whole-number stock."
                                }

                                else -> {
                                    scope.launch {
                                        isSaving = true
                                        errorMessage =
                                            ""

                                        try {
                                            var imageUrl =
                                                currentProduct
                                                    .image_url

                                            val newImage =
                                                imageBitmap

                                            if (
                                                newImage !=
                                                null
                                            ) {
                                                imageUrl =
                                                    repository
                                                        .uploadProductImage(
                                                            bitmap =
                                                                newImage,

                                                            fileName =
                                                                "product_$productId.jpg"
                                                        )
                                            }

                                            repository
                                                .updateProduct(
                                                    productId =
                                                        productId,

                                                    product =
                                                        currentProduct
                                                            .copy(
                                                                name =
                                                                    name.trim(),

                                                                category =
                                                                    category.trim(),

                                                                price =
                                                                    parsedPrice,

                                                                stock =
                                                                    parsedStock,

                                                                description =
                                                                    description.trim(),

                                                                image_url =
                                                                    imageUrl
                                                            )
                                                )

                                            onUpdated()
                                        } catch (
                                            exception:
                                            Exception
                                        ) {
                                            errorMessage =
                                                exception
                                                    .message
                                                    ?: "Unable to update product."
                                        } finally {
                                            isSaving =
                                                false
                                        }
                                    }
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
                                        EditProductGreen
                                )
                    ) {
                        if (isSaving) {
                            CircularProgressIndicator(
                                color =
                                    Color.White,

                                strokeWidth =
                                    2.dp,

                                modifier =
                                    Modifier
                                        .size(22.dp)
                            )
                        } else {
                            Text(
                                text =
                                    "Save Changes",

                                fontSize =
                                    16.sp
                            )
                        }
                    }
                }
            }
        }
    }
}