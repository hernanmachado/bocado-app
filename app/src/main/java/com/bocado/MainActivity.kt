package com.bocado

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.bocado.ui.screens.CartScreen
import com.bocado.ui.screens.LoginScreen
import com.bocado.ui.screens.MenuScreen
import com.bocado.ui.screens.OnboardingScreenV2
import com.bocado.ui.screens.OrderStatusScreen
import com.bocado.ui.screens.PaymentScreen
import com.bocado.ui.screens.RegisterScreen
import com.bocado.ui.screens.ScannerScreen
import com.bocado.ui.screens.SplashScreenV2
import com.bocado.ui.screens.TableConfirmationScreen
import com.bocado.ui.theme.BocadoTheme
import com.bocado.viewmodel.CartViewModel
import com.bocado.viewmodel.MenuViewModel
import com.bocado.viewmodel.PaymentViewModel

class MainActivity : ComponentActivity() {
    private lateinit var database: BocadoDatabase
    private lateinit var menuViewModel: MenuViewModel
    private lateinit var cartViewModel: CartViewModel
    private lateinit var paymentViewModel: PaymentViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        database = BocadoDatabase.getInstance(this)

        val dishRepository = DishRepository(database.dishDao(), ApiClient.apiService)
        val orderRepository = OrderRepository(database.orderDao(), database.orderItemDao(), ApiClient.apiService)
        val paymentRepository = PaymentRepository(database.paymentDao(), ApiClient.apiService)

        menuViewModel = MenuViewModel(dishRepository)
        cartViewModel = CartViewModel(orderRepository, dishRepository)
        paymentViewModel = PaymentViewModel(paymentRepository)

        setContent {
            BocadoTheme {
                BocadoApp(
                    database = database,
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
    database: BocadoDatabase,
    menuViewModel: MenuViewModel,
    cartViewModel: CartViewModel,
    paymentViewModel: PaymentViewModel
) {
    val navController = rememberNavController()
    val cartUiState by cartViewModel.uiState.collectAsState()

    NavHost(
        navController = navController,
        startDestination = "splash" // 1. ARRANCAMOS EN EL SPLASH ANIMADO
    ) {
        composable("splash") {
            SplashScreenV2(
                onNavigateToQR = {
                    navController.navigate("login") {
                        popUpTo("splash") { inclusive = true }
                    }
                }
            )
        }

        composable("login") {
            LoginScreen(
                userDao = database.userDao(),
                onLoginSuccess = {
                    navController.navigate("onboarding") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onNavigateToRegister = { navController.navigate("register") }
            )
        }

        composable("register") {
            RegisterScreen(
                userDao = database.userDao(),
                onRegisterSuccess = {
                    navController.navigate("login") {
                        popUpTo("register") { inclusive = true }
                    }
                },
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable("onboarding") {
            OnboardingScreenV2(
                onContinue = {
                    navController.navigate("scanner") {
                        popUpTo("onboarding") { inclusive = true }
                    }
                }
            )
        }

        composable("scanner") {
            ScannerScreen(
                menuViewModel = menuViewModel,
                onScanSuccess = {
                    // AL ESCANEAR, VAMOS A LA CONFIRMACIÓN DE MESA
                    navController.navigate("table_confirmation") {
                        popUpTo("scanner") { inclusive = true }
                    }
                },
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // NUEVA RUTA: Confirmación de mesa animada
        composable("table_confirmation") {
            TableConfirmationScreen(
                tableNumber = 0, // 0 para que sea aleatoria como lo programó tu compañero
                waiterName = "Juan Pérez", // Podés ponerle el nombre que quieras
                onGoToMenu = {
                    navController.navigate("menu") {
                        popUpTo("table_confirmation") { inclusive = true }
                    }
                },
                onScanAgain = {
                    navController.navigate("scanner") {
                        popUpTo("table_confirmation") { inclusive = true }
                    }
                }
            )
        }

        composable("menu") {
            MenuScreen(
                menuViewModel = menuViewModel,
                cartViewModel = cartViewModel,
                cartItemsCount = cartUiState.cartItems.size,
                onNavigateToCart = { navController.navigate("cart") }
            )
        }

        composable("cart") {
            CartScreen(
                cartViewModel = cartViewModel,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToPayment = {
                    val currentOrder = cartUiState.currentOrder
                    if (currentOrder != null) {
                        val total = cartUiState.totalAmount * 1.1
                        navController.navigate("payment/${currentOrder.id}/$total")
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
                onNavigateBack = { navController.popBackStack() },
                onPaymentSuccess = {
                    cartViewModel.clearCart()
                    navController.navigate("order_status") {
                        popUpTo("payment/{orderId}/{amount}") { inclusive = true }
                        popUpTo("cart") { inclusive = true }
                    }
                }
            )
        }

        composable("order_status") {
            OrderStatusScreen(
                onNavigateToMenu = {
                    navController.navigate("menu") {
                        popUpTo("order_status") { inclusive = true }
                    }
                }
            )
        }
    }
}

// Actualizamos el Builder para que no crashee al cambiar la versión de la DB
fun BocadoDatabase.Companion.getInstance(context: Context): BocadoDatabase {
    return Room.databaseBuilder(
        context.applicationContext,
        BocadoDatabase::class.java,
        "bocado.db"
    ).fallbackToDestructiveMigration()
        .build()
}




