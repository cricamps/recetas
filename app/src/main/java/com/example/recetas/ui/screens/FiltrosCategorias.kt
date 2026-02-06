package com.example.recetas.ui.screens

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Enum que representa las categorías disponibles para filtrar recetas.
 */
enum class CategoriaFiltro(val displayName: String) {
    PLATO_PRINCIPAL("Plato Principal"),
    SOPA_GUISO("Sopa/Guiso"),
    POSTRE("Postre"),
    ENSALADA("Ensalada"),
    ACOMPANAMIENTO("Acompañamiento");
    
    companion object {
        fun fromString(categoria: String): CategoriaFiltro? {
            return when (categoria.trim()) {
                "Plato Principal" -> PLATO_PRINCIPAL
                "Sopa/Guiso", "Sopa", "Guiso" -> SOPA_GUISO
                "Postre" -> POSTRE
                "Ensalada" -> ENSALADA
                "Acompañamiento" -> ACOMPANAMIENTO
                else -> null
            }
        }
    }
}

/**
 * Función de extensión para convertir string de categoría a CategoriaFiltro.
 */
fun String.toCategoriaFiltro(): CategoriaFiltro? {
    return CategoriaFiltro.fromString(this)
}

/**
 * Componente de filtros por categoría con chips horizontales.
 * 
 * @param categoriasSeleccionadas Set de categorías actualmente seleccionadas
 * @param onCategoriaToggle Callback cuando se hace click en una categoría
 * @param modifier Modificador del componente
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FiltrosCategorias(
    categoriasSeleccionadas: Set<CategoriaFiltro>,
    onCategoriaToggle: (CategoriaFiltro) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        // Título
        Text(
            text = "Filtrar por categoría:",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        
        // Chips en scroll horizontal
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Botón "Todos"
            FilterChip(
                selected = categoriasSeleccionadas.size == CategoriaFiltro.entries.size,
                onClick = {
                    // Si están todas seleccionadas, deseleccionar todas
                    // Si no están todas, seleccionar todas
                    val todasSeleccionadas = categoriasSeleccionadas.size == CategoriaFiltro.entries.size
                    if (todasSeleccionadas) {
                        // Deseleccionar todas (vaciar el set)
                        CategoriaFiltro.entries.forEach { onCategoriaToggle(it) }
                    } else {
                        // Seleccionar las que faltan
                        CategoriaFiltro.entries.forEach { categoria ->
                            if (categoria !in categoriasSeleccionadas) {
                                onCategoriaToggle(categoria)
                            }
                        }
                    }
                },
                label = { Text("Todas") },
                leadingIcon = if (categoriasSeleccionadas.size == CategoriaFiltro.entries.size) {
                    {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Seleccionado",
                            modifier = Modifier.size(FilterChipDefaults.IconSize)
                        )
                    }
                } else null
            )
            
            // Chip para cada categoría
            CategoriaFiltro.entries.forEach { categoria ->
                FilterChip(
                    selected = categoria in categoriasSeleccionadas,
                    onClick = { onCategoriaToggle(categoria) },
                    label = { Text(categoria.displayName) },
                    leadingIcon = if (categoria in categoriasSeleccionadas) {
                        {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Seleccionado",
                                modifier = Modifier.size(FilterChipDefaults.IconSize)
                            )
                        }
                    } else null
                )
            }
        }
    }
}
