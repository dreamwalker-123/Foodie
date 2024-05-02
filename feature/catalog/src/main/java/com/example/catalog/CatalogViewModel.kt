package com.example.catalog

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodie_api.RetrofitClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CatalogViewModel @Inject constructor(private val retrofitClient: RetrofitClient) : ViewModel() {

    private val _categoriesUiState: MutableStateFlow<CategoriesUiState> =
        MutableStateFlow(CategoriesUiState.Loading)
    val categoriesUiState = _categoriesUiState.asStateFlow()

    var uiState = MutableStateFlow(
        CatalogUiState(
            products = null,
            categories = CategoriesUiState.Loading,
            tags = null,
        )
    )
        private set

    init {
        viewModelScope.launch {
            getProducts()
            getCategories()
            getTags()
        }
    }

    fun getProducts() {
        viewModelScope.launch {
            uiState.value = uiState.value.copy(products = retrofitClient.getProducts())
        }
    }
    fun getCategories() {
        viewModelScope.launch {
            uiState.value = uiState.value.copy(categories = retrofitClient.getCategories())
        }
    }
    fun getTags() {
        viewModelScope.launch {
            uiState.value = uiState.value.copy(tags = retrofitClient.getTags())
        }
    }









//    val uiState: StateFlow<DetailsUiState> = savedStateHandle
//        .getStateFlow<Long?>("id", null)
//        .filterNotNull()
//        .flatMapLatest {  id ->
//            myModelRepository.observeModelById(id)
//        }.map { model ->
//            DetailsUiState.Success(
//                title = model.title,
//                description = model.description
//            )
//        }
//        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), DetailsUiState.Loading)
}