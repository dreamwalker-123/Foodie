package com.example.catalog

import com.example.network.model.Category

interface CategoriesUiState {
    data class Success(val categories: List<com.example.network.model.Category>) : CategoriesUiState
    data object Empty : CategoriesUiState
    data object Error : CategoriesUiState
    data object Loading : CategoriesUiState
}