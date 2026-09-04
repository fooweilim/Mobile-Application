package com.example.githubdemo.screen.farmer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.githubdemo.model.farmer.Product
import com.example.githubdemo.repository.farmer.ProductFarmerRepository
import com.example.githubdemo.screen.farmer.components.FarmerBottomBar
import kotlinx.coroutines.launch

private val ProductScreenGreen =
    Color(0xFF28785B)

private val ProductScreenBackground =
    Color(0xFFF8F5ED)

@Composable
fun FarmerProductScreen(
    onAddProduct: () -> Unit,
    onEditProduct: (Product) -> Unit,
    onNavigate: (String) -> Unit
) {
    val repository = remember {
        ProductFarmerRepository()
    }

    val coroutineScope =
        rememberCoroutineScope()

    var products by remember {
        mutableStateOf(
            emptyList<Product>()
        )
    }

    var isLoading by remember {
        mutableStateOf(true)
    }

    var errorMessage by remember {
        mutableStateOf("")
    }

    var productToDelete by remember {
        mutableStateOf<Product?>(null)
    }

    fun loadProducts() {
        coroutineScope.launch {
            isLoading = true
            errorMessage = ""

            try {
                products =
                    repository.getProducts()
            } catch (
                exception: Exception
            ) {
                products =
                    emptyList()

                errorMessage =
                    exception.message
                        ?: "Unable to load products."
            } finally {
                isLoading = false
            }
        }
    }

    LaunchedEffect(Unit) {
        loadProducts()
    }

    Scaffold(
        containerColor =
            ProductScreenBackground,

        bottomBar = {
            FarmerBottomBar(
                current =
                    "products",

                onNavigate =
                    onNavigate
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(20.dp)
                .fillMaxSize()
        ) {
            Row(
                modifier =
                    Modifier.fillMaxWidth(),

                horizontalArrangement =
                    Arrangement.SpaceBetween,

                verticalAlignment =
                    Alignment.CenterVertically
            ) {
                Text(
                    text = "My Products",

                    fontSize = 24.sp,

                    fontWeight =
                        FontWeight.Bold
                )

                Button(
                    onClick =
                        onAddProduct,

                    colors =
                        ButtonDefaults
                            .buttonColors(
                                containerColor =
                                    ProductScreenGreen
                            ),

                    shape =
                        RoundedCornerShape(
                            25.dp
                        )
                ) {
                    Text(
                        text = "+ Add New"
                    )
                }
            }

            Spacer(
                modifier =
                    Modifier.height(
                        20.dp
                    )
            )

            when {
                isLoading -> {
                    Box(
                        modifier =
                            Modifier.fillMaxSize(),

                        contentAlignment =
                            Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            color =
                                ProductScreenGreen
                        )
                    }
                }

                errorMessage.isNotEmpty() -> {
                    Box(
                        modifier =
                            Modifier.fillMaxSize(),

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
                                    errorMessage,

                                color =
                                    Color.Red
                            )

                            Spacer(
                                modifier =
                                    Modifier.height(
                                        10.dp
                                    )
                            )

                            Button(
                                onClick = {
                                    loadProducts()
                                }
                            ) {
                                Text(
                                    text = "Retry"
                                )
                            }
                        }
                    }
                }

                products.isEmpty() -> {
                    Box(
                        modifier =
                            Modifier.fillMaxSize(),

                        contentAlignment =
                            Alignment.Center
                    ) {
                        Text(
                            text =
                                "No Products Found",

                            color =
                                Color.Gray,

                            fontSize =
                                16.sp
                        )
                    }
                }

                else -> {
                    LazyColumn(
                        verticalArrangement =
                            Arrangement.spacedBy(
                                15.dp
                            )
                    ) {
                        items(
                            items = products,

                            key = { product ->
                                product.id
                                    ?: product.name
                            }
                        ) { product ->
                            FarmerProductCard(
                                product = product,

                                onEdit = {
                                    onEditProduct(
                                        product
                                    )
                                },

                                onRemove = {
                                    productToDelete =
                                        product
                                }
                            )
                        }
                    }
                }
            }
        }
    }

    val selectedProduct =
        productToDelete

    if (selectedProduct != null) {
        AlertDialog(
            onDismissRequest = {
                productToDelete = null
            },

            title = {
                Text(
                    text =
                        "Remove Product"
                )
            },

            text = {
                Text(
                    text =
                        "Are you sure you want to remove ${selectedProduct.name}?"
                )
            },

            confirmButton = {
                Button(
                    onClick = {
                        val productId =
                            selectedProduct.id

                        productToDelete =
                            null

                        if (
                            !productId
                                .isNullOrBlank()
                        ) {
                            coroutineScope.launch {
                                try {
                                    repository
                                        .deleteProduct(
                                            productId
                                        )

                                    loadProducts()
                                } catch (
                                    exception:
                                    Exception
                                ) {
                                    errorMessage =
                                        exception
                                            .message
                                            ?: "Unable to remove product."
                                }
                            }
                        }
                    },

                    colors =
                        ButtonDefaults
                            .buttonColors(
                                containerColor =
                                    Color.Red
                            )
                ) {
                    Text(
                        text = "Remove"
                    )
                }
            },

            dismissButton = {
                TextButton(
                    onClick = {
                        productToDelete =
                            null
                    }
                ) {
                    Text(
                        text = "Cancel"
                    )
                }
            }
        )
    }
}

