# GUÍA DE PRESENTACIÓN - BOCADO

## Introducción (1 minuto)

"Buenas tardes profesores, somos el Equipo 3. Hoy les presentamos **BOCADO**, una solución innovadora de gestión de pedidos para restaurantes basada en tecnología QR.

¿Alguna vez esperaste mucho para ser atendido en un restaurante? ¿O tuviste problemas con la comunicación del pedido a cocina? BOCADO resuelve esto permitiendo que los clientes ordenen desde sus propios dispositivos escaneando un código QR, centralizando todo el proceso."

---

## Problema (1 minuto)

### El Problema
- 😤 Clientes: Esperas largas para ser atendidos
- 🤯 Meseros: Anotaciones manuales propensas a errores
- 😞 Cocina: Pedidos inciertos o mal comunicados
- 📉 Restaurante: Pérdida de eficiencia y dinero

### El Contexto
En Argentina, especialmente en Buenos Aires, muchos restaurantes aún usan sistemas de pedidos tradicionales. Esto genera:
- Errores en órdenes (50% de devoluciones)
- Tiempo promedio de 15-20 minutos para recibir atención
- Dificultad en control de inventario
- Inconsistencia en horarios pico

---

## Solución (2 minutos)

### ¿Qué es BOCADO?

Una aplicación mobile 100% en **Kotlin** con arquitectura **MVVM + Repository** que permite:

1. **Escanear QR** → Acceso al menú
2. **Ver menú digital** → Platos con fotos y precios
3. **Crear pedido** → Seleccionar items y cantidades
4. **Pagar** → Múltiples métodos
5. **Confirmación** → Recibo digital

### Flujo de Usuario

```
Cliente llega → Escanea QR
                    ↓
             Ve menú interactivo
                    ↓
             Selecciona platos
                    ↓
             Revisa carrito
                    ↓
             Completa datos + paga
                    ↓
             Recibe confirmación
                    ↓
         Cocina prepara el pedido
```

---

## Características Técnicas (3 minutos)

### Arquitectura

**Patrón MVVM + Repository Pattern**

```
View (UI)  ←→  ViewModel  ←→  Repository  ←→  Data Layer
Compose        State Mgmt     Abstraction      Room/Retrofit
```

**Ventajas:**
- ✅ Separación clara de responsabilidades
- ✅ Fácil testing
- ✅ UI reactiva y responsiva
- ✅ Manejo eficiente del ciclo de vida

### Tecnologías Utilizadas

| Aspecto | Tecnología | Razón |
|--------|-----------|-------|
| **UI** | Jetpack Compose | Moderna, declarativa, menos código |
| **BD Local** | Room + SQLite | Persistencia offline, consultas rápidas |
| **API REST** | Retrofit + Gson | Consumo de APIs de forma limpia |
| **Async** | Coroutines + Flow | Manejo eficiente de operaciones |
| **Lenguaje** | Kotlin | Seguridad, interoperabilidad |

### Modelos de Datos

1. **Dish** - Platos del menú
2. **Order** - Pedidos de clientes
3. **OrderItem** - Items dentro de un pedido
4. **Payment** - Información de pagos
5. **Restaurant** - Datos del restaurante

---

## Validaciones de Negocio (2 minutos)

### Stock
```
Si cantidad solicitada > stock disponible
    → Mostrar error: "Stock insuficiente"
```

### Pedidos
```
Validaciones previas a confirmar:
✓ Nombre de cliente requerido
✓ Número de mesa válido
✓ Carrito no vacío
✓ Total > 0
```

### Pagos
```
Si método = Tarjeta:
    ✓ Número: 16 dígitos
    ✓ Fecha: MM/YY válida
    ✓ CVV: 3 dígitos
    ✓ Titular: requerido
```

### Flujo Transaccional
```
1. Usuario agrega items
2. Valida stock disponible
3. Confirma pedido (BD local)
4. Procesa pago (API)
5. Si pago = APROBADO → Decrementa stock
6. Si pago = RECHAZADO → Mantiene stock igual
```

---

## Demostración Live (10 minutos)

### Escenario 1: Happy Path
1. Abre la app → SplashScreen
2. Presiona "Escanear QR" → MenuScreen
3. Muestra 4 categorías de platos
4. Selecciona "Pizza Margherita" - cantidad 2
5. Presiona Agregar
6. Abre Carrito → Muestra 2 items
7. Completa nombre: "Juan Pérez"
8. Número de mesa: 5
9. Presiona "Confirmar Pedido"
10. Navega a Pago
11. Selecciona "Tarjeta de Crédito"
12. Completa datos ficticios
13. Presiona "Confirmar Pago"
14. Muestra pantalla de éxito con recibo

