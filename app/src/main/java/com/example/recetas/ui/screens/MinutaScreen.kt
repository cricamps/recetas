package com.example.recetas.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.graphicsLayer
import com.example.recetas.data.MinutasRepository
import com.example.recetas.data.Receta
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.WeekFields
import java.util.Locale
import androidx.compose.animation.core.*

/**
 * Pantalla que muestra la Minuta Semanal con 5 recetas.
 * Cumple con el requerimiento de la Semana 4:
 * "Generar un array en Kotlin que almacene los datos de 5 recetas semanales 
 * con sus respectivas recomendaciones nutricionales desde la view de Minuta."
 * 
 * @param onNavigateToReceta Callback para navegar al detalle de una receta
 * @param onNavigateBack Callback para volver atrás
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MinutaScreen(
    onNavigateToReceta: (String) -> Unit,
    onNavigateBack: () -> Unit
) {
    // Estado para la semana actual (0 = semana actual, 1 = próxima semana, 2 = en 2 semanas)
    var offsetSemana by remember { mutableStateOf(0) }
    
    // Calcular la semana base (actual) y el número de semana
    val semanaActual = remember { MinutasRepository.obtenerSemanaActual() }
    val semanaSeleccionada = semanaActual + offsetSemana
    
    // Obtener minuta para la semana seleccionada
    val minutaSemanal = remember(offsetSemana) { 
        MinutasRepository.obtenerMinutaSemanal(semanaSeleccionada) 
    }
    val recetasSemanales = remember(offsetSemana) { 
        minutaSemanal.obtenerTodasLasRecetas() 
    }
    
    // Calcular fechas de la semana seleccionada (Lunes a Viernes)
    val fechasSemanales = remember(offsetSemana) {
        val hoy = LocalDate.now()
        val primerDiaSemanaActual = hoy.with(WeekFields.of(Locale.getDefault()).dayOfWeek(), 1)
        val primerDiaSemanaSeleccionada = primerDiaSemanaActual.plusWeeks(offsetSemana.toLong())
        List(5) { dia ->
            primerDiaSemanaSeleccionada.plusDays(dia.toLong())
        }
    }
    
    // Estado para expandir/colapsar tarjetas
    var expandedCardIndex by remember { mutableStateOf(-1) }
    
    // Estado para mostrar/ocultar recomendaciones
    var showRecommendations by remember { mutableStateOf(false) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        "Minuta Semanal",
                        fontWeight = FontWeight.Bold
                    ) 
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { showRecommendations = !showRecommendations }) {
                        Icon(
                            imageVector = if (showRecommendations) 
                                Icons.Default.Check 
                            else 
                                Icons.Default.Info,
                            contentDescription = "Recomendaciones"
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
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Encabezado de la minuta
            item {
                MinutaHeaderCard(
                    nombreMinuta = minutaSemanal.nombre,
                    semana = minutaSemanal.semana,
                    totalCalorias = minutaSemanal.totalCaloriasSemana,
                    promedioCalorias = minutaSemanal.promedioCaloriasDiario,
                    fechaInicio = fechasSemanales.first(),
                    fechaFin = fechasSemanales.last(),
                    offsetSemana = offsetSemana,
                    onSemanaAnterior = { if (offsetSemana > 0) offsetSemana-- },
                    onSemanaSiguiente = { if (offsetSemana < 2) offsetSemana++ }
                )
            }
            
            // Recomendaciones nutricionales (expandible)
            item {
                AnimatedVisibility(
                    visible = showRecommendations,
                    enter = expandVertically() + fadeIn(),
                    exit = shrinkVertically() + fadeOut()
                ) {
                    RecommendationsCard(
                        recommendations = minutaSemanal.obtenerRecomendacionesSemanales()
                    )
                }
            }
            
            // Lista de las 5 recetas semanales
            itemsIndexed(recetasSemanales.toList()) { index, receta ->
                RecetaDiaCard(
                    receta = receta,
                    diaIndex = index,
                    fecha = fechasSemanales[index],
                    isExpanded = expandedCardIndex == index,
                    onExpandToggle = {
                        expandedCardIndex = if (expandedCardIndex == index) -1 else index
                    },
                    onRecetaClick = { onNavigateToReceta(receta.id) }
                )
            }
            
            // Espaciador final
            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

/**
 * Tarjeta con el encabezado de la minuta semanal.
 */
