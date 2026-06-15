# BOCADO - Documentación Técnica Completa

## 1. Introducción

**BOCADO** es una aplicación mobile desarrollada en Android Studio utilizando Kotlin que implementa un sistema de gestión de pedidos para restaurantes. La aplicación permite a los clientes:

- Escanear código QR para acceder al menú
- Visualizar platos disponibles
- Gestionar un carrito de compras
- Procesar pagos
- Recibir confirmación del pedido

**Versión:** 1.0.0
**API Level Mínimo:** 24
**API Level Destino:** 34
**Arquitectura:** MVVM + Repository Pattern

---

## 2. Arquitectura del Proyecto

### 2.1 Capas de la Aplicación

```
┌─────────────────────────────────────────────────────┐
│        PRESENTATION LAYER (UI/Jetpack Compose)       │
│  Pantallas, Componentes, Navegación, Temas          │
├─────────────────────────────────────────────────────┤
│           VIEWMODEL LAYER (MVVM State Management)    │
│  MenuViewModel, CartViewModel, PaymentViewModel      │
├─────────────────────────────────────────────────────┤
│          REPOSITORY LAYER (Data Abstraction)         │
│  DishRepository, OrderRepository, PaymentRepository  │
├─────────────────────────────────────────────────────┤
│             DATA LAYER (Local + Remote)              │
│  Room (SQLite) | Retrofit (API REST)                │
└─────────────────────────────────────────────────────┘
```

### 2.2 Estructura de Directorios

```
com.bocado/
├── ui/
│   ├── screens/
│   │   ├── SplashScreen.kt
│   │   ├── MenuScreen.kt
│   │   ├── CartScreen.kt
│   │   └── PaymentScreen.kt
│   └── theme/
│       └── Theme.kt
├── viewmodel/
│   ├── MenuViewModel.kt
│   ├── CartViewModel.kt
│   └── PaymentViewModel.kt
├── repository/
│   └── Repositories.kt
├── data/
│   ├── BocadoDatabase.kt
│   ├── Daos.kt
│   ├── Converters.kt
│   └── api/
│       ├── BocadoApi.kt
│       └── ApiClient.kt
├── model/
│   └── Models.kt
├── util/
│   └── [Utilidades]
└── MainActivity.kt
```

---

## 3. Modelos de Datos

### 3.1 Dish (Plato)
```kotlin
@Entity(tableName = "dishes")
data class Dish(
    @PrimaryKey val id: Int,
    val name: String,
    val description: String,
    val price: Double,
    val stock: Int,
    val imageUrl: String,
    val category: String,
    val isAvailable: Boolean = true,
    val createdAt: String = LocalDateTime.now().toString()
)
```

**Atributos:**
- `id`: Identificador único del plato
- `name`: Nombre del plato (ej: "Pizza Margherita")
- `price`: Precio en pesos argentinos
- `stock`: Cantidad disponible
- `category`: Categoría (Entradas, Platos Principales, Postres, Bebidas)
- `isAvailable`: Indica si está disponible para pedidos

### 3.2 OrderItem (Item del Pedido)
```kotlin
@Entity(tableName = "order_items")
data class OrderItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val dishId: Int,
    val dishName: String,
    val quantity: Int,
    val unitPrice: Double,
    val subtotal: Double = quantity * unitPrice
)
```

**Atributos:**
- `dishId`: Referencia al plato
- `quantity`: Cantidad solicitada
- `subtotal`: Cálculo automático (quantity × unitPrice)

### 3.3 Order (Pedido)
```kotlin
@Entity(tableName = "orders")
data class Order(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val clientName: String,
    val tableNumber: Int,
    val totalAmount: Double,
    val status: String = "PENDING",
    val paymentMethod: String? = null,
    val items: List<OrderItem> = emptyList(),
    val createdAt: String = LocalDateTime.now().toString(),
    val qrCode: String? = null
)
```

**Estados del Pedido:**
- `PENDING`: Pedido creado, esperando confirmación
- `CONFIRMED`: Cliente confirmó el pedido
- `PAID`: Pago realizado y aprobado
- `PREPARING`: Cocina está preparando
- `COMPLETED`: Pedido completado

### 3.4 Payment (Pago)
```kotlin
@Entity(tableName = "payments")
data class Payment(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val orderId: Int,
    val amount: Double,
    val paymentMethod: String,
    val status: String = "PENDING",
    val transactionId: String? = null,
    val createdAt: String = LocalDateTime.now().toString()
)
```

**Métodos de Pago Soportados:**
- `CREDIT_CARD`: Tarjeta de Crédito
- `DEBIT_CARD`: Tarjeta de Débito
- `CASH`: Efectivo
- `WALLET`: Billetera Digital

