package com.bocado.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.bocado.model.Dish
import com.bocado.model.Order
import com.bocado.model.OrderItem
import com.bocado.model.Payment
import com.bocado.model.Restaurant
import com.bocado.model.User
import kotlinx.coroutines.flow.Flow

// Dish DAO
@Dao
interface DishDao {
    @Query("SELECT * FROM dishes")
    fun getAllDishes(): Flow<List<Dish>>

    @Query("SELECT * FROM dishes WHERE id = :id")
    suspend fun getDishById(id: Int): Dish?

    @Query("SELECT * FROM dishes WHERE category = :category")
    fun getDishesByCategory(category: String): Flow<List<Dish>>

    @Insert
    suspend fun insertDish(dish: Dish)

    @Insert
    suspend fun insertMultipleDishes(dishes: List<Dish>)

    @Update
    suspend fun updateDish(dish: Dish)

    @Delete
    suspend fun deleteDish(dish: Dish)

    @Query("UPDATE dishes SET stock = stock - :quantity WHERE id = :id AND stock >= :quantity")
    suspend fun decreaseStock(id: Int, quantity: Int)

    @Query("SELECT COUNT(*) FROM dishes WHERE stock > 0")
    suspend fun getAvailableCount(): Int
}

// Order DAO
@Dao
interface OrderDao {
    @Query("SELECT * FROM orders ORDER BY created_at DESC")
    fun getAllOrders(): Flow<List<Order>>

    @Query("SELECT * FROM orders WHERE id = :id")
    suspend fun getOrderById(id: Int): Order?

    @Query("SELECT * FROM orders WHERE status = :status ORDER BY created_at DESC")
    fun getOrdersByStatus(status: String): Flow<List<Order>>

    @Insert
    suspend fun insertOrder(order: Order): Long

    @Update
    suspend fun updateOrder(order: Order)

    @Delete
    suspend fun deleteOrder(order: Order)

    @Query("UPDATE orders SET status = :status WHERE id = :id")
    suspend fun updateOrderStatus(id: Int, status: String)
}

// OrderItem DAO
@Dao
interface OrderItemDao {
    @Query("SELECT * FROM order_items")
    fun getAllOrderItems(): Flow<List<OrderItem>>

    @Query("SELECT * FROM order_items WHERE id = :id")
    suspend fun getOrderItemById(id: Int): OrderItem?

    @Insert
    suspend fun insertOrderItem(item: OrderItem): Long

    @Insert
    suspend fun insertMultipleItems(items: List<OrderItem>)

    @Update
    suspend fun updateOrderItem(item: OrderItem)

    @Delete
    suspend fun deleteOrderItem(item: OrderItem)
}

// Payment DAO
@Dao
interface PaymentDao {
    @Query("SELECT * FROM payments")
    fun getAllPayments(): Flow<List<Payment>>

    @Query("SELECT * FROM payments WHERE orderId = :orderId")
    suspend fun getPaymentByOrderId(orderId: Int): Payment?

    @Insert
    suspend fun insertPayment(payment: Payment): Long

    @Update
    suspend fun updatePayment(payment: Payment)

    @Delete
    suspend fun deletePayment(payment: Payment)

    @Query("UPDATE payments SET status = :status WHERE orderId = :orderId")
    suspend fun updatePaymentStatus(orderId: Int, status: String)
}

// Restaurant DAO
@Dao
interface RestaurantDao {
    @Query("SELECT * FROM restaurants")
    fun getAllRestaurants(): Flow<List<Restaurant>>

    @Query("SELECT * FROM restaurants WHERE id = :id")
    suspend fun getRestaurantById(id: Int): Restaurant?

    @Insert
    suspend fun insertRestaurant(restaurant: Restaurant)

    @Update
    suspend fun updateRestaurant(restaurant: Restaurant)

    @Delete
    suspend fun deleteRestaurant(restaurant: Restaurant)
}

// User DAO
@Dao
interface UserDao {
    @Insert
    suspend fun insertUser(user: User): Long

    @Query("SELECT * FROM users WHERE username = :username AND password = :password LIMIT 1")
    suspend fun login(username: String, password: String): User?

    @Query("SELECT * FROM users WHERE username = :username LIMIT 1")
    suspend fun getUserByUsername(username: String): User?
}

