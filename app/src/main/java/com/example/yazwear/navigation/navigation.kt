package com.example.yazwear.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.yazwear.YazwearApplication
import com.example.yazwear.screens.*

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object MenCategory : Screen("men_category")
    object ProductDetail : Screen("product_detail/{productId}") {
        fun createRoute(productId: Int) = "product_detail/$productId"
    }
    object Bag : Screen("bag")
}

@Composable
fun AppNavigation(navController: NavHostController) {
    val context = LocalContext.current
    val repository = (context.applicationContext as YazwearApplication).repository
    val bagViewModel: BagViewModel = viewModel(factory = BagViewModelFactory(repository))
    val productViewModel: ProductViewModel = viewModel()

    NavHost(navController = navController, startDestination = Screen.Home.route) {
        composable(Screen.Home.route) {
            HomeScreen(navController, bagViewModel)
        }
        composable(Screen.MenCategory.route) {
            MenCategoryScreen(navController, bagViewModel, productViewModel)
        }
        composable(
            route = Screen.ProductDetail.route,
            arguments = listOf(navArgument("productId") { type = NavType.IntType })
        ) { backStackEntry ->
            val productId = backStackEntry.arguments?.getInt("productId")
            if (productId != null) {
                ProductDetailScreen(navController, productId, bagViewModel, productViewModel)
            } else {
                navController.popBackStack()
            }
        }
        composable(Screen.Bag.route) {
            BagScreen(navController, bagViewModel)
        }
    }
}
