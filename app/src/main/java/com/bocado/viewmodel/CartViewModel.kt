package com.bocado.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bocado.model.Order
import com.bocado.model.OrderItem
import com.bocado.model.OrderRequest
import com.bocado.model.OrderItemRequest
import com.bocado.repository.DishRepository
import com.bocado.repository.OrderRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class CartUiState(
    val cartItems: List<OrderItem> = emptyList(),
    val totalAmount: Double = 0.0,
    val isLoading: Boolean = false,
    val error: String? = null,
    val orderConfirmed: Boolean = false,
    val currentOrder: Order? = null
)

class CartViewModel(
    private val orderRepository: OrderRepository,
    private val dishRepository: DishRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(CartUiState())
    val uiState: StateFlow<CartUiState> = _uiState.asStateFlow()

    fun addItemToCart(dishId: Int, dishName: String, unitPrice: Double, quantity: Int = 1) {
        val currentItems = _uiState.value.cartItems.toMutableList()
        val existingItem = currentItems.find { it.dishId == dishId }

        if (existingItem != null) {
            val index = currentItems.indexOf(existingItem)
            currentItems[index] = existingItem.copy(
                quantity = existingItem.quantity + quantity,
                subtotal = (existingItem.quantity + quantity) * unitPrice
            )
        } else {
            currentItems.add(
                OrderItem(
                    dishId = dishId,
                    dishName = dishName,
                    quantity = quantity,
                    unitPrice = unitPrice,
                    subtotal = quantity * unitPrice
                )
            )
        }

        updateCart(currentItems)
    }

    fun removeItemFromCart(itemId: Int) {
        val currentItems = _uiState.value.cartItems.toMutableList()
        currentItems.removeAll { it.id == itemId }
        updateCart(currentItems)
    }

    fun updateItemQuantity(itemId: Int, newQuantity: Int) {
        if (newQuantity <= 0) {
            removeItemFromCart(itemId)
            return
        }

        val currentItems = _uiState.value.cartItems.toMutableList()
        val item = currentItems.find { it.id == itemId }
        if (item != null) {
            val index = currentItems.indexOf(item)
            currentItems[index] = item.copy(
                quantity = newQuantity,
                subtotal = newQuantity * item.unitPrice
            )
            updateCart(currentItems)
        }
    }

    private fun updateCart(items: List<OrderItem>) {
        val total = items.sumOf { it.subtotal }
        _uiState.value = _uiState.value.copy(
            cartItems = items,
            totalAmount = total
        )
    }

    fun clearCart() {
        _uiState.value = _uiState.value.copy(
            cartItems = emptyList(),
            totalAmount = 0.0,
            orderConfirmed = false
        )
    }

    fun confirmOrder(clientName: String, tableNumber: Int) {
        if (_uiState.value.cartItems.isEmpty()) {
            _uiState.value = _uiState.value.copy(
                error = "El carrito está vacío"
            )
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                val orderItems = _uiState.value.cartItems.map {
                    OrderItemRequest(it.dishId, it.quantity)
                }

                val orderRequest = OrderRequest(
                    clientName = clientName,
                    tableNumber = tableNumber,
                    items = orderItems
                )

                val result = orderRepository.createOrder(orderRequest)
                result.onSuccess { order ->
                    _uiState.value = _uiState.value.copy(
                        orderConfirmed = true,
                        currentOrder = order,
                        isLoading = false
                    )
                    // Decrementar stock
                    _uiState.value.cartItems.forEach { item ->
                        dishRepository.decreaseStock(item.dishId, item.quantity)
                    }
                }.onFailure { error ->
                    _uiState.value = _uiState.value.copy(
                        error = error.message ?: "Error al confirmar pedido",
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = e.message ?: "Error desconocido",
                    isLoading = false
                )
            }
        }
    }

    fun getOrderTotal(): Double = _uiState.value.totalAmount
}
