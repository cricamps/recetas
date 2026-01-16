package com.example.recetas.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.recetas.accessibility.hablarTexto
import com.example.recetas.data.Receta
import com.example.recetas.data.RecetasRepository

/**
 * Pantalla principal que muestra la lista de recetas chilenas.
 * 
 * Componentes UI utilizados:
 * - LazyColumn: Lista eficiente de recetas
 * - Card: Para cada item de receta
 * - FloatingActionButton: Botón para agregar nueva receta
 * - TextField: Barra de búsqueda
 * - IconButton: Favoritos y cambio de tema
 * 
 * @param isDarkTheme Estado del tema oscuro/claro
 * @param onThemeChange Callback para cambiar el tema
 * @param onRecetaClick Callback cuando se selecciona una receta
 * @param onAgregarReceta Callback para agregar nueva receta
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecetasScreen(
    isDarkTheme: Boolean,
    onThemeChange: () -> Unit,
    onRecetaClick: (String) -> Unit,
    onAgregarReceta: () -> Unit
) {
    // Estado para la búsqueda de recetas
    var searchQuery by remember { mutableStateOf("") }
    
    // Obtener recetas filtradas según la búsqueda
    val recetas = remember(searchQuery) {
        RecetasRepository.searchRecetas(searchQuery)
    }
    
    // Scaffold proporciona la estructura básica con FAB
    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        "Recetas de la Cocina Chilena",
                        style = MaterialTheme.typography.headlineSmall
                    ) 
                },
                actions = {
                    // Botón para cambiar tema
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
        },
        floatingActionButton = {
            // Botón flotante para agregar nueva receta
            FloatingActionButton(
                onClick = onAgregarReceta,
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Agregar receta"
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Barra de búsqueda
            SearchBar(
                searchQuery = searchQuery,
                onSearchQueryChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
            
            // Lista de recetas
            if (recetas.isEmpty()) {
                // Mensaje cuando no hay resultados
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No se encontraron recetas",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(recetas) { receta ->
                        RecetaCard(
                            receta = receta,
                            onClick = { onRecetaClick(receta.id) }
                        )
                    }
                }
            }
        }
    }
}

/**
 * Barra de búsqueda para filtrar recetas.
 * 
 * @param searchQuery Texto de búsqueda actual
 * @param onSearchQueryChange Callback cuando cambia el texto
 * @param modifier Modificador del componente
 */
@Composable
fun SearchBar(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    
    OutlinedTextField(
        value = searchQuery,
        onValueChange = onSearchQueryChange,
        modifier = modifier,
        placeholder = { Text("Buscar receta chilena...") },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Buscar"
            )
        },
        trailingIcon = {
            // Botón TTS para personas con discapacidad del habla
            if (searchQuery.isNotEmpty()) {
                IconButton(
                    onClick = {
                        // Hablar el texto de búsqueda
                        hablarTexto(context, "Buscando: $searchQuery")
                    }
                ) {
                    Text(
                        text = "🔊",
                        fontSize = 20.sp
                    )
                }
            }
        },
        singleLine = true,
        shape = RoundedCornerShape(28.dp)
    )
}

/**
 * Tarjeta que muestra la información de una receta.
 * 
 * @param receta Datos de la receta a mostrar
 * @param onClick Callback cuando se hace click en la tarjeta
 */
@Composable
fun RecetaCard(
    receta: Receta,
    onClick: () -> Unit
) {
    val context = LocalContext.current
    // Estado para el icono de favorito (local, no persistente)
    var isFavorite by remember { mutableStateOf(receta.isFavorita) }
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Información de la receta
            Column(
                modifier = Modifier.weight(1f)
            ) {
                // Nombre de la receta con botón TTS
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = receta.nombre,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.weight(1f)
                    )
                    
                    // Botón TTS para decir el nombre de la receta
                    IconButton(
                        onClick = {
                            hablarTexto(context, receta.nombre)
                        },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Text(
                            text = "🔊",
                            fontSize = 16.sp
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(4.dp))
                
                // Descripción
                Text(
                    text = receta.descripcion,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // Información adicional (tiempo, dificultad, origen)
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    InfoChip(
                        icon = "⏱️",
                        text = receta.tiempoPreparacion
                    )
                    InfoChip(
                        icon = when(receta.dificultad) {
                            "Fácil" -> "✅"
                            "Media" -> "⚠️"
                            else -> "🔥"
                        },
                        text = receta.dificultad
                    )
                    InfoChip(
                        icon = "🇨🇱",
                        text = receta.origen
                    )
                }
            }
            
            // Botón de favorito
            IconButton(
                onClick = { isFavorite = !isFavorite }
            ) {
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
                        MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

/**
 * Chip informativo con icono y texto.
 * 
 * @param icon Emoji o texto del icono
 * @param text Texto a mostrar
 */
@Composable
fun InfoChip(
    icon: String,
    text: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = icon,
            fontSize = 14.sp
        )
        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
        )
    }
}
