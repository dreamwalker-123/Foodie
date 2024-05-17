package com.example.foodie

import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.basket.BasketRoute
import com.example.card_product.CARD_PRODUCT_SCREEN
import com.example.card_product.CardProductRoute
import com.example.card_product.PRODUCT_ID_ARGUMENT
import com.example.catalog.CatalogRoute

@Composable
fun Navigation(
    windowSizeClass: WindowSizeClass,
    navController: NavHostController = rememberNavController(),
) {
//    var id by rememberSaveable { mutableStateOf<Int?>(0) }
    NavHost(
        navController = navController,
        startDestination = CATALOG_SCREEN,
    ) {
        composable(
            route = CATALOG_SCREEN,
        ) {
            CatalogRoute(
                columns = if (windowSizeClass.widthSizeClass == WindowWidthSizeClass.Expanded) {
                    GridCells.Fixed(4)
                } else {
                    GridCells.Fixed(2)
                },
                // передача аргумента и также одновременно происходит запись аргумента в Bundle
                onProductClick = { navController.navigate("$CARD_PRODUCT_SCREEN/${it}") },
                onBasketClick = { navController.navigate(BASKET_SCREEN)}
            )
        }

        composable(
            route = "$CARD_PRODUCT_SCREEN/{$PRODUCT_ID_ARGUMENT}",
            // запись при получении аргумента в Bundle
            arguments = listOf(navArgument(PRODUCT_ID_ARGUMENT) {
                type = NavType.IntType
                defaultValue = 0
            })
        ) {
            CardProductRoute(
                onUpClick = navController::navigateUp,
                shouldShowExpandedLayout = windowSizeClass.widthSizeClass == WindowWidthSizeClass.Expanded,
            )
        }

        composable(BASKET_SCREEN) {
            BasketRoute(
                onUpClick = navController::navigateUp,
            )
        }
    }
}

private const val CATALOG_SCREEN = "CatalogScreen"
private const val BASKET_SCREEN = "BasketScreen"