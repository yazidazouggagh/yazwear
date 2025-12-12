package com.example.yazwear.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.yazwear.data.YazwearRepository
import com.example.yazwear.data.toProduct
import com.example.yazwear.data.toEntity
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class BagViewModel(private val repository: YazwearRepository) : ViewModel() {

    val bagItems: StateFlow<List<Product>> = repository.cartItems
        .map { entities -> entities.map { it.toProduct() } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val bagItemCount: StateFlow<Int> = bagItems.map { it.size }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    fun addToBag(product: Product) {
        viewModelScope.launch {
            repository.addToCart(product.toEntity())
        }
    }

    fun removeFromBag(product: Product) {
        viewModelScope.launch {
            repository.removeFromCart(product.name)
        }
    }
}

class BagViewModelFactory(private val repository: YazwearRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BagViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return BagViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
