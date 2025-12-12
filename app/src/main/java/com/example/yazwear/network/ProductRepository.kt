package com.example.yazwear.network

import com.example.yazwear.screens.Product

class ProductRepository(private val apiService: ApiService) {

    suspend fun getProducts(): List<Product> {
        return apiService.getProducts()
    }

    suspend fun getProductById(id: Int): Product {
        return apiService.getProductById(id)
    }
}