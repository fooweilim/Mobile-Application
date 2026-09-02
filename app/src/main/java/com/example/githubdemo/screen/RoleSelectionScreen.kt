package com.example.githubdemo.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.outlined.AdminPanelSettings
import androidx.compose.material.icons.outlined.Eco
import androidx.compose.material.icons.outlined.ShoppingBag
import androidx.compose.material.icons.outlined.VerifiedUser
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.githubdemo.data.UserRole
import com.example.githubdemo.ui.theme.AdminBackground
import com.example.githubdemo.ui.theme.BrandGreen
import com.example.githubdemo.ui.theme.BrightGreen
import com.example.githubdemo.ui.theme.BuyerBackground
import com.example.githubdemo.ui.theme.FarmerBackground
import com.example.githubdemo.ui.theme.ForestGreen
import com.example.githubdemo.ui.theme.GithubDemoTheme
import com.example.githubdemo.ui.theme.PrimaryGreen
import com.example.githubdemo.ui.theme.RoleIconBackground
import com.example.githubdemo.ui.theme.SecondaryText

@Composable
fun RoleSelectionScreen(
    onRoleSelected: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .statusBarsPadding(),

        contentAlignment = Alignment.TopCenter
    ) {
        LazyColumn(
            modifier = Modifier
                .widthIn(max = 620.dp)
                .fillMaxSize(),

            contentPadding = PaddingValues(
                start = 24.dp,
                top = 28.dp,
                end = 24.dp,
                bottom = 40.dp
            ),

            verticalArrangement =
                Arrangement.spacedBy(20.dp),

            horizontalAlignment =
                Alignment.CenterHorizontally
        ) {
            item {
                HarvestLinkLogo()
            }

            item {
                Text(
                    text = "Choose Your Role",

                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            top = 2.dp,
                            bottom = 16.dp
                        ),

                    color = ForestGreen,
                    fontSize = 34.sp,
                    lineHeight = 40.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            }

            item {
                RoleCard(
                    title = "I'm a Buyer",

                    description =
                        "Browse and purchase fresh produce, subscribe to food boxes, and get meal planning help.",

                    actionText =
                        "Continue as Buyer",

                    icon =
                        Icons.Outlined.ShoppingBag,

                    cardColor =
                        BuyerBackground,

                    iconBackground =
                        RoleIconBackground,

                    iconColor =
                        PrimaryGreen,

                    titleColor =
                        ForestGreen,

                    descriptionColor =
                        SecondaryText,

                    actionColor =
                        PrimaryGreen,

                    onClick = {
                        onRoleSelected(
                            UserRole.BUYER
                        )
                    }
                )
            }

            item {
                RoleCard(
                    title = "I'm a Farmer",

                    description =
                        "List your products, manage orders, and reach low-income families directly — no middlemen.",

                    note =
                        "Requires verification • Identity and farm documents needed",

                    actionText =
                        "Continue as Farmer",

                    icon = Icons.Outlined.Eco,

                    cardColor =
                        FarmerBackground,

                    iconBackground =
                        PrimaryGreen,

                    iconColor =
                        BrightGreen,

                    titleColor =
                        Color.White,

                    descriptionColor =
                        Color(0xFFC9D8D2),

                    noteColor =
                        Color(0xFF718F82),

                    actionColor =
                        BrightGreen,

                    showVerifiedBadge = true,

                    onClick = {
                        onRoleSelected(
                            UserRole.FARMER
                        )
                    }
                )
            }

            item {
                RoleCard(
                    title = "I'm an Admin",

                    description =
                        "Keep the platform safe, reliable, and well-managed.",

                    actionText =
                        "Continue as Admin",

                    icon =
                        Icons.Outlined
                            .AdminPanelSettings,

                    cardColor =
                        AdminBackground,

                    iconBackground =
                        Color.White,

                    iconColor =
                        ForestGreen,

                    titleColor =
                        PrimaryGreen,

                    descriptionColor =
                        SecondaryText,

                    actionColor =
                        BrightGreen,

                    onClick = {
                        onRoleSelected(
                            UserRole.ADMIN
                        )
                    }
                )
            }
        }
    }
}

