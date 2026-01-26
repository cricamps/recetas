package com.example.recetas.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer

/**
 * ANIMACIONES EXAGERADAS - Para demostración académica
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnimatedCard(
    onClick: () -> Unit,
    modifier: Modifier,
    elevation: CardElevation,
    colors: CardColors,
    content: @Composable ColumnScope.() -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    
    // EXAGERADO: 15% de reducción
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.85f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioLowBouncy,  // MÁS REBOTE
            stiffness = Spring.StiffnessVeryLow  // MÁS LENTO
        ),
        label = "card_scale"
    )
    
    Card(
        onClick = onClick,
        modifier = modifier.graphicsLayer {
            scaleX = scale
            scaleY = scale
        },
        elevation = elevation,
        colors = colors,
        interactionSource = interactionSource,
        content = content
    )
}

@Composable
fun AnimatedFAB(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    containerColor: androidx.compose.ui.graphics.Color,
    content: @Composable () -> Unit
) {
    var visible by remember { mutableStateOf(false) }
    
    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(500)  // 500ms de delay inicial
        visible = true
    }
    
    // EXAGERADO: Giro de 720° (dos vueltas completas)
    val scale by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioLowBouncy,
            stiffness = Spring.StiffnessVeryLow  // MUY LENTO
        ),
        label = "fab_scale"
    )
    
    val rotation by animateFloatAsState(
        targetValue = if (visible) 0f else 720f,  // DOS VUELTAS COMPLETAS
        animationSpec = tween(
            durationMillis = 1200,  // 1.2 SEGUNDOS
            easing = FastOutSlowInEasing
        ),
        label = "fab_rotation"
    )
    
    FloatingActionButton(
        onClick = onClick,
        modifier = modifier.graphicsLayer {
            scaleX = scale
            scaleY = scale
            rotationZ = rotation
        },
        containerColor = containerColor
    ) {
        content()
    }
}

@Composable
fun AnimatedIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    
    // EXAGERADO: 15% de reducción + REBOTE PRONUNCIADO
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.85f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioLowBouncy,  // MUCHO REBOTE
            stiffness = Spring.StiffnessVeryLow  // MUY LENTO
        ),
        label = "icon_button_scale"
    )
    
    // Rotación sutil al presionar
    val rotation by animateFloatAsState(
        targetValue = if (isPressed) 15f else 0f,  // Rota 15° al presionar
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioLowBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "icon_button_rotation"
    )
    
    IconButton(
        onClick = onClick,
        modifier = modifier.graphicsLayer {
            scaleX = scale
            scaleY = scale
            rotationZ = rotation
        },
        interactionSource = interactionSource
    ) {
        content()
    }
}

@Composable
fun AnimatedButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    colors: ButtonColors = ButtonDefaults.buttonColors(),
    content: @Composable RowScope.() -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    
    // EXAGERADO: 15% de reducción
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.85f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioLowBouncy,
            stiffness = Spring.StiffnessVeryLow
        ),
        label = "button_scale"
    )
    
    Button(
        onClick = onClick,
        modifier = modifier.graphicsLayer {
            scaleX = scale
            scaleY = scale
        },
        enabled = enabled,
        colors = colors,
        interactionSource = interactionSource,
        content = content
    )
}

@Composable
fun PulsingLoadingIndicator(
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "loading_pulse")
    
    val scale by infiniteTransition.animateFloat(
        initialValue = 0.5f,  // Mucho más pequeño
        targetValue = 1.5f,   // Mucho más grande
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1200,
                easing = FastOutSlowInEasing
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_scale"
    )
    
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.2f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1200,
                easing = FastOutSlowInEasing
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_alpha"
    )
    
    CircularProgressIndicator(
        modifier = modifier.graphicsLayer {
            scaleX = scale
            scaleY = scale
            this.alpha = alpha
        }
    )
}
