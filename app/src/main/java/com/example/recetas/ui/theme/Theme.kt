package com.example.recetas.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

/**
 * Esquema de colores para modo oscuro
 * Utiliza tonos más intensos de naranja y verde
 */
private val DarkColorScheme = darkColorScheme(
    primary = Orange80,
    secondary = OrangeGrey80,
    tertiary = Green80,
    background = Color(0xFF1C1B1F),
    surface = Color(0xFF2B2B2B),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFFFFFBFE),
    onSurface = Color(0xFFFFFBFE),
)

/**
 * Esquema de colores para modo claro
 * Utiliza tonos cálidos apropiados para una aplicación de recetas
 */
private val LightColorScheme = lightColorScheme(
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

/**
 * Tema principal de la aplicación Recetas
 * 
 * @param darkTheme Si es true, usa el esquema de colores oscuro
 * @param dynamicColor Si es true, usa colores dinámicos en Android 12+
 * @param content Contenido composable que usará este tema
 */
@Composable
fun RecetasTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false, // Deshabilitado para usar nuestros colores personalizados
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
