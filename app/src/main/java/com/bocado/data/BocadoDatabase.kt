package com.bocado.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.bocado.model.Dish
import com.bocado.model.Order
import com.bocado.model.OrderItem
import com.bocado.model.Payment
import com.bocado.model.Restaurant
import com.bocado.model.User

@Database(
    entities = [Dish::class, Order::class, OrderItem::class, Payment::class, Restaurant::class, User::class],
    version = 2, // <-- CAMBIAMOS A 2
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class BocadoDatabase : RoomDatabase() {
    abstract fun dishDao(): DishDao
    abstract fun orderDao(): OrderDao
    abstract fun orderItemDao(): OrderItemDao
    abstract fun paymentDao(): PaymentDao
    abstract fun restaurantDao(): RestaurantDao
    abstract fun userDao(): UserDao // <-- AGREGAMOS ESTO

    companion object {
        // Singleton instance will be created in MainActivity
    }
}

