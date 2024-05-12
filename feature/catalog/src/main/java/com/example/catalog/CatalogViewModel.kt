package com.example.catalog

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.repository.NetworkRepository
import com.example.network.model.Category
import com.example.network.model.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CatalogViewModel @Inject constructor(
    private val networkRepository: NetworkRepository
) : ViewModel() {
    private val _currentCategory = mutableStateOf<com.example.network.model.Category?>(null)
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
        }
    }

    fun getProducts() {
        viewModelScope.launch {
            networkRepository.getProducts().map { result ->
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

    fun updateCurrentCategory(category: com.example.network.model.Category) {
        if (_currentCategory.value != category) {
            _currentCategory.value = category
        }
    }

    fun addProductInCart(product: com.example.network.model.Product) {
        viewModelScope.launch {
            networkRepository.addProductInCart(product)
        }
    }

    fun removeProductFromCart(product: com.example.network.model.Product) {
        viewModelScope.launch {
            networkRepository.removeProductFromCart(product)
        }
    }
}