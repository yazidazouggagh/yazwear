package com.example.yazwear.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ReceiptLong
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.LocalShipping
import androidx.compose.material.icons.outlined.ShoppingBag
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.yazwear.R
import com.example.yazwear.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(navController: NavController, productName: String, bagViewModel: BagViewModel) {
    val product = getProductByName(productName)

    BottomSheetScaffold(
        sheetContent = { ProductInfoSheet(product, bagViewModel) },
        scaffoldState = rememberBottomSheetScaffoldState(),
        sheetPeekHeight = 170.dp,
        sheetShape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        sheetContainerColor = Color.White,
        sheetShadowElevation = 16.dp,
        topBar = { ProductTopBar(navController = navController, bagViewModel = bagViewModel, isTransparent = false, product = product) },
        containerColor = Color(0xFFE0E0E0)
    ) {
        Image(
            painter = painterResource(id = product.imageRes),
            contentDescription = product.name,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
    }
}

@Composable
fun ProductInfoSheet(product: Product, bagViewModel: BagViewModel) {
    Column(
        modifier = Modifier
            .padding(horizontal = 24.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Box(
            modifier = Modifier
                .padding(vertical = 16.dp)
                .width(40.dp)
                .height(4.dp)
                .background(Color(0xFFE0E0E0), shape = RoundedCornerShape(2.dp))
                .align(Alignment.CenterHorizontally)
        )

        Text(text = product.name, fontWeight = FontWeight.Bold, fontSize = 22.sp)
        Spacer(Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = product.price, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Outlined.FavoriteBorder, contentDescription = "Likes", modifier = Modifier.size(20.dp))
                Spacer(Modifier.width(4.dp))
                Text(text = product.likes.toString(), fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
        }
        Spacer(Modifier.height(24.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            SelectDropDown(title = "Select colour", modifier = Modifier.weight(1f))
            SelectDropDown(title = "Select size", modifier = Modifier.weight(1f))
        }
        Spacer(Modifier.height(24.dp))

        Button(
            onClick = { bagViewModel.addToBag(product) },
            modifier = Modifier.fillMaxWidth().height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text("ADD TO BAG", fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }
        Spacer(Modifier.height(24.dp))

        InfoRow(icon = Icons.Outlined.LocalShipping, text = "Free delivery")
        HorizontalDivider(color = Color(0xFFF0F0F0), thickness = 1.dp, modifier = Modifier.padding(vertical = 12.dp))
        InfoRow(icon = Icons.Default.Replay, text = "Free return", trailingText = "View return policy")
        Spacer(Modifier.height(24.dp))

        Text(text = "About product", fontWeight = FontWeight.Bold, fontSize = 18.sp)
        Spacer(Modifier.height(16.dp))

        val productInfos = listOf(
            ExpandableInfo(Icons.AutoMirrored.Filled.ReceiptLong, "Product details", "Details content..."),
            ExpandableInfo(Icons.Default.Sell, "Brand", "Brand content..."),
            ExpandableInfo(Icons.Default.Straighten, "Size and fit", "Size content..."),
            ExpandableInfo(Icons.Default.History, "History", "History content...")
        )

        productInfos.forEach { info ->
            ExpandableInfoCard(info = info)
            if (productInfos.last() != info) {
                HorizontalDivider(color = Color(0xFFF0F0F0), thickness = 1.dp)
            }
        }
        Spacer(Modifier.height(24.dp))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductTopBar(navController: NavController, bagViewModel: BagViewModel, isTransparent: Boolean, product: Product) {
    val bagItemCount by bagViewModel.bagItemCount.collectAsState()
    val contentColor = if (isTransparent) Color.White else Color.Black
    val bgColor = if (isTransparent) Color.Transparent else Color.White.copy(alpha = 0.8f)

    CenterAlignedTopAppBar(
        title = { Text(product.category, fontWeight = FontWeight.Bold, fontSize = 20.sp, color = contentColor) },
        navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = contentColor)
            }
        },
        actions = {
            BadgedBox(
                badge = {
                    if (bagItemCount > 0) {
                        Badge(
                            containerColor = Color.Red,
                            modifier = Modifier.offset(x = (-12).dp, y = 8.dp)
                        ) {
                            Text("$bagItemCount", color = Color.White, fontSize = 10.sp)
                        }
                    }
                }
            ) {
                IconButton(onClick = { navController.navigate(Screen.Bag.route) }) {
                    Icon(Icons.Outlined.ShoppingBag, contentDescription = "Shopping Bag", tint = contentColor, modifier = Modifier.size(28.dp))
                }
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = bgColor)
    )
}

@Composable
fun SelectDropDown(title: String, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .border(1.dp, Color(0xFFE0E0E0), RoundedCornerShape(8.dp))
            .padding(horizontal = 12.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(title, fontSize = 14.sp)
        Icon(Icons.Default.UnfoldMore, contentDescription = null, modifier = Modifier.size(20.dp))
    }
}

@Composable
fun InfoRow(icon: ImageVector, text: String, trailingText: String? = null) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 4.dp)) {
        Icon(icon, contentDescription = null, modifier = Modifier.size(24.dp), tint = Color.DarkGray)
        Spacer(Modifier.width(16.dp))
        Text(text, fontSize = 16.sp, fontWeight = FontWeight.Medium)
        if (trailingText != null) {
            Spacer(Modifier.weight(1f))
            Text(trailingText, color = Color.Gray, fontSize = 14.sp)
        }
    }
}

@Composable
fun ExpandableInfoCard(info: ExpandableInfo) {
    var expanded by remember { mutableStateOf(false) }
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = !expanded }
                .padding(vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(info.icon, contentDescription = null, modifier = Modifier.size(24.dp), tint = Color.DarkGray)
            Spacer(Modifier.width(16.dp))
            Text(info.title, fontSize = 16.sp, fontWeight = FontWeight.Medium)
            Spacer(Modifier.weight(1f))
            Icon(if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore, contentDescription = null)
        }
        AnimatedVisibility(visible = expanded) {
            Text(info.content, modifier = Modifier.padding(start = 40.dp, bottom = 16.dp), color = Color.Gray)
        }
    }
}

fun getProductByName(name: String): Product {
    val products = listOf(
        Product(R.drawable.black_sweatshirt, "Sweat-shirt -noir-", "390.00 MAD", 450, "Men"),
        Product(R.drawable.grise, "Fermeture éclair grise", "590.00 MAD", 320, "Men"),
        Product(R.drawable.leather, "Veste en cuir RETRO CLUB", "850.00 MAD", 680, "Men"),
        Product(R.drawable.allemand, "Maillot Allemagne -blanc-", "349.00 MAD", 550, "Men"),
        Product(R.drawable.demi_manchesnoir, "T-shirt à manches longues", "450.00 MAD", 450, "Men"),
        Product(R.drawable.vert, "Polo oversize imprimé Quiet place", "350.00 MAD", 210, "Men")
    )
    return products.find { it.name == name } ?: products.first()
}
