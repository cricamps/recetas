package com.example.recetas.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.recetas.accessibility.ContrastMode
import com.example.recetas.accessibility.ContrastModeControl
import com.example.recetas.accessibility.FontScale
import com.example.recetas.accessibility.FontSizeButton
import com.example.recetas.accessibility.SpeechManager
import com.example.recetas.accessibility.hablarTexto
import com.example.recetas.data.MinutasRepository
import com.example.recetas.data.RecetasRepository

/**
 * Pantalla que muestra el detalle completo de una receta.
 * Incluye ingredientes, preparación y toda la información detallada.
 * 
 * Componentes UI utilizados:
 * - Scaffold con TopAppBar
 * - Card: Para secciones de ingredientes y preparación
 * - Column con scroll vertical
 * - Divider: Separadores visuales
 * - FontSizeButton: Control de tamaño de fuente
 * - ContrastModeControl: Control de nivel de contraste
 * 
 * @param recetaId ID de la receta a mostrar
 * @param isDarkTheme Estado del tema oscuro/claro
 * @param onThemeChange Callback para cambiar el tema
 * @param fontScale Estado de la escala de fuente
 * @param onFontScaleChange Callback para cambiar la escala de fuente
 * @param contrastMode Estado del modo de contraste
 * @param onContrastModeChange Callback para cambiar el modo de contraste
 * @param onNavigateBack Callback para volver atrás
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleRecetaScreen(
    recetaId: String,
    isDarkTheme: Boolean,
    onThemeChange: () -> Unit,
    fontScale: MutableState<FontScale>,
    onFontScaleChange: (FontScale) -> Unit,
    contrastMode: MutableState<ContrastMode>,
    onContrastModeChange: (ContrastMode) -> Unit,
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    
    // Detener TTS cuando se sale de la pantalla
    DisposableEffect(Unit) {
        onDispose {
            SpeechManager.stop()
        }
    }
    
    // Obtener la receta del repositorio (primero intenta minutas, luego recetas normales)
    val receta = remember { 
        MinutasRepository.getRecetaById(recetaId) ?: RecetasRepository.getRecetaById(recetaId)
    }
    
    // Estado para favorito
    var isFavorite by remember { mutableStateOf(receta?.esFavorita ?: false) }
    
    if (receta == null) {
        // Mostrar mensaje si no se encuentra la receta
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Receta no encontrada",
                style = MaterialTheme.typography.headlineMedium
            )
        }
        return
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        text = receta.nombre,
                        style = MaterialTheme.typography.titleLarge
                    ) 
                },
                navigationIcon = {
                    // Botón de retroceso
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                },
                actions = {
                    // Botón de favorito
                    IconButton(onClick = { isFavorite = !isFavorite }) {
                        Icon(
                            imageVector = if (isFavorite) 
                                Icons.Default.Favorite 
                            else 
                                Icons.Default.FavoriteBorder,
                            contentDescription = if (isFavorite) 
                                "Quitar de favoritos" 
                            else 
                                "Agregar a favoritos",
                            tint = if (isFavorite) 
                                MaterialTheme.colorScheme.error 
                            else 
                                MaterialTheme.colorScheme.onSurface
                        )
                    }
                    
                    // Control de contraste
                    ContrastModeControl(
                        contrastMode = contrastMode.value,
                        onContrastModeChange = onContrastModeChange,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    
                    // Botón de tamaño de fuente
                    FontSizeButton(
                        currentScale = fontScale,
                        onScaleChange = onFontScaleChange
                    )
                    
                    // Botón de tema
                    IconButton(onClick = onThemeChange) {
                        Text(
                            text = if (isDarkTheme) "☀️" else "🌙",
                            fontSize = 24.sp
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Cabecera con información general
            HeaderCard(receta = receta)
            
            // Descripción
            DescriptionCard(descripcion = receta.descripcion)
            
            // Ingredientes
            IngredientsCard(ingredientes = receta.obtenerIngredientes())
            
            // Preparación
            PreparationCard(preparacion = receta.obtenerPasosPreparacion())
            
            // Información Nutricional (si existe)
            receta.obtenerInfoNutricional()?.let { info ->
                NutritionalInfoCard(info = info, calorias = receta.obtenerCaloriasTotales())
            }
        }
    }
}

/**
 * Tarjeta con la información general de la receta.
 */
