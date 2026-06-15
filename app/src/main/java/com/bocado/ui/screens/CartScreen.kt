package com.bocado.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bocado.model.OrderItem
import com.bocado.ui.theme.BocadoGray
import com.bocado.ui.theme.BocadoGreen
import com.bocado.ui.theme.BocadoLightGray
import com.bocado.ui.theme.BocadoOrange
import com.bocado.ui.theme.BocadoRed
import com.bocado.ui.theme.BocadoWhite
import com.bocado.viewmodel.CartUiState
import com.bocado.viewmodel.CartViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    cartViewModel: CartViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToPayment: () -> Unit
) {
    val cartUiState by cartViewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val clientName = remember { mutableStateOf("") }
    val tableNumber = remember { mutableStateOf("1") }
    val showConfirmDialog = remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Carrito de Compras",
                        style = MaterialTheme.typography.titleLarge,
                        color = BocadoWhite,
                        fontSize = 20.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Volver",
                            tint = BocadoWhite
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = BocadoOrange)
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        when {
            cartUiState.cartItems.isEmpty() -> {
                EmptyCartScreen(
                    modifier = Modifier.padding(paddingValues),
                    onNavigateBack = onNavigateBack
                )
            }

            cartUiState.orderConfirmed -> {
                OrderConfirmedScreen(
                    order = cartUiState.currentOrder,
                    onNavigateToPayment = onNavigateToPayment,
                    modifier = Modifier.padding(paddingValues)
                )
            }

            else -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    LazyColumn(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(cartUiState.cartItems) { item ->
                            CartItemCard(
                                item = item,
                                onRemove = { cartViewModel.removeItemFromCart(item.id) },
                                onQuantityChange = { newQuantity ->
                                    cartViewModel.updateItemQuantity(item.id, newQuantity)
                                }
                            )
                        }
                    }

                    Divider(modifier = Modifier.padding(vertical = 16.dp))

                    // Resumen y formulario
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 12.dp)
                    ) {
                        // Información de cliente
                        Text(
                            "Información del Pedido",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = clientName.value,
                            onValueChange = { clientName.value = it },
                            label = { Text("Nombre del cliente") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            shape = RoundedCornerShape(8.dp)
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedTextField(
                            value = tableNumber.value,
                            onValueChange = { tableNumber.value = it },
                            label = { Text("Número de mesa") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            shape = RoundedCornerShape(8.dp)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Resumen
                        PriceRow("Subtotal", cartUiState.totalAmount)
                        PriceRow("Impuestos (10%)", cartUiState.totalAmount * 0.1)
                        Divider(modifier = Modifier.padding(vertical = 8.dp))
                        PriceRow(
                            "Total",
                            cartUiState.totalAmount * 1.1,
                            isBold = true
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Botones
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Button(
                                onClick = { cartViewModel.clearCart() },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(48.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = BocadoGray
                                )
                            ) {
                                Text("Limpiar", color = BocadoWhite)
                            }

                            Button(
                                onClick = {
                                    if (clientName.value.isEmpty()) {
                                        // Mostrar error
                                    } else {
                                        cartViewModel.confirmOrder(
                                            clientName.value,
                                            tableNumber.value.toIntOrNull() ?: 1
                                        )
                                    }
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(48.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = BocadoOrange
                                ),
                                enabled = !cartUiState.isLoading
                            ) {
                                if (cartUiState.isLoading) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(20.dp),
                                        color = BocadoWhite,
                                        strokeWidth = 2.dp
                                    )
                                } else {
                                    Text("Confirmar", color = BocadoWhite)
                                }
                            }
                        }

                        if (cartUiState.error != null) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                cartUiState.error ?: "",
                                color = BocadoRed,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun CartItemCard(
    item: OrderItem,
    onRemove: () -> Unit,
    onQuantityChange: (Int) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = BocadoLightGray),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = item.dishName,
                        style = MaterialTheme.typography.titleMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = "$ ${String.format("%.2f", item.unitPrice)} c/u",
                        style = MaterialTheme.typography.bodySmall,
                        color = BocadoGray
                    )
                }

                IconButton(onClick = onRemove, modifier = Modifier.size(32.dp)) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Eliminar",
                        tint = BocadoRed
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Subtotal: $ ${String.format("%.2f", item.subtotal)}",
                    style = MaterialTheme.typography.titleSmall,
                    color = BocadoOrange
                )

                Row(
                    modifier = Modifier
                        .background(BocadoWhite, RoundedCornerShape(4.dp))
                        .padding(4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    IconButton(
                        onClick = { onQuantityChange(item.quantity - 1) },
                        modifier = Modifier.size(24.dp)
                    ) {
                        Text("-", style = MaterialTheme.typography.labelMedium)
                    }
                    Text(
                        item.quantity.toString(),
                        style = MaterialTheme.typography.bodyMedium
                    )
                    IconButton(
                        onClick = { onQuantityChange(item.quantity + 1) },
                        modifier = Modifier.size(24.dp)
                    ) {
                        Text("+", style = MaterialTheme.typography.labelMedium)
                    }
                }
            }
        }
    }
}

@Composable
private fun PriceRow(label: String, amount: Double, isBold: Boolean = false) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            label,
            style = if (isBold) MaterialTheme.typography.titleMedium else MaterialTheme.typography.bodyMedium
        )
        Text(
            "$ ${String.format("%.2f", amount)}",
            style = if (isBold) MaterialTheme.typography.titleMedium else MaterialTheme.typography.bodyMedium,
            color = if (isBold) BocadoOrange else Color.Unspecified
        )
    }
}

@Composable
private fun EmptyCartScreen(modifier: Modifier = Modifier, onNavigateBack: () -> Unit) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            "Tu carrito está vacío",
            style = MaterialTheme.typography.headlineSmall
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            "Agrega platos del menú para comenzar tu pedido",
            style = MaterialTheme.typography.bodyMedium,
            color = BocadoGray
        )
        Spacer(modifier = Modifier.height(32.dp))
        Button(
            onClick = onNavigateBack,
            colors = ButtonDefaults.buttonColors(containerColor = BocadoOrange),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) {
            Text("Volver al Menú", color = BocadoWhite)
        }
    }
}

@Composable
private fun OrderConfirmedScreen(
    order: com.bocado.model.Order?,
    onNavigateToPayment: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(80.dp)
                .background(BocadoGreen, RoundedCornerShape(50)),
            contentAlignment = Alignment.Center
        ) {
            Text("✓", style = MaterialTheme.typography.headlineLarge, color = BocadoWhite)
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            "¡Pedido Confirmado!",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (order != null) {
            Text(
                "Número de Pedido: #${order.id}",
                style = MaterialTheme.typography.titleMedium,
                color = BocadoOrange
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "Total: $ ${String.format("%.2f", order.totalAmount)}",
                style = MaterialTheme.typography.titleMedium
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = onNavigateToPayment,
            colors = ButtonDefaults.buttonColors(containerColor = BocadoOrange),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) {
            Text("Proceder al Pago", color = BocadoWhite)
        }
    }
}
