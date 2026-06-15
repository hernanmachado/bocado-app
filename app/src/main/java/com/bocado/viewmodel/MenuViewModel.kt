package com.bocado.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bocado.model.Dish
import com.bocado.repository.DishRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class MenuUiState(
    val dishes: List<Dish> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val selectedCategory: String = "Todos"
)

class MenuViewModel(private val dishRepository: DishRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(MenuUiState())
    val uiState: StateFlow<MenuUiState> = _uiState.asStateFlow()

    init {
        loadDishes()
    }

    fun loadDishes() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                dishRepository.getAllDishes().collect { dishes ->
                    _uiState.value = _uiState.value.copy(
                        dishes = dishes,
                        isLoading = false,
                        error = null
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Error desconocido"
                )
            }
        }
    }

    fun filterByCategory(category: String) {
        _uiState.value = _uiState.value.copy(selectedCategory = category)
        viewModelScope.launch {
            try {
                if (category == "Todos") {
                    loadDishes()
                } else {
                    dishRepository.getDishesByCategory(category).collect { dishes ->
                        _uiState.value = _uiState.value.copy(
                            dishes = dishes,
                            selectedCategory = category
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = e.message ?: "Error al filtrar"
                )
            }
        }
    }
}
