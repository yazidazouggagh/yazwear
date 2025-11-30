package com.example.yazwear.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.ShoppingBag
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.yazwear.R
import com.example.yazwear.navigation.Screen

data class Category(val imageRes: Int, val name: String)

data class BottomNavItem(val icon: ImageVector, val label: String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavHostController, bagViewModel: BagViewModel) {

    val bagItems by bagViewModel.bagItems.collectAsState()

    val categories = listOf(
        Category(R.drawable.women, "Women"),
        Category(R.drawable.men, "Men"),
        Category(R.drawable.kids, "Kids"),
        Category(R.drawable.home, "Home"),
        Category(R.drawable.decor, "Decor")
    )

    val bottomNavItems = listOf(
        BottomNavItem(Icons.Filled.Home, "Home"),
        BottomNavItem(Icons.Outlined.FavoriteBorder, "Likes"),
        BottomNavItem(Icons.Outlined.ShoppingBag, "Bag"),
        BottomNavItem(Icons.Outlined.Person, "Profile")
    )

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("YazWear", fontWeight = FontWeight.Bold, fontSize = 20.sp) },
                navigationIcon = {
                    IconButton(onClick = {  }) {
                        Icon(Icons.Default.Menu, contentDescription = "Menu")
                    }
                },
                actions = {
                    IconButton(onClick = { }) {
                        Icon(Icons.Outlined.Notifications, contentDescription = "Notifications")
                    }
                }
            )
        },
        bottomBar = {
            BottomAppBar(containerColor = Color.White) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    bottomNavItems.forEach { item ->
                        val selected = item.label == "Home"
                        val tint = if (selected) Color.Black else Color.Gray

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .weight(1f)
                                .clickable { 
                                    if (item.label == "Bag") {
                                        navController.navigate(Screen.Bag.route)
                                    }
                                 }
                        ) {
                            if (item.label == "Bag") {

                                BadgedBox(
                                    badge = {

                                        if (bagItems.isNotEmpty()) {
                                            Badge(
                                                containerColor = Color.Red,
                                                contentColor = Color.White
                                            ) { 

                                                Text(bagItems.size.toString(), fontSize = 10.sp)
                                            }
                                        }
                                    }
                                ) {
                                    Icon(item.icon, contentDescription = item.label, tint = tint)
                                }
                            } else {

                                Icon(item.icon, contentDescription = item.label, tint = tint)
                            }
                            Text(
                                text = item.label,
                                fontSize = 12.sp,
                                color = tint,
                                fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())

        ) {
            Image(
                painter = painterResource(id = R.drawable.bleumodel),
                contentDescription = "Main product image",
                modifier = Modifier
                    .height(440.dp)
                    .fillMaxWidth(),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "Categories",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(horizontal = 16.dp)
            ) {
                items(categories) { category ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.clickable {
                            when (category.name) {
                                "Men" -> navController.navigate(Screen.MenCategory.route)
                            }
                        }
                    ) {
                        Image(
                            painter = painterResource(id = category.imageRes),
                            contentDescription = category.name,
                            modifier = Modifier
                                .size(80.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(category.name, fontSize = 14.sp)
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
