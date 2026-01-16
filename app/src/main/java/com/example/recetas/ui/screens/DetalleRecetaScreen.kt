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
import com.example.recetas.accessibility.SpeechManager
import com.example.recetas.accessibility.hablarTexto
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
 * 
 * @param recetaId ID de la receta a mostrar
 * @param isDarkTheme Estado del tema oscuro/claro
 * @param onThemeChange Callback para cambiar el tema
 * @param onNavigateBack Callback para volver atrás
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleRecetaScreen(
    recetaId: String,
    isDarkTheme: Boolean,
    onThemeChange: () -> Unit,
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    
    // Detener TTS cuando se sale de la pantalla
    DisposableEffect(Unit) {
        onDispose {
            SpeechManager.stop()
        }
    }
    
    // Obtener la receta del repositorio
    val receta = remember { RecetasRepository.getRecetaById(recetaId) }
    
    // Estado para favorito
    var isFavorite by remember { mutableStateOf(receta?.isFavorita ?: false) }
    
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
            IngredientsCard(ingredientes = receta.ingredientes)
            
            // Preparación
            PreparationCard(preparacion = receta.preparacion)
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
                InfoItem(label = "Tiempo", value = receta.tiempoPreparacion, icon = "⏱️")
                InfoItem(
                    label = "Dificultad", 
                    value = receta.dificultad,
                    icon = when(receta.dificultad) {
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
                            text = ingrediente,
                            style = MaterialTheme.typography.bodyLarge
                        )
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
                            val textoPreparacion = "Preparación: " + 
                                preparacion.mapIndexed { index, paso -> 
                                    "Paso ${index + 1}: $paso" 
                                }.joinToString(". ")
                            hablarTexto(context, textoPreparacion)
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
                preparacion.forEachIndexed { index, paso ->
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
                                    text = "${index + 1}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onPrimary
                                )
                            }
                        }
                        
                        Spacer(modifier = Modifier.width(12.dp))
                        
                        // Texto del paso
                        Text(
                            text = paso,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.weight(1f)
                        )
                    }
                    
                    if (index < preparacion.size - 1) {
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
