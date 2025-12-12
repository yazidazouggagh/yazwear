
package com.example.yazwear.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BagScreen(navController: NavController, bagViewModel: BagViewModel) {
    val bagItems by bagViewModel.bagItems.collectAsState()
    val groupedItems = bagItems.groupBy { it }.mapValues { it.value.size }

    Scaffold(
        containerColor = Color.White,
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("My Bag", fontWeight = FontWeight.Bold, color = Color.Black) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.Black)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.White
                )
            )
        },
        bottomBar = {
            if (groupedItems.isNotEmpty()) {
                val subtotal = groupedItems.entries.sumOf { (product, quantity) ->
                    product.price * quantity
                }
                val shippingFee = 50.00
                val total = subtotal + shippingFee

                CheckoutSummary(
                    subtotal = subtotal,
                    shippingFee = shippingFee,
                    total = total
                )
            }
        }
    ) { innerPadding ->
        if (groupedItems.isEmpty()) {
            Box(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Your bag is empty.", fontSize = 18.sp, color = Color.Gray)
            }
        } else {
            LazyColumn(
                modifier = Modifier.padding(innerPadding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                items(groupedItems.entries.toList()) { (product, quantity) ->
                    BagItemCard(
                        product = product,
                        quantity = quantity,
                        onAdd = { bagViewModel.addToBag(product) },
                        onRemove = { bagViewModel.removeFromBag(product) }
                    )
                }

                item { PromoCodeSection() }

                item { PaymentMethodSection() }
            }
        }
    }
}

@Composable
fun BagItemCard(
    product: Product,
    quantity: Int,
    onAdd: () -> Unit,
    onRemove: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = rememberAsyncImagePainter(product.image),
            contentDescription = product.title,
            modifier = Modifier
                .size(90.dp)
                .clip(RoundedCornerShape(12.dp)),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(product.title, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text("${product.price} MAD", color = Color.Gray, fontSize = 14.sp)
        }
        QuantitySelector(
            quantity = quantity,
            onAdd = onAdd,
            onRemove = onRemove
        )
    }
}

@Composable
fun QuantitySelector(
    quantity: Int,
    onAdd: () -> Unit,
    onRemove: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier
            .border(1.dp, Color(0xFFEEEEEE), CircleShape)
            .padding(horizontal = 4.dp)
    ) {
        IconButton(onClick = onRemove, modifier = Modifier.size(28.dp), enabled = quantity > 0) {
            Icon(Icons.Default.Remove, contentDescription = "Remove one", tint = if(quantity > 0) Color.Black else Color.Gray)
        }
        Text(
            text = "$quantity",
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            textAlign = TextAlign.Center
        )
        IconButton(onClick = onAdd, modifier = Modifier.size(28.dp)) {
            Icon(Icons.Default.Add, contentDescription = "Add one", tint = Color.Black)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PromoCodeSection() {
    var promoCode by remember { mutableStateOf("") }
    Spacer(modifier = Modifier.height(16.dp))
    Text("Promo Code", fontWeight = FontWeight.Bold, fontSize = 18.sp)
    Spacer(modifier = Modifier.height(8.dp))
    Row(verticalAlignment = Alignment.CenterVertically) {
        OutlinedTextField(
            value = promoCode,
            onValueChange = { promoCode = it },
            placeholder = { Text("Enter code") },
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(12.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Black,
                unfocusedIndicatorColor = Color.LightGray
            )
        )
        Spacer(modifier = Modifier.width(8.dp))
        Button(
            onClick = {  },
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
            modifier = Modifier.size(56.dp),
            contentPadding = PaddingValues(0.dp)
        ) {
            Icon(Icons.Default.Check, contentDescription = "Apply promo code", tint = Color.White)
        }
    }
}

data class PaymentOption(val name: String, val icon: ImageVector)

@Composable
fun PaymentMethodSection() {
    val paymentOptions = listOf(
        PaymentOption("Visa", Icons.Default.CreditCard),
        PaymentOption("Mastercard", Icons.Default.CreditCard)
    )
    var selectedPayment by remember { mutableStateOf(paymentOptions.first()) }

    Spacer(modifier = Modifier.height(16.dp))
    Text("Payment Method", fontWeight = FontWeight.Bold, fontSize = 18.sp)
    Spacer(modifier = Modifier.height(8.dp))

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        paymentOptions.forEach { option ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, if(selectedPayment == option) Color.Black else Color.LightGray, RoundedCornerShape(12.dp))
                    .selectable(
                        selected = (selectedPayment == option),
                        onClick = { selectedPayment = option }
                    )
                    .padding(16.dp)
            ) {
                Icon(option.icon, contentDescription = "${option.name} Logo", modifier = Modifier.size(24.dp), tint = Color.Black)
                Spacer(modifier = Modifier.width(16.dp))
                Text(option.name, fontSize = 16.sp, modifier = Modifier.weight(1f))
                RadioButton(
                    selected = (selectedPayment == option),
                    onClick = { selectedPayment = option },
                    colors = RadioButtonDefaults.colors(selectedColor = Color.Black)
                )
            }
        }
    }
}


@Composable
fun CheckoutSummary(
    subtotal: Double,
    shippingFee: Double,
    total: Double
) {
    Surface(
        shadowElevation = 8.dp,
        color = Color.White,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            SummaryRow("Subtotal", String.format("%.2f MAD", subtotal))
            SummaryRow("Shipping Fee", String.format("%.2f MAD", shippingFee))
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
            SummaryRow("Total", String.format("%.2f MAD", total), isTotal = true)
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = {  },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
            ) {
                Text("PROCEED TO CHECKOUT", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun SummaryRow(label: String, value: String, isTotal: Boolean = false) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            fontSize = if (isTotal) 18.sp else 16.sp,
            fontWeight = if (isTotal) FontWeight.Bold else FontWeight.Normal,
            color = if (isTotal) Color.Black else Color.Gray
        )
        Text(
            text = value,
            fontSize = if (isTotal) 18.sp else 16.sp,
            fontWeight = if (isTotal) FontWeight.Bold else FontWeight.SemiBold,
            color = Color.Black
        )
    }
}
