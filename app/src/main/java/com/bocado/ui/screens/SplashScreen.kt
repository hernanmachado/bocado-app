package com.bocado.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.QrCode2
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.bocado.ui.theme.BocadoOrange
import com.bocado.ui.theme.BocadoWhite

@Composable
fun SplashScreen(onNavigateToQR: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BocadoOrange),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.QrCode2,
                contentDescription = "BOCADO Logo",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                tint = BocadoWhite
            )

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "BOCADO",
                style = MaterialTheme.typography.headlineLarge,
                color = BocadoWhite,
                fontSize = MaterialTheme.typography.headlineLarge.fontSize
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Sistema de Gestión de Pedidos\npara Restaurantes",
                style = MaterialTheme.typography.bodyLarge,
                color = BocadoWhite,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(48.dp))

            Button(
                onClick = onNavigateToQR,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = BocadoWhite
                ),
                shape = MaterialTheme.shapes.medium
            ) {
                Text(
                    text = "Escanear QR",
                    style = MaterialTheme.typography.titleMedium,
                    color = BocadoOrange
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "¿Necesitas ayuda? Contacta con el restaurante",
                style = MaterialTheme.typography.bodySmall,
                color = BocadoWhite,
                textAlign = TextAlign.Center,
                modifier = Modifier.clickable { /* TODO: Help screen */ }
            )
        }
    }
}

@Composable
fun OnboardingScreen(onContinue: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BocadoWhite)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = androidx.compose.foundation.layout.Arrangement.Center
    ) {
        Text(
            text = "¡Bienvenido a BOCADO!",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Paso 1
        OnboardingStep(
            step = 1,
            title = "Escanea el QR",
            description = "Cada mesa tiene un código QR único que accede al menú digital"
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Paso 2
        OnboardingStep(
            step = 2,
            title = "Selecciona tu Orden",
            description = "Elige los platos que deseas de nuestro menú completo"
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Paso 3
        OnboardingStep(
            step = 3,
            title = "Realiza el Pago",
            description = "Paga de forma segura con tarjeta o efectivo"
        )

        Spacer(modifier = Modifier.height(48.dp))

        Button(
            onClick = onContinue,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = BocadoOrange)
        ) {
            Text(
                "Comenzar",
                style = MaterialTheme.typography.titleMedium,
                color = BocadoWhite
            )
        }
    }
}

@Composable
private fun OnboardingStep(step: Int, title: String, description: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = Color(0xFFFFE8E0),
                shape = MaterialTheme.shapes.medium
            )
            .padding(16.dp)
    ) {
        Column {
            Text(
                text = "Paso $step",
                style = MaterialTheme.typography.labelLarge,
                color = BocadoOrange
            )
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
