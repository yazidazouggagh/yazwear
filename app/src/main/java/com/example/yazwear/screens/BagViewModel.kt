package com.example.yazwear.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class BagViewModel : ViewModel() {

    private val _bagItems = MutableStateFlow<List<Product>>(emptyList())
    val bagItems = _bagItems.asStateFlow()

    val bagItemCount = _bagItems.map { it.size }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    fun addToBag(product: Product) {
        _bagItems.value = _bagItems.value + product
    }

    fun removeFromBag(product: Product) {
        val currentList = _bagItems.value.toMutableList()
        if (currentList.contains(product)) {
            currentList.remove(product)
            _bagItems.value = currentList
        }
    }
}