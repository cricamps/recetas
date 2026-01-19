package com.example.recetas.voice

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.recetas.accessibility.scaledSp

/**
 * Botón de navegación por voz con indicador visual de estado.
 * 
 * Este componente proporciona:
 * - Botón flotante para activar/desactivar reconocimiento de voz
 * - Animación pulsante cuando está escuchando
 * - Feedback visual del estado (escuchando/inactivo)
 * - Mensajes de confirmación y error
 * - Diseño accesible con colores de alto contraste
 * 
 * @param voiceManager Gestor de navegación por voz
 * @param modifier Modificador de Compose
 */
@Composable
fun VoiceNavigationButton(
    voiceManager: VoiceNavigationManager,
    modifier: Modifier = Modifier
) {
    val isListening by voiceManager.isListening
    val feedbackMessage by voiceManager.feedbackMessage
    
    // Animación de pulsación cuando está escuchando
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(600, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )
    
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Mensaje de feedback
        if (feedbackMessage.isNotEmpty()) {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = if (isListening) 
                        MaterialTheme.colorScheme.primaryContainer
                    else 
                        MaterialTheme.colorScheme.surfaceVariant
                ),
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                Text(
                    text = feedbackMessage,
                    style = MaterialTheme.typography.bodyMedium,
                    fontSize = 14.scaledSp(),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(12.dp),
                    textAlign = TextAlign.Center
                )
            }
        }
        
        // Botón de voz
        FloatingActionButton(
            onClick = {
                if (isListening) {
                    voiceManager.stopListening()
                } else {
                    voiceManager.startListening()
                }
            },
            containerColor = if (isListening) 
                MaterialTheme.colorScheme.error
            else 
                MaterialTheme.colorScheme.primary,
            contentColor = if (isListening)
                MaterialTheme.colorScheme.onError
            else
                MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier
                .size(if (isListening) 72.dp else 64.dp)
                .then(if (isListening) Modifier.scale(scale) else Modifier)
        ) {
            Text(
                text = if (isListening) "🎤" else "🎙️",
                fontSize = if (isListening) 36.sp else 32.sp
            )
        }
        
        // Indicador de estado
        Text(
            text = if (isListening) "🎤 Escuchando..." else "Toca para hablar",
            style = MaterialTheme.typography.labelMedium,
            fontSize = 12.scaledSp(),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}

/**
 * Botón compacto de voz para la barra superior
 * 
 * @param voiceManager Gestor de navegación por voz
 * @param modifier Modificador de Compose
 */
@Composable
fun VoiceNavigationIconButton(
    voiceManager: VoiceNavigationManager,
    modifier: Modifier = Modifier
) {
    val isListening by voiceManager.isListening
    
    // Animación de pulsación cuando está escuchando
    val infiniteTransition = rememberInfiniteTransition(label = "pulse_icon")
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(600, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale_icon"
    )
    
    Box(modifier = modifier) {
        IconButton(
            onClick = {
                if (isListening) {
                    voiceManager.stopListening()
                } else {
                    voiceManager.startListening()
                }
            },
            modifier = Modifier.then(if (isListening) Modifier.scale(scale) else Modifier)
        ) {
            Text(
                text = if (isListening) "🎤" else "🎙️",
                fontSize = 24.sp
            )
        }
        
        // Indicador visual de escucha
        if (isListening) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .align(Alignment.TopEnd)
                    .background(
                        color = MaterialTheme.colorScheme.error,
                        shape = CircleShape
                    )
            )
        }
    }
}

/**
 * Snackbar con el mensaje de feedback del comando de voz
 * 
 * @param voiceManager Gestor de navegación por voz
 * @param snackbarHostState Estado del Snackbar
 */
@Composable
fun VoiceFeedbackSnackbar(
    voiceManager: VoiceNavigationManager,
    snackbarHostState: SnackbarHostState
) {
    val feedbackMessage by voiceManager.feedbackMessage
    
    LaunchedEffect(feedbackMessage) {
        if (feedbackMessage.isNotEmpty()) {
            snackbarHostState.showSnackbar(
                message = feedbackMessage,
                duration = SnackbarDuration.Short
            )
        }
    }
}
