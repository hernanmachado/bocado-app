package com.bocado.ui.screens

import androidx.compose.animation.core.animateFloatAsState
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
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.QrCode2
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bocado.ui.theme.BocadoOrange
import com.bocado.ui.theme.BocadoWhite

@Composable
fun OnboardingScreenV2(onContinue: () -> Unit) {
    var currentStep by remember { mutableStateOf(0) }
    val totalSteps = 3

    val steps = listOf(
        OnboardingStepData(
            icon = Icons.Default.QrCode2,
            title = "Escanea el QR",
            description = "Busca el código QR en tu mesa y escanéalo con tu teléfono",
            color = androidx.compose.ui.graphics.Color.Black
        ),
        OnboardingStepData(
            icon = Icons.Default.ShoppingCart,
            title = "Elige tu Orden",
            description = "Explora nuestro menú digital y selecciona los platos que deseas",
            color = androidx.compose.ui.graphics.Color.Black
        ),
        OnboardingStepData(
            icon = Icons.Default.CreditCard,
            title = "Paga Fácil",
            description = "Completa tu pago de forma segura con tarjeta o efectivo",
            color = androidx.compose.ui.graphics.Color.Black
        )
    )

    val currentStepData = steps[currentStep]

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BocadoWhite)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header con progreso
            Text(
                text = "¡Bienvenido a BOCADO!",
                style = MaterialTheme.typography.headlineSmall,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = BocadoOrange
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Progreso visual - MEJORADO
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                repeat(totalSteps) { index ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(8.dp)
                            .background(
                                color = if (index <= currentStep) BocadoOrange
                                else BocadoOrange.copy(alpha = 0.2f),
                                shape = MaterialTheme.shapes.extraSmall
                            )
                    )
                }
            }

            Spacer(modifier = Modifier.height(56.dp))

            // Icono grande animado
            Box(
                modifier = Modifier
                    .size(160.dp)
                    .background(
                        color = currentStepData.color.copy(alpha = 0.1f),
                        shape = MaterialTheme.shapes.extraLarge
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = currentStepData.icon,
                    contentDescription = null,
                    modifier = Modifier.size(80.dp),
                    tint = currentStepData.color
                )
            }

            Spacer(modifier = Modifier.height(56.dp))

            // Contenido del paso - MEJORADO
            Text(
                text = currentStepData.title,
                style = MaterialTheme.typography.headlineSmall,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = currentStepData.color,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = currentStepData.description,
                style = MaterialTheme.typography.bodyLarge,
                color = currentStepData.color.copy(alpha = 0.75f),
                textAlign = TextAlign.Center,
                fontSize = 16.sp,
                lineHeight = 24.sp
            )

            Spacer(modifier = Modifier.weight(1f))

            // Botones
            Button(
                onClick = {
                    if (currentStep < totalSteps - 1) {
                        currentStep++
                    } else {
                        onContinue()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = BocadoOrange),
                shape = MaterialTheme.shapes.large
            ) {
                Text(
                    text = if (currentStep < totalSteps - 1) "Siguiente" else "¡Empezar!",
                    style = MaterialTheme.typography.titleMedium,
                    color = BocadoWhite,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            if (currentStep > 0) {
                Button(
                    onClick = { currentStep-- },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = BocadoOrange.copy(alpha = 0.1f)
                    ),
                    shape = MaterialTheme.shapes.large
                ) {
                    Text(
                        text = "Atrás",
                        style = MaterialTheme.typography.titleMedium,
                        color = BocadoOrange,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

data class OnboardingStepData(
    val icon: ImageVector,
    val title: String,
    val description: String,
    val color: androidx.compose.ui.graphics.Color
)


