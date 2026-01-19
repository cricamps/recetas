package com.example.recetas.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color
import com.example.recetas.accessibility.ContrastMode

/**
 * Esquemas de color con diferentes niveles de contraste para mejorar la accesibilidad.
 * 
 * Implementa las pautas WCAG 2.1 para contraste de color:
 * - Nivel AA: Ratio mínimo 4.5:1 para texto normal
 * - Nivel AAA: Ratio mínimo 7:1 para texto normal
 * 
 * Cada modo de contraste ajusta los colores para garantizar legibilidad óptima
 * para personas con diferentes capacidades visuales.
 */
object ContrastColorSchemes {
    
    // ============================================
    // TEMA CLARO - CONTRASTE NORMAL (WCAG AA)
    // ============================================
    
    private val LightNormal = lightColorScheme(
        primary = Orange40,
        secondary = OrangeGrey40,
        tertiary = Green40,
        background = CreamBackground,
        surface = Color.White,
        onPrimary = Color.White,
        onSecondary = Color.White,
        onTertiary = Color.White,
        onBackground = BrownText,
        onSurface = BrownText,
        surfaceVariant = Color(0xFFFFF3E0),
        onSurfaceVariant = BrownText,
        primaryContainer = Color(0xFFFFE0B2),
        onPrimaryContainer = Color(0xFFE65100)
    )
    
    // ============================================
    // TEMA CLARO - CONTRASTE AUMENTADO
    // ============================================
    
    private val LightAumentado = lightColorScheme(
        primary = Color(0xFFE65100), // Naranja más oscuro
        secondary = Color(0xFFD84315), // Naranja rojizo más oscuro
        tertiary = Color(0xFF2E7D32), // Verde más oscuro
        background = Color(0xFFFFFBF5), // Fondo más claro
        surface = Color.White,
        onPrimary = Color.White,
        onSecondary = Color.White,
        onTertiary = Color.White,
        onBackground = Color(0xFF3E2723), // Texto mucho más oscuro
        onSurface = Color(0xFF3E2723),
        surfaceVariant = Color(0xFFFFECB3),
        onSurfaceVariant = Color(0xFF3E2723),
        primaryContainer = Color(0xFFFFCC80),
        onPrimaryContainer = Color(0xFFBF360C)
    )
    
    // ============================================
    // TEMA CLARO - CONTRASTE ALTO (WCAG AAA)
    // ============================================
    
    private val LightAlto = lightColorScheme(
        primary = Color(0xFFBF360C), // Naranja muy oscuro
        secondary = Color(0xFFB71C1C), // Rojo muy oscuro
        tertiary = Color(0xFF1B5E20), // Verde muy oscuro
        background = Color.White, // Fondo blanco puro
        surface = Color.White,
        onPrimary = Color.White,
        onSecondary = Color.White,
        onTertiary = Color.White,
        onBackground = Color(0xFF1A1A1A), // Texto casi negro
        onSurface = Color(0xFF1A1A1A),
        surfaceVariant = Color(0xFFFFF9F0),
        onSurfaceVariant = Color(0xFF1A1A1A),
        primaryContainer = Color(0xFFFFB74D),
        onPrimaryContainer = Color(0xFF000000)
    )
    
    // ============================================
    // TEMA CLARO - CONTRASTE MUY ALTO
    // ============================================
    
    private val LightMuyAlto = lightColorScheme(
        primary = Color(0xFF8B0000), // Rojo oscuro intenso
        secondary = Color(0xFF4A0000), // Marrón rojizo oscuro
        tertiary = Color(0xFF004D00), // Verde muy oscuro
        background = Color.White, // Blanco puro
        surface = Color.White,
        onPrimary = Color.White,
        onSecondary = Color.White,
        onTertiary = Color.White,
        onBackground = Color.Black, // Negro puro
        onSurface = Color.Black,
        surfaceVariant = Color(0xFFFFFEFC),
        onSurfaceVariant = Color.Black,
        primaryContainer = Color(0xFFFFA726),
        onPrimaryContainer = Color.Black
    )
    
    // ============================================
    // TEMA OSCURO - CONTRASTE NORMAL (WCAG AA)
    // ============================================
    
    private val DarkNormal = darkColorScheme(
        primary = Orange80,
        secondary = OrangeGrey80,
        tertiary = Green80,
        background = Color(0xFF1C1B1F),
        surface = Color(0xFF2B2B2B),
        onPrimary = Color.White,
        onSecondary = Color.White,
        onTertiary = Color.White,
        onBackground = Color(0xFFFFFBFE),
        onSurface = Color(0xFFFFFBFE)
    )
    
    // ============================================
    // TEMA OSCURO - CONTRASTE AUMENTADO
    // ============================================
    
    private val DarkAumentado = darkColorScheme(
        primary = Color(0xFFFFCC80), // Naranja más claro
        secondary = Color(0xFFFFE0B2), // Naranja crema más claro
        tertiary = Color(0xFFC8E6C9), // Verde más claro
        background = Color(0xFF121212), // Fondo más oscuro
        surface = Color(0xFF1E1E1E),
        onPrimary = Color.Black,
        onSecondary = Color.Black,
        onTertiary = Color.Black,
        onBackground = Color(0xFFFFFFFF), // Texto blanco puro
        onSurface = Color(0xFFFFFFFF)
    )
    
    // ============================================
    // TEMA OSCURO - CONTRASTE ALTO (WCAG AAA)
    // ============================================
    
    private val DarkAlto = darkColorScheme(
        primary = Color(0xFFFFE082), // Amarillo naranja muy claro
        secondary = Color(0xFFFFECB3), // Crema muy claro
        tertiary = Color(0xFFDCEDC8), // Verde muy claro
        background = Color(0xFF000000), // Negro puro
        surface = Color(0xFF0A0A0A),
        onPrimary = Color.Black,
        onSecondary = Color.Black,
        onTertiary = Color.Black,
        onBackground = Color.White, // Blanco puro
        onSurface = Color.White
    )
    
    // ============================================
    // TEMA OSCURO - CONTRASTE MUY ALTO
    // ============================================
    
    private val DarkMuyAlto = darkColorScheme(
        primary = Color(0xFFFFFFAA), // Amarillo muy claro
        secondary = Color(0xFFFFFFCC), // Amarillo crema muy claro
        tertiary = Color(0xFFE8F5E9), // Verde muy muy claro
        background = Color.Black, // Negro absoluto
        surface = Color.Black,
        onPrimary = Color.Black,
        onSecondary = Color.Black,
        onTertiary = Color.Black,
        onBackground = Color.White, // Blanco absoluto
        onSurface = Color.White
    )
    
    /**
     * Obtiene el esquema de colores apropiado según el tema y nivel de contraste
     * 
     * @param isDarkTheme Si es true, usa esquema oscuro; si es false, esquema claro
     * @param contrastMode Nivel de contraste deseado
     * @return ColorScheme con los colores correspondientes
     */
    fun getColorScheme(isDarkTheme: Boolean, contrastMode: ContrastMode): ColorScheme {
        return when {
            isDarkTheme -> when (contrastMode) {
                ContrastMode.NORMAL -> DarkNormal
                ContrastMode.AUMENTADO -> DarkAumentado
                ContrastMode.ALTO -> DarkAlto
                ContrastMode.MUY_ALTO -> DarkMuyAlto
            }
            else -> when (contrastMode) {
                ContrastMode.NORMAL -> LightNormal
                ContrastMode.AUMENTADO -> LightAumentado
                ContrastMode.ALTO -> LightAlto
                ContrastMode.MUY_ALTO -> LightMuyAlto
            }
        }
    }
}
