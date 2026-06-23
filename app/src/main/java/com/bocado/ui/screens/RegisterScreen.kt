package com.bocado.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.bocado.data.UserDao
import com.bocado.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun RegisterScreen(
    userDao: UserDao,
    onRegisterSuccess: () -> Unit,
    onNavigateBack: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current // Agregamos el contexto para el Toast
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var error by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Crear Cuenta", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(value = username, onValueChange = { username = it }, label = { Text("Usuario") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(value = password, onValueChange = { password = it }, label = { Text("Contraseña") }, visualTransformation = PasswordVisualTransformation(), modifier = Modifier.fillMaxWidth())

        if (error.isNotEmpty()) {
            Text(error, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(top = 8.dp))
        }

        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                scope.launch {
                    val cleanUser = username.trim()
                    val cleanPass = password.trim()

                    val existing = withContext(Dispatchers.IO) { userDao.getUserByUsername(cleanUser) }
                    if (existing != null) {
                        error = "El usuario ya existe"
                    } else if (cleanUser.isNotBlank() && cleanPass.isNotBlank()) {
                        // Intentamos guardar y guardamos el ID que nos devuelve Room
                        val rowId = withContext(Dispatchers.IO) { userDao.insertUser(User(username = cleanUser, password = cleanPass)) }

                        if (rowId > 0) {
                            // ¡ÉXITO! El usuario se guardó
                            Toast.makeText(context, "Usuario registrado con ID: $rowId", Toast.LENGTH_LONG).show()
                            onRegisterSuccess()
                        } else {
                            error = "Error desconocido al guardar"
                        }
                    } else {
                        error = "Completa todos los campos"
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) { Text("Registrarse", color = Color.White) }

        TextButton(onClick = onNavigateBack) { Text("Volver a Login") }
    }
}

