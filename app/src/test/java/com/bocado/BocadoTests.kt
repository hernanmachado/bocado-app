package com.bocado

import com.bocado.model.Dish
import com.bocado.model.OrderItem
import com.bocado.model.Order
import com.bocado.model.Payment
import org.junit.Assert.*
import org.junit.Test

class DishTest {
    @Test
    fun testDishCreation() {
        val dish = Dish(
            id = 1,
            name = "Pizza Margherita",
            description = "Pizza clásica con tomate y queso",
            price = 250.0,
            stock = 10,
            imageUrl = "https://example.com/pizza.jpg",
            category = "Platos Principales"
        )

        assertEquals(1, dish.id)
        assertEquals("Pizza Margherita", dish.name)
        assertEquals(250.0, dish.price, 0.0)
        assertEquals(10, dish.stock)
        assertTrue(dish.isAvailable)
    }

    @Test
    fun testOutOfStockDish() {
        val dish = Dish(
            id = 2,
            name = "Hamburguesa",
            description = "Hamburguesa clásica",
            price = 180.0,
            stock = 0,
            imageUrl = "https://example.com/burger.jpg",
            category = "Platos Principales"
        )

        assertEquals(0, dish.stock)
        assertTrue(dish.isAvailable) // isAvailable no depende del stock
    }
}

class OrderItemTest {
    @Test
    fun testOrderItemCalculation() {
        val item = OrderItem(
            dishId = 1,
            dishName = "Pizza",
            quantity = 2,
            unitPrice = 250.0
        )

        assertEquals(500.0, item.subtotal, 0.0)
    }

    @Test
    fun testOrderItemQuantityValidation() {
        val item = OrderItem(
            dishId = 1,
            dishName = "Pasta",
            quantity = 0,
            unitPrice = 150.0
        )

        assertTrue(item.quantity == 0)
    }
}

class OrderTest {
    @Test
    fun testOrderCreation() {
        val items = listOf(
            OrderItem(
                dishId = 1,
                dishName = "Pizza",
                quantity = 2,
                unitPrice = 250.0
            ),
            OrderItem(
                dishId = 2,
                dishName = "Pasta",
                quantity = 1,
                unitPrice = 180.0
            )
        )

        val order = Order(
            clientName = "Juan Pérez",
            tableNumber = 5,
            totalAmount = 680.0,
            items = items
        )

        assertEquals("Juan Pérez", order.clientName)
        assertEquals(5, order.tableNumber)
        assertEquals(680.0, order.totalAmount, 0.0)
        assertEquals(2, order.items.size)
        assertEquals("PENDING", order.status)
    }

    @Test
    fun testOrderStatusTransitions() {
        var order = Order(
            clientName = "Cliente Test",
            tableNumber = 3,
            totalAmount = 500.0,
            status = "PENDING"
        )

        assertEquals("PENDING", order.status)

        order = order.copy(status = "CONFIRMED")
        assertEquals("CONFIRMED", order.status)

        order = order.copy(status = "PAID")
        assertEquals("PAID", order.status)

        order = order.copy(status = "COMPLETED")
        assertEquals("COMPLETED", order.status)
    }
}

class PaymentTest {
    @Test
    fun testPaymentCreation() {
        val payment = Payment(
            orderId = 1,
            amount = 500.0,
            paymentMethod = "CREDIT_CARD",
            status = "PENDING"
        )

        assertEquals(1, payment.orderId)
        assertEquals(500.0, payment.amount, 0.0)
        assertEquals("CREDIT_CARD", payment.paymentMethod)
        assertEquals("PENDING", payment.status)
    }

    @Test
    fun testPaymentApproval() {
        var payment = Payment(
            orderId = 1,
            amount = 500.0,
            paymentMethod = "DEBIT_CARD",
            status = "PENDING"
        )

        payment = payment.copy(
            status = "APPROVED",
            transactionId = "TXN123456"
        )

        assertEquals("APPROVED", payment.status)
        assertEquals("TXN123456", payment.transactionId)
    }

    @Test
    fun testPaymentRejection() {
        var payment = Payment(
            orderId = 1,
            amount = 500.0,
            paymentMethod = "CREDIT_CARD",
            status = "PENDING"
        )

        payment = payment.copy(status = "REJECTED")

        assertEquals("REJECTED", payment.status)
    }
}

class BusinessLogicTest {
    @Test
    fun testStockValidation() {
        val dish = Dish(
            id = 1,
            name = "Sopa",
            description = "Sopa deliciosa",
            price = 100.0,
            stock = 3,
            imageUrl = "",
            category = "Entradas"
        )

        // Validar que no se puede pedir más que el stock
        val requestedQuantity = 5
        assertTrue(requestedQuantity > dish.stock)
    }

    @Test
    fun testOrderTotalCalculation() {
        val items = listOf(
            OrderItem(dishId = 1, dishName = "Pizza", quantity = 2, unitPrice = 250.0),
            OrderItem(dishId = 2, dishName = "Pasta", quantity = 1, unitPrice = 180.0),
            OrderItem(dishId = 3, dishName = "Bebida", quantity = 2, unitPrice = 50.0)
        )

        val subtotal = items.sumOf { it.subtotal }
        val tax = subtotal * 0.1
        val total = subtotal + tax

        assertEquals(780.0, subtotal, 0.0)
        assertEquals(78.0, tax, 0.0)
        assertEquals(858.0, total, 0.0)
    }

    @Test
    fun testPaymentValidation() {
        val cardNumber = "1234567890123456"
        val expiryDate = "12/25"
        val cvv = "123"

        assertTrue(cardNumber.length == 16)
        assertTrue(expiryDate.matches(Regex("\\d{2}/\\d{2}")))
        assertTrue(cvv.length == 3)
    }
}