### Escenario 2: Validaciones
1. Intenta agregar cantidad > stock → Muestra error
2. Intenta confirmar sin nombre → Muestra validación
3. Intenta pagar con CVV de 2 dígitos → Rechaza
4. Comprobación: stock se mantiene igual si pago falla

### Pantallas a Mostrar

**MenuScreen**
- Listado con cards de platos
- Imagen placeholder
- Precio, descripción, stock
- Controles +/- para cantidad
- FAB con contador de carrito

**CartScreen**
- Tabla con items agregados
- Subtotal, impuestos, total
- Formulario de cliente/mesa
- Botones confirmar/limpiar

**PaymentScreen**
- Resumen de monto
- Selector de método de pago
- Formulario dinámico de tarjeta
- Pantalla de confirmación

---

## Performance y Optimizaciones (1 minuto)

### Cold Start
- **Objetivo:** < 2.5 segundos
- **Logrado:** 1.8 segundos en Pixel 9 Pro
- **Técnicas:** Lazy loading, caching local

### Scroll Fluido
- **Objetivo:** > 54 fps
- **Logrado:** 60 fps
- **Técnicas:** LazyColumn, recomposición eficiente

### Manejo de Conectividad
```
Si hay internet:
    → Cargar desde API
    → Guardar en BD local
    
Si no hay internet:
    → Leer desde BD local
    → Mostrar última versión cacheada
```

---

## Pruebas Unitarias (1 minuto)

### Tests Implementados
```kotlin
✅ DishTest - Creación y validación de platos
✅ OrderTest - Estados y cálculos de pedidos
✅ PaymentTest - Estados de pagos
✅ BusinessLogicTest - Validaciones críticas
   - Stock validation
   - Order total calculation
   - Payment validation
```

### Resultados
- **14 tests totales**
- **14 tests pasados** ✅
- **0 tests fallidos** ✅
- **Cobertura:** ~85% en lógica crítica

---

## Accesibilidad (1 minuto)

### Material Design 3
✅ Colores accesibles (contraste WCAG AAA)
✅ Tipografía escalable
✅ Iconografía clara

### Dark Mode
✅ Soporte automático para tema oscuro
✅ Paleta de colores adaptada

### Responsive Design
✅ Funciona en tablets
✅ Rotación de pantalla soportada
✅ Diferentes tamaños de fuente

---

## Decisiones de Diseño (2 minutos)

### ¿Por qué Jetpack Compose?
- ✅ UI moderna y declarativa
- ✅ Menos boilerplate que XML
- ✅ Mejor performance que tradicional
- ✅ Integración perfecta con Kotlin

### ¿Por qué MVVM + Repository?
- ✅ Estándar de la industria
- ✅ Fácil testing (mockeo de repos)
- ✅ Separación clara de capas
- ✅ Escalable para futuras features

### ¿Por qué Room + Retrofit?
- ✅ Persistencia offline-first
- ✅ Consumo de API simplificado
- ✅ Type-safe con Kotlin
- ✅ Part de Jetpack ecosystem

### ¿Por qué Coroutines?
- ✅ Manejo simple de async
- ✅ Cancellable por defecto
- ✅ Mejor performance que threads
- ✅ Integración con Flow (reactivo)

---

## Requisitos Cumplidos (1 minuto)

### Requisitos Funcionales
- ✅ Onboarding inicial
- ✅ 2+ flujos de pantalla
- ✅ CRUD de Dishes/Orders
- ✅ Listados con cards
- ✅ Sensor: Cámara (QR Scanner)
- ✅ Fuentes escalables

### Requisitos No-Funcionales
- ✅ Cold start < 2.5 seg
- ✅ Scroll > 54 fps
- ✅ Manejo de errores de conectividad
- ✅ API Level 24+

### Requisitos Arquitectónicos
- ✅ Kotlin 100%
- ✅ MVVM + Repository
- ✅ Jetpack Compose
- ✅ Retrofit para API
- ✅ Material 3, Dark Mode, Dynamic Color

### Requisitos UI/UX
- ✅ Material Design 3
- ✅ Heurísticas Nielsen (10/10)
- ✅ Figma design system
- ✅ Prototipo alta fidelidad

---

## Métricas del Proyecto

| Métrica | Valor |
|---------|-------|
| Líneas de código | ~3500 |
| Clases Kotlin | 25+ |
| Funciones | 100+ |
| Tests unitarios | 14 |
| Cobertura | 85% lógica crítica |
| Dependencias | 30+ |
| Pantallas | 4 + Onboarding |
| Componentes UI | 15+ |
| API Endpoints | 12+ |
| Validaciones | 8+ |

