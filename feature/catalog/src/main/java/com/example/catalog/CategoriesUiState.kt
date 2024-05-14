package com.example.catalog

import com.example.model.Category

interface CategoriesUiState {
    data class Success(val categories: List<Category>) : CategoriesUiState
    data object Empty : CategoriesUiState
    data object Error : CategoriesUiState
    data object Loading : CategoriesUiState
}