@Composable
fun HeaderCard(receta: com.example.recetas.data.Receta) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = receta.nombre,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                InfoItem(label = "Origen", value = receta.origen, icon = "🇨🇱")
                InfoItem(label = "Tiempo", value = receta.obtenerTiempoPreparacion(), icon = "⏱️")
                InfoItem(
                    label = "Dificultad", 
                    value = receta.obtenerNivelDificultad(),
                    icon = when(receta.obtenerNivelDificultad()) {
                        "Fácil" -> "✅"
                        "Media" -> "⚠️"
                        else -> "🔥"
                    }
                )
            }
        }
    }
}

/**
 * Tarjeta con la descripción de la receta.
 */
@Composable
fun DescriptionCard(descripcion: String) {
    val context = LocalContext.current
    
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Título con botón TTS
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "📝 Descripción",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                
                // Botón TTS para leer la descripción
                if (descripcion.isNotEmpty()) {
                    IconButton(
                        onClick = {
                            hablarTexto(context, "Descripción: $descripcion")
                        }
                    ) {
                        Text(
                            text = "🔊",
                            fontSize = 24.sp
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = descripcion,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

/**
 * Tarjeta con la lista de ingredientes.
 */
@Composable
fun IngredientsCard(ingredientes: List<String>) {
    val context = LocalContext.current
    
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Título con botón TTS
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "🛒 Ingredientes",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                
                // Botón TTS para leer todos los ingredientes
                if (ingredientes.isNotEmpty()) {
                    IconButton(
                        onClick = {
                            val textoIngredientes = "Ingredientes: " + 
                                ingredientes.joinToString(", ")
                            hablarTexto(context, textoIngredientes)
                        }
                    ) {
                        Text(
                            text = "🔊",
                            fontSize = 24.sp
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            if (ingredientes.isEmpty()) {
                Text(
                    text = "No hay ingredientes especificados",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                ingredientes.forEach { ingrediente ->
                    val texto = ingrediente.trim()
                    // Detectar separadores de sección (--- Postre: ... --- / --- Ensalada: ... ---)
                    val esSeparador = texto.startsWith("---") && texto.endsWith("---")
                    
                    if (texto.isEmpty()) {
                        // Saltar elemtos vacios
                    } else if (esSeparador) {
                        // Separador visual de sección
                        Spacer(modifier = Modifier.height(12.dp))
                        Divider(
                            modifier = Modifier.padding(vertical = 2.dp),
                            color = MaterialTheme.colorScheme.outlineVariant,
                            thickness = 1.dp
                        )
                        val colorSep = when {
                            texto.contains("Postre", ignoreCase = true) -> MaterialTheme.colorScheme.error
                            texto.contains("Ensalada", ignoreCase = true) -> MaterialTheme.colorScheme.secondary
                            else -> MaterialTheme.colorScheme.tertiary
                        }
                        val iconoSep = when {
                            texto.contains("Postre", ignoreCase = true) -> "🍰"
                            texto.contains("Ensalada", ignoreCase = true) -> "🥗"
                            else -> "🥗"
                        }
                        Row(
                            modifier = Modifier.padding(vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = iconoSep, style = MaterialTheme.typography.titleMedium)
                            Spacer(modifier = Modifier.width(6.dp))
                            // Extraer nombre entre los guiones
                            val nombre = texto.removePrefix("---").removeSuffix("---").trim()
                            Text(
                                text = nombre,
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = colorSep
                            )
                        }
                    } else {
                        // Ingrediente normal
                        Row(
                            modifier = Modifier.padding(vertical = 4.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Text(
                                text = "• ",
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = texto,
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * Tarjeta con los pasos de preparación.
 */
@Composable
fun PreparationCard(preparacion: List<String>) {
    val context = LocalContext.current
    
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Título con botón TTS
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "👨‍🍳 Preparación",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                
                // Botón TTS para leer todos los pasos
                if (preparacion.isNotEmpty()) {
                    IconButton(
                        onClick = {
                            val partes = mutableListOf<String>()
                            var contador = 0
                            preparacion.forEach { paso ->
                                val pasoTrimmed = paso.trim()
                                val esSeparador = pasoTrimmed.startsWith("Preparación del") ||
                                                  pasoTrimmed.startsWith("Preparación de la")
                                if (esSeparador) {
                                    contador = 0
                                    partes.add(pasoTrimmed)
                                } else if (pasoTrimmed.isNotEmpty()) {
                                    contador++
                                    partes.add("Paso $contador: $pasoTrimmed")
                                }
                            }
                            hablarTexto(context, "Preparación: " + partes.joinToString(". "))
                        }
                    ) {
                        Text(
                            text = "🔊",
                            fontSize = 24.sp
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            if (preparacion.isEmpty()) {
                Text(
                    text = "No hay pasos de preparación especificados",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                var contador = 0
                preparacion.forEachIndexed { index, paso ->
                    val pasoTrimmed = paso.trim()
                    
                    // Detectar separadores de sección (Preparación del postre / ensalada / acompañamiento)
                    val esSeparador = pasoTrimmed.startsWith("Preparación del") ||
                                      pasoTrimmed.startsWith("Preparación de la")
                    
                    if (esSeparador) {
                        // Reiniciar contador
                        contador = 0
                        
                        // Espacio antes del separador
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        // Línea divisora
                        Divider(
                            modifier = Modifier.padding(vertical = 4.dp),
                            color = MaterialTheme.colorScheme.outlineVariant,
                            thickness = 1.5.dp
                        )
                        
                        // Título de la sección con color según tipo
                        val colorTitulo = when {
                            pasoTrimmed.contains("postre", ignoreCase = true) -> MaterialTheme.colorScheme.error
                            pasoTrimmed.contains("ensalada", ignoreCase = true) -> MaterialTheme.colorScheme.secondary
                            else -> MaterialTheme.colorScheme.tertiary  // acompañamiento
                        }
                        val iconoTitulo = when {
                            pasoTrimmed.contains("postre", ignoreCase = true) -> "🍰"
                            pasoTrimmed.contains("ensalada", ignoreCase = true) -> "🥗"
                            else -> "🥗"  // acompañamiento
                        }
                        
                        Row(
                            modifier = Modifier.padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = iconoTitulo, style = MaterialTheme.typography.titleMedium)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = pasoTrimmed,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = colorTitulo
                            )
                        }
                    } else {
                        // Paso normal
                        contador++
                        
                        Row(
                            modifier = Modifier.padding(vertical = 8.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            // Número del paso
                            Surface(
                                modifier = Modifier.size(28.dp),
                                shape = MaterialTheme.shapes.small,
                                color = MaterialTheme.colorScheme.primary
                            ) {
                                Box(
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "$contador",
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onPrimary
                                    )
                                }
                            }
                            
                            Spacer(modifier = Modifier.width(12.dp))
                            
                            // Texto del paso
                            Text(
                                text = pasoTrimmed,
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.weight(1f)
                            )
                        }
                        
                        // Divisor entre pasos normales (no al último antes de un separador)
                        val siguientePaso = preparacion.getOrNull(index + 1)?.trim() ?: ""
                        val siguienteEsSeparador = siguientePaso.startsWith("Preparación del") ||
                                                   siguientePaso.startsWith("Preparación de la")
                        if (index < preparacion.size - 1 && !siguienteEsSeparador) {
                            Divider(
                                modifier = Modifier.padding(vertical = 4.dp),
                                color = MaterialTheme.colorScheme.outlineVariant
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * Componente que muestra un item de información con icono.
 */
@Composable
fun InfoItem(
    label: String,
    value: String,
    icon: String
) {
    Column {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = icon,
                fontSize = 16.sp
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}

/**
 * Tarjeta con información nutricional completa.
 */
@Composable
fun NutritionalInfoCard(
    info: com.example.recetas.data.NutritionalInfo,
    calorias: Int
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "🔥 Información Nutricional",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onTertiaryContainer
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Filas de información nutricional
            NutritionalRow("Proteínas", "${info.proteinas}g")
            NutritionalRow("Carbohidratos", "${info.carbohidratos}g")
            NutritionalRow("Grasas", "${info.grasas}g")
            if (info.fibra > 0) {
                NutritionalRow("Fibra", "${info.fibra}g")
            }
            if (info.azucar > 0) {
                NutritionalRow("Azúcares", "${info.azucar}g")
            }
            NutritionalRow("Sodio", "${info.sodio}mg")
            
            Spacer(modifier = Modifier.height(8.dp))
            Divider(color = MaterialTheme.colorScheme.onTertiaryContainer.copy(alpha = 0.2f))
            Spacer(modifier = Modifier.height(8.dp))
            
            // Mensaje de recomendación
            if (calorias < 350) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "✓",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Bajo en calorías - ideal para control de peso",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onTertiaryContainer
                    )
                }
            }
        }
    }
}

/**
 * Fila de información nutricional.
 */
@Composable
fun NutritionalRow(
    label: String,
    value: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onTertiaryContainer
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onTertiaryContainer
        )
    }
}
