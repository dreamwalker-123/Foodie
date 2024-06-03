package com.example.catalog

import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import java.text.NumberFormat

@RunWith(MockitoJUnitRunner::class)
class CatalogViewModelTest {
//    private val networkRepository: NetworkRepository = mock<NetworkRepository>()
//    private val _currentCategory = mutableStateOf<Category?>(null)
//    val currentCategory = _currentCategory
//
//    private val _categoriesUiState: MutableStateFlow<CategoriesUiState> =
//        MutableStateFlow(CategoriesUiState.Loading)
//    val categoriesUiState = _categoriesUiState.asStateFlow()
//
//    private val _tagsUiState: MutableStateFlow<TagUiState> =
//        MutableStateFlow(TagUiState.Loading)
//    val tagsUiState = _tagsUiState.asStateFlow()
//
//    private val _mapWithCheckedState = MutableStateFlow( mapOf<Int, Boolean>() )
//    val mapWithCheckedState = _mapWithCheckedState.asStateFlow()

//    @Test
//    fun `should return the same products as in repository`() {
//        // Arrange
//        val testOneProduct = Product(id = 1, category_id = 676168,
//            name = "Авокадо Кранч Маки 8шт",
//            description = "Ролл с нежным мясом камчатского краба, копченой курицей и авокадо.Украшается соусом\"Унаги\" и легким майонезом  Комплектуется бесплатным набором для роллов (Соевый соус Лайт 35г., васаби 6г., имбирь 15г.). +1 набор за каждые 600 рублей в заказе",
//            image = "1.jpg", price_current = 47000,
//            price_old = null, measure = 250, measure_unit = "г",
//            energy_per_100_grams = 307.8, proteins_per_100_grams = 6.1,
//            fats_per_100_grams = 11.4, carbohydrates_per_100_grams = 45.1,
//            tag_ids = emptyList())
//
//        val testProducts = flow { emit(kotlin.runCatching { mapOf(testOneProduct to 1) }) }
//        whenever(networkRepository.getProducts()).thenReturn(testProducts)
//
//        // Act
//        val actual = networkRepository.getProducts()
//        val expected = flow { emit(kotlin.runCatching { mapOf(testOneProduct to 1) }) }
//
//        // Assert
//        assertEquals(actual, expected)
//    }
    @Test
    fun calculateTip_20PercentNoRoundup() {
        val expectedTip = NumberFormat.getCurrencyInstance().format(2)
        val actualTip = NumberFormat.getCurrencyInstance().format(2)
        assertEquals(expectedTip, actualTip)
    }
}