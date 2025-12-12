package com.example.yazwear.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.yazwear.screens.Product
import com.example.yazwear.screens.Rating

@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey
    val id: Int,
    val title: String,
    val price: Double,
    val description: String,
    val category: String,
    val image: String,
    val rating_rate: Double,
    val rating_count: Int
)

fun ProductEntity.toProduct(): Product {
    return Product(
        id = id,
        title = title,
        price = price,
        description = description,
        category = category,
        image = image,
        rating = Rating(rate = rating_rate, count = rating_count)
    )
}

fun Product.toEntity(): ProductEntity {
    return ProductEntity(
        id = id,
        title = title,
        price = price,
        description = description,
        category = category,
        image = image,
        rating_rate = rating.rate,
        rating_count = rating.count
    )
}
