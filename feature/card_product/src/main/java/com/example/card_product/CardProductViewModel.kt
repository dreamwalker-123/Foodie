package com.example.card_product

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.repository.NetworkRepository
import com.example.model.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CardProductViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val productsRepository: NetworkRepository
) : ViewModel() {
    // получение записанного аргумента из Bundle при помощи savedStateHandle
    private val productId: Int = checkNotNull(savedStateHandle[PRODUCT_ID_ARGUMENT])
    val uiState = productsRepository.getProductById(productId).map { result ->
        result.fold(
            onSuccess = { pair ->
                pair?.let { CardProductUiState.Success(it) } ?: CardProductUiState.Empty
            },
            onFailure = { CardProductUiState.Error(it) }
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = CardProductUiState.Loading
    )

    fun addProductInCart(product: Product) {
        viewModelScope.launch {
            productsRepository.addProductInCart(product)
        }
    }

    fun removeProductFromCart(product: Product) {
        viewModelScope.launch {
            productsRepository.removeProductFromCart(product)
        }
    }
}

const val CARD_PRODUCT_SCREEN = "product_route"
const val PRODUCT_ID_ARGUMENT = "product_id"