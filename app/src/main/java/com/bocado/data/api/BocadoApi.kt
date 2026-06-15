package com.bocado.data.api

import com.bocado.model.ApiResponse
import com.bocado.model.Dish
import com.bocado.model.Order
import com.bocado.model.OrderRequest
import com.bocado.model.Payment
import com.bocado.model.PaymentRequest
import com.bocado.model.Restaurant
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface BocadoApi {
    // Dishes Endpoints
    @GET("api/v1/dishes")
    suspend fun getAllDishes(): ApiResponse<List<Dish>>

    @GET("api/v1/dishes/{id}")
    suspend fun getDishById(@Path("id") id: Int): ApiResponse<Dish>

    @GET("api/v1/dishes/category/{category}")
    suspend fun getDishesByCategory(@Path("category") category: String): ApiResponse<List<Dish>>

    // Orders Endpoints
    @POST("api/v1/orders")
    suspend fun createOrder(@Body orderRequest: OrderRequest): ApiResponse<Order>

    @GET("api/v1/orders/{id}")
    suspend fun getOrderById(@Path("id") id: Int): ApiResponse<Order>

    @GET("api/v1/orders")
    suspend fun getAllOrders(): ApiResponse<List<Order>>

    @PUT("api/v1/orders/{id}")
    suspend fun updateOrder(
        @Path("id") id: Int,
        @Body order: Order
    ): ApiResponse<Order>

    @PUT("api/v1/orders/{id}/status/{status}")
    suspend fun updateOrderStatus(
        @Path("id") id: Int,
        @Path("status") status: String
    ): ApiResponse<Order>

    // Payments Endpoints
    @POST("api/v1/payments")
    suspend fun processPayment(@Body paymentRequest: PaymentRequest): ApiResponse<Payment>

    @GET("api/v1/payments/{orderId}")
    suspend fun getPaymentByOrderId(@Path("orderId") orderId: Int): ApiResponse<Payment>

    // Restaurants Endpoints
    @GET("api/v1/restaurants/{qrCode}")
    suspend fun getRestaurantByQRCode(@Path("qrCode") qrCode: String): ApiResponse<Restaurant>

    @GET("api/v1/restaurants")
    suspend fun getAllRestaurants(): ApiResponse<List<Restaurant>>
}
