package com.example.catalog

import com.example.network.model.Tag

interface TagUiState {
    data class Success(val tags: List<com.example.network.model.Tag>) : TagUiState
    data object Empty : TagUiState
    data object Error : TagUiState
    data object Loading : TagUiState
}