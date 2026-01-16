package com.example.recetas.accessibility

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics

/**
 * Utilidades de accesibilidad para la aplicación Recetas.
 * 
 * Este archivo contiene funciones helper para mejorar la accesibilidad
 * de la aplicación para personas con discapacidad visual.
 * 
 * Características implementadas:
 * - Feedback háptico para confirmación de acciones
 * - Descripciones semánticas para lectores de pantalla
 * - Anuncios de accesibilidad
 * - Soporte para TalkBack
 */

/**
 * Proporciona feedback háptico al usuario.
 * Útil para confirmar acciones como:
 * - Presionar botones
 * - Completar formularios
 * - Navegar entre pantallas
 * - Errores o validaciones
 * 
 * @param context Contexto de Android
 * @param duracion Duración de la vibración en milisegundos (default: 50ms)
 */
fun hapticFeedback(context: Context, duracion: Long = 50) {
    try {
        val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            vibratorManager.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        }
        
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(
                VibrationEffect.createOneShot(
                    duracion,
                    VibrationEffect.DEFAULT_AMPLITUDE
                )
            )
        } else {
            @Suppress("DEPRECATION")
            vibrator.vibrate(duracion)
        }
    } catch (e: Exception) {
        // Si falla la vibración, no hacer nada (dispositivo sin vibrador)
        e.printStackTrace()
    }
}

/**
 * Feedback háptico para éxito (vibración corta)
 */
fun hapticSuccess(context: Context) {
    hapticFeedback(context, 50)
}

/**
 * Feedback háptico para error (dos vibraciones cortas)
 */
fun hapticError(context: Context) {
    try {
        hapticFeedback(context, 50)
        Thread.sleep(100)
        hapticFeedback(context, 50)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

/**
 * Feedback háptico para advertencia (vibración media)
 */
fun hapticWarning(context: Context) {
    hapticFeedback(context, 100)
}

/**
 * Extension function para agregar descripción de accesibilidad
 * a cualquier Modifier de Compose.
 * 
 * Uso:
 * ```
 * Button(
 *     modifier = Modifier.accessibilityDescription("Botón de inicio de sesión")
 * ) { ... }
 * ```
 * 
 * @param description Descripción para lectores de pantalla
 */
fun androidx.compose.ui.Modifier.accessibilityDescription(
    description: String
): androidx.compose.ui.Modifier {
    return this.semantics {
        contentDescription = description
    }
}

/**
 * Genera descripciones de accesibilidad descriptivas para campos de texto.
 * 
 * @param label Label del campo
 * @param value Valor actual del campo
 * @param error Mensaje de error (si existe)
 * @param isRequired Si el campo es obligatorio
 * @return Descripción completa para TalkBack
 */
fun textFieldAccessibilityDescription(
    label: String,
    value: String,
    error: String? = null,
    isRequired: Boolean = false
): String {
    val parts = mutableListOf<String>()
    
    parts.add(label)
    
    if (isRequired) {
        parts.add("campo obligatorio")
    }
    
    if (value.isNotEmpty()) {
        parts.add("contiene ${value.length} caracteres")
    } else {
        parts.add("campo vacío")
    }
    
    if (error != null) {
        parts.add("error: $error")
    }
    
    return parts.joinToString(", ")
}

/**
 * Genera descripciones de accesibilidad para botones.
 * 
 * @param label Texto del botón
 * @param isEnabled Si el botón está habilitado
 * @param additionalInfo Información adicional opcional
 * @return Descripción completa para TalkBack
 */
fun buttonAccessibilityDescription(
    label: String,
    isEnabled: Boolean = true,
    additionalInfo: String? = null
): String {
    val parts = mutableListOf<String>()
    
    parts.add("Botón: $label")
    
    if (!isEnabled) {
        parts.add("deshabilitado")
    }
    
    if (additionalInfo != null) {
        parts.add(additionalInfo)
    }
    
    return parts.joinToString(", ")
}

/**
 * Genera descripciones de accesibilidad para iconos/imágenes.
 * 
 * @param description Descripción de la imagen
 * @param isDecorative Si es puramente decorativa
 * @return Descripción para TalkBack o null si es decorativa
 */
fun imageAccessibilityDescription(
    description: String,
    isDecorative: Boolean = false
): String? {
    return if (isDecorative) {
        null // Las imágenes decorativas deben ser ignoradas por TalkBack
    } else {
        description
    }
}

/**
 * Genera descripciones de accesibilidad para elementos de lista.
 * 
 * @param title Título del elemento
 * @param subtitle Subtítulo opcional
 * @param position Posición en la lista (ej: "1 de 10")
 * @param additionalInfo Información adicional
 * @return Descripción completa para TalkBack
 */
fun listItemAccessibilityDescription(
    title: String,
    subtitle: String? = null,
    position: String? = null,
    additionalInfo: String? = null
): String {
    val parts = mutableListOf<String>()
    
    parts.add(title)
    
    if (subtitle != null) {
        parts.add(subtitle)
    }
    
    if (position != null) {
        parts.add(position)
    }
    
    if (additionalInfo != null) {
        parts.add(additionalInfo)
    }
    
    return parts.joinToString(", ")
}

/**
 * Anuncia un mensaje para lectores de pantalla.
 * Útil para notificar cambios importantes de estado.
 * 
 * Nota: Esta es una función placeholder. Para implementación completa,
 * usar AccessibilityManager.announce() o LiveRegion.
 * 
 * @param context Contexto de Android
 * @param message Mensaje a anunciar
 */
fun announceForAccessibility(context: Context, message: String) {
    // TODO: Implementar usando AccessibilityManager
    // Por ahora, solo log para desarrollo
    println("🔊 Accessibility announcement: $message")
}

/**
 * Constantes de accesibilidad para uso en toda la app
 */
object AccessibilityConstants {
    // Tiempo mínimo entre anuncios (para evitar spam)
    const val MIN_ANNOUNCEMENT_INTERVAL_MS = 1000L
    
    // Duraciones de feedback háptico
    const val HAPTIC_SHORT = 50L
    const val HAPTIC_MEDIUM = 100L
    const val HAPTIC_LONG = 200L
    
    // Mensajes comunes de accesibilidad
    const val LOADING = "Cargando contenido"
    const val SUCCESS = "Acción completada con éxito"
    const val ERROR = "Error al realizar la acción"
    const val NAVIGATION = "Navegando a nueva pantalla"
    const val FORM_ERROR = "Hay errores en el formulario"
    const val FORM_SUCCESS = "Formulario enviado correctamente"
}