@Composable
fun HarvestLinkLogo() {
    Column(
        horizontalAlignment =
            Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(112.dp)
                .border(
                    width = 4.dp,
                    color = BrandGreen,
                    shape = CircleShape
                ),

            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Outlined.Eco,

                contentDescription =
                    "HarvestLink logo",

                modifier =
                    Modifier.size(70.dp),

                tint = BrandGreen
            )
        }

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        Text(
            text = "HarvestLink",
            color = ForestGreen,
            fontSize = 30.sp,
            lineHeight = 32.sp,
            fontWeight = FontWeight.ExtraBold
        )

        Text(
            text = "Connecting Fresh Food",
            color = BrandGreen,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
fun RoleCard(
    title: String,
    description: String,
    actionText: String,
    icon: ImageVector,
    cardColor: Color,
    iconBackground: Color,
    iconColor: Color,
    titleColor: Color,
    descriptionColor: Color,
    actionColor: Color,
    onClick: () -> Unit,
    note: String? = null,
    noteColor: Color = SecondaryText,
    showVerifiedBadge: Boolean = false
) {
    Card(
        onClick = onClick,

        modifier =
            Modifier.fillMaxWidth(),

        shape =
            RoundedCornerShape(28.dp),

        colors = CardDefaults.cardColors(
            containerColor = cardColor
        ),

        elevation =
            CardDefaults.cardElevation(
                defaultElevation =
                    if (
                        cardColor ==
                        FarmerBackground
                    ) {
                        0.dp
                    } else {
                        2.dp
                    },

                pressedElevation = 1.dp
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 26.dp,
                    top = 24.dp,
                    end = 26.dp,
                    bottom = 24.dp
                )
        ) {
            Box(
                modifier = Modifier
                    .size(54.dp)
                    .background(
                        color = iconBackground,

                        shape =
                            RoundedCornerShape(18.dp)
                    ),

                contentAlignment =
                    Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,

                    modifier =
                        Modifier.size(31.dp),

                    tint = iconColor
                )
            }

            Spacer(
                modifier = Modifier.height(24.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),

                verticalAlignment =
                    Alignment.CenterVertically,

                horizontalArrangement =
                    Arrangement.SpaceBetween
            ) {
                Text(
                    text = title,
                    color = titleColor,
                    fontSize = 25.sp,
                    lineHeight = 30.sp,
                    fontWeight = FontWeight.Bold
                )

                if (showVerifiedBadge) {
                    VerifiedBadge()
                }
            }

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            Text(
                text = description,
                color = descriptionColor,
                fontSize = 18.sp,
                lineHeight = 25.sp
            )

            if (note != null) {
                Spacer(
                    modifier =
                        Modifier.height(15.dp)
                )

                Text(
                    text = note,
                    color = noteColor,
                    fontSize = 14.sp,
                    lineHeight = 19.sp
                )
            }

            Spacer(
                modifier = Modifier.height(22.dp)
            )

            Row(
                verticalAlignment =
                    Alignment.CenterVertically
            ) {
                Text(
                    text = actionText,
                    color = actionColor,
                    fontSize = 18.sp,
                    fontWeight =
                        FontWeight.SemiBold
                )

                Icon(
                    imageVector =
                        Icons.Filled
                            .KeyboardArrowRight,

                    contentDescription =
                        "Open $title",

                    modifier = Modifier
                        .padding(start = 5.dp)
                        .size(24.dp),

                    tint = actionColor
                )
            }
        }
    }
}

@Composable
fun VerifiedBadge() {
    Row(
        modifier = Modifier
            .background(
                color = Color(0xFF255F4C),
                shape = RoundedCornerShape(50)
            )
            .padding(
                horizontal = 12.dp,
                vertical = 7.dp
            ),

        verticalAlignment =
            Alignment.CenterVertically
    ) {
        Icon(
            imageVector =
                Icons.Outlined.VerifiedUser,

            contentDescription =
                "Verified farmer",

            modifier =
                Modifier.size(17.dp),

            tint = BrightGreen
        )

        Text(
            text = "Verified",

            modifier =
                Modifier.padding(start = 6.dp),

            color = BrightGreen,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Preview(
    name = "Role Selection Portrait",
    showBackground = true,
    widthDp = 412,
    heightDp = 915
)
@Composable
fun RoleSelectionPortraitPreview() {
    GithubDemoTheme(
        darkTheme = false,
        dynamicColor = false
    ) {
        RoleSelectionScreen(
            onRoleSelected = {}
        )
    }
}

@Preview(
    name = "Role Selection Landscape",
    showBackground = true,
    widthDp = 915,
    heightDp = 412
)
@Composable
fun RoleSelectionLandscapePreview() {
    GithubDemoTheme(
        darkTheme = false,
        dynamicColor = false
    ) {
        RoleSelectionScreen(
            onRoleSelected = {}
        )
    }
}