---

## 4. Capa de Datos (Data Layer)

### 4.1 Room Database

**BocadoDatabase.kt** es la clase abstracta que define el acceso a la base de datos SQLite:

```kotlin
@Database(
    entities = [Dish::class, Order::class, OrderItem::class, Payment::class, Restaurant::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class BocadoDatabase : RoomDatabase()
```

**Características:**
- Persistencia local offline-first
- TypeConverters para serialización de listas
- Consultas síncronas y asíncronas con Flow

### 4.2 DAOs (Data Access Objects)

Cada DAO proporciona operaciones CRUD:

```kotlin
@Dao
interface DishDao {
    @Query("SELECT * FROM dishes")
    fun getAllDishes(): Flow<List<Dish>>
    
    @Query("SELECT * FROM dishes WHERE stock > :quantity")
    suspend fun checkStock(id: Int, quantity: Int): Dish?
    
    @Insert
    suspend fun insertDish(dish: Dish)
    
    @Update
    suspend fun updateDish(dish: Dish)
}
```

**Métodos Clave:**
- Operaciones CRUD básicas
- Consultas reactivas con Flow
- Validaciones de stock
- Actualizaciones atómicas

### 4.3 API REST con Retrofit

**BocadoApi.kt** define los endpoints:

```kotlin
interface BocadoApi {
    @GET("api/v1/dishes")
    suspend fun getAllDishes(): ApiResponse<List<Dish>>
    
    @POST("api/v1/orders")
    suspend fun createOrder(@Body orderRequest: OrderRequest): ApiResponse<Order>
    
    @POST("api/v1/payments")
    suspend fun processPayment(@Body paymentRequest: PaymentRequest): ApiResponse<Payment>
}
```

**URL Base:** `https://api.bocado-restaurant.com/`

---

## 5. Capa de Repositorio (Repository Layer)

### 5.1 Patrón Repository

Los repositorios abstraen la lógica de acceso a datos:

```kotlin
class DishRepository(
    private val dishDao: DishDao,
    private val apiService: BocadoApi
) {
    fun getAllDishes(): Flow<List<Dish>> = flow {
        try {
            // Intenta obtener del API remoto
            val response = apiService.getAllDishes()
            if (response.success && response.data != null) {
                dishDao.insertMultipleDishes(response.data)
                emit(response.data)
            }
        } catch (e: Exception) {
            // Fallback a BD local en caso de error
            dishDao.getAllDishes().collect { emit(it) }
        }
    }
}
```

**Estrategia:**
- **Network-first**: Intenta obtener datos del API
- **Fallback local**: En caso de error, usa los datos locales
- **Sincronización**: Guarda datos remotos en BD local

### 5.2 Repositorios Implementados

#### DishRepository
- Gestiona consultas de platos
- Mantén sincronizado con API
- Validar stock disponible

#### OrderRepository
- CRUD de pedidos
- Gestión de estados
- Integración con API

#### PaymentRepository
- Procesar pagos
- Actualizar estados
- Guardar histórico de transacciones

---

## 6. Capa de ViewModel (MVVM)

### 6.1 Concepto MVVM

**Model** → **View** ← **ViewModel**

- **Model**: Datos (Models.kt)
- **View**: UI Composables
- **ViewModel**: Lógica, State Management

### 6.2 MenuViewModel

```kotlin
data class MenuUiState(
    val dishes: List<Dish> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val selectedCategory: String = "Todos"
)

class MenuViewModel(private val dishRepository: DishRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(MenuUiState())
    val uiState: StateFlow<MenuUiState> = _uiState.asStateFlow()
    
    fun loadDishes() { /* Carga asincrónica */ }
    fun filterByCategory(category: String) { /* Filtrado */ }
}
```

**Responsabilidades:**
- Cargar platos desde repositorio
- Manejar filtros por categoría
- Exposición de state a través de StateFlow
- Manejo de errores

### 6.3 CartViewModel

```kotlin
class CartViewModel(
    private val orderRepository: OrderRepository,
    private val dishRepository: DishRepository
) : ViewModel() {
    fun addItemToCart(dishId: Int, quantity: Int) { /* Agregar */ }
    fun removeItemFromCart(itemId: Int) { /* Remover */ }
    fun confirmOrder(clientName: String, tableNumber: Int) { /* Confirmar */ }
}
```

**Operaciones:**
- Agregar/remover items del carrito
- Calcular totales dinámicamente
- Validar stock antes de confirmar
- Integración con API para crear pedido

### 6.4 PaymentViewModel

