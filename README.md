# BOCADO - Sistema de Gestión de Pedidos para Restaurantes

![BOCADO](https://img.shields.io/badge/BOCADO-v1.0.0-orange)
![Android](https://img.shields.io/badge/Android-API%2024-green)
![Kotlin](https://img.shields.io/badge/Kotlin-1.9.10-purple)

**BOCADO** es una aplicación mobile innovadora desarrollada para restaurantes que necesitan un sistema eficiente y moderno de gestión de pedidos basado en códigos QR.

## Características Principales

### Para Clientes
- **Escaneo de QR** - Acceso rápido al menú digital
- **Menú Digital Interactivo** - Visualización de platos con fotos y descripciones
- **Carrito Inteligente** - Gestión fácil de pedidos
- **Múltiples Métodos de Pago** - Tarjeta, efectivo, billetera digital
- **Confirmación Instantánea** - Recibo digital del pedido

### Para Restaurantes
- **Control de Stock** - Gestión automática de inventario
- **Órdenes en Tiempo Real** - Cocina recibe pedidos al instante
- **Análisis de Ventas** - Datos de comportamiento del cliente
- **Seguridad** - Transacciones encriptadas

## Arquitectura

```
┌──────────────────────────┐
│  UI Layer (Compose)      │ ← Screens, Components, Navigation
├──────────────────────────┤
│  ViewModel Layer (MVVM)  │ ← State Management
├──────────────────────────┤
│  Repository Layer        │ ← Data Abstraction
├──────────────────────────┤
│  Data Layer              │ ← Room DB + Retrofit API
└──────────────────────────┘
```

**Patrón:** MVVM + Repository
**Framework UI:** Jetpack Compose
**Base de Datos:** Room (SQLite)
**API REST:** Retrofit + Gson

## Requisitos

- Android Studio Flamingo o superior
- Kotlin 1.9.10
- Gradle 8.1.0
- SDK mínimo: API 24 (Android 7.0)
- SDK destino: API 34 (Android 14)

## Instalación

### 1. Clonar el Repositorio
```bash
git clone https://github.com/tu-usuario/bocado.git
cd bocado
```

### 2. Abrir en Android Studio
```bash
# Android Studio detectará automáticamente
File → Open → Seleccionar carpeta bocado
```

### 3. Configurar Gradle
```bash
# Descargar dependencias
./gradlew build

# O en Windows
gradlew.bat build
```

### 4. Ejecutar en Emulador/Dispositivo
```bash
# Compilar y ejecutar
./gradlew installDebug

# O usar Android Studio: Run → Run 'app'
```

## Uso de la Aplicación

### Pantalla de Bienvenida
1. Abre BOCADO
2. Presiona "Escanear QR"

### Menú Digital
1. Visualiza todos los platos disponibles
2. Filtra por categoría (Entradas, Platos Principales, etc.)
3. Presiona el plato para ver detalles
4. Ajusta la cantidad con +/-
5. Presiona "Agregar" al carrito

### Carrito de Compras
1. Revisa todos los items agregados
2. Modifica cantidades o elimina items
3. Ingresa tu nombre y número de mesa
4. Presiona "Confirmar Pedido"

### Pago
1. Selecciona método de pago
2. Completa datos de tarjeta (si aplica)
3. Presiona "Confirmar Pago"
4. Recibe confirmación con número de pedido

## Estructura del Proyecto

## 📁 Estructura del Proyecto

```
app/
└── src/main/
    ├── manifests/
    │   └── AndroidManifest.xml
    │
    └── kotlin+java/
        └── com.bocado/
            │
            ├── data/                      # Capa de datos (Model)
            │   ├── api/
            │   │   ├── ApiClient.kt        # Configuración del cliente HTTP
            │   │   └── BocadoApi.kt        # Definición de endpoints (Retrofit/Ktor)
            │   ├── BocadoDatabase.kt       # Configuración de la base de datos local
            │   ├── Converters.kt           # Type converters para Room
            │   └── Daos.kt                 # Data Access Objects (consultas DB)
            │
            ├── model/                      # Modelos de datos
            │   ├── Models.kt                # Entidades / data classes
            │   └── User.kt                  # Modelo de usuario
            │
            ├── repository/                  # Repositorios (puente entre data y viewmodel)
            │   └── Repositories.kt
            │
            ├── ui/                          # Capa de presentación (View)
            │   ├── screens/
            │   │   ├── CartScreen.kt
            │   │   ├── LoginScreen.kt
            │   │   ├── MenuScreen.kt
            │   │   ├── OrderStatusScreen.kt
            │   │   ├── PaymentScreen.kt
            │   │   ├── RegisterScreen.kt
            │   │   ├── ScannerScreen.kt
            │   │   └── SplashScreen.kt
            │   └── theme/
            │       └── Theme.kt             # Estilos y tema de Jetpack Compose
            │
            ├── util/
            │   └── MockData.kt              # Datos de prueba / utilidades
            │
            ├── viewmodel/                    # Capa de lógica de presentación (ViewModel)
            │   ├── CartViewModel.kt
            │   ├── MenuViewModel.kt
            │   └── PaymentViewModel.kt
            │
            └── MainActivity.kt              # Punto de entrada de la aplicación
```

## Diseño Visual

### Paleta de Colores (Según Figma)
- **Primario:** `#FF6B42` (Naranja BOCADO)
- **Secundario:** `#808080` (Gris)
- **Éxito:** `#66BB6A` (Verde)
- **Error:** `#E63946` (Rojo)
- **Fondo:** `#FFFFFF` (Blanco)
- **Texto:** `#1A1A1A` (Negro)

### Tipografía
- **Headlines:** Bold, 24-32sp
- **Titles:** SemiBold, 18-20sp
- **Body:** Regular, 14-16sp
- **Labels:** Medium, 12-14sp

### Componentes UI
- Material Design 3
- Rounded Corners: 8-12dp
- Spacing: 8dp, 16dp, 24dp, 32dp

## Flujo de Navegación

```
┌─────────────┐
│   Splash    │
└──────┬──────┘
       ↓
┌─────────────┐     ┌──────────┐
│  Onboarding │────→│   Menu   │
└─────────────┘     └────┬─────┘
                         ↓
                    ┌──────────┐
                    │  Carrito │
                    └────┬─────┘
                         ↓
                    ┌──────────┐
                    │  Pago    │
                    └────┬─────┘
                         ↓
                  ┌─────────────┐
                  │  Confirmado │
                  └─────────────┘
```

## Modelos de Datos

### Dish (Plato)
```kotlin
id: Int                  // ID único
name: String             // Nombre del plato
description: String      // Descripción
price: Double            // Precio
stock: Int               // Stock disponible
imageUrl: String         // URL de imagen
category: String         // Categoría
isAvailable: Boolean     // Disponibilidad
```

### Order (Pedido)
```kotlin
id: Int                  // ID único
clientName: String       // Nombre del cliente
tableNumber: Int         // Número de mesa
totalAmount: Double      // Total con impuestos
status: String           // PENDING → CONFIRMED → PAID
items: List<OrderItem>   // Items del pedido
```

### Payment (Pago)
```kotlin
id: Int                  // ID único
orderId: Int             // ID del pedido
amount: Double           // Monto
paymentMethod: String    // CREDIT_CARD, DEBIT_CARD, CASH, WALLET
status: String           // PENDING, APPROVED, REJECTED
transactionId: String    // ID de transacción
```

## Validaciones de Negocio

### Stock
- No permite agregar cantidad mayor al stock disponible
- Stock se decrementa solo al aprobar el pago
- Muestra alerta si stock es bajo

### Pedidos
- Requiere nombre de cliente
- Número de mesa válido (> 0)
- No permite pedido vacío

### Pagos
- Tarjeta: 16 dígitos
- Fecha expiración: MM/YY válida
- CVV: 3 dígitos
- Monto: mayor a 0

## Pruebas

### Ejecutar Pruebas Unitarias
```bash
./gradlew test
```

### Pruebas Incluidas
- Creación de modelos
- Cálculos de totales
- Validaciones de negocio
- Transiciones de estado
- Validaciones de tarjeta

## API REST

### Base URL
```
https://api.bocado-restaurant.com/
```

### Endpoints Principales

#### Dishes
```
GET    /api/v1/dishes              - Todos los platos
GET    /api/v1/dishes/:id          - Detalle de plato
GET    /api/v1/dishes/category/:cat - Por categoría
```

#### Orders
```
POST   /api/v1/orders              - Crear pedido
GET    /api/v1/orders/:id          - Ver pedido
PUT    /api/v1/orders/:id/status   - Actualizar estado
```

#### Payments
```
POST   /api/v1/payments            - Procesar pago
GET    /api/v1/payments/:orderId   - Ver estado
```

## Seguridad

- Validación de datos de entrada
- HTTPS para todas las peticiones
- Encriptación de datos sensibles
- Manejo seguro de CVV (no se persiste)
- Tokens de autorización (futuro)

## Performance

- **Cold Start:** < 2.5 segundos
- **Scroll Fluido:** > 54 fps
- **Carga de Imagen:** Lazy loading
- **BD Local:** Caché offline-first

## Stack Tecnológico

### Lenguaje
- Kotlin 1.9.10

### Frameworks
- Android Jetpack (Core, Lifecycle, Room, Navigation)
- Jetpack Compose (UI)
- Kotlin Coroutines (Concurrencia)

### Librerías Externas
- Retrofit 2.9.0 (HTTP Client)
- Gson 2.10.1 (JSON Serialization)
- OkHttp 4.11.0 (HTTP Interceptor)
- ML Kit (QR Scanner)
- CameraX (Camera API)

### Bases de Datos
- Room (SQLite)
- DataStore (Preferences)

## Mejoras Futuras

- [ ] Autenticación de usuarios
- [ ] Historial de pedidos
- [ ] Favoritos
- [ ] Sistema de promociones
- [ ] Notificaciones push
- [ ] Multi-idioma
- [ ] Soporte offline mejorado
- [ ] Integraciones con MercadoPago
- [ ] Dashboard de administrador
- [ ] Analytics avanzados

## Equipo

**Equipo 3 - IFTS 18**
- **Nahuel David Díaz Zapata** - Backend/Data
- **Victoria Escobar Quarin** - UI/UX
- **Hernán Machado** - Arquitectura/DevOps

## Licencia

Este proyecto está bajo licencia MIT. Ver archivo `LICENSE` para más detalles.

## Contacto y Soporte

- Email: bocado@ifts18.edu.ar
- GitHub: [BOCADO Repository](https://github.com/tu-usuario/bocado)
- WhatsApp: +54 11 XXXX-XXXX

## Documentación Completa

Para documentación técnica detallada, ver:
- [DOCUMENTACION_TECNICA.md](./DOCUMENTACION_TECNICA.md)
- [Figma Design System](https://figma.com/bocado-design)

---

**Versión:** 1.0.0
**Última actualización:** Junio 2026
**Estado:** En desarrollo - H1 completado 

¡Gracias por usar BOCADO! 
