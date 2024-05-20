package com.example.catalog

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.repository.NetworkRepository
import com.example.model.Category
import com.example.model.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CatalogViewModel @Inject constructor(
    private val networkRepository: NetworkRepository
) : ViewModel() {
    private val _currentCategory = mutableStateOf<Category?>(null)
    val currentCategory = _currentCategory

    private val _categoriesUiState: MutableStateFlow<CategoriesUiState> =
        MutableStateFlow(CategoriesUiState.Loading)
    val categoriesUiState = _categoriesUiState.asStateFlow()

    private val _tagsUiState: MutableStateFlow<TagUiState> =
        MutableStateFlow(TagUiState.Loading)
    val tagsUiState = _tagsUiState.asStateFlow()

    private val _listWithIdTags = MutableStateFlow( mutableListOf<Int>() )
    val listWithIdTags = _listWithIdTags.asStateFlow()

    init {
        viewModelScope.launch {
            getTags()
            getCategories()
        }
    }

    val productsUiState = networkRepository.getProducts().map { result ->
        result.fold(
            onSuccess = { cart ->
                if (cart.isNotEmpty()) {
                    ProductsUiState.Success(cart)
                } else {
                    ProductsUiState.Empty
                }
            },
            onFailure = { ProductsUiState.Error(it) }
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = ProductsUiState.Loading
    )

    fun getCategories() {
        viewModelScope.launch {
            networkRepository.getCategories()
                .onSuccess {
                    _categoriesUiState.value = if (it.isEmpty()) {
                        CategoriesUiState.Empty
                    } else {
                        CategoriesUiState.Success(it)
                    }
                }
                .onFailure {
                    _categoriesUiState.value = CategoriesUiState.Error
                }
        }
    }

    fun getTags() {
        viewModelScope.launch {
            networkRepository.getTags()
                .onSuccess {
                    _tagsUiState.value = if (it.isEmpty()) {
                        TagUiState.Empty
                    } else {
                        TagUiState.Success(it)
                    }
                }
                .onFailure {
                    _tagsUiState.value = TagUiState.Error
                }
        }
    }

    fun addOrRemoveTagId(id: Int) {
        if (_listWithIdTags.value.contains(id)) {
            _listWithIdTags.value.remove(id)
        } else _listWithIdTags.value.add(id)
    }

    // добавил сбрасывание категории при нажатии на уже выбранную
    fun updateCurrentCategory(category: Category) {
        if (_currentCategory.value == category) {
            _currentCategory.value = null
        } else {
            _currentCategory.value = category
        }
    }

    fun addProductInCart(product: Product) {
        viewModelScope.launch {
            networkRepository.addProductInCart(product)
        }
    }

    fun removeProductFromCart(product: Product) {
        viewModelScope.launch {
            networkRepository.removeProductFromCart(product)
        }
    }
}