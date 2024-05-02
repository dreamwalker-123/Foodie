package com.example.foodie

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
    navController: NavHostController = rememberNavController()
) {
    NavHost(navController = navController, startDestination = "CatalogScreen") {
        composable("CatalogScreen") {
            CatalogScreen(
//                onGoToItem = { id ->
//                    navController.navigate("details/$id")
//                }
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

enum class Screens(name: String) {
    CATALOG("catalog"),
    CARD_PRODUCT("card_product"),
    BASKET("basket"),
}