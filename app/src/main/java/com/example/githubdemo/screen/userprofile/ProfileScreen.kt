package com.example.githubdemo.screen.userprofile

import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.githubdemo.data.AppData
import com.example.githubdemo.ui.theme.PrimaryGreen
import com.example.githubdemo.viewmodel.userprofile.CustomerOrderViewModel
import com.example.githubdemo.viewmodel.userprofile.ProfileViewModel

private enum class BuyerProfilePage {
    MAIN,
    INFORMATION,
    EDIT,
    ORDERS,
    HELP,
    PRIVACY,
    CHANGE_PASSWORD
}

object BuyerProfileEntry {

    private var requestedPage =
        BuyerProfilePage.MAIN

    fun openOrders() {
        requestedPage =
            BuyerProfilePage.ORDERS
    }

    internal fun takeRequestedPage(): String {
        val page = requestedPage

        requestedPage =
            BuyerProfilePage.MAIN

        return page.name
    }
}

@Composable
fun ProfileScreen(
    onNavigate: (String) -> Unit,
    profileViewModel: ProfileViewModel =
        viewModel(),
    orderViewModel: CustomerOrderViewModel =
        viewModel()
) {
    var pageName by rememberSaveable {
        mutableStateOf(
            BuyerProfilePage.MAIN.name
        )
    }

    LaunchedEffect(Unit) {
        pageName =
            BuyerProfileEntry.takeRequestedPage()

        profileViewModel.loadProfile()
    }

    val page =
        BuyerProfilePage.entries
            .firstOrNull {
                it.name == pageName
            }
            ?: BuyerProfilePage.MAIN

    BackHandler(
        enabled =
            page != BuyerProfilePage.MAIN
    ) {
        pageName = when (page) {
            BuyerProfilePage.EDIT ->
                BuyerProfilePage
                    .INFORMATION.name

            BuyerProfilePage.CHANGE_PASSWORD ->
                BuyerProfilePage
                    .PRIVACY.name

            else ->
                BuyerProfilePage.MAIN.name
        }
    }

    when (page) {
        BuyerProfilePage.MAIN -> {
            BuyerProfileMainScreen(
                onOpenInformation = {
                    pageName =
                        BuyerProfilePage
                            .INFORMATION.name
                },
                onOpenOrders = {
                    pageName =
                        BuyerProfilePage
                            .ORDERS.name
                },
                onOpenHelp = {
                    pageName =
                        BuyerProfilePage
                            .HELP.name
                },
                onOpenPrivacy = {
                    pageName =
                        BuyerProfilePage
                            .PRIVACY.name
                },
                onNavigate = onNavigate,
                profileViewModel =
                    profileViewModel
            )
        }

        BuyerProfilePage.INFORMATION -> {
            ProfileInformationScreen(
                onBack = {
                    pageName =
                        BuyerProfilePage.MAIN.name
                },
                onEditClick = {
                    pageName =
                        BuyerProfilePage.EDIT.name
                },
                profileViewModel =
                    profileViewModel
            )
        }

        BuyerProfilePage.EDIT -> {
            EditProfileScreen(
                onBack = {
                    pageName =
                        BuyerProfilePage
                            .INFORMATION.name
                },
                onSave = {
                    pageName =
                        BuyerProfilePage
                            .INFORMATION.name
                },
                profileViewModel =
                    profileViewModel
            )
        }

        BuyerProfilePage.ORDERS -> {
            MyOrderScreen(
                onBack = {
                    pageName =
                        BuyerProfilePage.MAIN.name
                },
                orderViewModel =
                    orderViewModel
            )
        }

        BuyerProfilePage.HELP -> {
            HelpSupportScreen(
                onBack = {
                    pageName =
                        BuyerProfilePage.MAIN.name
                }
            )
        }

        BuyerProfilePage.PRIVACY -> {
            PrivacySecurityScreen(
                onBack = {
                    pageName =
                        BuyerProfilePage.MAIN.name
                },
                onChangePassword = {
                    pageName =
                        BuyerProfilePage
                            .CHANGE_PASSWORD.name
                }
            )
        }

        BuyerProfilePage.CHANGE_PASSWORD -> {
            ChangePasswordScreen(
                onBack = {
                    pageName =
                        BuyerProfilePage
                            .PRIVACY.name
                }
            )
        }
    }
}

