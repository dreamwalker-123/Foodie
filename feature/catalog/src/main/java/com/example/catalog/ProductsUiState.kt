package com.example.catalog

import com.example.model.Product

interface ProductsUiState {
    data class Success(val product: Map<Product, Int>): ProductsUiState
    data object Empty : ProductsUiState
    data class Error(val error: Throwable) : ProductsUiState
    data object Loading : ProductsUiState
}