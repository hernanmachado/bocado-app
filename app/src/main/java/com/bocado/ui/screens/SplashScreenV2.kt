package com.bocado.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.RestaurantMenu
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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

@Composable
fun SplashScreenV2(onNavigateToQR: () -> Unit) {
    val scaleAnimation = remember { Animatable(0.8f) }
    val alphaAnimation = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        scaleAnimation.animateTo(
            targetValue = 1f,
            animationSpec = tween(800)
        )
        alphaAnimation.animateTo(
            targetValue = 1f,
            animationSpec = tween(800)
        )
    }

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
            // Logo animado
            Box(
                modifier = Modifier
                    .scale(scaleAnimation.value)
                    .size(140.dp)
                    .background(
                        color = BocadoWhite,
                        shape = MaterialTheme.shapes.extraLarge
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.RestaurantMenu,
                    contentDescription = "BOCADO",
                    modifier = Modifier.size(80.dp),
                    tint = BocadoOrange
                )
            }

            Spacer(modifier = Modifier.height(40.dp))

            Text(
                text = "BOCADO",
                style = MaterialTheme.typography.headlineLarge,
                fontSize = 48.sp,
                fontWeight = FontWeight.Bold,
                color = BocadoWhite
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Tu menú digital,\nal alcance de tu mano",
                style = MaterialTheme.typography.bodyLarge,
                color = BocadoWhite.copy(alpha = 0.9f),
                textAlign = TextAlign.Center,
                fontSize = 16.sp
            )

            Spacer(modifier = Modifier.height(60.dp))

            Button(
                onClick = onNavigateToQR,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = BocadoWhite
                ),
                shape = MaterialTheme.shapes.large
            ) {
                Text(
                    text = "Escanear QR",
                    style = MaterialTheme.typography.titleMedium,
                    color = BocadoOrange,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = onNavigateToQR,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = BocadoWhite.copy(alpha = 0.2f)
                ),
                shape = MaterialTheme.shapes.large
            ) {
                Text(
                    text = "¿Primera vez aquí? Lee las instrucciones",
                    style = MaterialTheme.typography.bodyMedium,
                    color = BocadoWhite,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
        }
    }
}