---

## Aprendizajes y Experiencias (1 minuto)

### Técnicos
1. **MVVM + Repository Pattern** - Arquitectura escalable y testeable
2. **Jetpack Compose** - UI moderno sin XML boilerplate
3. **Coroutines + Flow** - Asincronía elegante en Kotlin
4. **Room Database** - Persistencia local offline-first

### Profesionales
1. **Trabajo en equipo** - Roles definidos (PO, Tech Lead, UX, etc.)
2. **Gestión de proyecto** - Tablero Jira/GitHub Projects
3. **Git workflow** - Branching strategy (GitFlow)
4. **Code review** - PRs con retroalimentación

### Desafíos Resueltos
1. 🔧 Validaciones complejas de negocio
2. 🌐 Manejo offline-first
3. 🚀 Performance en dispositivos con pocos recursos
4. 🎨 Accesibilidad en UI

---

## Roadmap Futuro (1 minuto)

### Fase 2 (Próximas semanas)
- [ ] Autenticación de usuarios
- [ ] Historial de pedidos
- [ ] Sistema de favoritos
- [ ] Notificaciones push

### Fase 3 (Próximos meses)
- [ ] Integraciones MercadoPago
- [ ] Dashboard de administrador
- [ ] Multi-restaurante
- [ ] Analytics avanzados

### Fase 4 (Long-term)
- [ ] Entregas a domicilio
- [ ] Integraciones con POS
- [ ] Machine Learning (recomendaciones)
- [ ] Expansion a otras plataformas (iOS, Web)

---

## Q&A - Posibles Preguntas

### P: ¿Cómo manejan la seguridad de pagos?
**R:** 
- Validamos datos antes de enviar a API
- El CVV nunca se persiste en BD
- Usamos HTTPS para todas las llamadas
- En producción usaríamos tokenización de MercadoPago

### P: ¿Qué pasa si se corta internet?
**R:**
- Los datos locales se guardan en Room
- El usuario puede seguir viendo el menú offline
- Al recuperar conexión, sincroniza automáticamente
- Mostramos un indicador visual del estado

### P: ¿Cuál es el límite de capacidad?
**R:**
- BD local: +10,000 platos sin problema
- Scroll fluido con 100+ items
- En producción usaríamos paginación

### P: ¿Cómo testean validaciones?
**R:**
- 14 tests unitarios automatizados
- Validación de stock, pagos, pedidos
- Casos edge: cantidad 0, monto negativo, etc.

### P: ¿Por qué MVVM y no MVI o MVP?
**R:**
- MVVM es el estándar de Google/JetBrains
- Mejor integración con Compose
- Más comunidad y recursos
- Escala bien a proyectos grandes

---

## Cierre (1 minuto)

"En conclusión, BOCADO es una solución moderna que:

✅ **Resuelve un problema real** - Ineficiencia en restaurantes
✅ **Usa tecnología moderna** - Kotlin, Compose, MVVM
✅ **Sigue buenas prácticas** - Arquitectura escalable, testeable
✅ **Es accesible** - Material Design 3, Dark Mode
✅ **Está lista para producción** - Tests, validaciones, error handling

Hemos aprendido mucho sobre arquitectura Android moderna, work en equipo, y cómo construir aplicaciones de calidad.

Estamos listos para pasar a H2 donde ampliaremos funcionalidades, mejoraremos analytics, e integraremos pagos reales.

**¡Gracias por su atención!**"

---

## Notas para la Presentación

### Visual Aids
- 📱 Demo live en dispositivo real o emulador
- 📊 Gráficos de arquitectura proyectados
- 🎨 Mostrar Figma durante la explicación UI
- 📈 Screenshots de tests

### Timing
- Introducción: 1 min
- Problema: 1 min
- Solución: 2 min
- Técnicas: 3 min
- Demo: 10 min ← **Más tiempo aquí**
- Tests: 1 min
- Futuro: 1 min
- Total: **19 minutos** (+ Q&A)

### Recomendaciones
- ✅ Ensaya la demo varias veces
- ✅ Ten respuestas alternativas memorizada
- ✅ Lleva tu laptop y cable HDMI
- ✅ Instala APK en teléfono como backup
- ✅ Prepara internet estable para demo
- ✅ Viste profesional (no necesariamente formal)
- ✅ Habla claro y con pausas
- ✅ Haz contacto visual con profesores

---

**Éxito en la presentación! 🚀**

Equipo 3 - IFTS 18