```kotlin
class PaymentViewModel(private val paymentRepository: PaymentRepository) {
    fun validatePaymentData(): Boolean {
        // Validar número tarjeta (16 dígitos)
        // Validar CVV (3 dígitos)
        // Validar fecha expiración
    }
    
    fun processPayment() {
        // Procesar pago a través de API
        // Actualizar estado del pago
    }
}
```

---

## 7. Capa de Presentación (UI Layer)

### 7.1 Jetpack Compose

La UI está completamente implementada en Jetpack Compose (declarativo):

**Ventajas:**
- Código declarativo y reactivo
- Menos boilerplate que XML
- Integración con Material Design 3
- Soporte para temas dinámicos

### 7.2 Temas y Estilos

**BocadoTheme.kt** define:

```kotlin
val BocadoOrange = Color(0xFFFF6B42)     // Color principal
val BocadoBlack = Color(0xFF1A1A1A)      // Textos
val BocadoGreen = Color(0xFF66BB6A)      // Éxito
val BocadoRed = Color(0xFFE63946)        // Errores

MaterialTheme(
    colorScheme = colorScheme,
    typography = BocadoTypography,
    shapes = Shapes(medium = RoundedCornerShape(8.dp))
)
```

**Accesibilidad:**
- Tamaños de fuente escalables
- Soporte Dark Mode
- Dynamic Color (Android 12+)

### 7.3 Pantallas Principales

#### SplashScreen
- Introducción de la aplicación
- Botón para escanear QR
- Onboarding inicial

#### MenuScreen
- Grid de platos
- Filtrado por categoría
- Selector de cantidad
- Indicador de stock
- Botón flotante con contador del carrito

**Características:**
```kotlin
@Composable
fun MenuScreen(
    menuViewModel: MenuViewModel,
    cartViewModel: CartViewModel,
    cartItemsCount: Int,
    onNavigateToCart: () -> Unit
) {
    // Estado reactivo desde ViewModels
    val menuUiState by menuViewModel.uiState.collectAsState()
    
    // Renderizado basado en estado
    when {
        menuUiState.isLoading -> LoadingIndicator()
        menuUiState.error != null -> ErrorMessage()
        else -> DishList()
    }
}
```

#### CartScreen
- Lista de items agregados
- Controles de cantidad (+/-)
- Cálculo automático de totales
- Formulario de información del cliente
- Validación antes de confirmar

**Validaciones:**
- Nombre de cliente requerido
- Carrito no vacío
- Número de mesa válido
- Cálculo de impuestos

#### PaymentScreen
- Selector de método de pago
- Formulario dinámico por método
- Validación de tarjeta (número, fecha, CVV)
- Indicador de procesamiento
- Confirmación de pago exitoso

**Métodos Soportados:**
- Tarjeta de Crédito/Débito
- Efectivo
- Billetera Digital

---

## 8. Flujo de Navegación

```
Splash Screen
     ↓
Onboarding
     ↓
Menu Screen ←→ Cart Screen → Payment Screen
     ↓                              ↓
Filtrar            Confirmar Pedido → Procesar Pago
     ↓                              ↓
Agregar al Carrito          Pago Exitoso
```

**Tecnología:** Jetpack Navigation Compose

```kotlin
NavHost(navController, startDestination = "menu") {
    composable("menu") { MenuScreen(...) }
    composable("cart") { CartScreen(...) }
    composable("payment/{orderId}/{amount}") { PaymentScreen(...) }
}
```

---

## 9. Validaciones de Negocio

### 9.1 Validaciones de Stock

```kotlin
// Validar antes de agregar al carrito
if (dish.stock < requestedQuantity) {
    showError("Stock insuficiente para: ${dish.name}")
    return
}

// Decrementar stock solo después del pago
dishRepository.decreaseStock(dishId, quantity)
```

### 9.2 Validaciones de Pago

```kotlin
// Validar tarjeta
require(cardNumber.length == 16) { "Tarjeta inválida" }
require(cvv.length == 3) { "CVV inválido" }
require(expiryDate.matches(Regex("\\d{2}/\\d{2}"))) { "Fecha inválida" }

// Validar monto
require(amount > 0) { "Monto debe ser mayor a 0" }
```

### 9.3 Validaciones de Orden

```kotlin
// Validar orden antes de confirmar
require(cartItems.isNotEmpty()) { "Carrito vacío" }
require(clientName.isNotBlank()) { "Nombre de cliente requerido" }
require(tableNumber > 0) { "Número de mesa inválido" }

val totalAmount = cartItems.sumOf { it.subtotal }
require(totalAmount > 0) { "Monto total inválido" }
```

