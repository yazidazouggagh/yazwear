package com.example.yazwear.screens

sealed class Screen(val route: String) {
    object ProductList : Screen("product_list")

    object ProductDetail : Screen("product_detail/{productId}") {
        fun passId(id: Int): String = "product_detail/$id"
    }
}
