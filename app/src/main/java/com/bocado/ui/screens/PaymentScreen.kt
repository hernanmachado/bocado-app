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
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bocado.ui.theme.BocadoGray
import com.bocado.ui.theme.BocadoGreen
import com.bocado.ui.theme.BocadoLightGray
import com.bocado.ui.theme.BocadoOrange
import com.bocado.ui.theme.BocadoRed
import com.bocado.ui.theme.BocadoWhite
import com.bocado.viewmodel.PaymentViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentScreen(
    paymentViewModel: PaymentViewModel,
    amount: Double,
    orderId: Int,
    onNavigateBack: () -> Unit,
    onPaymentSuccess: () -> Unit
) {
    val paymentUiState by paymentViewModel.uiState.collectAsState()

    LaunchedEffect(orderId, amount) {
        paymentViewModel.initializePayment(orderId, amount)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Realizar Pago",
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
        }
    ) { paddingValues ->
        when {
            paymentUiState.isPaymentSuccess -> {
                PaymentSuccessScreen(
                    payment = paymentUiState.paymentResult,
                    onNavigateBack = onPaymentSuccess,
                    modifier = Modifier.padding(paddingValues)
                )
            }

            else -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = BocadoLightGray),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    "Monto a Pagar",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = BocadoGray
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    "$ ${String.format("%.2f", amount)}",
                                    style = MaterialTheme.typography.headlineSmall,
                                    color = BocadoOrange
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    "Pedido #$orderId",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = BocadoGray
                                )
                            }
                        }
                    }

                    item {
                        PaymentMethodSelector(
                            currentMethod = paymentUiState.paymentMethod,
                            onMethodSelected = { paymentViewModel.updatePaymentMethod(it) }
                        )
                    }

                    if (paymentUiState.paymentMethod in listOf("CREDIT_CARD", "DEBIT_CARD")) {
                        item {
                            CardPaymentForm(
                                cardNumber = paymentUiState.cardNumber,
                                expiryDate = paymentUiState.expiryDate,
                                cvv = paymentUiState.cvv,
                                holderName = paymentUiState.holderName,
                                onCardNumberChange = { paymentViewModel.updateCardNumber(it) },
                                onExpiryDateChange = { paymentViewModel.updateExpiryDate(it) },
                                onCvvChange = { paymentViewModel.updateCvv(it) },
                                onHolderNameChange = { paymentViewModel.updateHolderName(it) }
                            )
                        }
                    }

                    if (paymentUiState.error != null) {
                        item {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(
                                    containerColor = Color(0xFFFFEBEE)
                                ),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    paymentUiState.error ?: "",
                                    color = BocadoRed,
                                    style = MaterialTheme.typography.bodyMedium,
                                    modifier = Modifier.padding(12.dp)
                                )
                            }
                        }
                    }

                    item {
                        Button(
                            onClick = { paymentViewModel.processPayment() },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = BocadoOrange
                            ),
                            enabled = !paymentUiState.isProcessing
                        ) {
                            if (paymentUiState.isProcessing) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(20.dp),
                                    color = BocadoWhite,
                                    strokeWidth = 2.dp
                                )
                            } else {
                                Text(
                                    "Confirmar Pago",
                                    color = BocadoWhite,
                                    style = MaterialTheme.typography.titleMedium
                                )
                            }
                        }
                    }

                    item { Spacer(modifier = Modifier.height(16.dp)) }
                }
            }
        }
    }
}

@Composable
private fun PaymentMethodSelector(currentMethod: String, onMethodSelected: (String) -> Unit) {
    Column {
        Text("Método de Pago", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(12.dp))

        val methods = listOf(
            "CREDIT_CARD" to "Tarjeta de Crédito",
            "DEBIT_CARD" to "Tarjeta de Débito",
            "CASH" to "Efectivo",
            "WALLET" to "Billetera Digital"
        )

        methods.forEach { (value, label) ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .selectable(
                        selected = (value == currentMethod),
                        onClick = { onMethodSelected(value) },
                        role = Role.RadioButton
                    )
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = (value == currentMethod),
                    onClick = { onMethodSelected(value) }
                )
                Spacer(modifier = Modifier.padding(start = 8.dp))
                Text(label, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}

@Composable
private fun CardPaymentForm(
    cardNumber: String,
    expiryDate: String,
    cvv: String,
    holderName: String,
    onCardNumberChange: (String) -> Unit,
    onExpiryDateChange: (String) -> Unit,
    onCvvChange: (String) -> Unit,
    onHolderNameChange: (String) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Datos de la Tarjeta", style = MaterialTheme.typography.titleMedium)

        OutlinedTextField(
            value = cardNumber,
            onValueChange = { if (it.length <= 16) onCardNumberChange(it) },
            label = { Text("Número de Tarjeta") },
            placeholder = { Text("1234 5678 9012 3456") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = expiryDate,
                onValueChange = { if (it.length <= 5) onExpiryDateChange(it) },
                label = { Text("MM/YY") },
                placeholder = { Text("01/25") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(8.dp)
            )

            OutlinedTextField(
                value = cvv,
                onValueChange = { if (it.length <= 3) onCvvChange(it) },
                label = { Text("CVV") },
                placeholder = { Text("123") },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(8.dp)
            )
        }

        OutlinedTextField(
            value = holderName,
            onValueChange = { onHolderNameChange(it) },
            label = { Text("Titular de la Tarjeta") },
            placeholder = { Text("JUAN PEREZ") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp)
        )
    }
}

@Composable
private fun PaymentSuccessScreen(
    payment: com.bocado.model.Payment?,
    onNavigateBack: () -> Unit,
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
                .size(100.dp)
                .background(BocadoGreen, RoundedCornerShape(50)),
            contentAlignment = Alignment.Center
        ) {
            Text("✓", style = MaterialTheme.typography.headlineLarge, color = BocadoWhite)
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text("¡Pago Exitoso!", style = MaterialTheme.typography.headlineSmall)

        Spacer(modifier = Modifier.height(16.dp))

        if (payment != null) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = BocadoLightGray),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    PaymentDetailRow("Monto", "$ ${String.format("%.2f", payment.amount)}")
                    Spacer(modifier = Modifier.height(8.dp))
                    PaymentDetailRow("Método", payment.paymentMethod ?: "")
                    Spacer(modifier = Modifier.height(8.dp))
                    PaymentDetailRow("Estado", "Aprobado")
                    if (payment.transactionId != null) {
                        Spacer(modifier = Modifier.height(8.dp))
                        PaymentDetailRow("ID Transacción", payment.transactionId)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = onNavigateBack,
            colors = ButtonDefaults.buttonColors(containerColor = BocadoOrange),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) {
            Text("Finalizar", color = BocadoWhite)
        }
    }
}

@Composable
private fun PaymentDetailRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, style = MaterialTheme.typography.bodyMedium, color = BocadoGray)
        Text(value, style = MaterialTheme.typography.bodyMedium)
    }
}
