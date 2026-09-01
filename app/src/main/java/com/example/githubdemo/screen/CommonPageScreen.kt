package com.example.githubdemo.screen

import android.content.res.Configuration
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.githubdemo.model.PageFeature
import com.example.githubdemo.ui.theme.DividerColor
import com.example.githubdemo.ui.theme.ForestGreen
import com.example.githubdemo.ui.theme.MainText
import com.example.githubdemo.ui.theme.PageBackground
import com.example.githubdemo.ui.theme.PrimaryGreen
import com.example.githubdemo.ui.theme.SecondaryText
import com.example.githubdemo.ui.theme.SoftGreen


@Composable
fun CommonPageScreen(
    eyebrow: String,
    title: String,
    subtitle: String,
    searchPlaceholder: String,
    sectionTitle: String,
    features: List<PageFeature>
) {
    /*
     * rememberSaveable keeps the search text
     * after the phone rotates.
     */
    var searchText by rememberSaveable {
        mutableStateOf("")
    }

    /*
     * The Box keeps the UI centred.
     *
     * In portrait:
     * The content uses the available phone width.
     *
     * In landscape:
     * The content keeps a portrait-like maximum width
     * instead of becoming too wide.
     */
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(PageBackground),

        contentAlignment = Alignment.TopCenter
    ) {
        LazyColumn(
            modifier = Modifier
                .widthIn(max = 600.dp)
                .fillMaxWidth()
                .fillMaxHeight()
                .background(PageBackground),

            contentPadding = PaddingValues(
                bottom = 24.dp
            )
        ) {
            item {
                PageHeader(
                    eyebrow = eyebrow,
                    title = title,
                    subtitle = subtitle,
                    searchText = searchText,
                    searchPlaceholder = searchPlaceholder,

                    onSearchTextChange = {
                        searchText = it
                    }
                )
            }

            item {
                Text(
                    text = sectionTitle,

                    modifier = Modifier.padding(
                        start = 24.dp,
                        top = 28.dp,
                        end = 24.dp,
                        bottom = 14.dp
                    ),

                    color = MainText,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            items(
                items = features,
                key = { feature ->
                    feature.title
                }
            ) { feature ->

                FeatureCard(
                    feature = feature,

                    modifier = Modifier.padding(
                        horizontal = 24.dp,
                        vertical = 7.dp
                    )
                )
            }
        }
    }
}

@Composable
private fun PageHeader(
    eyebrow: String,
    title: String,
    subtitle: String,
    searchText: String,
    searchPlaceholder: String,
    onSearchTextChange: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = ForestGreen,

                shape = RoundedCornerShape(
                    bottomStart = 32.dp,
                    bottomEnd = 32.dp
                )
            )
            .statusBarsPadding()
            .padding(
                start = 24.dp,
                top = 24.dp,
                end = 24.dp,
                bottom = 28.dp
            )
    ) {
        Text(
            text = eyebrow,
            color = Color(0xFFC6D9D1),
            fontSize = 16.sp
        )

        Spacer(
            modifier = Modifier.height(4.dp)
        )

        Text(
            text = title,
            color = Color.White,
            fontSize = 32.sp,
            lineHeight = 38.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(
            modifier = Modifier.height(6.dp)
        )

        Text(
            text = subtitle,
            color = Color(0xFFC6D9D1),
            fontSize = 15.sp
        )

        Spacer(
            modifier = Modifier.height(22.dp)
        )

        OutlinedTextField(
            value = searchText,

            onValueChange = {
                onSearchTextChange(it)
            },

            modifier = Modifier.fillMaxWidth(),

            placeholder = {
                Text(
                    text = searchPlaceholder,
                    color = Color(0xFFC6D9D1)
                )
            },

            leadingIcon = {
                Icon(
                    imageVector =
                        Icons.Outlined.Search,

                    contentDescription = "Search",

                    tint = Color(0xFFC6D9D1)
                )
            },

            singleLine = true,

            shape = RoundedCornerShape(20.dp),

            colors =
                OutlinedTextFieldDefaults.colors(
                    focusedTextColor =
                        Color.White,

                    unfocusedTextColor =
                        Color.White,

                    cursorColor =
                        Color.White,

                    focusedContainerColor =
                        Color(0xFF315248),

                    unfocusedContainerColor =
                        Color(0xFF315248),

                    focusedBorderColor =
                        Color(0xFF617B72),

                    unfocusedBorderColor =
                        Color(0xFF617B72)
                )
        )
    }
}

@Composable
private fun FeatureCard(
    feature: PageFeature,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),

        shape = RoundedCornerShape(22.dp),

        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),

        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),

            verticalAlignment =
                Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(54.dp)
                    .background(
                        color = SoftGreen,

                        shape =
                            RoundedCornerShape(17.dp)
                    ),

                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = feature.icon,
                    contentDescription = feature.title,
                    tint = PrimaryGreen,
                    modifier = Modifier.size(28.dp)
                )
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 16.dp),

                verticalArrangement =
                    Arrangement.Center
            ) {
                Text(
                    text = feature.title,
                    color = MainText,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(
                    modifier = Modifier.height(4.dp)
                )

                Text(
                    text = feature.description,
                    color = SecondaryText,
                    fontSize = 14.sp,
                    lineHeight = 19.sp
                )
            }
        }
    }
}