@Composable
private fun MinutaHeaderCard(
    nombreMinuta: String,
    semana: Int,
    totalCalorias: Int,
    promedioCalorias: Double,
    fechaInicio: LocalDate,
    fechaFin: LocalDate,
    offsetSemana: Int,
    onSemanaAnterior: () -> Unit,
    onSemanaSiguiente: () -> Unit
) {
    val formatoFecha = DateTimeFormatter.ofPattern("d 'de' MMM", Locale("es", "CL"))
    
    // Determinar el texto descriptivo de la semana
    val textoSemana = when(offsetSemana) {
        0 -> "Esta Semana"
        1 -> "Próxima Semana"
        2 -> "En 2 Semanas"
        else -> "Semana #$semana"
    }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Animación de calendario simple
            Box(
                modifier = Modifier.size(120.dp),
                contentAlignment = Alignment.Center
            ) {
                val infiniteTransition = rememberInfiniteTransition(label = "calendar")
                val scale by infiniteTransition.animateFloat(
                    initialValue = 0.9f,
                    targetValue = 1.1f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(1500, easing = FastOutSlowInEasing),
                        repeatMode = RepeatMode.Reverse
                    ),
                    label = "scale"
                )
                Text(
                    text = "📅",
                    fontSize = 72.sp,
                    modifier = Modifier.graphicsLayer { 
                        scaleX = scale
                        scaleY = scale
                    }
                )
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Controles de navegación de semanas
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onSemanaAnterior,
                    enabled = offsetSemana > 0
                ) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowLeft,
                        contentDescription = "Semana anterior",
                        tint = if (offsetSemana > 0) 
                            MaterialTheme.colorScheme.primary 
                        else 
                            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                    )
                }
                
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(horizontal = 16.dp)
                ) {
                    Text(
                        text = textoSemana,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "Semana #$semana",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                    )
                }
                
                IconButton(
                    onClick = onSemanaSiguiente,
                    enabled = offsetSemana < 2
                ) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowRight,
                        contentDescription = "Semana siguiente",
                        tint = if (offsetSemana < 2) 
                            MaterialTheme.colorScheme.primary 
                        else 
                            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                    )
                }
            }
            
            // Mostrar rango de fechas
            Text(
                text = "${fechaInicio.format(formatoFecha)} - ${fechaFin.format(formatoFecha)}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f),
                fontWeight = FontWeight.Medium
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Información nutricional resumida
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                NutritionInfoItem(
                    icon = Icons.Default.Favorite,
                    label = "Total Semanal",
                    value = "$totalCalorias kcal"
                )
                
                NutritionInfoItem(
                    icon = Icons.Default.Star,
                    label = "Promedio Diario",
                    value = "${String.format("%.0f", promedioCalorias)} kcal"
                )
            }
        }
    }
}

/**
 * Item individual de información nutricional.
 */
@Composable
private fun NutritionInfoItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(8.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold
        )
    }
}

/**
 * Tarjeta con recomendaciones nutricionales semanales.
 */
