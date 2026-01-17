package com.example.recetas.accessibility

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Sistema de escalado de fuente para accesibilidad.
 * 
 * PROPÓSITO:
 * Permite a usuarios con baja visión ajustar el tamaño de todas las fuentes
 * de la aplicación según sus necesidades.
 * 
 * NIVELES:
 * - Pequeño: 0.85x (para usuarios con buena visión)
 * - Normal: 1.0x (tamaño estándar)
 * - Grande: 1.15x (para facilitar lectura)
 * - Extra Grande: 1.3x (para baja visión)
 * - Muy Grande: 1.5x (para visión muy reducida)
 * 
 * CUMPLIMIENTO:
 * - WCAG 2.1 Criterio 1.4.4: El texto puede cambiar hasta 200% sin pérdida
 * - WCAG 2.1 Criterio 1.4.8: Presentación visual personalizable
 * - Android Accessibility: Soporte para preferencias de tamaño de texto
 */

/**
 * Niveles de escala de fuente disponibles.
 */
enum class FontScale(val scale: Float, val label: String, val emoji: String) {
    SMALL(0.85f, "Pequeño", "A"),
    NORMAL(1.0f, "Normal", "A"),
    LARGE(1.15f, "Grande", "A"),
    EXTRA_LARGE(1.3f, "Extra Grande", "A"),
    VERY_LARGE(1.5f, "Muy Grande", "A");
    
    /**
     * Escala un valor de sp según este nivel.
     */
    fun scaleSp(baseSp: Float): TextUnit {
        return (baseSp * scale).sp
    }
}

/**
 * CompositionLocal para el estado de escala de fuente.
 */
val LocalFontScale = compositionLocalOf { mutableStateOf(FontScale.NORMAL) }

/**
 * Extension function para escalar automáticamente valores sp.
 * 
 * Uso:
 * ```kotlin
 * fontSize = 16.scaledSp()  // Se escala según el nivel actual
 * ```
 */
@Composable
fun Int.scaledSp(): TextUnit {
    val fontScale = LocalFontScale.current
    return fontScale.value.scaleSp(this.toFloat())
}

@Composable
fun Float.scaledSp(): TextUnit {
    val fontScale = LocalFontScale.current
    return fontScale.value.scaleSp(this)
}

/**
 * Manager para persistir la preferencia de escala de fuente.
 */
object FontScaleManager {
    private const val PREFS_NAME = "font_scale_prefs"
    private const val KEY_SCALE = "scale"
    
    /**
     * Guarda la escala de fuente seleccionada.
     */
    fun saveScale(context: Context, scale: FontScale) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_SCALE, scale.name).apply()
    }
    
    /**
     * Carga la escala de fuente guardada.
     */
    fun loadScale(context: Context): FontScale {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val scaleName = prefs.getString(KEY_SCALE, FontScale.NORMAL.name)
        return try {
            FontScale.valueOf(scaleName ?: FontScale.NORMAL.name)
        } catch (e: IllegalArgumentException) {
            FontScale.NORMAL
        }
    }
}

/**
 * Botón compacto para cambiar tamaño de fuente.
 * Se puede colocar en la TopAppBar de cualquier pantalla.
 */
@Composable
fun FontSizeButton(
    currentScale: MutableState<FontScale>,
    onScaleChange: (FontScale) -> Unit,
    modifier: Modifier = Modifier
) {
    var showMenu by remember { mutableStateOf(false) }
    
    Box(modifier = modifier) {
        // Botón que muestra el emoji del nivel actual
        IconButton(
            onClick = { showMenu = true },
            modifier = Modifier.semantics {
                contentDescription = "Tamaño de texto: ${currentScale.value.label}. Toca para cambiar"
            }
        ) {
            Text(
                text = "A",
                fontSize = when(currentScale.value) {
                    FontScale.SMALL -> 16.sp
                    FontScale.NORMAL -> 20.sp
                    FontScale.LARGE -> 24.sp
                    FontScale.EXTRA_LARGE -> 28.sp
                    FontScale.VERY_LARGE -> 32.sp
                },
                fontWeight = FontWeight.Bold
            )
        }
        
        // Menú dropdown con todos los niveles
        DropdownMenu(
            expanded = showMenu,
            onDismissRequest = { showMenu = false }
        ) {
            FontScale.entries.forEach { scale ->
                DropdownMenuItem(
                    text = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = scale.emoji,
                                fontSize = 20.sp
                            )
                            Text(
                                text = scale.label,
                                fontSize = scale.scaleSp(16f),
                                fontWeight = if (scale == currentScale.value) 
                                    FontWeight.Bold 
                                else 
                                    FontWeight.Normal
                            )
                            if (scale == currentScale.value) {
                                Text(
                                    text = "✓",
                                    color = MaterialTheme.colorScheme.primary,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    },
                    onClick = {
                        onScaleChange(scale)
                        showMenu = false
                    }
                )
            }
        }
    }
}

/**
 * Panel completo para configuración de tamaño de fuente.
 * Se puede usar en una pantalla de ajustes.
 */
@Composable
fun FontSizeControlPanel(
    currentScale: MutableState<FontScale>,
    onScaleChange: (FontScale) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "📏 Tamaño de Texto",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            
            Text(
                text = "Selecciona el tamaño que mejor se adapte a tu vista:",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Botones para cada nivel
            FontScale.entries.forEach { scale ->
                val isSelected = currentScale.value == scale
                
                Button(
                    onClick = { onScaleChange(scale) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .then(
                            if (isSelected) {
                                Modifier.border(
                                    width = 2.dp,
                                    color = MaterialTheme.colorScheme.primary,
                                    shape = RoundedCornerShape(8.dp)
                                )
                            } else Modifier
                        )
                        .semantics {
                            contentDescription = "${scale.label}: ${if (isSelected) "Seleccionado" else "No seleccionado"}"
                        },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isSelected) 
                            MaterialTheme.colorScheme.primaryContainer
                        else 
                            MaterialTheme.colorScheme.surface
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = scale.emoji,
                                fontSize = 24.sp
                            )
                            Text(
                                text = scale.label,
                                fontSize = scale.scaleSp(16f),
                                color = if (isSelected)
                                    MaterialTheme.colorScheme.onPrimaryContainer
                                else
                                    MaterialTheme.colorScheme.onSurface
                            )
                        }
                        
                        if (isSelected) {
                            Text(
                                text = "✓",
                                fontSize = 24.sp,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Texto de ejemplo
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Vista previa:",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Este es un ejemplo de cómo se verá el texto con el tamaño seleccionado. " +
                                "Las recetas chilenas tradicionales como el Charquicán y el Pastel de Papas " +
                                "se mostrarán con esta tipografía.",
                        fontSize = currentScale.value.scaleSp(16f),
                        lineHeight = currentScale.value.scaleSp(24f)
                    )
                }
            }
        }
    }
}
