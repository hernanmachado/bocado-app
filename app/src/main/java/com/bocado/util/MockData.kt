package com.bocado.util

import com.bocado.model.Dish
import com.bocado.model.Order
import com.bocado.model.OrderItem
import com.bocado.model.Payment
import java.time.LocalDateTime

object MockData {
    // Mock Dishes
    val mockDishes = listOf(
        Dish(
            id = 1,
            name = "Pizza Margherita",
            description = "Pizza clásica con tomate, mozzarella y albahaca",
            price = 320.0,
            stock = 15,
            imageUrl = "https://example.com/pizza_margherita.jpg",
            category = "Platos Principales"
        ),
        Dish(
            id = 2,
            name = "Hamburguesa Premium",
            description = "Carne 100% angus con queso cheddar y cebolla caramelizada",
            price = 380.0,
            stock = 20,
            imageUrl = "https://example.com/burger.jpg",
            category = "Platos Principales"
        ),
        Dish(
            id = 3,
            name = "Ensalada César",
            description = "Lechuga romana, parmesano, croutons y salsa César",
            price = 280.0,
            stock = 25,
            imageUrl = "https://example.com/salad.jpg",
            category = "Entradas"
        ),
        Dish(
            id = 4,
            name = "Tabla de Quesos y Embutidos",
            description = "Selección de quesos argentinos e ibéricos con jamón serrano",
            price = 450.0,
            stock = 10,
            imageUrl = "https://example.com/tabla.jpg",
            category = "Entradas"
        ),
        Dish(
            id = 5,
            name = "Pasta a la Bolognesa",
            description = "Tallarín casero con ragú de carne y tomate",
            price = 340.0,
            stock = 18,
            imageUrl = "https://example.com/pasta.jpg",
            category = "Platos Principales"
        ),
        Dish(
            id = 6,
            name = "Salmón a la Mantequilla",
            description = "Filete fresco con limón, alcaparras y espárragos",
            price = 520.0,
            stock = 8,
            imageUrl = "https://example.com/salmon.jpg",
            category = "Platos Principales"
        ),
        Dish(
            id = 7,
            name = "Tiramisú",
            description = "Postre italiano clásico con mascarpone y café",
            price = 180.0,
            stock = 12,
            imageUrl = "https://example.com/tiramisu.jpg",
            category = "Postres"
        ),
        Dish(
            id = 8,
            name = "Helado de Dulce de Leche",
            description = "Helado artesanal con swirl de dulce de leche",
            price = 150.0,
            stock = 30,
            imageUrl = "https://example.com/helado.jpg",
            category = "Postres"
        ),
        Dish(
            id = 9,
            name = "Agua Mineral",
            description = "Botella 500ml de agua mineral sin gas",
            price = 50.0,
            stock = 100,
            imageUrl = "https://example.com/agua.jpg",
            category = "Bebidas"
        ),
        Dish(
            id = 10,
            name = "Vino Malbec Reserva",
            description = "Vino tinto de bodega Catena Zapata, cosecha 2019",
            price = 380.0,
            stock = 25,
            imageUrl = "https://example.com/vino.jpg",
            category = "Bebidas"
        ),
        Dish(
            id = 11,
            name = "Coca-Cola",
            description = "Botella 350ml de Coca-Cola fría",
            price = 80.0,
            stock = 50,
            imageUrl = "https://example.com/coca.jpg",
            category = "Bebidas"
        ),
        Dish(
            id = 12,
            name = "Milanesa Napolitana",
            description = "Milanesa de ternera con jamón, queso y tomate",
            price = 380.0,
            stock = 16,
            imageUrl = "https://example.com/milanesa.jpg",
            category = "Platos Principales"
        )
    )

    // Mock Order Items
    val mockOrderItems = listOf(
        OrderItem(
            id = 1,
            dishId = 1,
            dishName = "Pizza Margherita",
            quantity = 2,
            unitPrice = 320.0,
            subtotal = 640.0
        ),
        OrderItem(
            id = 2,
            dishId = 2,
            dishName = "Hamburguesa Premium",
            quantity = 1,
            unitPrice = 380.0,
            subtotal = 380.0
        ),
        OrderItem(
            id = 3,
            dishId = 9,
            dishName = "Agua Mineral",
            quantity = 3,
            unitPrice = 50.0,
            subtotal = 150.0
        )
    )

    // Mock Order
    val mockOrder = Order(
        id = 123,
        clientName = "Juan Pérez",
        tableNumber = 5,
        totalAmount = 1170.0, // 640 + 380 + 150 = 1170
        status = "PENDING",
        items = mockOrderItems,
        qrCode = "REST_001_TABLE_05"
    )

