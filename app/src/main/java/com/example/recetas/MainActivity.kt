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
import androidx.navigation.compose.rememberNavController
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
 * ✅ Navegación entre múltiples pantallas
 * ✅ Paso de parámetros (ID de receta)
 * ✅ Búsqueda de recetas
 * ✅ Formularios con validación
 * ✅ Diseño responsivo y adaptable
 * ✅ Iconografía y emojis descriptivos
 * ✅ Documentación completa del código
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
        setContent {
            // Estado global para controlar el tema (claro/oscuro)
            // Se mantiene durante toda la sesión de la app
            val isDarkTheme = remember { mutableStateOf(false) }
            
            // Aplicar el tema de la aplicación
            RecetasTheme(darkTheme = isDarkTheme.value) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Iniciar la aplicación con el sistema de navegación
                    RecetasApp(
                        isDarkTheme = isDarkTheme,
                        onThemeChange = { isDarkTheme.value = !isDarkTheme.value }
                    )
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
 */
@Composable
fun RecetasApp(
    isDarkTheme: MutableState<Boolean>,
    onThemeChange: () -> Unit
) {
    // Crear el NavController - API principal de Navigation
    // rememberNavController() asegura que se mantenga durante recomposiciones
    val navController = rememberNavController()
    
    // Configurar el grafo de navegación con todas las pantallas
    NavigationGraph(
        navController = navController,
        isDarkTheme = isDarkTheme,
        onThemeChange = onThemeChange
    )
}