@Composable
private fun BuyerProfileMainScreen(
    onOpenInformation: () -> Unit,
    onOpenOrders: () -> Unit,
    onOpenHelp: () -> Unit,
    onOpenPrivacy: () -> Unit,
    onNavigate: (String) -> Unit,
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

    val menuItems = listOf(
        ProfileMenuItem(
            Icons.Default.Person,
            "Profile Information",
            onOpenInformation
        ),
        ProfileMenuItem(
            Icons.Default.Inventory2,
            "My Orders",
            onOpenOrders
        ),
        ProfileMenuItem(
            Icons.Default.Favorite,
            "Favourite"
        ) {
            onNavigate("meal_favourites")
        },
        ProfileMenuItem(
            Icons.Default.Inventory,
            "Food Box Management"
        ) {
            onNavigate(
                AppData.FOOD_BOX_MANAGE_ROUTE
            )
        },
        ProfileMenuItem(
            Icons.Default.Help,
            "Help & Support",
            onOpenHelp
        ),
        ProfileMenuItem(
            Icons.Default.Security,
            "Privacy & Security",
            onOpenPrivacy
        ),
        ProfileMenuItem(
            Icons.Default.Logout,
            "Logout"
        ) {
            onNavigate(
                AppData.ROLE_SELECTION_ROUTE
            )
        }
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Color(0xFFF4F6EE)
            )
    ) {
        item {
            Surface(
                color = PrimaryGreen,
                shape = RoundedCornerShape(
                    bottomStart = 35.dp,
                    bottomEnd = 35.dp
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            top = 40.dp,
                            bottom = 30.dp
                        ),
                    horizontalAlignment =
                        Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(90.dp)
                            .clip(CircleShape)
                            .background(Color.White),
                        contentAlignment =
                            Alignment.Center
                    ) {
                        Icon(
                            imageVector =
                                Icons.Default.Person,
                            contentDescription = null,
                            tint = PrimaryGreen,
                            modifier =
                                Modifier.size(50.dp)
                        )
                    }

                    Spacer(Modifier.height(12.dp))

                    Text(
                        text =
                            profile?.fullName
                                ?: "User",
                        color = Color.White,
                        style =
                            MaterialTheme.typography
                                .headlineSmall
                    )

                    Text(
                        text =
                            profile?.email
                                .orEmpty(),
                        color = Color.White
                    )
                }
            }
        }

        if (
            isLoading &&
            profile == null
        ) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(36.dp),
                    contentAlignment =
                        Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = PrimaryGreen
                    )
                }
            }
        }

        if (message.isNotBlank()) {
            item {
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
                        Modifier.padding(16.dp)
                )
            }
        }

        items(menuItems) { item ->
            ProfileItem(
                icon = item.icon,
                title = item.title,
                onClick = item.onClick
            )
        }

        item {
            Spacer(Modifier.height(24.dp))
        }
    }
}

private data class ProfileMenuItem(
    val icon: ImageVector,
    val title: String,
    val onClick: () -> Unit
)

@Composable
private fun ProfileItem(
    icon: ImageVector,
    title: String,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 16.dp,
                vertical = 6.dp
            ),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalAlignment =
                Alignment.CenterVertically,
            horizontalArrangement =
                Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment =
                    Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = PrimaryGreen
                )

                Text(
                    text = title,
                    modifier =
                        Modifier.padding(
                            start = 16.dp
                        ),
                    style =
                        MaterialTheme.typography
                            .titleMedium
                )
            }

            Icon(
                imageVector =
                    Icons.Default.ChevronRight,
                contentDescription = null,
                tint = Color.Gray
            )
        }
    }
}

@Composable
internal fun BuyerProfilePageHeader(
    title: String,
    onBack: () -> Unit
) {
    Surface(
        color = PrimaryGreen,
        shape = RoundedCornerShape(
            bottomStart = 30.dp,
            bottomEnd = 30.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = 32.dp,
                    bottom = 18.dp,
                    start = 6.dp,
                    end = 16.dp
                ),
            verticalAlignment =
                Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onBack
            ) {
                Icon(
                    imageVector =
                        Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White
                )
            }

            Text(
                text = title,
                color = Color.White,
                style =
                    MaterialTheme.typography
                        .headlineSmall
            )
        }
    }
}