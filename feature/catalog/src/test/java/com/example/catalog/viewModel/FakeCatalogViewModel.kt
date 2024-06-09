package com.example.catalog.viewModel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.catalog.CategoriesUiState
import com.example.catalog.ProductsUiState
import com.example.catalog.TagUiState
import com.example.data.repository.NetworkRepository
import com.example.model.Category
import com.example.model.Product
import com.example.model.Tag
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.mockito.kotlin.mock

class FakeCatalogViewModel: ViewModel() {

    private val networkRepository: NetworkRepository = mock<NetworkRepository>()

    private val _currentCategory = mutableStateOf<Category?>(null)
    val currentCategory = _currentCategory

    private val _categoriesUiState: MutableStateFlow<CategoriesUiState> =
        MutableStateFlow(CategoriesUiState.Loading)
    val categoriesUiState = _categoriesUiState.asStateFlow()

    private val _tagsUiState: MutableStateFlow<TagUiState> =
        MutableStateFlow(TagUiState.Loading)
    val tagsUiState = _tagsUiState.asStateFlow()

    //  состояния для чекбоксов
    private val _mapWithCheckedState = MutableStateFlow( mapOf<Int, Boolean>() )
    val mapWithCheckedState = _mapWithCheckedState.asStateFlow()

    private val fakeCard = mapOf(Product(
        id = 1, category_id = 676168,
        name = "Авокадо Кранч Маки 8шт",
        description = "Ролл с нежным мясом камчатского краба, копченой курицей и авокадо.Украшается соусом\"Унаги\" и легким майонезом  Комплектуется бесплатным набором для роллов (Соевый соус Лайт 35г., васаби 6г., имбирь 15г.). +1 набор за каждые 600 рублей в заказе",
        image = "1.jpg", price_current = 47000,
        price_old = null, measure = 250, measure_unit = "г",
        energy_per_100_grams = 307.8, proteins_per_100_grams = 6.1,
        fats_per_100_grams = 11.4, carbohydrates_per_100_grams = 45.1,
        tag_ids = emptyList()) to 0, Product() to 1, Product() to 2, Product() to 3, Product() to 4)

    val productsUiState = networkRepository.getProducts().map { result ->
        result.fold(
            onSuccess = {
                ProductsUiState.Success(fakeCard)
            },
            onFailure = { ProductsUiState.Success(fakeCard) /*ProductsUiState.Error(it)*/ }
        )
    }.stateIn(
        scope = TestScope(),
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = ProductsUiState.Loading
    )

    private val fakeCategories = listOf(
        Category(1,"Суши"),Category(2,"Соусы"),
        Category(3,"Десерты"),Category(4,"Напитки"))
    fun getCategories() {
        runTest {
            networkRepository.getCategories()
                .onSuccess {
                    _categoriesUiState.value = CategoriesUiState.Success(fakeCategories)
                }
                .onFailure {
                    _categoriesUiState.value = CategoriesUiState.Success(fakeCategories)
                }
        }
    }

    private val fakeTags = listOf(
        Tag(1,"Орехи"),Tag(2,"Ягоды"),
        Tag(3,"Овощи"),Tag(4,"Грибы"))
    fun getTags() {
        runTest {
            networkRepository.getTags()
                .onSuccess {
                    _tagsUiState.value = if (fakeTags.isEmpty()) {
                        TagUiState.Empty
                    } else {
                        // создаем мапу со всеми айди и даем по умолчанию всем false,
                        // т.е. изначально чекбоксы не будут выбраны
                        val map = mutableMapOf<Int, Boolean>()
                        fakeTags.map { tag -> tag.id }.forEach { int -> map[int] = false }
                        _mapWithCheckedState.value = map.toMap()

                        TagUiState.Success(fakeTags)
                    }
                }
                .onFailure {
                    _tagsUiState.value = TagUiState.Error
                }
        }
    }
    fun addOrRemoveTagId(id: Int) {
        if (_mapWithCheckedState.value.contains(id)) {
            val map = _mapWithCheckedState.value.toMutableMap()
            map[id] = !map[id]!!
            _mapWithCheckedState.value = map
        }
    }

    fun updateCurrentCategory(category: Category) {
        if (_currentCategory.value == category) {
            _currentCategory.value = null
        } else {
            _currentCategory.value = category
        }
    }

// чтобы затестить эти 2 функции надо создать fakeRuntimeDataSource
//    fun addProductInCart(product: Product) {
//        viewModelScope.launch {
//            networkRepository.addProductInCart(product)
//        }
//    }
//
//    fun removeProductFromCart(product: Product) {
//        viewModelScope.launch {
//            networkRepository.removeProductFromCart(product)
//        }
//    }
}
