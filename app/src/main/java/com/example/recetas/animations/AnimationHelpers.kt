package com.example.recetas.animations

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.runtime.*

/**
 * Funciones helper para animaciones reutilizables
 */

/**
 * Animación de aparición escalonada para listas
 */
@Composable
fun rememberStaggeredListAnimation(
    itemIndex: Int,
    delayPerItem: Int = 50
): State<Float> {
    return animateFloatAsState(
        targetValue = 1f,
        animationSpec = tween(
            durationMillis = 300,
            delayMillis = itemIndex * delayPerItem,
            easing = FastOutSlowInEasing
        ),
        label = "staggered_list_item_$itemIndex"
    )
}

/**
 * Animación de pulsación para botones
 */
@Composable
fun rememberPressAnimation(isPressed: Boolean): State<Float> {
    return animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "press_animation"
    )
}

/**
 * Animación de shake para errores
 */
@Composable
fun rememberShakeAnimation(trigger: Boolean): State<Float> {
    val transition = updateTransition(targetState = trigger, label = "shake")
    
    return transition.animateFloat(
        transitionSpec = {
            keyframes {
                durationMillis = 500
                0f at 0
                -10f at 100
                10f at 200
                -10f at 300
                10f at 400
                0f at 500
            }
        },
        label = "shake_offset"
    ) { state ->
        if (state) 0f else 0f
    }
}

/**
 * Animación de pulso (para loading o indicadores)
 */
@Composable
fun rememberPulseAnimation(): State<Float> {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    
    return infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1000,
                easing = FastOutSlowInEasing
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_scale"
    )
}

/**
 * Animación de rotación continua (para loading spinners)
 */
@Composable
fun rememberRotationAnimation(): State<Float> {
    val infiniteTransition = rememberInfiniteTransition(label = "rotation")
    
    return infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1000,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation_degrees"
    )
}

/**
 * Animación de fade in/out para elementos que aparecen/desaparecen
 */
@Composable
fun rememberVisibilityAnimation(visible: Boolean): State<Float> {
    return animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = tween(
            durationMillis = 300,
            easing = FastOutSlowInEasing
        ),
        label = "visibility"
    )
}