@Composable
private fun FarmerProductCard(
    product: Product,
    onEdit: () -> Unit,
    onRemove: () -> Unit
) {
    Card(
        modifier =
            Modifier.fillMaxWidth(),

        shape =
            RoundedCornerShape(
                20.dp
            )
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),

            horizontalArrangement =
                Arrangement.spacedBy(
                    15.dp
                ),

            verticalAlignment =
                Alignment.CenterVertically
        ) {
            Card(
                modifier =
                    Modifier.size(
                        100.dp
                    ),

                shape =
                    RoundedCornerShape(
                        15.dp
                    )
            ) {
                if (
                    !product.image_url
                        .isNullOrBlank()
                ) {
                    AsyncImage(
                        model =
                            product.image_url,

                        contentDescription =
                            product.name,

                        modifier =
                            Modifier.fillMaxSize(),

                        contentScale =
                            ContentScale.Crop
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Color(
                                    0xFFE8F5EC
                                )
                            ),

                        contentAlignment =
                            Alignment.Center
                    ) {
                        Icon(
                            imageVector =
                                Icons.Default
                                    .Image,

                            contentDescription =
                                null,

                            tint =
                                ProductScreenGreen
                        )
                    }
                }
            }

            Column(
                modifier =
                    Modifier.weight(1f)
            ) {
                Text(
                    text = product.name,

                    fontSize = 17.sp,

                    fontWeight =
                        FontWeight.Bold
                )

                Text(
                    text =
                        "RM %.2f".format(
                            product.price
                        ),

                    color =
                        ProductScreenGreen,

                    fontWeight =
                        FontWeight.Bold
                )

                Text(
                    text =
                        "Category: ${product.category}",

                    fontSize =
                        13.sp
                )

                Text(
                    text =
                        "Stock: ${product.stock}",

                    fontSize =
                        13.sp
                )

                Spacer(
                    modifier =
                        Modifier.height(
                            8.dp
                        )
                )

                Row(
                    horizontalArrangement =
                        Arrangement.spacedBy(
                            8.dp
                        )
                ) {
                    Button(
                        onClick = onEdit,

                        colors =
                            ButtonDefaults
                                .buttonColors(
                                    containerColor =
                                        Color(
                                            0xFFE8F5EC
                                        )
                                )
                    ) {
                        Icon(
                            imageVector =
                                Icons.Default
                                    .Edit,

                            contentDescription =
                                "Edit product",

                            tint =
                                ProductScreenGreen
                        )
                    }

                    Spacer(
                        modifier =
                            Modifier.width(
                                2.dp
                            )
                    )

                    Button(
                        onClick = onRemove,

                        colors =
                            ButtonDefaults
                                .buttonColors(
                                    containerColor =
                                        Color.Red
                                )
                    ) {
                        Icon(
                            imageVector =
                                Icons.Default
                                    .Delete,

                            contentDescription =
                                "Remove product",

                            tint =
                                Color.White
                        )
                    }
                }
            }
        }
    }
}