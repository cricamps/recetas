# Recetas Chilenas

**Curso:** DSY2204 - Desarrollo de Aplicaciones Móviles  
**Estudiante:** Cristobal Camps  
**Semana:** 5 - Funcionalidades Avanzadas en Kotlin  
**Fecha:** Febrero 2026

---

## Descripción

App de recetas tradicionales chilenas enfocada en accesibilidad. Desarrollada 100% en Kotlin con Jetpack Compose.

**32 recetas** organizadas en 5 categorías:
- Plato Principal (15 recetas)
- Sopa/Guiso (8 recetas)
- Ensalada (4 recetas)
- Acompañamiento (3 recetas)
- Postre (2 recetas)

---

## Características

### Funcionalidades Principales
- Búsqueda en tiempo real
- Filtros por categoría
- Sistema de favoritos
- Información nutricional completa
- Envío de recetas por email

### Accesibilidad
- **Tamaño de letra ajustable** - 5 niveles (0.85x a 1.5x)
- **Text-to-Speech** - Lee recetas en voz alta
- **Alto contraste** - 4 modos (incluye WCAG AA y AAA)
- **Tema claro/oscuro** - Persiste preferencias
- **Navegación por voz** - Comandos en español

### Animaciones
- Animaciones staggered en lista de recetas
- Animación de escala al presionar cards
- Transiciones fluidas entre pantallas

---

## Implementación - Semana 5

Esta sección documenta los conceptos avanzados de Kotlin aplicados en el proyecto.

### 1. Lambdas

Las lambdas se usan principalmente para callbacks y eventos. Aquí algunos ejemplos:

#### Ejemplo 1: Lambda para manejar filtros
**Ubicación:** `RecetasScreen.kt` línea 248

```kotlin
onCategoriaToggle = { categoria ->
    if (categoria in categoriasSeleccionadas) {
        if (categoriasSeleccionadas.size > 1) {
            categoriasSeleccionadas = categoriasSeleccionadas - categoria
        }
    } else {
        categoriasSeleccionadas = categoriasSeleccionadas + categoria
    }
}
```
**Qué hace:** Se ejecuta cuando haces click en un filtro. Agrega o quita la categoría, pero siempre deja al menos una seleccionada.

---

#### Ejemplo 2: Lambda con filter
**Ubicación:** `RecetasScreen.kt` línea 90

```kotlin
.filter { receta ->
    if (todasCategoriasSeleccionadas) {
        true
    } else {
        val categoria = receta.obtenerCategoria().toCategoriaFiltro()
        categoria != null && categoria in categoriasSeleccionadas
    }
}
```
**Qué hace:** Filtra las recetas según las categorías seleccionadas. Si todas están activas, muestra todo.

---

#### Ejemplo 3: Lambda en remember
**Ubicación:** `RecetasScreen.kt` línea 87

```kotlin
val todasLasRecetas = remember(searchQuery, categoriasSeleccionadas) {
    RecetasRepository.searchRecetas(searchQuery)
        .filter { receta -> /* ... */ }
}
```
**Qué hace:** Cachea las recetas filtradas. Solo vuelve a calcular cuando cambia el texto de búsqueda o los filtros.

---

#### Ejemplo 4: Lambda para navegación
**Ubicación:** `RecetasScreen.kt` línea 178

```kotlin
IconButton(
    onClick = {
        navController.navigate("login") {
            popUpTo("recetas") { inclusive = true }
        }
    }
)
```
**Qué hace:** Lambda que se ejecuta cuando presionas el botón de cerrar sesión. Navega al login y limpia el stack.

---

### 2. Funciones de Orden Superior

Son funciones que reciben o devuelven otras funciones.

| Función | Ubicación | Descripción |
|---------|-----------|-------------|
| `filter` | RecetasScreen.kt:90 | Filtra lista según condición |
| `remember` | RecetasScreen.kt:87 | Cachea valores en Compose |
| `groupBy` | RecetasScreen.kt:96 | Agrupa recetas por categoría |
| `forEach` | FiltrosCategorias.kt:110 | Itera sobre categorías |
| `compareBy` | RecetasScreen.kt:98 | Ordena categorías |

#### Ejemplo: groupBy
```kotlin
val recetasAgrupadas = todasLasRecetas.groupBy { it.obtenerCategoria() }
    .toSortedMap(compareBy {
        when(it) {
            "Plato Principal" -> 1
            "Sopa/Guiso" -> 2
            "Acompañamiento" -> 3
            "Ensalada" -> 4
            "Postre" -> 5
            else -> 6
        }
    })
```
**Qué hace:** Agrupa las recetas por categoría y las ordena (platos principales primero, postres al final).

---

### 3. Funciones de Extensión

Permiten agregar funcionalidad a clases existentes.

#### String.toCategoriaFiltro()
**Ubicación:** `FiltrosCategorias.kt` línea 30

```kotlin
fun String.toCategoriaFiltro(): CategoriaFiltro? {
    return CategoriaFiltro.fromString(this)
}
```

**Uso:**
```kotlin
val categoria = receta.obtenerCategoria().toCategoriaFiltro()
```

**Por qué es útil:** Es mucho más legible que `CategoriaFiltro.fromString(receta.obtenerCategoria())`.

---

#### Int.scaledSp()
**Ubicación:** `AccessibilityUtils.kt` línea 60

```kotlin
@Composable
fun Int.scaledSp(): TextUnit {
    val fontScale = LocalFontScale.current
    return (this * fontScale.value).sp
}
```

**Uso:**
```kotlin
Text(
    text = receta.nombre,
    fontSize = 20.scaledSp()  // Se ajusta según preferencias
)
```

**Por qué es útil:** Hace que todos los tamaños de fuente se ajusten automáticamente según las preferencias del usuario.

---

### 4. Colecciones

Uso extensivo de colecciones y sus operaciones.

#### Set para filtros
**Ubicación:** `RecetasScreen.kt` línea 83

```kotlin
var categoriasSeleccionadas by remember {
    mutableStateOf(CategoriaFiltro.entries.toSet())
}
```

**Por qué Set:** No quiero duplicados y es eficiente para verificar si un elemento está presente con `in`.

#### Operaciones disponibles
```kotlin
// Agregar
categoriasSeleccionadas = categoriasSeleccionadas + categoria

// Quitar
categoriasSeleccionadas = categoriasSeleccionadas - categoria

// Verificar
if (categoria in categoriasSeleccionadas) { ... }

// Tamaño
categoriasSeleccionadas.size
```

#### Otras colecciones usadas
- **List**: Para las recetas (`List<Receta>`)
- **Map**: Para agrupar por categoría (`Map<String, List<Receta>>`)
- **Enum.entries**: Todos los valores del enum (`List<CategoriaFiltro>`)

