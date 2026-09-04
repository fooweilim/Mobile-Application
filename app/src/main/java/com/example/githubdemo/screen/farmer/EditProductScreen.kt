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
import androidx.compose.runtime.LaunchedEffect
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
import coil.compose.AsyncImage
import com.example.githubdemo.model.farmer.Product
import com.example.githubdemo.repository.farmer.ProductFarmerRepository
import kotlinx.coroutines.launch

private val EditProductGreen =
    Color(0xFF28785B)

private val EditProductBackground =
    Color(0xFFF8F5ED)

private const val EDIT_PRICE_PATTERN =
    "^\\d*(\\.\\d{0,2})?$"

private const val EDIT_STOCK_PATTERN =
    "^\\d+$"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProductScreen(
    productId: String,
    onBack: () -> Unit,
    onUpdated: () -> Unit
) {
    val repository = remember {
        ProductFarmerRepository()
    }

    val coroutineScope =
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
            imageBitmap = bitmap
        }

    LaunchedEffect(productId) {
        isLoading = true
        errorMessage = ""

        try {
            val loadedProduct =
                repository
                    .getProductById(
                        productId
                    )

            product = loadedProduct

            if (loadedProduct != null) {
                name =
                    loadedProduct.name

                category =
                    loadedProduct.category

                price =
                    loadedProduct.price
                        .toString()

                stock =
                    loadedProduct.stock
                        .toString()

                description =
                    loadedProduct.description
                        .orEmpty()
            } else {
                errorMessage =
                    "Product was not found."
            }
        } catch (
            exception: Exception
        ) {
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
                        text =
                            "Edit Product",

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
        when {
            isLoading -> {
                Box(
                    modifier = Modifier
                        .padding(
                            innerPadding
                        )
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
                        .padding(
                            innerPadding
                        )
                        .fillMaxSize(),

                    contentAlignment =
                        Alignment.Center
                ) {
                    Column(
                        horizontalAlignment =
                            Alignment
                                .CenterHorizontally
                    ) {
                        Text(
                            text =
                                errorMessage.ifBlank {
                                    "Product was not found."
                                },

                            color =
                                Color.Red
                        )

                        Button(
                            onClick = onBack
                        ) {
                            Text(
                                text = "Go Back"
                            )
                        }
                    }
                }
            }

            else -> {
                val currentProduct =
                    product ?: return@Scaffold

                Column(
                    modifier = Modifier
                        .padding(
                            innerPadding
                        )
                        .padding(
                            20.dp
                        )
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
                            .height(
                                180.dp
                            ),

                        shape =
                            RoundedCornerShape(
                                20.dp
                            ),

                        onClick = {
                            cameraLauncher
                                .launch(null)
                        }
                    ) {
                        Box(
                            modifier =
                                Modifier
                                    .fillMaxSize(),

                            contentAlignment =
                                Alignment.Center
                        ) {
                            val selectedBitmap =
                                imageBitmap

                            when {
                                selectedBitmap != null -> {
                                    Image(
                                        bitmap =
                                            selectedBitmap
                                                .asImageBitmap(),

                                        contentDescription =
                                            "New product image",

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
                                            currentProduct
                                                .name,

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
                                            "Add product image",

                                        tint =
                                            EditProductGreen,

                                        modifier =
                                            Modifier.size(
                                                50.dp
                                            )
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
                                text =
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
                            Text(
                                text =
                                    "Category"
                            )
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

                            when {
                                newValue.isEmpty() -> {
                                    price = ""
                                    errorMessage = ""
                                }

                                newValue.matches(
                                    Regex(
                                        EDIT_PRICE_PATTERN
                                    )
                                ) -> {
                                    price =
                                        newValue

                                    errorMessage =
                                        ""
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

                        modifier =
                            Modifier
                                .fillMaxWidth(),

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
                                        EDIT_STOCK_PATTERN
                                    )
                                ) -> {
                                    stock =
                                        newValue

                                    errorMessage =
                                        ""
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

                        modifier =
                            Modifier
                                .fillMaxWidth(),

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
                                    "Description"
                            )
                        },

                        modifier = Modifier
                            .fillMaxWidth()
                            .height(
                                120.dp
                            )
                    )

                    if (
                        errorMessage
                            .isNotEmpty()
                    ) {
                        Text(
                            text =
                                errorMessage,

                            color =
                                Color.Red,

                            fontSize =
                                13.sp
                        )
                    }

                    Button(
                        enabled = !isSaving,

                        onClick = {
                            if (
                                name.isBlank() ||
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

                            coroutineScope.launch {
                                isSaving = true
                                errorMessage = ""

                                try {
                                    var imageUrl =
                                        currentProduct
                                            .image_url

                                    val selectedBitmap =
                                        imageBitmap

                                    if (
                                        selectedBitmap !=
                                        null
                                    ) {
                                        imageUrl =
                                            repository
                                                .uploadProductImage(
                                                    bitmap =
                                                        selectedBitmap,

                                                    fileName =
                                                        "product_$productId.jpg"
                                                )
                                    }

                                    val updatedProduct =
                                        currentProduct.copy(
                                            name =
                                                name.trim(),

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
                                                imageUrl
                                        )

                                    repository
                                        .updateProduct(
                                            id =
                                                productId,

                                            product =
                                                updatedProduct
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
                        },

                        modifier = Modifier
                            .fillMaxWidth()
                            .height(
                                55.dp
                            ),

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

                                modifier =
                                    Modifier.size(
                                        22.dp
                                    )
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