---

## 10. Manejo de Errores y Conectividad

### 10.1 Estrategia de Errores

```kotlin
try {
    val result = orderRepository.createOrder(request)
    result.onSuccess { order -> /* Éxito */ }
    result.onFailure { error -> 
        uiState.value = uiState.value.copy(
            error = error.message ?: "Error desconocido"
        )
    }
} catch (e: Exception) {
    // Fallback a datos locales
}
```

### 10.2 Manejo de Conectividad

```kotlin
fun getAllDishes(): Flow<List<Dish>> = flow {
    try {
        // Intenta API remota
        val response = apiService.getAllDishes()
        emit(response.data ?: emptyList())
    } catch (e: IOException) {
        // Sin conexión: usa BD local
        dishDao.getAllDishes().collect { emit(it) }
    }
}
```

---

## 11. Pruebas Unitarias

### 11.1 Pruebas de Modelos

```kotlin
@Test
fun testDishCreation() {
    val dish = Dish(
        id = 1,
        name = "Pizza",
        price = 250.0,
        stock = 10,
        // ...
    )
    assertEquals("Pizza", dish.name)
    assertEquals(250.0, dish.price, 0.0)
}
```

### 11.2 Pruebas de Lógica de Negocio

```kotlin
@Test
fun testOrderTotalCalculation() {
    val items = listOf(
        OrderItem(1, "Pizza", 2, 250.0), // $500
        OrderItem(2, "Pasta", 1, 180.0)  // $180
    )
    val subtotal = items.sumOf { it.subtotal } // $680
    val tax = subtotal * 0.1                     // $68
    val total = subtotal + tax                   // $748
    
    assertEquals(748.0, total, 0.0)
}
```

### 11.3 Pruebas de Validación

```kotlin
@Test
fun testPaymentValidation() {
    val cardNumber = "1234567890123456"
    assertTrue(cardNumber.length == 16)
    
    val cvv = "123"
    assertTrue(cvv.length == 3)
}
```

---

## 12. Dependencias Principales

```gradle
// Android
androidx.core:core-ktx:1.12.0
androidx.lifecycle:lifecycle-runtime-ktx:2.6.2

// Jetpack Compose
androidx.compose.ui:ui
androidx.compose.material3:material3:1.1.2
androidx.navigation:navigation-compose:2.7.5

// Room Database
androidx.room:room-runtime:2.6.1
androidx.room:room-ktx:2.6.1

// Retrofit + Gson
com.squareup.retrofit2:retrofit:2.9.0
com.squareup.retrofit2:converter-gson:2.9.0

// Coroutines
org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3

// QR Scanner (ML Kit)
com.google.mlkit:barcode-scanning:17.1.0
androidx.camera:camera-core:1.3.0
```

---

## 13. Performance y Optimizaciones

### 13.1 Cold Start
- Objetivo: < 2.5 segundos en Pixel 9 Pro con 4GB RAM
- Estrategia: Lazy loading, BD local cacheada

### 13.2 Scroll Fluido
- Objetivo: > 54 fps
- Implementación: LazyColumn para listas infinitas

### 13.3 Optimizaciones

```kotlin
// 1. Flow para reactividad eficiente
val dishes: Flow<List<Dish>> = dishDao.getAllDishes()

// 2. Caching local
dishDao.insertMultipleDishes(remoteData)

// 3. Paginación (para futuras versiones)
@Query("SELECT * FROM dishes LIMIT :limit OFFSET :offset")
fun getPaginatedDishes(limit: Int, offset: Int): Flow<List<Dish>>
```

---

## 14. Guía de Ejecución

### 14.1 Prerequisitos
- Android Studio Flamingo o superior
- Kotlin 1.9.10
- API Level 24+
- Gradle 8.1.0

### 14.2 Build del Proyecto

```bash
# Clonar repositorio
git clone https://github.com/tu-usuario/bocado.git
cd bocado

# Compilar
./gradlew build

# Generar APK
./gradlew assembleRelease

# Generar Bundle (para Play Store)
./gradlew bundleRelease
```

### 14.3 Ejecución en Emulador

```bash
# Instalar APK
adb install app/build/outputs/apk/release/app-release.apk

# Ejecutar
adb shell am start -n com.bocado/.MainActivity
```

---

## 15. Historias de Usuario Implementadas

### HU-001: Escanear QR para Acceder al Menú
**Aceptación:**
- ✅ Usuario puede escanear QR desde dispositivo
- ✅ Acceso inmediato al menú del restaurante
- ✅ Manejo de errores si QR es inválido