    // Mock Payments
    val mockPayments = listOf(
        Payment(
            id = 1,
            orderId = 123,
            amount = 1170.0,
            paymentMethod = "CREDIT_CARD",
            status = "APPROVED",
            transactionId = "TXN_20240622_001"
        ),
        Payment(
            id = 2,
            orderId = 124,
            amount = 850.0,
            paymentMethod = "DEBIT_CARD",
            status = "APPROVED",
            transactionId = "TXN_20240622_002"
        ),
        Payment(
            id = 3,
            orderId = 125,
            amount = 450.0,
            paymentMethod = "CASH",
            status = "PENDING"
        )
    )

    // Helper function para obtener platos por categoría
    fun getDishesbyCategory(category: String): List<Dish> {
        return mockDishes.filter { it.category == category }
    }

    // Helper function para obtener categorías disponibles
    fun getAvailableCategories(): List<String> {
        return mockDishes.map { it.category }.distinct()
    }

    // Helper function para crear orden de prueba
    fun createTestOrder(clientName: String, tableNumber: Int, items: List<OrderItem>): Order {
        val total = items.sumOf { it.subtotal }
        return Order(
            id = (Math.random() * 1000).toInt(),
            clientName = clientName,
            tableNumber = tableNumber,
            totalAmount = total,
            status = "PENDING",
            items = items
        )
    }

    // Helper function para simular procesamiento de pago
    fun simulatePaymentProcessing(
        orderId: Int,
        amount: Double,
        method: String
    ): Payment {
        // Simular 95% de éxito
        val isApproved = (Math.random() > 0.05)
        
        return Payment(
            id = (Math.random() * 1000).toInt(),
            orderId = orderId,
            amount = amount,
            paymentMethod = method,
            status = if (isApproved) "APPROVED" else "REJECTED",
            transactionId = if (isApproved) "TXN_${System.currentTimeMillis()}" else null
        )
    }

    // Data para pruebas
    data class TestScenario(
        val name: String,
        val description: String,
        val order: Order,
        val expectedTotal: Double
    )

    val testScenarios = listOf(
        TestScenario(
            name = "Pedido Simple",
            description = "1 pizza + 1 agua",
            order = Order(
                clientName = "Test User 1",
                tableNumber = 1,
                totalAmount = 370.0,
                items = listOf(
                    OrderItem(1, 1, "Pizza Margherita", 1, 320.0),
                    OrderItem(2, 9, "Agua Mineral", 1, 50.0)
                )
            ),
            expectedTotal = 370.0
        ),
        TestScenario(
            name = "Pedido Completo",
            description = "Entrada + Plato principal + Postre + Bebida",
            order = Order(
                clientName = "Test User 2",
                tableNumber = 2,
                totalAmount = 1010.0,
                items = listOf(
                    OrderItem(1, 3, "Ensalada César", 1, 280.0),
                    OrderItem(2, 5, "Pasta a la Bolognesa", 1, 340.0),
                    OrderItem(3, 7, "Tiramisú", 1, 180.0),
                    OrderItem(4, 11, "Coca-Cola", 1, 80.0)
                )
            ),
            expectedTotal = 880.0
        ),
        TestScenario(
            name = "Pedido Grande",
            description = "Para 4 personas",
            order = Order(
                clientName = "Test User 3",
                tableNumber = 3,
                totalAmount = 1520.0,
                items = listOf(
                    OrderItem(1, 1, "Pizza Margherita", 2, 320.0),
                    OrderItem(2, 2, "Hamburguesa Premium", 2, 380.0),
                    OrderItem(3, 8, "Helado de Dulce de Leche", 4, 150.0),
                    OrderItem(4, 10, "Vino Malbec Reserva", 1, 380.0)
                )
            ),
            expectedTotal = 2360.0
        )
    )
}

// Extensión para simular pagos
fun Order.simulatePayment(method: String = "CREDIT_CARD"): Payment {
    return Payment(
        orderId = this.id,
        amount = this.totalAmount,
        paymentMethod = method,
        status = "APPROVED",
        transactionId = "TXN_${System.currentTimeMillis()}"
    )
}

// Extensión para calcular total con impuestos
fun Order.getTotalWithTax(taxRate: Double = 0.1): Double {
    return this.totalAmount * (1 + taxRate)
}

// Extensión para obtener resumen del pedido
fun Order.getSummary(): String {
    val itemCount = this.items.size
    val dishCount = this.items.sumOf { it.quantity }
    return "$itemCount tipos de platos, $dishCount unidades totales"
}
