package com.example.yazwear.model

import com.google.gson.annotations.SerializedName

data class ProductModel(
    val id: Int,
    val title: String,
    val price: Double,
    val description: String,
    val category: String,
    val image: String,
    val rating: Rating
)

data class Rating(
    val rate: Double,
    @SerializedName("count")
    val reviewCount: Int
)