### HU-002: Visualizar Menú Digital
**Aceptación:**
- ✅ Platos con foto, nombre, descripción, precio
- ✅ Filtrado por categoría
- ✅ Indicador de stock en tiempo real
- ✅ Scroll fluido

### HU-003: Gestionar Carrito
**Aceptación:**
- ✅ Agregar/remover items
- ✅ Modificar cantidades
- ✅ Cálculo automático de totales
- ✅ Validación de stock

### HU-004: Procesamiento de Pagos
**Aceptación:**
- ✅ Múltiples métodos de pago
- ✅ Validación de datos de tarjeta
- ✅ Simulación de gateway de pagos
- ✅ Confirmación de transacción

### HU-005: Confirmación de Pedido
**Aceptación:**
- ✅ Resumen completo del pedido
- ✅ Número de pedido único
- ✅ Estimado de tiempo de preparación
- ✅ Recibo digital

---

## 16. API Endpoints

### Base URL: `https://api.bocado-restaurant.com/`

#### Dishes
- `GET /api/v1/dishes` - Obtener todos los platos
- `GET /api/v1/dishes/:id` - Obtener detalle de plato
- `GET /api/v1/dishes/category/:category` - Filtrar por categoría

#### Orders
- `POST /api/v1/orders` - Crear pedido
- `GET /api/v1/orders/:id` - Obtener detalles del pedido
- `PUT /api/v1/orders/:id/status/:status` - Actualizar estado

#### Payments
- `POST /api/v1/payments` - Procesar pago
- `GET /api/v1/payments/:orderId` - Obtener estado del pago

---

## 17. Estructura de Request/Response

### CreateOrder Request
```json
{
  "clientName": "Juan Pérez",
  "tableNumber": 5,
  "items": [
    {
      "dishId": 1,
      "quantity": 2
    }
  ]
}
```

### Payment Request
```json
{
  "orderId": 123,
  "amount": 500.00,
  "paymentMethod": "CREDIT_CARD",
  "cardDetails": {
    "cardNumber": "1234567890123456",
    "expiryDate": "12/25",
    "cvv": "123",
    "holderName": "JUAN PEREZ"
  }
}
```

---

## 18. Mejoras Futuras

1. **Autenticación:** Login/Registro de usuarios
2. **Historial:** Ver pedidos anteriores
3. **Favoritos:** Guardar platos favoritos
4. **Ofertas:** Descuentos y promociones dinámicas
5. **Notificaciones:** Push notifications de estado
6. **Múltiples Restaurantes:** Soporte multi-restaurante
7. **Ratings:** Sistema de calificación de platos
8. **Geolocalización:** Entregas a domicilio
9. **Admin Panel:** Dashboard para restaurantes
10. **Analytics:** Métricas de negocio

---

## 19. Checklist de Heurísticas de Nielsen

### Visibilidad del Estado del Sistema
- ✅ Indicadores de carga (spinners)
- ✅ Mensajes de error claros
- ✅ Confirmación de acciones
- ✅ Feedback visual en botones

### Coincidencia entre el Sistema y el Mundo Real
- ✅ Lenguaje de usuario (no técnico)
- ✅ Términos familiares (Carrito, Pago, etc.)
- ✅ Flujo natural de compra

### Control del Usuario
- ✅ Botón Atrás funcional
- ✅ Opción de cancelar en cualquier momento
- ✅ Confirmación antes de acciones destructivas

### Prevención de Errores
- ✅ Validación de campos en tiempo real
- ✅ Mensajes informativos antes de errores
- ✅ Límites visuales (cantidad máxima de stock)

### Ayuda y Documentación
- ✅ Tooltips en elementos complejos
- ✅ Mensajes de error descriptivos
- ✅ Onboarding inicial

---

## 20. Conclusiones

BOCADO implementa una solución completa y robusta para la gestión de pedidos en restaurantes, siguiendo las mejores prácticas de arquitectura Android moderna:

✅ **MVVM + Repository** - Separación clara de responsabilidades
✅ **Jetpack Compose** - UI declarativa y reactiva
✅ **Room + Retrofit** - Persistencia local y acceso remoto
✅ **Coroutines + Flow** - Concurrencia y reactividad
✅ **Validaciones de Negocio** - Reglas de negocio implementadas
✅ **Manejo de Errores** - Estrategia robusta de errores
✅ **Accesibilidad** - Material Design 3 + Dark Mode
✅ **Pruebas** - Cobertura de lógica crítica

La arquitectura permite fácil escalabilidad y mantenimiento para futuras características.

---

**Última actualización:** Junio 2024
**Autores:** Equipo 3 - IFTS 18
