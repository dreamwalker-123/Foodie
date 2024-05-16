package com.example.card_product

import com.example.model.Product

interface CardProductUiState {
    data class Success(val productWithQuantity: Pair<Product, Int>) : CardProductUiState
    data object Empty : CardProductUiState
    data object Loading : CardProductUiState
    data class Error(val error: Throwable) : CardProductUiState
}