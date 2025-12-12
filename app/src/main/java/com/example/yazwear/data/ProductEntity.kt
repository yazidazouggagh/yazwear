package com.example.yazwear.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.yazwear.screens.Product

@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val imageRes: Int,
    val name: String,
    val price: String,
    val likes: Int,
    val category: String // Added category
)

fun ProductEntity.toProduct(): Product {
    return Product(
        imageRes = imageRes,
        name = name,
        price = price,
        likes = likes,
        category = category
    )
}

fun Product.toEntity(): ProductEntity {
    return ProductEntity(
        imageRes = imageRes,
        name = name,
        price = price,
        likes = likes,
        category = category
    )
}
