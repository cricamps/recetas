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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.recetas.accessibility.FontScale
import com.example.recetas.accessibility.FontSizeButton
import com.example.recetas.accessibility.ContrastMode
import com.example.recetas.accessibility.ContrastModeControl
import com.example.recetas.accessibility.hablarTexto
import com.example.recetas.accessibility.scaledSp
import com.example.recetas.data.Receta
import com.example.recetas.data.RecetasRepository
import androidx.navigation.NavController
import com.example.recetas.voice.rememberVoiceNavigationManager
import com.example.recetas.voice.VoiceNavigationIconButton
import androidx.compose.animation.core.*
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import com.example.recetas.animations.RecetaAnimations
import com.example.recetas.ui.components.AnimatedFAB
import com.example.recetas.ui.components.AnimatedIconButton

/**
 * Pantalla principal que muestra la lista de recetas chilenas.
 * 
 * Componentes UI utilizados:
 * - LazyColumn: Lista eficiente de recetas
 * - Card: Para cada item de receta
 * - FloatingActionButton: Botón para agregar nueva receta
 * - TextField: Barra de búsqueda
 * - IconButton: Favoritos y cambio de tema
 * - FontSizeButton: Control de tamaño de fuente
 * 
 * @param isDarkTheme Estado del tema oscuro/claro
 * @param onThemeChange Callback para cambiar el tema
 * @param fontScale Estado de la escala de fuente
 * @param onFontScaleChange Callback para cambiar la escala de fuente
 * @param contrastMode Estado del modo de contraste
 * @param onContrastModeChange Callback para cambiar el modo de contraste
 * @param onRecetaClick Callback cuando se selecciona una receta
 * @param onAgregarReceta Callback para agregar nueva receta
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecetasScreen(
    navController: NavController,
    isDarkTheme: Boolean,
    onThemeChange: () -> Unit,
    fontScale: MutableState<FontScale>,
    onFontScaleChange: (FontScale) -> Unit,
    contrastMode: MutableState<ContrastMode>,
    onContrastModeChange: (ContrastMode) -> Unit,
    onRecetaClick: (String) -> Unit,
    onAgregarReceta: () -> Unit
) {
    // Estado para la búsqueda de recetas
    var searchQuery by remember { mutableStateOf("") }
    
    // Obtener recetas filtradas según la búsqueda
    val recetas = remember(searchQuery) {
        RecetasRepository.searchRecetas(searchQuery)
    }
    
    // Gestor de navegación por voz
    val voiceManager = rememberVoiceNavigationManager(
        navController = navController,
        onThemeChange = onThemeChange,
        onFontScaleIncrease = {
            val currentIndex = FontScale.entries.indexOf(fontScale.value)
            if (currentIndex < FontScale.entries.size - 1) {
                onFontScaleChange(FontScale.entries[currentIndex + 1])
            }
        },
        onFontScaleDecrease = {
            val currentIndex = FontScale.entries.indexOf(fontScale.value)
            if (currentIndex > 0) {
                onFontScaleChange(FontScale.entries[currentIndex - 1])
            }
        },
        onSearchQuery = { query ->
            searchQuery = query
        },
        onRecipeSelected = { recipeId ->
            onRecetaClick(recipeId.toString())
        }
    )
    
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
                    // Botón de navegación por voz
                    VoiceNavigationIconButton(
                        voiceManager = voiceManager,
                        modifier = Modifier.padding(end = 4.dp)
                    )
                    
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
            // Botón flotante animado para agregar nueva receta
            AnimatedFAB(
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
                    itemsIndexed(recetas) { index, receta ->
                        // Animación staggered: cada card aparece con delay
                        RecetaCard(
                            receta = receta,
                            index = index,
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
            // Botón TTS animado para personas con discapacidad del habla
            if (searchQuery.isNotEmpty()) {
                AnimatedIconButton(
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
 * Tarjeta que muestra la información de una receta con animación.
 * 
 * @param receta Datos de la receta a mostrar
 * @param index Índice en la lista para animación staggered
 * @param onClick Callback cuando se hace click en la tarjeta
 */
@Composable
fun RecetaCard(
    receta: Receta,
    index: Int = 0,
    onClick: () -> Unit
) {
    val context = LocalContext.current
    // Estado para el icono de favorito (local, no persistente)
    var isFavorite by remember { mutableStateOf(receta.isFavorita) }
    
    // Animación staggered de entrada
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay((index * 150).toLong())  // 150ms de delay entre cada card
        visible = true
    }
    
    val alpha by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = tween(
            durationMillis = 800,  // 800ms = MÁS LENTO
            easing = FastOutSlowInEasing
        ),
        label = "card_alpha"
    )
    
    val offsetY by animateDpAsState(
        targetValue = if (visible) 0.dp else 60.dp,  // 60dp = MUY EVIDENTE
        animationSpec = tween(
            durationMillis = 800,  // 800ms = MÁS LENTO
            easing = FastOutSlowInEasing
        ),
        label = "card_offset"
    )
    
    // Animación de escala al presionar
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    
    val pressScale by animateFloatAsState(
        targetValue = if (isPressed) 0.85f else 1f,  // 15% de reducción - EXAGERADO
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioLowBouncy,  // Rebote BAJO = MÁS REBOTE
            stiffness = Spring.StiffnessLow  // Rigidez baja = MÁS LENTO
        ),
        label = "press_scale"
    )
    
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .graphicsLayer {
                this.alpha = alpha
                translationY = offsetY.toPx()
                scaleX = pressScale
                scaleY = pressScale
            },
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp,
            pressedElevation = 8.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        interactionSource = interactionSource
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
                    fontSize = 20.scaledSp(),
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.weight(1f)
                    )
                    
                    // Botón TTS animado para decir el nombre de la receta
                    AnimatedIconButton(
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
                    fontSize = 14.scaledSp(),
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
            
            // Botón de favorito con animación
            AnimatedIconButton(
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
