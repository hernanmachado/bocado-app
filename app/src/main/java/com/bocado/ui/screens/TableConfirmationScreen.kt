package com.bocado.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bocado.ui.theme.BocadoOrange
import com.bocado.ui.theme.BocadoWhite
import kotlin.random.Random

@Composable
fun TableConfirmationScreen(
    tableNumber: Int = 0,
    waiterName: String = "Tu Mesero",
    onGoToMenu: () -> Unit,
    onScanAgain: () -> Unit
) {
    val actualTableNumber = if (tableNumber == 0) {
        remember { Random.nextInt(1, 51) }
    } else {
        tableNumber
    }
    val scaleAnimation = remember { Animatable(0.8f) }
    val checkScaleAnimation = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        scaleAnimation.animateTo(
            targetValue = 1f,
            animationSpec = tween(600)
        )
        checkScaleAnimation.animateTo(
            targetValue = 1f,
            animationSpec = tween(400, delayMillis = 300)
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BocadoOrange)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Checkmark animado
            Box(
                modifier = Modifier
                    .scale(checkScaleAnimation.value)
                    .size(100.dp)
                    .background(
                        color = BocadoWhite,
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Confirmado",
                    modifier = Modifier.size(60.dp),
                    tint = BocadoOrange
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "¡Bienvenido!",
                style = MaterialTheme.typography.headlineSmall,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = BocadoWhite,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Tu mesa ha sido confirmada",
                style = MaterialTheme.typography.bodyLarge,
                color = BocadoWhite.copy(alpha = 0.9f),
                fontSize = 16.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(40.dp))

            // Mesa confirmada
            Box(
                modifier = Modifier
                    .scale(scaleAnimation.value)
                    .fillMaxWidth()
                    .height(150.dp)
                    .background(
                        color = BocadoWhite,
                        shape = MaterialTheme.shapes.extraLarge
                    )
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Tu Mesa",
                        style = MaterialTheme.typography.labelMedium,
                        color = Color.Black,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "#$actualTableNumber",
                        style = MaterialTheme.typography.headlineMedium,
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Información del mesero
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp),
                shape = MaterialTheme.shapes.large,
                colors = CardDefaults.cardColors(
                    containerColor = BocadoWhite.copy(alpha = 0.95f)
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Mesero",
                        modifier = Modifier.size(32.dp),
                        tint = BocadoOrange
                    )

                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 12.dp)
                    ) {
                        Text(
                            text = "Tu Mesero Asignado es :",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.Black,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = waiterName,
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.Black,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Estás sentado en la mesa $actualTableNumber\nDisfruta tu comida",
                style = MaterialTheme.typography.bodyMedium,
                color = BocadoWhite.copy(alpha = 0.85f),
                textAlign = TextAlign.Center,
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(48.dp))

            // Botón principal - Ver menú
            Button(
                onClick = onGoToMenu,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = BocadoWhite
                ),
                shape = MaterialTheme.shapes.large
            ) {
                Text(
                    text = "Ver Menú",
                    style = MaterialTheme.typography.titleMedium,
                    color = BocadoOrange,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Botón secundario - Escanear de nuevo
            Button(
                onClick = onScanAgain,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = BocadoWhite.copy(alpha = 0.2f)
                ),
                shape = MaterialTheme.shapes.large
            ) {
                Text(
                    text = "Escanear Otra Mesa",
                    style = MaterialTheme.typography.titleMedium,
                    color = BocadoWhite,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

