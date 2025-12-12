package com.example.yazwear.screens

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.yazwear.data.YazwearRepository
import com.example.yazwear.data.toProduct
import com.example.yazwear.data.toEntity
import com.example.yazwear.util.NotificationHelper
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class BagViewModel(private val repository: YazwearRepository, private val application: Application) : ViewModel() {

    val bagItems: StateFlow<List<Product>> = repository.cartItems
        .map { entities -> entities.map { it.toProduct() } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val bagItemCount: StateFlow<Int> = bagItems.map { it.size }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    fun addToBag(product: Product) {
        viewModelScope.launch {
            repository.addToCart(product.toEntity())
            checkItemCountAndNotify()
        }
    }

    fun removeFromBag(product: Product) {
        viewModelScope.launch {
            repository.removeFromCart(product.id)
        }
    }

    private fun checkItemCountAndNotify() {
        viewModelScope.launch {
            if (bagItems.value.size == 2) {
                val notificationHelper = NotificationHelper(application)
                notificationHelper.showNotification("Panier Yazwear", "Votre panier dépasse maintenant 2 articles !")
            }
        }
    }
}

class BagViewModelFactory(private val repository: YazwearRepository, private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BagViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return BagViewModel(repository, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
