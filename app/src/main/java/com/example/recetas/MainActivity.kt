package com.example.recetas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.rememberNavController
import com.example.recetas.accessibility.FontScale
import com.example.recetas.accessibility.FontScaleManager
import com.example.recetas.accessibility.LocalFontScale
import com.example.recetas.accessibility.ContrastMode
import com.example.recetas.accessibility.ContrastManager
import com.example.recetas.accessibility.LocalContrastMode
import com.example.recetas.navigation.NavigationGraph
import com.example.recetas.ui.theme.RecetasTheme

/**
 * Actividad principal de la aplicación de Recetas Chilenas.
 * 
 * Esta aplicación implementa Navigation Component según las guías de la Semana 2:
 * - NavController: Gestiona el historial de navegación
 * - NavHost: Conecta el navController con los composables
 * - Navegación con argumentos entre pantallas
 * 
 * Arquitectura de la aplicación:
 * ==============================
 * 
 * 1. DATA (Modelo de datos)
 *    - Receta.kt: Modelo de datos para recetas
 *    - RecetasRepository.kt: Repositorio con recetas chilenas
 * 
 * 2. NAVIGATION (Sistema de navegación)
 *    - Screen.kt: Define las rutas de navegación
 *    - NavigationGraph.kt: Configura el grafo de navegación
 * 
 * 3. UI/SCREENS (Pantallas de la aplicación)
 *    - LoginScreen.kt: Pantalla de inicio de sesión
 *    - RecetasScreen.kt: Lista de recetas con búsqueda
 *    - DetalleRecetaScreen.kt: Detalle completo de una receta
 *    - AgregarRecetaScreen.kt: Formulario para nueva receta
 * 
 * 4. UI/THEME (Temas y estilos)
 *    - Theme.kt: Configuración del tema Material Design 3
 *    - Color.kt: Paleta de colores
 *    - Type.kt: Tipografía
 * 
 * Flujo de navegación:
 * ====================
 * Login → Recetas → Detalle Receta
 *              ↓
 *         Agregar Receta
 * 
 * Componentes UI de Material Design utilizados:
 * ==============================================
 * - Scaffold: Estructura base de pantallas
 * - TopAppBar: Barra superior con título y acciones
 * - Card: Tarjetas para mostrar información
 * - TextField/OutlinedTextField: Campos de entrada
 * - Button/IconButton: Botones de acción
 * - FloatingActionButton: Botón flotante para acción principal
 * - LazyColumn: Lista eficiente de elementos
 * - Icon: Iconos de Material Design
 * - Divider: Separadores visuales
 * - DropdownMenu: Menús desplegables
 * - Snackbar: Mensajes temporales
 * 
 * Características implementadas:
 * ==============================
 * ✅ Tema oscuro/claro configurable
 * ✅ Tamaño de fuente ajustable (5 niveles)
 * ✅ Contraste mejorado (4 niveles: Normal, Aumentado, Alto, Muy Alto)
 * ✅ Navegación entre múltiples pantallas
 * ✅ Paso de parámetros (ID de receta)
 * ✅ Búsqueda de recetas
 * ✅ Formularios con validación
 * ✅ Diseño responsivo y adaptable
 * ✅ Iconografía y emojis descriptivos
 * ✅ Documentación completa del código
 * ✅ Text-to-Speech completo
 * ✅ Accesibilidad visual completa
 * 
 * @author Cristobal Camps
 * @course DSY2204 - Desarrollo de Aplicaciones Móviles
 * @date Enero 2026
 * @version 2.0 - Semana 2
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        // Manejar intent desde widgets
        val widgetAction = intent.getStringExtra("widget_action")
        val fromWidget = intent.getBooleanExtra("from_widget", false)
        
        setContent {
            val context = LocalContext.current
            
            // Estado global para controlar el tema (claro/oscuro)
            val isDarkTheme = remember { mutableStateOf(false) }
            
            // Estado global para la escala de fuente
            // Cargar la preferencia guardada al iniciar
            val fontScale = remember { 
                mutableStateOf(FontScaleManager.loadScale(context)) 
            }
            
            // Estado global para el modo de contraste
            // Cargar la preferencia guardada al iniciar
            val contrastMode = remember {
                mutableStateOf(ContrastManager.loadContrastMode(context))
            }
            
            // Aplicar el tema de la aplicación con soporte de escala de fuente y contraste
            RecetasTheme(
                darkTheme = isDarkTheme.value,
                contrastMode = contrastMode.value
            ) {
                // Proveer la escala de fuente y el modo de contraste a toda la app
                CompositionLocalProvider(
                    LocalFontScale provides fontScale,
                    LocalContrastMode provides contrastMode
                ) {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        // Iniciar la aplicación con el sistema de navegación
                        RecetasApp(
                            isDarkTheme = isDarkTheme,
                            onThemeChange = { isDarkTheme.value = !isDarkTheme.value },
                            fontScale = fontScale,
                            onFontScaleChange = { newScale ->
                                fontScale.value = newScale
                                FontScaleManager.saveScale(context, newScale)
                            },
                            contrastMode = contrastMode,
                            onContrastModeChange = { newMode ->
                                contrastMode.value = newMode
                                ContrastManager.saveContrastMode(context, newMode)
                            },
                            widgetAction = widgetAction,
                            fromWidget = fromWidget
                        )
                    }
                }
            }
        }
    }
}

/**
 * Composable principal que configura la navegación de la aplicación.
 * Usa Navigation Component de Jetpack Compose para gestionar las pantallas.
 * 
 * Navigation Component proporciona:
 * - Gestión automática del back stack
 * - Transiciones entre pantallas
 * - Paso de argumentos tipado
 * - Deep linking (enlaces profundos)
 * - Integración con el botón de retroceso del sistema
 * 
 * @param isDarkTheme Estado mutable del tema oscuro/claro
 * @param onThemeChange Función para alternar el tema
 * @param fontScale Estado mutable de la escala de fuente
 * @param onFontScaleChange Función para cambiar la escala de fuente
 * @param contrastMode Estado mutable del modo de contraste
 * @param onContrastModeChange Función para cambiar el modo de contraste
 */
@Composable
fun RecetasApp(
    isDarkTheme: MutableState<Boolean>,
    onThemeChange: () -> Unit,
    fontScale: MutableState<FontScale>,
    onFontScaleChange: (FontScale) -> Unit,
    contrastMode: MutableState<ContrastMode>,
    onContrastModeChange: (ContrastMode) -> Unit,
    widgetAction: String? = null,
    fromWidget: Boolean = false
) {
    // Crear el NavController - API principal de Navigation
    // rememberNavController() asegura que se mantenga durante recomposiciones
    val navController = rememberNavController()
    
    // Configurar el grafo de navegación con todas las pantallas
    NavigationGraph(
        navController = navController,
        isDarkTheme = isDarkTheme,
        onThemeChange = onThemeChange,
        fontScale = fontScale,
        onFontScaleChange = onFontScaleChange,
        contrastMode = contrastMode,
        onContrastModeChange = onContrastModeChange
    )
}
