package com.bocado.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.bocado.model.Dish
import com.bocado.model.Order
import com.bocado.model.OrderItem
import com.bocado.model.Payment
import com.bocado.model.Restaurant

@Database(
    entities = [Dish::class, Order::class, OrderItem::class, Payment::class, Restaurant::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class BocadoDatabase : RoomDatabase() {
    abstract fun dishDao(): DishDao
    abstract fun orderDao(): OrderDao
    abstract fun orderItemDao(): OrderItemDao
    abstract fun paymentDao(): PaymentDao
    abstract fun restaurantDao(): RestaurantDao

    companion object {
        // Singleton instance will be created in MainActivity
    }
}
