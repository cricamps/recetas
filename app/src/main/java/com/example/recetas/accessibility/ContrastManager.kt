package com.example.recetas.accessibility

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.MutableState

/**
 * Gestor de preferencias de contraste.
 * 
 * Guarda y carga las preferencias del usuario utilizando SharedPreferences
 * para que se mantengan entre sesiones de la aplicación.
 * 
 * Funcionalidades:
 * - Guardar nivel de contraste seleccionado
 * - Cargar nivel de contraste guardado
 * - Valor por defecto: NORMAL
 */
object ContrastManager {
    private const val PREFS_NAME = "recetas_accessibility_prefs"
    private const val KEY_CONTRAST_MODE = "contrast_mode"
    
    /**
     * Guarda el nivel de contraste en SharedPreferences
     * @param context Contexto de la aplicación
     * @param mode Modo de contraste a guardar
     */
    fun saveContrastMode(context: Context, mode: ContrastMode) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_CONTRAST_MODE, mode.name).apply()
    }
    
    /**
     * Carga el nivel de contraste desde SharedPreferences
     * @param context Contexto de la aplicación
     * @return Modo de contraste guardado o NORMAL si no existe
     */
    fun loadContrastMode(context: Context): ContrastMode {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val modeName = prefs.getString(KEY_CONTRAST_MODE, ContrastMode.NORMAL.name)
        return ContrastMode.fromString(modeName ?: ContrastMode.NORMAL.name)
    }
}

/**
 * CompositionLocal para proveer el estado de contraste a toda la app
 */
val LocalContrastMode = compositionLocalOf<MutableState<ContrastMode>> { 
    error("No ContrastMode provided") 
}
