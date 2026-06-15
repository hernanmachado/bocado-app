# GUÍA DE DEPLOYMENT - BOCADO

## Contenido

1. [Build para Desarrollo](#build-desarrollo)
2. [Build para Producción](#build-producción)
3. [Instalación en Dispositivos](#instalación)
4. [Testing Antes de Release](#testing)
5. [Publicación en Play Store](#play-store)
6. [Troubleshooting](#troubleshooting)

---

## Build para Desarrollo

### Android Studio (GUI)

```
1. Abre Android Studio
2. Clic en: Build → Build Bundle(s) / APK(s) → Build APK(s)
3. Espera a que compile
4. Android Studio mostrará localización del APK
```

### Terminal (Gradle)

```bash
# Debug APK
./gradlew assembleDebug

# Output: app/build/outputs/apk/debug/app-debug.apk

# Ejecutar directamente
./gradlew installDebug

# O en Windows
gradlew.bat assembleDebug
```

### Emulador

```bash
# Listar emuladores disponibles
emulator -list-avds

# Iniciar emulador
emulator -avd Pixel_9_API_34

# O desde Android Studio: 
# Tools → Device Manager → Launch
```

---

## Build para Producción

### 1. Crear Keystore

```bash
# Generar keystore (primera vez)
keytool -genkey -v -keystore bocado-release.keystore \
  -keyalg RSA -keysize 2048 -validity 10000 \
  -alias bocado-key

# Datos requeridos:
# Keystore password: [Tu contraseña segura]
# Key password: [Tu contraseña]
# First and Last Name: BOCADO Release
# Organizational Unit: Development
# Organization: IFTS 18
# City: Buenos Aires
# Country: AR
```

**⚠️ IMPORTANTE:** Guarda el keystore en lugar seguro y haz backup!

### 2. Configurar Gradle para Firmar

Edita `app/build.gradle.kts`:

```gradle
android {
    // ... otros configs ...
    
    signingConfigs {
        create("release") {
            storeFile = file("../bocado-release.keystore")
            storePassword = System.getenv("KEYSTORE_PASSWORD") ?: "default"
            keyAlias = "bocado-key"
            keyPassword = System.getenv("KEY_PASSWORD") ?: "default"
        }
    }
    
    buildTypes {
        release {
            signingConfig = signingConfigs["release"]
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
}
```

### 3. Generar APK Firmado

```bash
# Export variables de entorno
export KEYSTORE_PASSWORD="tu_password"
export KEY_PASSWORD="tu_password"

# Compilar APK de release
./gradlew assembleRelease

# Output: app/build/outputs/apk/release/app-release.apk
```

### 4. Generar AAB (App Bundle)

```bash
# Para Google Play Store
./gradlew bundleRelease

# Output: app/build/outputs/bundle/release/app-release.aab

# El AAB es requerido para publicar en Play Store
```

### 5. Verificar Firma

```bash
# Verificar que el APK está correctamente firmado
jarsigner -verify -verbose -certs app/build/outputs/apk/release/app-release.apk

# Extraer información de firma
keytool -printcert -jarfile app/build/outputs/apk/release/app-release.apk
```

---

## Instalación en Dispositivos

### En Dispositivo Físico (USB)

```bash
# 1. Conectar dispositivo y habilitar DEBUG USB
# Settings → Developer Options → USB Debugging ON

# 2. Verificar conexión
adb devices

# 3. Instalar APK
adb install app/build/outputs/apk/debug/app-debug.apk

# 4. Esperar mensaje: Success

# 5. Lanzar app
adb shell am start -n com.bocado/.MainActivity
```

### En Emulador

```bash
# 1. Iniciar emulador
emulator -avd Pixel_9_API_34

# 2. Esperar a que cargue

# 3. Instalar
./gradlew installDebug

# 4. O manualmente:
adb install app/build/outputs/apk/debug/app-debug.apk
```

### Instalar Release APK Manualmente

```bash
# Copiar APK a dispositivo
adb push app/build/outputs/apk/release/app-release.apk /data/local/tmp/

# Instalar
adb shell pm install /data/local/tmp/app-release.apk
```

### Desde Archivo (Sin adb)

```bash
# 1. Transferir app-release.apk via:
#    - Email
#    - Google Drive
#    - USB

# 2. En dispositivo:
#    - Settings → Security → Unknown Sources ON
#    - Abrir file manager
#    - Navegar a APK
#    - Tap para instalar

# 3. Confirmar permisos
```

---

## Testing Antes de Release

### Checklist de Testing

#### Funcionalidad
- [ ] Menu carga correctamente
- [ ] Filtros por categoría funcionan
- [ ] Agregar items al carrito
- [ ] Modificar cantidades
- [ ] Remover items
- [ ] Confirmar pedido
- [ ] Procesar pago (test)
- [ ] Pantalla de éxito

#### Validaciones
- [ ] No permite stock > disponible
- [ ] Validar nombre de cliente requerido
- [ ] Validar número de mesa > 0
- [ ] Validar tarjeta 16 dígitos
- [ ] Validar CVV 3 dígitos
- [ ] Validar fecha expiración

#### Performance
- [ ] Cold start < 2.5 seg
- [ ] Scroll fluido (> 54 fps)
- [ ] Sin crashes
- [ ] Memoria estable

#### UI/UX
- [ ] Botón atrás funciona
- [ ] Navegación fluida
- [ ] Mensajes de error claros
- [ ] Colores legibles
- [ ] Sin elementos cortados

#### Conectividad
- [ ] Funciona con internet
- [ ] Maneja desconexiones
- [ ] Reintenta después de error
- [ ] Carga desde BD local

#### Permisos
- [ ] Solicita permisos correctos
- [ ] Funciona sin permisos (graceful degradation)
- [ ] No pide permisos innecesarios

### Automated Testing

```bash
# Ejecutar tests unitarios
./gradlew test

# Ejecutar tests instrumentados (en emulador)
./gradlew connectedAndroidTest

# Con reporte
./gradlew test --info

# Generar reporte HTML
./gradlew testReport
```

### Manual Testing Checklist

```bash
# 1. Clean install
./gradlew clean
./gradlew build

# 2. Install fresh
adb uninstall com.bocado
./gradlew installDebug

# 3. Verificar:
# - Primera ejecución (onboarding)
# - Permisos solicitados
# - Pantalla splash
# - Menu carga
# - Sin crashes

# 4. Scenario testing
# - Happy path: Add → Confirm → Pay → Success
# - Error path: Invalid input → Error message
# - Edge cases: Empty cart, zero stock, network error
```

---

## Publicación en Play Store

### Pre-requisitos

1. **Google Play Developer Account**
   ```
   - Costo: $25 USD (una sola vez)
   - URL: https://play.google.com/console
   ```

2. **Aplicación registrada en Play Console**
   - Crear nueva app
   - Completar información basica
   - Aceptar términos

3. **AAB (App Bundle) firmado**
   ```bash
   ./gradlew bundleRelease
   ```

### Paso 1: Preparar Información

```
Nombre de la app: BOCADO
Descripción corta (80 caracteres): 
"Pedidos digitales en restaurantes con QR"

Descripción completa (4000 caracteres):
[Ver README.md para contenido]

Icono de app: 
- 512x512 PNG
- Sin transparencia

Screenshots (mínimo 2, máximo 8):
- 1080x1920 pixels
- Formato: PNG o JPEG

Video promocional (opcional):
- YouTube URL o upload

Política de privacidad:
- URL pública o integrada

Contacto de soporte:
- Email válido
```

### Paso 2: Setup en Play Console

```
1. Ir a https://play.google.com/console
2. Seleccionar app BOCADO
3. Left menu → Releases
4. Seleccionar "Production"
5. Crear nuevo release
```

### Paso 3: Upload AAB

```
1. Play Console → Release → Production
2. "Create new release"
3. Upload file → Seleccionar: app/build/outputs/bundle/release/app-release.aab
4. Google Play procesará automáticamente
5. Esperar verificación (puede tomar horas)
```

### Paso 4: Review y Aprobación

```
Tiempo típico: 24-72 horas

Google revisa:
✓ Contenido ofensivo
✓ Malware/Spyware
✓ Política de privacidad
✓ Permisos necesarios
✓ Funcionalidad

Si hay problemas: Email de rechazo con detalles
```

### Paso 5: Publicación

```
1. Si aprobado: Click "Publish"
2. App aparecerá en Play Store en ~2 horas
3. URL será: 
   play.google.com/store/apps/details?id=com.bocado

4. Compartir link con usuarios
```

---

## Versionado

### Sistema Semántico

```
versión = MAJOR.MINOR.PATCH

Ejemplo: 1.0.0

MAJOR (1): Cambios incompatibles
MINOR (0): Nuevas features compatibles
PATCH (0): Bug fixes
```

### Actualizar Versión

Edita `app/build.gradle.kts`:

```gradle
defaultConfig {
    versionCode = 2        // Incrementa siempre
    versionName = "1.1.0"  // Semántico
}
```

**Regla:** `versionCode` siempre debe crecer (Play Store lo requiere)

---

## Troubleshooting

### Error: `INSTALL_FAILED_INVALID_APK`

```
Causa: APK corrupto o incompatible

Solución:
1. Clean build: ./gradlew clean
2. Rebuild: ./gradlew build
3. Reinstalar: adb uninstall com.bocado
4. Instalar: ./gradlew installDebug
```

### Error: `INSTALL_FAILED_INSUFFICIENT_STORAGE`

```
Causa: Espacio insuficiente en dispositivo

Solución:
1. Liberar espacio
2. O instalar en emulador con más storage
```

### Error: `INSTALL_FAILED_PERMISSION_MODEL_DOWNGRADE`

```
Causa: Instalando versión debug sobre release (o vice versa)

Solución:
adb uninstall com.bocado
./gradlew installDebug
```

### Crash en startup

```
Solucionar:
1. Ver logs: adb logcat | grep bocado
2. Verificar:
   - Android API Level compatible
   - Permisos en AndroidManifest
   - Dependencies compiladas correctamente
```

### Gradle Sync Failed

```
Solucionar:
1. File → Sync Now
2. File → Invalidate Caches → Restart
3. ./gradlew --stop && ./gradlew build
```

### App lenta en emulador

```
Optimizaciones:
- Usar emulador con GPU acceleration
- Asignar más RAM (-m 4096)
- Correr en máquina con SSD
- O usar dispositivo físico
```

### Proguard issues (Minification errors)

```
Si hay crashes en release:
1. Ver crash log: adb logcat | grep AndroidRuntime
2. Agregar reglas a proguard-rules.pro
3. Rebuild

Ejemplo:
-keep class com.bocado.** { *; }
```

---

## Monitoreo Post-Release

### Firebase Crashlytics (Opcional)

```gradle
// Agregar a build.gradle
dependencies {
    implementation 'com.google.firebase:firebase-crashlytics-ktx'
}
```

Configura en Play Console para recibir reportes de crashes automáticos.

### Google Analytics

Integrar para entender uso:
- Usuarios activos
- Eventos clave
- Rutas más usadas
- Crashes

### Play Store Analytics

Play Console proporciona:
- Descargas/Installs
- Crashes
- Ratings
- Feedback de usuarios

---

## Checklist Final Antes de Publicar

### Código
- [ ] Código limpio (sin TODOs)
- [ ] Sin logs de debug
- [ ] Sin hardcoded values
- [ ] Versión incrementada
- [ ] Changelog actualizado

### Testing
- [ ] Tests pasan todos
- [ ] No hay crashes
- [ ] Performance aceptable
- [ ] Validaciones funcionan

### Assets
- [ ] Icono en tamaño correcto
- [ ] Screenshots actualizados
- [ ] Descripción lista
- [ ] Política de privacidad lista

### Configuración
- [ ] API endpoints correctos
- [ ] BuildConfig correcto
- [ ] Keystore seguro
- [ ] Permisos mínimos necesarios

### Documentación
- [ ] README actualizado
- [ ] Changelog completado
- [ ] Instrucciones de soporte listas
- [ ] FAQ preparadas

---

## Comandos Útiles

```bash
# Estadísticas de código
./gradlew properties | grep Version

# Build sin tests
./gradlew build -x test

# Build verbose (para debugging)
./gradlew build --info

# Force rebuild
./gradlew build --rerun-tasks

# Listar Tasks disponibles
./gradlew tasks

# Ver reportes de build
./gradlew build --warning-mode all
```

---

## Próximas Versiones

### Versión 1.1.0 (Beta)
- [ ] Autenticación de usuario
- [ ] Historial de pedidos
- [ ] Sistema de favoritos

### Versión 1.2.0
- [ ] Integraciones MercadoPago
- [ ] Notificaciones push
- [ ] Admin panel

### Versión 2.0.0
- [ ] Multi-restaurante
- [ ] Entregas a domicilio
- [ ] iOS version

---

**Última actualización:** Junio 2024

Para más información contactar al equipo de desarrollo.
