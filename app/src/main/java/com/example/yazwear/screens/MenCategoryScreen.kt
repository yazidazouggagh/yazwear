package com.example.yazwear.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.PersonOutline
import androidx.compose.material.icons.outlined.ShoppingBag
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.yazwear.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenCategoryScreen(navController: NavController, bagViewModel: BagViewModel, productViewModel: ProductViewModel = viewModel()) {
    val products by productViewModel.products.collectAsState()

    LaunchedEffect(Unit) {
        productViewModel.getProducts()
    }

    Scaffold(
        containerColor = Color.White,
        topBar = {
            Column {
                val bagItemCount by bagViewModel.bagItemCount.collectAsState()
                CenterAlignedTopAppBar(
                    title = { Text("Men", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = Color.Black) },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.Black)
                        }
                    },
                    actions = {
                        BadgedBox(
                            badge = {
                                if (bagItemCount > 0) {
                                    Badge(
                                        modifier = Modifier.offset(x = (-12).dp, y = 8.dp),
                                        containerColor = Color.Red
                                    ) { Text("$bagItemCount", color = Color.White, fontSize = 10.sp) }
                                }
                            }
                        ) {
                            IconButton(onClick = { navController.navigate(Screen.Bag.route) }) {
                                Icon(Icons.Outlined.ShoppingBag, contentDescription = "Shopping Bag", tint = Color.Black, modifier = Modifier.size(28.dp))
                            }
                        }
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.White)
                )
                HorizontalDivider(thickness = 1.dp, color = Color(0xFFF0F0F0))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween, // Pousse les éléments aux extrémités
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable {  }
                    ) {
                        Text("Sort by", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                        Icon(Icons.Default.UnfoldMore, contentDescription = "Sort by", modifier = Modifier.size(20.dp))
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.clickable {  }
                        ) {
                            Icon(Icons.Default.FilterList, contentDescription = "Filter", modifier = Modifier.size(20.dp), tint = Color.Black)
                            Spacer(Modifier.width(4.dp))
                            Text("Filter", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                        }
                        Icon(Icons.Default.GridView, contentDescription = "Grid view", tint = Color.Gray)
                        Icon(Icons.Outlined.PersonOutline, contentDescription = "Models", tint = Color.Gray)
                    }
                }
            }
        },
    ) { innerPadding ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.padding(innerPadding),
            contentPadding = PaddingValues(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(products.filter { it.category == "men's clothing" }) { product ->
                ProductCard(product = product, navController = navController)
            }
        }
    }
}

@Composable
fun ProductCard(product: Product, navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { navController.navigate(Screen.ProductDetail.createRoute(product.id)) }
    ) {
        Image(
            painter = rememberAsyncImagePainter(product.image),
            contentDescription = product.title,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(0.75f)
                .clip(RoundedCornerShape(16.dp)),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = product.title,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = Color.Black
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${product.price} MAD",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = Color.Black
            )
            Icon(
                imageVector = Icons.Outlined.FavoriteBorder,
                contentDescription = "Add to favorites",
                tint = Color.Black,
                modifier = Modifier.size(22.dp)
            )
        }
    }
}
