package com.example.catalog.viewModel

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.example.catalog.CategoriesUiState
import com.example.catalog.ProductsUiState
import com.example.catalog.TagUiState
import com.example.model.Category
import com.example.model.Product
import com.example.model.Tag
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import java.text.NumberFormat

@RunWith(MockitoJUnitRunner::class)
class CatalogViewModelTest {
    private val fakeCatalogViewModel: FakeCatalogViewModel = FakeCatalogViewModel()

    // Arrange
    val testOneProduct = Product(
        id = 1, category_id = 676168,
        name = "Авокадо Кранч Маки 8шт",
        description = "Ролл с нежным мясом камчатского краба, копченой курицей и авокадо.Украшается соусом\"Унаги\" и легким майонезом  Комплектуется бесплатным набором для роллов (Соевый соус Лайт 35г., васаби 6г., имбирь 15г.). +1 набор за каждые 600 рублей в заказе",
        image = "1.jpg", price_current = 47000,
        price_old = null, measure = 250, measure_unit = "г",
        energy_per_100_grams = 307.8, proteins_per_100_grams = 6.1,
        fats_per_100_grams = 11.4, carbohydrates_per_100_grams = 45.1,
        tag_ids = emptyList())
    @Test
    fun `should return null in currentCategory`() = runTest {

        // Arrange
        val actual = fakeCatalogViewModel.currentCategory.value

        // Assert
        assertEquals(null, actual)
    }

    @Test
    fun `currentCategory variable change is correct`() {
        // Act
        fakeCatalogViewModel.updateCurrentCategory(Category(id = 123, name = "Напитки"))

        // Assert
        assertEquals(Category(id = 123, name = "Напитки"), fakeCatalogViewModel.currentCategory.value)
    }

    @Test
    fun `getCategories is correct`() {
        val expected = listOf(
            Category(1,"Суши"),Category(2,"Соусы"),
            Category(3,"Десерты"),Category(4,"Напитки"))
        // изменение категории и проверка, соответствует ли новое установленное значение ожидаемому
        fakeCatalogViewModel.getCategories()
        val actualState = fakeCatalogViewModel.categoriesUiState.value
        var actualCategories = listOf(Category())
        if (actualState is CategoriesUiState.Success) {
            actualCategories = actualState.categories
        }
        assertEquals(expected, actualCategories)
    }

    @Test
    fun `getTags is correct`() {
        val expected = listOf(
            Tag(1, "Орехи"), Tag(2, "Ягоды"),
            Tag(3, "Овощи"), Tag(4, "Грибы")
        )
        // изменение tags и проверка, соответствует ли новое установленное значение ожидаемому
        fakeCatalogViewModel.getTags()
        val actualState = fakeCatalogViewModel.tagsUiState.value
        var actualTags = listOf(Tag())
        if (actualState is TagUiState.Success) {
            actualTags = actualState.tags
        }
        assertEquals(expected, actualTags)
    }

    @Test
    fun `mapWithCheckedState should be is empty`() {
        val actual = fakeCatalogViewModel.mapWithCheckedState.value
        assertEquals(mapOf<Int, Boolean>(), actual)
    }

    @Test
    fun `addOrRemoveTagId is correct`() {
        // получили теги
        fakeCatalogViewModel.getTags()
        // изменяем значение тега с id "1" на true (изначально false)
        fakeCatalogViewModel.addOrRemoveTagId(1)
        val expected = true
        val actual = fakeCatalogViewModel.mapWithCheckedState.value[1]
        assertEquals(expected, actual)
    }
}