package com.example.yazwear

import android.app.Application
import com.example.yazwear.data.AppDatabase
import com.example.yazwear.data.YazwearRepository

class YazwearApplication : Application() {

    private val database by lazy { AppDatabase.getDatabase(this) }


    val repository by lazy { YazwearRepository(database.productDao(), database.userDao()) }
}
