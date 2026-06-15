package com.bocado.repository

import com.bocado.data.DishDao
import com.bocado.data.OrderDao
import com.bocado.data.OrderItemDao
import com.bocado.data.PaymentDao
import com.bocado.data.api.BocadoApi
import com.bocado.model.Dish
import com.bocado.model.Order
import com.bocado.model.OrderItem
import com.bocado.model.OrderRequest
import com.bocado.model.Payment
import com.bocado.model.PaymentRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class DishRepository(
    private val dishDao: DishDao,
    private val apiService: BocadoApi
) {
    fun getAllDishes(): Flow<List<Dish>> = flow {
        try {
            // Intentar obtener del API remoto
            val response = apiService.getAllDishes()
            if (response.success && response.data != null) {
                // Guardar en BD local
                dishDao.insertMultipleDishes(response.data)
                emit(response.data)
            }
        } catch (e: Exception) {
            // Fallback a BD local en caso de error
            dishDao.getAllDishes().collect { emit(it) }
        }
    }.catch { exception ->
        // Si todo falla, emitir vacío
        emit(emptyList())
    }

    fun getDishesByCategory(category: String): Flow<List<Dish>> = flow {
        try {
            val response = apiService.getDishesByCategory(category)
            if (response.success && response.data != null) {
                emit(response.data)
            }
        } catch (e: Exception) {
            dishDao.getDishesByCategory(category).collect { emit(it) }
        }
    }

    suspend fun getDishById(id: Int): Dish? {
        return try {
            val response = apiService.getDishById(id)
            response.data
        } catch (e: Exception) {
            dishDao.getDishById(id)
        }
    }

    suspend fun decreaseStock(dishId: Int, quantity: Int) {
        dishDao.decreaseStock(dishId, quantity)
    }
}

class OrderRepository(
    private val orderDao: OrderDao,
    private val orderItemDao: OrderItemDao,
    private val apiService: BocadoApi
) {
    fun getAllOrders(): Flow<List<Order>> = orderDao.getAllOrders()

    fun getOrdersByStatus(status: String): Flow<List<Order>> = orderDao.getOrdersByStatus(status)

    suspend fun getOrderById(id: Int): Order? = orderDao.getOrderById(id)

    suspend fun createOrder(orderRequest: OrderRequest): Result<Order> = try {
        val response = apiService.createOrder(orderRequest)
        if (response.success && response.data != null) {
            orderDao.insertOrder(response.data)
            Result.success(response.data)
        } else {
            Result.failure(Exception(response.error ?: "Error desconocido"))
        }
    } catch (e: Exception) {
        Result.failure(e)
    }

    suspend fun updateOrderStatus(orderId: Int, status: String): Result<Order> = try {
        val response = apiService.updateOrderStatus(orderId, status)
        if (response.success && response.data != null) {
            orderDao.updateOrder(response.data)
            Result.success(response.data)
        } else {
            Result.failure(Exception(response.error ?: "Error desconocido"))
        }
    } catch (e: Exception) {
        Result.failure(e)
    }

    suspend fun addOrderLocally(order: Order): Long = orderDao.insertOrder(order)

    suspend fun updateOrder(order: Order) = orderDao.updateOrder(order)
}

class PaymentRepository(
    private val paymentDao: PaymentDao,
    private val apiService: BocadoApi
) {
    fun getAllPayments(): Flow<List<Payment>> = paymentDao.getAllPayments()

    suspend fun getPaymentByOrderId(orderId: Int): Payment? {
        return try {
            val response = apiService.getPaymentByOrderId(orderId)
            response.data
        } catch (e: Exception) {
            paymentDao.getPaymentByOrderId(orderId)
        }
    }

    suspend fun processPayment(paymentRequest: PaymentRequest): Result<Payment> = try {
        val response = apiService.processPayment(paymentRequest)
        if (response.success && response.data != null) {
            paymentDao.insertPayment(response.data)
            Result.success(response.data)
        } else {
            Result.failure(Exception(response.error ?: "Error en el procesamiento del pago"))
        }
    } catch (e: Exception) {
        Result.failure(e)
    }

    suspend fun updatePaymentStatus(orderId: Int, status: String) {
        paymentDao.updatePaymentStatus(orderId, status)
    }
}
