package com.example.foodie

import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.basket.BasketScreen
import com.example.card_product.CardProductScreen
import com.example.catalog.CatalogScreen

@Composable
fun Navigation(
    windowSizeClass: WindowSizeClass,
    navController: NavHostController = rememberNavController(),
) {
    NavHost(navController = navController, startDestination = "CatalogScreen") {
        composable("CatalogScreen") {
            CatalogScreen(
                columns = if (windowSizeClass.widthSizeClass == WindowWidthSizeClass.Expanded) {
                    GridCells.Fixed(4)
                } else {
                    GridCells.Fixed(2)
                },
                onProductClick = { navController.navigate("$PRODUCT_ROUTE/$id") },
                onCartClick = { navController.navigate("CardProductScreen")}
            )
        }
        composable("CardProductScreen") {
            CardProductScreen(
//                onGoToItem = { id ->
//                    navController.navigate("details/$id")
//                }
            )
        }

        composable("BasketScreen") {
            BasketScreen(
//                onGoToItem = { id ->
//                    navController.navigate("details/$id")
//                }
            )
        }

//        composable(
//            "details/{id}",
//            listOf(navArgument("id") { type = NavType.LongType })
//        ) {
//            DetailsRoute(
//                onGoBack = {
//                    navController.popBackStack()
//                }
//            )
//        }
    }
}

private const val PRODUCT_ROUTE = "product_route"

//enum class Screens(name: String) {
//    CATALOG("catalog"),
//    CARD_PRODUCT("card_product"),
//    BASKET("basket"),
//}