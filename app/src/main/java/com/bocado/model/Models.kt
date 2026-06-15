package com.bocado.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// Entidad Dish (Plato)
@Entity(tableName = "dishes")
data class Dish(
    @PrimaryKey val id: Int,
    val name: String,
    val description: String,
    val price: Double,
    val stock: Int,
    val imageUrl: String,
    val category: String,
    val isAvailable: Boolean = true,
    @ColumnInfo(name = "created_at")
    @SerializedName("created_at")
    val createdAt: String = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault()).format(Date())
)

// Entidad OrderItem (Item del pedido)
@Entity(tableName = "order_items")
data class OrderItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val dishId: Int,
    val dishName: String,
    val quantity: Int,
    val unitPrice: Double,
    val subtotal: Double = quantity * unitPrice
)

// Entidad Order (Pedido)
@Entity(tableName = "orders")
data class Order(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val clientName: String? = null,
    val tableNumber: Int = 0,
    val totalAmount: Double = 0.0,
    val status: String = "PENDING",
    val paymentMethod: String? = null,
    val items: List<OrderItem> = emptyList(),
    @ColumnInfo(name = "created_at")
    @SerializedName("created_at")
    val createdAt: String? = null,
    val qrCode: String? = null
)

// Entidad Payment (Pago)
@Entity(tableName = "payments")
data class Payment(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val orderId: Int,
    val amount: Double,
    val paymentMethod: String, // CREDIT_CARD, DEBIT_CARD, CASH, WALLET
    val status: String = "PENDING", // PENDING, APPROVED, REJECTED
    val transactionId: String? = null,
    @ColumnInfo(name = "created_at")
    @SerializedName("created_at")
    val createdAt: String = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault()).format(Date())
)

// Entidad Restaurant (Restaurante)
@Entity(tableName = "restaurants")
data class Restaurant(
    @PrimaryKey val id: Int,
    val name: String,
    val location: String,
    val phone: String,
    val email: String,
    val logoUrl: String
)

// Data Transfer Objects (DTOs)
data class OrderRequest(
    val clientName: String,
    val tableNumber: Int,
    val items: List<OrderItemRequest>
)

data class OrderItemRequest(
    val dishId: Int,
    val quantity: Int
)

data class PaymentRequest(
    val orderId: Int,
    val amount: Double,
    val paymentMethod: String,
    val cardDetails: CardDetails? = null
)

data class CardDetails(
    val cardNumber: String,
    val expiryDate: String,
    val cvv: String,
    val holderName: String
)

data class ApiResponse<T>(
    val success: Boolean,
    val data: T? = null,
    val message: String? = null,
    val error: String? = null
)
