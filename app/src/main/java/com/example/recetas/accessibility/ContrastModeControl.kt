package com.example.recetas.accessibility

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.recetas.R

/**
 * Control visual para ajustar el nivel de contraste.
 * 
 * Muestra la letra "C" (Contraste) con diferentes tamaños visuales
 * para indicar el nivel actual de contraste. Al hacer clic, cicla
 * entre los cuatro niveles disponibles.
 * 
 * Niveles visuales:
 * - Normal: "C" tamaño base (20sp)
 * - Aumentado: "C" ligeramente más grande (24sp)
 * - Alto: "C" grande (28sp)
 * - Muy Alto: "C" muy grande (32sp)
 * 
 * El fondo del control también cambia de intensidad para reflejar
 * visualmente el nivel de contraste actual.
 * 
 * @param contrastMode Nivel de contraste actual
 * @param onContrastModeChange Callback cuando se cambia el nivel
 * @param modifier Modificador opcional
 */
@Composable
fun ContrastModeControl(
    contrastMode: ContrastMode,
    onContrastModeChange: (ContrastMode) -> Unit,
    modifier: Modifier = Modifier
) {
    // Calcular el tamaño de la "C" basado en el modo de contraste
    val cSize = when (contrastMode) {
        ContrastMode.NORMAL -> 20.sp
        ContrastMode.AUMENTADO -> 24.sp
        ContrastMode.ALTO -> 28.sp
        ContrastMode.MUY_ALTO -> 32.sp
    }
    
    // Calcular el color de fondo basado en el nivel de contraste
    val backgroundColor = when (contrastMode) {
        ContrastMode.NORMAL -> MaterialTheme.colorScheme.surfaceVariant
        ContrastMode.AUMENTADO -> MaterialTheme.colorScheme.primaryContainer
        ContrastMode.ALTO -> MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
        ContrastMode.MUY_ALTO -> MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
    }
    
    Box(
        modifier = modifier
            .size(50.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(backgroundColor)
            .border(
                width = 2.dp,
                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(8.dp)
            )
            .clickable {
                // Ciclar al siguiente modo de contraste
                val nextMode = ContrastMode.getNext(contrastMode)
                onContrastModeChange(nextMode)
            }
            .padding(4.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "C",
            fontSize = cSize,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

/**
 * Indicador de nivel de contraste con ícono y etiqueta.
 * 
 * Muestra el icono de contraste junto con el nombre del nivel actual.
 * Útil para mostrar en menús o configuraciones.
 * 
 * @param contrastMode Nivel de contraste actual
 * @param modifier Modificador opcional
 */
@Composable
fun ContrastModeIndicator(
    contrastMode: ContrastMode,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_contrast),
            contentDescription = "Contraste",
            modifier = Modifier.size(24.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        
        Column {
            Text(
                text = "Contraste: ${contrastMode.label}",
                fontSize = 14.scaledSp(),
                fontWeight = FontWeight.Medium
            )
            
            Text(
                text = contrastMode.description,
                fontSize = 12.scaledSp(),
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
        }
    }
}
