package com.example.recetas.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.recetas.accessibility.hapticFeedback

/**
 * Logo de Instagram con gradiente y cámara
 */
@Composable
fun InstagramLogo(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    
    Box(
        modifier = modifier
            .size(64.dp)
            .clip(CircleShape)
            .clickable {
                hapticFeedback(context)
                onClick()
            },
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val gradient = Brush.linearGradient(
                colors = listOf(
                    Color(0xFFFCAF45),  // Amarillo/naranja
                    Color(0xFFE1306C),  // Rosa
                    Color(0xFFC13584),  // Morado
                    Color(0xFF833AB4)   // Morado oscuro
                ),
                start = Offset(0f, size.height),
                end = Offset(size.width, 0f)
            )
            
            // Fondo circular con gradiente
            drawCircle(
                brush = gradient,
                radius = size.width / 2
            )
            
            val padding = size.width * 0.25f
            val iconSize = size.width - (padding * 2)
            val cornerRadius = iconSize * 0.25f
            
            // Cuadrado redondeado (marco de la cámara)
            drawRoundRect(
                color = Color.White,
                topLeft = Offset(padding, padding),
                size = Size(iconSize, iconSize),
                cornerRadius = CornerRadius(cornerRadius, cornerRadius),
                style = Stroke(width = 3.dp.toPx())
            )
            
            // Círculo interior (lente)
            val centerX = size.width / 2
            val centerY = size.height / 2
            val lensRadius = iconSize * 0.28f
            
            drawCircle(
                color = Color.White,
                radius = lensRadius,
                center = Offset(centerX, centerY),
                style = Stroke(width = 3.dp.toPx())
            )
            
            // Punto arriba derecha (flash)
            val flashX = size.width - padding - (iconSize * 0.25f)
            val flashY = padding + (iconSize * 0.25f)
            
            drawCircle(
                color = Color.White,
                radius = 2.5.dp.toPx(),
                center = Offset(flashX, flashY)
            )
        }
    }
}

