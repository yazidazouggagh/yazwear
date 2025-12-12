package com.example.yazwear.data

import kotlinx.coroutines.flow.Flow


class YazwearRepository(
    private val productDao: ProductDao,
    private val userDao: UserDao
) {


    val cartItems: Flow<List<ProductEntity>> = productDao.getAllProducts()

    suspend fun addToCart(product: ProductEntity) {
        productDao.insertProduct(product)
    }

    suspend fun removeFromCart(productName: String) {
        productDao.deleteOneByName(productName)
    }


    suspend fun getLoggedInUser(): UserEntity? {
        return userDao.getLoggedInUser()
    }

    suspend fun insertUser(user: UserEntity) {
        userDao.insertUser(user)
    }
}
