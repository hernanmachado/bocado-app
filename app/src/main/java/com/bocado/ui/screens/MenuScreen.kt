package com.bocado.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bocado.model.Dish
import com.bocado.ui.theme.BocadoGreen
import com.bocado.ui.theme.BocadoGray
import com.bocado.ui.theme.BocadoLightGray
import com.bocado.ui.theme.BocadoOrange
import com.bocado.ui.theme.BocadoRed
import com.bocado.ui.theme.BocadoWhite
import com.bocado.viewmodel.CartViewModel
import com.bocado.viewmodel.MenuUiState
import com.bocado.viewmodel.MenuViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuScreen(
    menuViewModel: MenuViewModel,
    cartViewModel: CartViewModel,
    cartItemsCount: Int = 0,
    onNavigateToCart: () -> Unit
) {
    val menuUiState by menuViewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "MENÚ BOCADO",
                        style = MaterialTheme.typography.titleLarge,
                        color = BocadoWhite,
                        fontSize = 20.sp
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = BocadoOrange
                ),
                actions = {
                    Box(
                        modifier = Modifier
                            .padding(16.dp)
                            .clickable { onNavigateToCart() }
                    ) {
                        Icon(
                            imageVector = Icons.Default.ShoppingCart,
                            contentDescription = "Carrito",
                            tint = BocadoWhite,
                            modifier = Modifier.size(24.dp)
                        )
                        if (cartItemsCount > 0) {
                            Box(
                                modifier = Modifier
                                    .size(20.dp)
                                    .background(BocadoRed, RoundedCornerShape(50))
                                    .align(Alignment.TopEnd),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    cartItemsCount.toString(),
                                    style = MaterialTheme.typography.labelSmall,
                                    color = BocadoWhite,
                                    fontSize = 10.sp
                                )
                            }
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            if (cartItemsCount > 0) {
                FloatingActionButton(
                    onClick = onNavigateToCart,
                    containerColor = BocadoOrange,
                    contentColor = BocadoWhite
                ) {
                    Icon(Icons.Default.ShoppingCart, "Carrito")
                }
            }
        }
    ) { paddingValues ->
        when {
            menuUiState.isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = BocadoOrange)
                }
            }

            menuUiState.error != null -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            "Error: ${menuUiState.error}",
                            color = BocadoRed,
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Button(
                            onClick = { menuViewModel.loadDishes() },
                            modifier = Modifier.padding(top = 16.dp)
                        ) {
                            Text("Reintentar")
                        }
                    }
                }
            }

            else -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    // Categorías
                    CategoriesRow(menuViewModel)

                    // Grid de platos
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(menuUiState.dishes) { dish ->
                            DishCard(
                                dish = dish,
                                onAddToCart = { quantity ->
                                    cartViewModel.addItemToCart(
                                        dishId = dish.id,
                                        dishName = dish.name,
                                        unitPrice = dish.price,
                                        quantity = quantity
                                    )
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CategoriesRow(menuViewModel: MenuViewModel) {
    val categories = listOf("Todos", "Entradas", "Platos Principales", "Postres", "Bebidas")
    
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(categories) { category ->
            CategoryChip(
                category = category,
                onClick = { menuViewModel.filterByCategory(category) }
            )
        }
    }
}

@Composable
private fun CategoryChip(category: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .background(
                color = if (category == "Todos") BocadoOrange else BocadoLightGray,
                shape = RoundedCornerShape(20.dp)
            )
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
            text = category,
            style = MaterialTheme.typography.labelMedium,
            color = if (category == "Todos") BocadoWhite else BocadoGray,
            fontSize = 12.sp
        )
    }
}

@Composable
private fun DishCard(dish: Dish, onAddToCart: (Int) -> Unit) {
    val quantity = remember { mutableStateOf(1) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(4.dp, RoundedCornerShape(12.dp)),
        colors = CardDefaults.cardColors(containerColor = BocadoWhite),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(modifier = Modifier.fillMaxWidth()) {
                // Imagen placeholder
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .background(BocadoLightGray, RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Foto", style = MaterialTheme.typography.bodySmall)
                }

                Spacer(modifier = Modifier.width(12.dp))

                // Info del plato
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = dish.name,
                        style = MaterialTheme.typography.titleMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Text(
                        text = dish.description,
                        style = MaterialTheme.typography.bodySmall,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        color = BocadoGray
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "$ ${String.format("%.2f", dish.price)}",
                            style = MaterialTheme.typography.titleMedium,
                            color = BocadoOrange
                        )

                        if (dish.stock <= 0) {
                            Text(
                                text = "Sin stock",
                                style = MaterialTheme.typography.labelSmall,
                                color = BocadoRed
                            )
                        } else if (dish.stock < 5) {
                            Text(
                                text = "Últimas ${dish.stock}",
                                style = MaterialTheme.typography.labelSmall,
                                color = BocadoOrange
                            )
                        }
                    }
                }
            }

            // Controles de cantidad y botón agregar
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier
                        .background(BocadoLightGray, RoundedCornerShape(8.dp))
                        .padding(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = {
                            if (quantity.value > 1) quantity.value--
                        },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Remove,
                            contentDescription = "Disminuir",
                            modifier = Modifier.size(16.dp),
                            tint = BocadoOrange
                        )
                    }

                    Text(
                        text = quantity.value.toString(),
                        modifier = Modifier.padding(horizontal = 8.dp),
                        style = MaterialTheme.typography.labelMedium
                    )

                    IconButton(
                        onClick = { quantity.value++ },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Aumentar",
                            modifier = Modifier.size(16.dp),
                            tint = BocadoOrange
                        )
                    }
                }

                Button(
                    onClick = {
                        if (dish.stock >= quantity.value) {
                            onAddToCart(quantity.value)
                            quantity.value = 1
                        }
                    },
                    modifier = Modifier.height(40.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (dish.stock > 0) BocadoOrange else BocadoGray
                    ),
                    enabled = dish.stock > 0
                ) {
                    Text(
                        "Agregar",
                        style = MaterialTheme.typography.labelMedium,
                        color = BocadoWhite,
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}
