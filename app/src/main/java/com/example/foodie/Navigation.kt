package com.example.foodie

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.lazy.grid.GridCells
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
import com.example.splash.SplashScreen

@Composable
fun Navigation(
    windowSizeClass: WindowWidthSizeClass,
    navController: NavHostController = rememberNavController(),
) {
    NavHost(
        navController = navController,
        startDestination = SPLASH_SCREEN,
        enterTransition = { slideInHorizontally(initialOffsetX = { 1000 }) + fadeIn() },
        exitTransition = { slideOutHorizontally(targetOffsetX = { -1000 }) + fadeOut() },
        popEnterTransition = { slideInHorizontally(initialOffsetX = { -1000 }) + fadeIn() },
        popExitTransition = { slideOutHorizontally(targetOffsetX = { 1000 }) + fadeOut() },
    ) {
        composable(route = SPLASH_SCREEN) {
            SplashScreen(
                popBackStack = { navController.popBackStack() },
                navigate = { navController.navigate(CATALOG_SCREEN) }
            )
        }

        composable(route = CATALOG_SCREEN) {
            CatalogRoute(
                columns = if (windowSizeClass == WindowWidthSizeClass.Expanded) {
                    GridCells.Fixed(4)
                } else {
                    GridCells.Fixed(2)
                },
                // передача аргумента в CARD_PRODUCT_SCREEN и также
                // одновременно происходит запись аргумента в Bundle
                onProductClick = { navController.navigate("$CARD_PRODUCT_SCREEN/${it}") },
                onBasketClick = { navController.navigate(BASKET_SCREEN)}
            )
        }

        composable(route = "$CARD_PRODUCT_SCREEN/{$PRODUCT_ID_ARGUMENT}",
            // запись при получении аргумента в Bundle
            arguments = listOf(navArgument(PRODUCT_ID_ARGUMENT) {
                type = NavType.IntType
                defaultValue = 0
            })
        ) {
            CardProductRoute(
                onUpClick = navController::navigateUp,
                shouldShowExpandedLayout = windowSizeClass == WindowWidthSizeClass.Expanded,
            )
        }

        composable(route = BASKET_SCREEN) {
            BasketRoute(
                onUpClick = navController::navigateUp,
            )
        }
    }
}

internal const val CATALOG_SCREEN = "CatalogScreen"
internal const val BASKET_SCREEN = "BasketScreen"
internal const val SPLASH_SCREEN = "SplashScreen"