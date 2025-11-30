package com.example.yazwear.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.yazwear.screens.*

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object MenCategory : Screen("men_category")
    object ProductDetail : Screen("product_detail/{productName}") {
        fun createRoute(productName: String) = "product_detail/$productName"
    }
    object Bag : Screen("bag")
}

@Composable
fun AppNavigation(navController: NavHostController) {
    val bagViewModel: BagViewModel = viewModel()

    NavHost(navController = navController, startDestination = Screen.Home.route) {
        composable(Screen.Home.route) {
            HomeScreen(navController, bagViewModel)
        }
        composable(Screen.MenCategory.route) {
            MenCategoryScreen(navController, bagViewModel)
        }
        composable(
            route = Screen.ProductDetail.route,
            arguments = listOf(navArgument("productName") { type = NavType.StringType })
        ) { backStackEntry ->
            val productName = backStackEntry.arguments?.getString("productName")
            if (productName != null) {
                ProductDetailScreen(navController, productName, bagViewModel)
            } else {
                navController.popBackStack()
            }
        }
        composable(Screen.Bag.route) {
            BagScreen(navController, bagViewModel)
        }
    }
}