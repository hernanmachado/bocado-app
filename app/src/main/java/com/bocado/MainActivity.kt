package com.bocado

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.bocado.data.BocadoDatabase
import com.bocado.data.api.ApiClient
import com.bocado.repository.DishRepository
import com.bocado.repository.OrderRepository
import com.bocado.repository.PaymentRepository
import com.bocado.ui.screens.MenuScreen
import com.bocado.ui.screens.CartScreen
import com.bocado.ui.screens.PaymentScreen
import com.bocado.ui.screens.SplashScreen
import com.bocado.ui.screens.OnboardingScreen
import com.bocado.ui.theme.BocadoTheme
import com.bocado.viewmodel.MenuViewModel
import com.bocado.viewmodel.CartViewModel
import com.bocado.viewmodel.PaymentViewModel

class MainActivity : ComponentActivity() {
    private lateinit var database: BocadoDatabase
    private lateinit var menuViewModel: MenuViewModel
    private lateinit var cartViewModel: CartViewModel
    private lateinit var paymentViewModel: PaymentViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializar base de datos
        database = BocadoDatabase.getInstance(this)

        // Inicializar repositorios
        val dishRepository = DishRepository(
            database.dishDao(),
            ApiClient.apiService
        )

        val orderRepository = OrderRepository(
            database.orderDao(),
            database.orderItemDao(),
            ApiClient.apiService
        )

        val paymentRepository = PaymentRepository(
            database.paymentDao(),
            ApiClient.apiService
        )

        // Inicializar ViewModels
        menuViewModel = MenuViewModel(dishRepository)
        cartViewModel = CartViewModel(orderRepository, dishRepository)
        paymentViewModel = PaymentViewModel(paymentRepository)

        setContent {
            BocadoTheme {
                BocadoApp(
                    menuViewModel = menuViewModel,
                    cartViewModel = cartViewModel,
                    paymentViewModel = paymentViewModel
                )
            }
        }
    }
}

@Composable
fun BocadoApp(
    menuViewModel: MenuViewModel,
    cartViewModel: CartViewModel,
    paymentViewModel: PaymentViewModel
) {
    val navController = rememberNavController()
    val showOnboarding = remember { mutableStateOf(true) }
    val cartUiState by cartViewModel.uiState.collectAsState()

    if (showOnboarding.value) {
        OnboardingScreen(onContinue = { showOnboarding.value = false })
    } else {
        NavHost(
            navController = navController,
            startDestination = "splash"
        ) {
            composable("splash") {
                SplashScreen(
                    onNavigateToQR = {
                        navController.navigate("menu") {
                            popUpTo("splash") { inclusive = true }
                        }
                    }
                )
            }

            composable("menu") {
                MenuScreen(
                    menuViewModel = menuViewModel,
                    cartViewModel = cartViewModel,
                    cartItemsCount = cartUiState.cartItems.size,
                    onNavigateToCart = {
                        navController.navigate("cart")
                    }
                )
            }

            composable("cart") {
                CartScreen(
                    cartViewModel = cartViewModel,
                    onNavigateBack = {
                        navController.popBackStack()
                    },
                    onNavigateToPayment = {
                        val currentOrder = cartUiState.currentOrder
                        if (currentOrder != null) {
                            navController.navigate("payment/${currentOrder.id}/${currentOrder.totalAmount}")
                        }
                    }
                )
            }

            composable("payment/{orderId}/{amount}") { backStackEntry ->
                val orderId = backStackEntry.arguments?.getString("orderId")?.toIntOrNull() ?: 0
                val amount = backStackEntry.arguments?.getString("amount")?.toDoubleOrNull() ?: 0.0

                PaymentScreen(
                    paymentViewModel = paymentViewModel,
                    amount = amount,
                    orderId = orderId,
                    onNavigateBack = {
                        navController.popBackStack()
                    },
                    onPaymentSuccess = {
                        cartViewModel.clearCart()
                        navController.navigate("menu") {
                            popUpTo("payment/{orderId}/{amount}") { inclusive = true }
                        }
                    }
                )
            }
        }
    }
}

fun BocadoDatabase.Companion.getInstance(context: Context): BocadoDatabase {
    return Room.databaseBuilder(
        context.applicationContext,
        BocadoDatabase::class.java,
        "bocado.db"
    ).build()
}


