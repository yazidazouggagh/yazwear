package com.example.yazwear.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.yazwear.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow


data class ProductScreenState(
    val products: List<Product> = emptyList(),
    val isLoading: Boolean = false
)


class ProductViewModel : ViewModel() {


    private val _uiState = MutableStateFlow(ProductScreenState())
    // Public, read-only state for the UI to observe
    val uiState: StateFlow<ProductScreenState> = _uiState.asStateFlow()

    init {

        _uiState.value = ProductScreenState(
            products = listOf(
                Product(R.drawable.black_sweatshirt, "Sweat-shirt -noir-", "390.00 MAD", 450, "Men"),
                Product(R.drawable.grise, "Fermeture éclair grise", "590.00 MAD", 320, "Men"),
                Product(R.drawable.leather, "Veste en cuir RETRO CLUB", "850.00 MAD", 680, "Men"),
                Product(R.drawable.allemand, "Maillot Allemagne -blanc-", "349.00 MAD", 550, "Men"),
                Product(R.drawable.demi_manchesnoir, "T-shirt à manches longues", "450.00 MAD", 450, "Men"),
                Product(R.drawable.vert, "Polo oversize imprimé Quiet place", "350.00 MAD", 210, "Men")
            )
        )
    }


    fun getProductByName(name: String): Product? {
        return _uiState.value.products.find { it.name == name }
    }
}

class ProductViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProductViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ProductViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
