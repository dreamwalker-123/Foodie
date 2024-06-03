package com.example.catalog

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeDown
import androidx.compose.ui.test.swipeLeft
import androidx.compose.ui.test.swipeRight
import androidx.compose.ui.test.swipeUp
import androidx.test.platform.app.InstrumentationRegistry
import com.example.model.Category
import com.example.model.Product
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class CatalogScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var dataForTests: UserData

    @Before
    fun createLogHistory() {
        dataForTests = UserData(
            listWithCategories = listOf(
                Category(id = 1, name = "Роллы"),
                Category(name = "Суши"),
                Category(name = "Наборы"),
                Category(name = "Горячие блюда"),
                Category(name = "Супы"),
                Category(name = "Десерты"),
            ),
            listWithProducts = mapOf(
                Product(
                    name = "Том Ям",
                    category_id = 1,
                    price_current = 72000,
                    price_old = 80000,
                    measure = 500,
                    measure_unit = "г",
                    tag_ids = listOf(1)
                ) to 2,
                *List(5) {
                    Product(
                        name = "Название блюда $it",
                        category_id = 1,
                        price_current = 48000,
                        measure = 500,
                        measure_unit = "г",
                        tag_ids = listOf(2)
                    ) to 0
                }.toTypedArray()
            )
        )
    }

    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.example.catalog.test", appContext.packageName)
    }

    @Test
    fun listItemCategories_IsDisplayed_() {
        composeTestRule.setContent {
            CatalogScreen(
                categories = CategoriesUiState.Success(
                    dataForTests.listWithCategories
                ),
            )
        }

        Thread.sleep(3000)

        composeTestRule
            .onNodeWithTag("ListItemCategories")
            .assertIsDisplayed()
            .performTouchInput { swipeRight(durationMillis = 1000) }
//            .assertLeftPositionInRootIsEqualTo()
            .performTouchInput { swipeLeft(durationMillis = 1000) }

        composeTestRule
            .onNodeWithTag("ItemList_or_LazyVerticalGrid")
            .assertIsNotDisplayed()
    }

    @Test
    fun listItemCategories_and_ItemList_IsDisplayed_() {
        composeTestRule.setContent {
            CatalogScreen(
                categories = CategoriesUiState.Success(
                    dataForTests.listWithCategories
                ),
                products = ProductsUiState.Success(
                    dataForTests.listWithProducts
                )
            )
        }

        Thread.sleep(3000)

        composeTestRule
            .onNodeWithTag("ListItemCategories")
            .assertIsDisplayed()
            .performTouchInput { swipeLeft(durationMillis = 2000) }
            .performTouchInput { swipeRight(durationMillis = 2000) }

        composeTestRule
            .onNodeWithTag("ItemList_or_LazyVerticalGrid")
            .assertIsDisplayed()
            .performTouchInput { swipeUp(durationMillis = 2000) }
            .performTouchInput { swipeDown(durationMillis = 2000) }
    }
}

data class UserData(val listWithCategories: List<Category>, val listWithProducts: Map<Product, Int>)