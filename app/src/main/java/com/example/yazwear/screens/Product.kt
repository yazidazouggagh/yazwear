package com.example.yazwear.screens

import androidx.compose.ui.graphics.vector.ImageVector

data class Product(
    val imageRes: Int,
    val name: String,
    val price: String,
    val likes: Int,
    val category: String // Added category
)

data class ExpandableInfo(
    val icon: ImageVector,
    val title: String,
    val content: String
)
