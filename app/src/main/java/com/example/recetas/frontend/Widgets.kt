package com.example.recetas.frontend

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.example.recetas.MainActivity
import com.example.recetas.R

/**
 * COMPONENTE 4: WIDGETS (App Widgets para Home Screen)
 * 
 * Los App Widgets son vistas en miniatura de la aplicación que se pueden
 * incrustar en otras aplicaciones (como el launcher).
 */

/**
 * Widget de Receta del Día
 * Muestra la receta recomendada del día en la pantalla principal
 */
class RecetaDelDiaWidget : AppWidgetProvider() {
    
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        appWidgetIds.forEach { widgetId ->
            actualizarWidget(context, appWidgetManager, widgetId)
        }
    }
    
    private fun actualizarWidget(
        context: Context,
        appWidgetManager: AppWidgetManager,
        widgetId: Int
    ) {
        val views = RemoteViews(context.packageName, R.layout.widget_receta_del_dia)
        
        // Obtener receta del día
        val recetaDelDia = obtenerRecetaDelDia()
        
        // Configurar textos
        views.setTextViewText(R.id.widget_titulo, "Receta del Día")
        views.setTextViewText(R.id.widget_nombre_receta, recetaDelDia)
        views.setTextViewText(R.id.widget_categoria, "Plato Principal")
        
        // Configurar click para abrir la app directamente en la receta
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            putExtra("widget_action", "open_recipe")
            putExtra("recipe_name", recetaDelDia)
            putExtra("from_widget", true)
        }
        
        val pendingIntent = PendingIntent.getActivity(
            context, 
            widgetId, // Usar widgetId como requestCode para que cada widget sea único
            intent, 
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        views.setOnClickPendingIntent(R.id.widget_container, pendingIntent)
        
        // Actualizar el widget
        appWidgetManager.updateAppWidget(widgetId, views)
    }
    
    private fun obtenerRecetaDelDia(): String {
        val recetas = listOf(
            "Pastel de Choclo",
            "Empanadas de Pino",
            "Cazuela de Vacuno",
            "Curanto",
            "Completo",
            "Charquicán",
            "Porotos Granados"
        )
        return recetas.random()
    }
}

/**
 * Widget de Minuta Semanal
 * Muestra resumen de la minuta de la semana
 */
class MinutaSemanalWidget : AppWidgetProvider() {
    
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        appWidgetIds.forEach { widgetId ->
            actualizarMinuta(context, appWidgetManager, widgetId)
        }
    }
    
    private fun actualizarMinuta(
        context: Context,
        appWidgetManager: AppWidgetManager,
        widgetId: Int
    ) {
        val views = RemoteViews(context.packageName, R.layout.widget_minuta_semanal)
        
        // Configurar título
        views.setTextViewText(R.id.widget_minuta_titulo, "Minuta Semanal")
        
        // Obtener día actual
        val diaActual = obtenerDiaActual()
        views.setTextViewText(R.id.widget_dia_actual, "Hoy: $diaActual")
        
        // Obtener receta de hoy
        val recetaHoy = obtenerRecetaDeHoy()
        views.setTextViewText(R.id.widget_receta_hoy, recetaHoy)
        
        // Configurar acción de click para abrir directamente la minuta
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            putExtra("widget_action", "open_minuta")
            putExtra("from_widget", true)
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            widgetId,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        views.setOnClickPendingIntent(R.id.widget_minuta_container, pendingIntent)
        
        appWidgetManager.updateAppWidget(widgetId, views)
    }
    
    private fun obtenerDiaActual(): String {
        val dias = listOf("Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo")
        val calendar = java.util.Calendar.getInstance()
        val diaIndex = calendar.get(java.util.Calendar.DAY_OF_WEEK) - 2
        return dias.getOrNull(if (diaIndex < 0) 6 else diaIndex) ?: "Lunes"
    }
    
    private fun obtenerRecetaDeHoy(): String {
        val recetas = mapOf(
            0 to "Cazuela de Vacuno",
            1 to "Pastel de Choclo",
            2 to "Porotos Granados",
            3 to "Empanadas de Pino",
            4 to "Charquicán",
            5 to "Asado Chileno",
            6 to "Curanto"
        )
        val diaIndex = java.util.Calendar.getInstance().get(java.util.Calendar.DAY_OF_WEEK) - 2
        return recetas[if (diaIndex < 0) 6 else diaIndex] ?: "Cazuela de Vacuno"
    }
}

/**
 * Widget de Estadísticas
 * Muestra estadísticas de uso de la app
 */
class EstadisticasWidget : AppWidgetProvider() {
    
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        appWidgetIds.forEach { widgetId ->
            actualizarEstadisticas(context, appWidgetManager, widgetId)
        }
    }
    
    private fun actualizarEstadisticas(
        context: Context,
        appWidgetManager: AppWidgetManager,
        widgetId: Int
    ) {
        val views = RemoteViews(context.packageName, R.layout.widget_estadisticas)
        
        views.setTextViewText(R.id.widget_stats_titulo, "Mis Recetas")
        views.setTextViewText(R.id.widget_recetas_guardadas, "12 guardadas")
        views.setTextViewText(R.id.widget_recetas_cocinadas, "8 cocinadas")
        
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            putExtra("widget_action", "open_stats")
            putExtra("from_widget", true)
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            widgetId,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        views.setOnClickPendingIntent(R.id.widget_stats_container, pendingIntent)
        
        appWidgetManager.updateAppWidget(widgetId, views)
    }
}

/**
 * Utilidades para Widgets
 */
object WidgetUtils {
    
    /**
     * Actualizar todos los widgets de un tipo específico
     */
    fun actualizarWidgets(context: Context, widgetClass: Class<out AppWidgetProvider>) {
        val intent = Intent(context, widgetClass)
        intent.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
        
        val widgetManager = AppWidgetManager.getInstance(context)
        val ids = widgetManager.getAppWidgetIds(
            android.content.ComponentName(context, widgetClass)
        )
        
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids)
        context.sendBroadcast(intent)
    }
    
    /**
     * Verificar si hay widgets activos
     */
    fun tieneWidgetsActivos(context: Context, widgetClass: Class<out AppWidgetProvider>): Boolean {
        val widgetManager = AppWidgetManager.getInstance(context)
        val ids = widgetManager.getAppWidgetIds(
            android.content.ComponentName(context, widgetClass)
        )
        return ids.isNotEmpty()
    }
}

/**
 * Proveedor de información para widgets
 * Centraliza la lógica de datos para los widgets
 */
object WidgetDataProvider {
    
    fun obtenerRecetaDelDia(): WidgetReceta {
        return WidgetReceta(
            nombre = "Pastel de Choclo",
            categoria = "Plato Principal",
            tiempo = "45 min"
        )
    }
    
    fun obtenerMinutaHoy(): WidgetMinuta {
        return WidgetMinuta(
            dia = "Lunes",
            almuerzo = "Cazuela de Vacuno",
            cena = "Ensalada Chilena"
        )
    }
    
    fun obtenerEstadisticas(): WidgetEstadisticas {
        return WidgetEstadisticas(
            recetasGuardadas = 12,
            recetasCocinadas = 8,
            favoritos = 5
        )
    }
}

data class WidgetReceta(
    val nombre: String,
    val categoria: String,
    val tiempo: String
)

data class WidgetMinuta(
    val dia: String,
    val almuerzo: String,
    val cena: String
)

data class WidgetEstadisticas(
    val recetasGuardadas: Int,
    val recetasCocinadas: Int,
    val favoritos: Int
)
