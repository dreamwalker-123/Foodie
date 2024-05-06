package com.example.catalog

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.repository.NetworkRepository
import com.example.foodie_api.model.Category
import com.example.foodie_api.model.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CatalogViewModel @Inject constructor(private val networkRepository: NetworkRepository) : ViewModel() {
    private val _currentCategory = mutableStateOf<Category?>(null)
    val currentCategory = _currentCategory

    private val _categoriesUiState: MutableStateFlow<CategoriesUiState> =
        MutableStateFlow(CategoriesUiState.Loading)
    val categoriesUiState = _categoriesUiState.asStateFlow()

    private val _productsUiState: MutableStateFlow<ProductsUiState> =
        MutableStateFlow(ProductsUiState.Loading)
    val productsUiState = _productsUiState.asStateFlow()

    private val _tagsUiState: MutableStateFlow<TagUiState> =
        MutableStateFlow(TagUiState.Loading)
    val tagsUiState = _productsUiState.asStateFlow()

    init {
        viewModelScope.launch {
            getProducts()
            getCategories()
            getTags()
        }
    }

    fun getProducts() {
        viewModelScope.launch {
            networkRepository.getProducts()
                .onSuccess {
                    _productsUiState.value = if (it.isEmpty()) {
                        ProductsUiState.Empty
                    } else {
                        ProductsUiState.Success(it)
                    }
                }
                .onFailure {
                    _productsUiState.value = ProductsUiState.Error(it)
                }
        }
    }

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

    fun updateCurrentCategory(category: Category) {
        if (_currentCategory.value != category) {
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