package com.example.catalog

import com.example.foodie_api.model.Product

interface ProductsUiState {
    data class Success(val product: List<Product>): ProductsUiState
    data object Empty : ProductsUiState
    data class Error(val error: Throwable) : ProductsUiState
    data object Loading : ProductsUiState
}