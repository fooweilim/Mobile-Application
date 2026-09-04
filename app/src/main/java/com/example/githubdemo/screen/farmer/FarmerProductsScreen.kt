package com.example.githubdemo.screen.farmer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.githubdemo.model.market.Product
import com.example.githubdemo.nav.FarmerRoute
import com.example.githubdemo.supabase.FarmerProductRepository
import kotlinx.coroutines.launch

private val ProductGreen =
    Color(0xFF28785B)

private val ProductBackground =
    Color(0xFFF8F5ED)

@Composable
fun FarmerProductScreen(
    onAddProduct: () -> Unit,
    onEditProduct: (Product) -> Unit,
    onNavigate: (String) -> Unit
) {
    val repository = remember {
        FarmerProductRepository()
    }

    val scope =
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

    var productToRemove by remember {
        mutableStateOf<Product?>(null)
    }

    fun loadProducts() {
        scope.launch {
            isLoading = true
            errorMessage = ""

            try {
                products =
                    repository.getProducts()
            } catch (exception: Exception) {
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
            ProductBackground,

        bottomBar = {
            FarmerBottomBar(
                currentRoute =
                    FarmerRoute.PRODUCTS,

                onNavigate =
                    onNavigate
            )
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .padding(paddingValues)
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

                Row(
                    verticalAlignment =
                        Alignment
                            .CenterVertically
                ) {
                    IconButton(
                        onClick =
                            ::loadProducts
                    ) {
                        Icon(
                            imageVector =
                                Icons.Default
                                    .Refresh,

                            contentDescription =
                                "Refresh products",

                            tint =
                                ProductGreen
                        )
                    }

                    Button(
                        onClick =
                            onAddProduct,

                        colors =
                            ButtonDefaults
                                .buttonColors(
                                    containerColor =
                                        ProductGreen
                                ),

                        shape =
                            RoundedCornerShape(
                                25.dp
                            )
                    ) {
                        Text("+ Add New")
                    }
                }
            }

            Spacer(
                modifier =
                    Modifier.height(20.dp)
            )

            when {
                isLoading -> {
                    Box(
                        modifier =
                            Modifier
                                .fillMaxSize(),

                        contentAlignment =
                            Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            color =
                                ProductGreen
                        )
                    }
                }

                errorMessage.isNotEmpty() -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),

                        contentAlignment =
                            Alignment.Center
                    ) {
                        Text(
                            text =
                                errorMessage,

                            color =
                                Color(
                                    0xFFB3261E
                                )
                        )
                    }
                }

                products.isEmpty() -> {
                    Box(
                        modifier =
                            Modifier
                                .fillMaxSize(),

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
                            Arrangement
                                .spacedBy(
                                    15.dp
                                )
                    ) {
                        items(
                            items = products,

                            key = {
                                    product ->

                                product.id
                                    ?: product.name
                            }
                        ) { product ->

                            FarmerProductCard(
                                product =
                                    product,

                                onEdit = {
                                    onEditProduct(
                                        product
                                    )
                                },

                                onRemove = {
                                    productToRemove =
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
        productToRemove

    if (selectedProduct != null) {
        RemoveProductDialog(
            productName =
                selectedProduct.name,

            onDismiss = {
                productToRemove = null
            },

            onConfirm = {
                val productId =
                    selectedProduct.id

                if (productId == null) {
                    productToRemove = null

                    errorMessage =
                        "This product does not have a valid ID."
                } else {
                    scope.launch {
                        try {
                            repository
                                .deleteProduct(
                                    productId
                                )

                            productToRemove =
                                null

                            loadProducts()
                        } catch (
                            exception:
                            Exception
                        ) {
                            productToRemove =
                                null

                            errorMessage =
                                exception.message
                                    ?: "Unable to remove product."
                        }
                    }
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
            RoundedCornerShape(20.dp)
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
                    Modifier.size(100.dp),

                shape =
                    RoundedCornerShape(
                        15.dp
                    )
            ) {
                if (
                    !product
                        .image_url
                        .isNullOrBlank()
                ) {
                    AsyncImage(
                        model =
                            product.image_url,

                        contentDescription =
                            product.name,

                        modifier =
                            Modifier
                                .fillMaxSize(),

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
                                ProductGreen
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
                        ProductGreen,

                    fontWeight =
                        FontWeight.Bold
                )

                Text(
                    text =
                        "Category: ${product.category}",

                    fontSize = 13.sp
                )

                Text(
                    text =
                        "Stock: ${product.stock}",

                    fontSize = 13.sp
                )

                Spacer(
                    modifier =
                        Modifier.height(8.dp)
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
                                "Edit ${product.name}",

                            tint =
                                ProductGreen
                        )
                    }

                    Button(
                        onClick = onRemove,

                        colors =
                            ButtonDefaults
                                .buttonColors(
                                    containerColor =
                                        Color(
                                            0xFFB3261E
                                        )
                                )
                    ) {
                        Icon(
                            imageVector =
                                Icons.Default
                                    .Delete,

                            contentDescription =
                                "Remove ${product.name}",

                            tint =
                                Color.White
                        )
                    }
                }
            }
        }
    }
}