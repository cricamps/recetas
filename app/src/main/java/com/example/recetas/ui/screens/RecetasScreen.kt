package com.example.recetas.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.recetas.ui.theme.*

/**
 * Data class que representa una receta
 * 
 * @param nombre Nombre de la receta
 * @param cocina Tipo de cocina
 * @param tiempo Tiempo de preparación
 * @param dificultad Nivel de dificultad (Fácil, Media, Difícil)
 * @param descripcion Descripción breve de la receta
 */
data class Receta(
    val nombre: String,
    val cocina: String,
    val tiempo: String,
    val dificultad: String,
    val descripcion: String = ""
)

/**
 * Pantalla principal de Recetas
 * Muestra una lista de recetas tradicionales con búsqueda y favoritos
 * 
 * @author Cristobal Camps
 * @date Enero 2026
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecetasScreen() {
    // Estado para el texto de búsqueda
    var searchText by remember { mutableStateOf("") }
    
    // Lista de recetas tradicionales con descripciones
    val recetas = remember {
        listOf(
            Receta("Charquicán", "Chile", "60 min", "Fácil", "Guiso tradicional con papas, zapallo y carne"),
            Receta("Pastel de Papas", "Chile", "90 min", "Media", "Delicioso pastel con carne molida y puré"),
            Receta("Cazuela", "Chile", "80 min", "Fácil", "Sopa nutritiva con verduras y carne"),
            Receta("Porotos con Riendas", "Chile", "120 min", "Media", "Porotos con fideos y zapallo"),
            Receta("Empanadas de Pino", "Chile", "120 min", "Media", "Empanadas rellenas con carne, cebolla y aceitunas"),
            Receta("Carbonada", "Chile", "70 min", "Fácil", "Guiso con zapallo, choclo y carne"),
            Receta("Plateada", "Chile", "180 min", "Difícil", "Carne de vacuno cocida lentamente"),
            Receta("Arroz Graneado", "Chile", "30 min", "Fácil", "Arroz suelto y perfectamente cocido"),
            Receta("Pollo Asado", "Chile", "50 min", "Fácil", "Pollo dorado al horno con especias"),
            Receta("Leche Asada", "Chile", "70 min", "Media", "Postre tradicional con leche y caramelo")
        )
    }
    
    // Filtrar recetas por búsqueda
    val recetasFiltradas = recetas.filter { 
        it.nombre.contains(searchText, ignoreCase = true) 
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Recetas",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "${recetasFiltradas.size} recetas disponibles",
                            fontSize = 12.sp,
                            color = Color.White.copy(alpha = 0.9f)
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { }) {
                        Icon(
                            Icons.Default.Person,
                            contentDescription = "Perfil",
                            tint = Color.White
                        )
                    }
                    IconButton(onClick = { }) {
                        Icon(
                            Icons.Default.Menu,
                            contentDescription = "Menú",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = ChileanRed
                )
            )
        }
    ) { paddingValues ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            color = BackgroundLight
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // Campo de búsqueda
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    OutlinedTextField(
                        value = searchText,
                        onValueChange = { searchText = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { 
                            Text(
                                "Buscar receta...",
                                color = TextMuted
                            ) 
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Buscar",
                                tint = ChileanRed
                            )
                        },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = ChileanRed,
                            unfocusedBorderColor = Color.Transparent,
                            focusedLabelColor = ChileanRed,
                            cursorColor = ChileanRed,
                            unfocusedContainerColor = Color.White,
                            focusedContainerColor = Color.White
                        ),
                        shape = RoundedCornerShape(16.dp)
                    )
                }
                
                // Mensaje si no hay resultados
                if (recetasFiltradas.isEmpty()) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 32.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(text = "🔍", fontSize = 48.sp)
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "No se encontraron recetas",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextDark
                            )
                            Text(
                                text = "Intenta con otro término de búsqueda",
                                fontSize = 14.sp,
                                color = TextMuted
                            )
                        }
                    }
                } else {
                    // Lista de recetas
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(recetasFiltradas) { receta ->
                            RecetaCard(receta)
                        }
                    }
                }
            }
        }
    }
}

/**
 * Componente Card que muestra la información de una receta
 * Incluye nombre, descripción, tiempo, dificultad y botón de favorito
 * 
 * @param receta Objeto Receta con la información a mostrar
 */
@Composable
fun RecetaCard(receta: Receta) {
    // Estado para controlar si la receta está marcada como favorita
    var isFavorite by remember { mutableStateOf(false) }
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp,
            pressedElevation = 8.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                // Columna con la información de la receta
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Nombre de la receta
                    Text(
                        text = receta.nombre,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextDark
                    )
                    
                    // Descripción
                    if (receta.descripcion.isNotEmpty()) {
                        Text(
                            text = receta.descripcion,
                            fontSize = 13.sp,
                            color = TextMuted,
                            lineHeight = 18.sp
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(4.dp))
                    
                    // Información adicional (tiempo y dificultad)
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        InfoChip(
                            text = "⏱️ ${receta.tiempo}",
                            backgroundColor = BorderSoft,
                            textColor = TextDark
                        )
                        InfoChip(
                            text = getDifficultyIcon(receta.dificultad) + " ${receta.dificultad}",
                            backgroundColor = getDifficultyColor(receta.dificultad),
                            textColor = getDifficultyTextColor(receta.dificultad)
                        )
                    }
                }
                
                // Botón de favorito
                IconButton(
                    onClick = { isFavorite = !isFavorite }
                ) {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = "Favorito",
                        tint = if (isFavorite) ChileanRed else Color.Gray,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
        }
    }
}

/**
 * Función auxiliar para obtener el icono de dificultad
 */
fun getDifficultyIcon(dificultad: String): String {
    return when (dificultad) {
        "Fácil" -> "✅"
        "Media" -> "⭐"
        else -> "🔥"
    }
}

/**
 * Función auxiliar para obtener el color de fondo según dificultad
 */
fun getDifficultyColor(dificultad: String): Color {
    return when (dificultad) {
        "Fácil" -> Color(0xFFE8F5E9)
        "Media" -> Color(0xFFFFF3E0)
        else -> Color(0xFFFFEBEE)
    }
}

/**
 * Función auxiliar para obtener el color del texto según dificultad
 */
fun getDifficultyTextColor(dificultad: String): Color {
    return when (dificultad) {
        "Fácil" -> Color(0xFF2E7D32)
        "Media" -> Color(0xFFE65100)
        else -> Color(0xFFC62828)
    }
}

/**
 * Componente que muestra un chip de información
 * Usado para mostrar tiempo y dificultad de las recetas
 * 
 * @param text Texto a mostrar en el chip
 * @param backgroundColor Color de fondo del chip
 * @param textColor Color del texto
 */
@Composable
fun InfoChip(
    text: String,
    backgroundColor: Color = BorderSoft,
    textColor: Color = TextDark
) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = backgroundColor
    ) {
        Text(
            text = text,
            fontSize = 12.sp,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            color = textColor,
            fontWeight = FontWeight.Medium
        )
    }
}

/**
 * Vista previa de la pantalla completa de recetas
 */
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun RecetasScreenPreview() {
    RecetasTheme {
        RecetasScreen()
    }
}

/**
 * Vista previa de una tarjeta de receta individual
 */
@Preview(showBackground = true)
@Composable
fun RecetaCardPreview() {
    RecetasTheme {
        RecetaCard(
            Receta("Charquicán", "Chile", "60 min", "Fácil", "Guiso tradicional con papas, zapallo y carne")
        )
    }
}