@Composable
private fun RecommendationsCard(
    recommendations: List<String>
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.tertiary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Recomendaciones Nutricionales",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            recommendations.forEach { recommendation ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Text(
                        text = recommendation,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

/**
 * Tarjeta de receta para cada día de la semana.
 */
@Composable
private fun RecetaDiaCard(
    receta: Receta,
    diaIndex: Int,
    fecha: LocalDate,
    isExpanded: Boolean,
    onExpandToggle: () -> Unit,
    onRecetaClick: () -> Unit
) {
    val dia = when(diaIndex) {
        0 -> "LUNES"
        1 -> "MARTES"
        2 -> "MIÉRCOLES"
        3 -> "JUEVES"
        4 -> "VIERNES"
        else -> "DÍA ${diaIndex + 1}"
    }
    
    val formatoFecha = DateTimeFormatter.ofPattern("d MMM", Locale("es", "CL"))
    val fechaTexto = fecha.format(formatoFecha)
    
    val diaColor = when(diaIndex) {
        0 -> MaterialTheme.colorScheme.primary
        1 -> MaterialTheme.colorScheme.secondary
        2 -> MaterialTheme.colorScheme.tertiary
        3 -> MaterialTheme.colorScheme.error
        4 -> MaterialTheme.colorScheme.primary
        else -> MaterialTheme.colorScheme.primary
    }
    
    // Extraer componentes del nombre de la receta
    val componentesMenu = extraerComponentesMenu(receta.nombre)
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onExpandToggle() },
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isExpanded) 8.dp else 2.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Encabezado del día
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Etiqueta del día
                Surface(
                    modifier = Modifier.clip(RoundedCornerShape(8.dp)),
                    color = diaColor.copy(alpha = 0.15f)
                ) {
                    Column(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = dia,
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = diaColor
                        )
                        Text(
                            text = fechaTexto,
                            style = MaterialTheme.typography.labelSmall,
                            color = diaColor.copy(alpha = 0.8f)
                        )
                    }
                }
                
                Icon(
                    imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = if (isExpanded) "Colapsar" else "Expandir"
                )
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Nombre del plato principal
            Text(
                text = componentesMenu.platoPrincipal,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            
            // Mostrar acompañamiento si existe
            componentesMenu.acompanamiento?.let { acomp ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = 6.dp)
                ) {
                    Text(text = "🥗", style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "con $acomp",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.tertiary,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            
            // Mostrar ensalada si existe
            componentesMenu.ensalada?.let { ensalada ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = 6.dp)
                ) {
                    Text(text = "🥗", style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = ensalada,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.secondary,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            
            // Mostrar postre si existe
            componentesMenu.postre?.let { postre ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = 6.dp)
                ) {
                    Text(text = "🍰", style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = postre,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.error,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            
            // Descripción
            Text(
                text = receta.descripcion,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Información básica
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                RecipeInfoChip(
                    icon = Icons.Default.Place,
                    text = receta.obtenerTiempoPreparacion()
                )
                RecipeInfoChip(
                    icon = Icons.Default.Star,
                    text = receta.obtenerNivelDificultad()
                )
                if (receta.tieneInfoNutricional()) {
                    RecipeInfoChip(
                        icon = Icons.Default.Favorite,
                        text = "${receta.obtenerCaloriasTotales()} kcal"
                    )
                }
            }
            
            // Contenido expandible
            AnimatedVisibility(
                visible = isExpanded,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                ) {
                    Divider()
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    // Información nutricional detallada
                    receta.obtenerInfoNutricional()?.let { nutrition ->
                        Text(
                            text = "Información Nutricional",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                                    RoundedCornerShape(8.dp)
                                )
                                .padding(12.dp)
                        ) {
                            NutritionRow("Proteínas", "${nutrition.proteinas}g")
                            NutritionRow("Carbohidratos", "${nutrition.carbohidratos}g")
                            NutritionRow("Grasas", "${nutrition.grasas}g")
                            if (nutrition.fibra > 0) {
                                NutritionRow("Fibra", "${nutrition.fibra}g")
                            }
                            if (nutrition.sodio > 0) {
                                NutritionRow("Sodio", "${nutrition.sodio}mg")
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        // Recomendaciones específicas
                        nutrition.obtenerRecomendaciones().firstOrNull()?.let { rec ->
                            Surface(
                                modifier = Modifier.fillMaxWidth(),
                                color = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.5f),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    text = rec,
                                    modifier = Modifier.padding(12.dp),
                                    style = MaterialTheme.typography.bodySmall,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                    
                    // Botón para ver receta completa
                    Button(
                        onClick = onRecetaClick,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = null
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Ver Receta Completa")
                    }
                }
            }
        }
    }
}

/**
 * Chip con información de la receta.
 */
@Composable
private fun RecipeInfoChip(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    text: String
) {
    Surface(
        color = MaterialTheme.colorScheme.secondaryContainer,
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(16.dp),
                tint = MaterialTheme.colorScheme.onSecondaryContainer
            )
            Text(
                text = text,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )
        }
    }
}

/**
 * Fila con información nutricional.
 */
@Composable
private fun NutritionRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Medium
        )
    }
}

/**
 * Data class para almacenar los componentes del menú diario.
 */
data class ComponentesMenu(
    val platoPrincipal: String,
    val acompanamiento: String? = null,
    val ensalada: String? = null,
    val postre: String? = null
)

/**
 * Extrae los componentes del menú desde el nombre de la receta.
 * Formato esperado: "Plato Principal con Acompañamiento + Ensalada + Postre"
 */
private fun extraerComponentesMenu(nombreCompleto: String): ComponentesMenu {
    var platoPrincipal = nombreCompleto
    var acompanamiento: String? = null
    var ensalada: String? = null
    var postre: String? = null
    
    // Dividir por " + " para separar plato principal de ensalada/postre
    val partesPrincipales = nombreCompleto.split(" + ")
    
    if (partesPrincipales.isNotEmpty()) {
        // La primera parte contiene el plato principal (posiblemente con acompañamiento)
        val primeraparte = partesPrincipales[0]
        
        // Verificar si tiene "con" para acompañamiento
        if (primeraparte.contains(" con ", ignoreCase = true)) {
            val partesAcomp = primeraparte.split(" con ", limit = 2)
            platoPrincipal = partesAcomp[0].trim()
            acompanamiento = partesAcomp.getOrNull(1)?.trim()
        } else {
            platoPrincipal = primeraparte.trim()
        }
        
        // Procesar el resto de las partes (ensaladas y postres)
        for (i in 1 until partesPrincipales.size) {
            val parte = partesPrincipales[i].trim()
            
            // Verificar si es ensalada (contiene "Ensalada")
            if (parte.contains("Ensalada", ignoreCase = true)) {
                ensalada = parte
            }
            // Si no es ensalada, asumimos que es postre
            else {
                postre = parte
            }
        }
    }
    
    return ComponentesMenu(
        platoPrincipal = platoPrincipal,
        acompanamiento = acompanamiento,
        ensalada = ensalada,
        postre = postre
    )
}
