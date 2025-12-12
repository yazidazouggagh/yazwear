package com.example.yazwear.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.yazwear.network.ProductRepository
import com.example.yazwear.network.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProductViewModel : ViewModel() {

    private val repository = ProductRepository(RetrofitInstance.api)

    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products

    private val _product = MutableStateFlow<Product?>(null)
    val product: StateFlow<Product?> = _product

    fun getProducts() {
        viewModelScope.launch {
            try {
                _products.value = repository.getProducts()
            } catch (e: Exception) {

            }
        }
    }

    fun getProductById(id: Int) {
        viewModelScope.launch {
            try {
                _product.value = repository.getProductById(id)
            } catch (e: Exception) {

            }
        }
    }
}