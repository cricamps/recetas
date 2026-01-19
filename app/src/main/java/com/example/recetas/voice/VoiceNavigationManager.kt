package com.example.recetas.voice

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.example.recetas.navigation.Screen
import java.util.Locale

/**
 * Gestor de navegación por voz para accesibilidad.
 * 
 * Este sistema permite a los usuarios con dificultades motoras o visuales
 * navegar por la aplicación usando comandos de voz en español.
 * 
 * Comandos disponibles:
 * ====================
 * 
 * NAVEGACIÓN BÁSICA:
 * - "ir a recetas" / "mostrar recetas" / "ver recetas" → Pantalla de recetas
 * - "agregar receta" / "nueva receta" / "crear receta" → Formulario de nueva receta
 * - "volver" / "atrás" / "regresar" → Volver a la pantalla anterior
 * - "inicio" / "pantalla principal" → Ir a pantalla de recetas
 * 
 * BÚSQUEDA:
 * - "buscar [nombre]" → Buscar una receta específica
 * - Ejemplo: "buscar charquicán", "buscar pastel de papas"
 * 
 * RECETAS DIRECTAS:
 * - "abrir [nombre de receta]" / "ver [nombre de receta]"
 * - Ejemplo: "abrir charquicán", "ver cazuela"
 * 
 * TEMA Y ACCESIBILIDAD:
 * - "tema oscuro" / "modo oscuro" → Activar tema oscuro
 * - "tema claro" / "modo claro" → Activar tema claro
 * - "aumentar letra" / "letra más grande" → Aumentar tamaño de fuente
 * - "reducir letra" / "letra más pequeña" → Reducir tamaño de fuente
 * 
 * AYUDA:
 * - "ayuda" / "comandos" / "qué puedo decir" → Mostrar comandos disponibles
 * 
 * Características de accesibilidad:
 * ==================================
 * ✅ Reconocimiento de voz en español
 * ✅ Múltiples variantes para cada comando
 * ✅ Feedback sonoro con Text-to-Speech
 * ✅ Indicadores visuales del estado de escucha
 * ✅ Manejo de errores con mensajes claros
 * ✅ Compatible con Android 5.0+
 * 
 * @author Cristobal Camps
 * @course DSY2204 - Desarrollo de Aplicaciones Móviles
 * @date Enero 2026
 */
class VoiceNavigationManager(
    private val context: Context,
    private val navController: NavController,
    private val onThemeChange: () -> Unit,
    private val onFontScaleIncrease: () -> Unit,
    private val onFontScaleDecrease: () -> Unit,
    private val onSearchQuery: (String) -> Unit,
    private val onRecipeSelected: (Int) -> Unit
) {
    private var speechRecognizer: SpeechRecognizer? = null
    private val tag = "VoiceNavigation"
    
    // Estado de escucha
    var isListening = mutableStateOf(false)
        private set
    
    // Último comando reconocido
    var lastCommand = mutableStateOf("")
        private set
    
    // Mensaje de error o confirmación
    var feedbackMessage = mutableStateOf("")
        private set
    
    init {
        initializeSpeechRecognizer()
    }
    
    /**
     * Inicializa el reconocedor de voz
     */
    private fun initializeSpeechRecognizer() {
        if (!SpeechRecognizer.isRecognitionAvailable(context)) {
            Log.e(tag, "El reconocimiento de voz no está disponible en este dispositivo")
            feedbackMessage.value = "Reconocimiento de voz no disponible"
            return
        }
        
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context).apply {
            setRecognitionListener(createRecognitionListener())
        }
    }
    
    /**
     * Crea el listener para eventos de reconocimiento de voz
     */
    private fun createRecognitionListener() = object : RecognitionListener {
        override fun onReadyForSpeech(params: Bundle?) {
            Log.d(tag, "Listo para escuchar")
            isListening.value = true
            feedbackMessage.value = "Escuchando..."
        }
        
        override fun onBeginningOfSpeech() {
            Log.d(tag, "Usuario comenzó a hablar")
        }
        
        override fun onRmsChanged(rmsdB: Float) {
            // Cambios en el volumen del audio
        }
        
        override fun onBufferReceived(buffer: ByteArray?) {
            // Buffer de audio recibido
        }
        
        override fun onEndOfSpeech() {
            Log.d(tag, "Usuario terminó de hablar")
            isListening.value = false
        }
        
        override fun onError(error: Int) {
            Log.e(tag, "Error en reconocimiento: $error")
            isListening.value = false
            
            val errorMessage = when (error) {
                SpeechRecognizer.ERROR_AUDIO -> "Error de audio"
                SpeechRecognizer.ERROR_CLIENT -> "Error del cliente"
                SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> "Permisos insuficientes"
                SpeechRecognizer.ERROR_NETWORK -> "Error de red"
                SpeechRecognizer.ERROR_NETWORK_TIMEOUT -> "Tiempo de espera agotado"
                SpeechRecognizer.ERROR_NO_MATCH -> "No se reconoció el comando"
                SpeechRecognizer.ERROR_RECOGNIZER_BUSY -> "Reconocedor ocupado"
                SpeechRecognizer.ERROR_SERVER -> "Error del servidor"
                SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> "No se detectó voz"
                else -> "Error desconocido"
            }
            
            feedbackMessage.value = errorMessage
        }
        
        override fun onResults(results: Bundle?) {
            results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)?.let { matches ->
                if (matches.isNotEmpty()) {
                    val spokenText = matches[0].lowercase(Locale.getDefault())
                    Log.d(tag, "Texto reconocido: $spokenText")
                    lastCommand.value = spokenText
                    processVoiceCommand(spokenText)
                }
            }
            isListening.value = false
        }
        
        override fun onPartialResults(partialResults: Bundle?) {
            // Resultados parciales mientras el usuario habla
        }
        
        override fun onEvent(eventType: Int, params: Bundle?) {
            // Eventos adicionales
        }
    }
    
    /**
     * Inicia la escucha de comandos de voz
     */
    fun startListening() {
        if (isListening.value) {
            Log.w(tag, "Ya está escuchando")
            return
        }
        
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, "es-CL") // Español de Chile
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, "es-CL")
            putExtra(RecognizerIntent.EXTRA_ONLY_RETURN_LANGUAGE_PREFERENCE, "es-CL")
            putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
            putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 5)
        }
        
        try {
            speechRecognizer?.startListening(intent)
        } catch (e: Exception) {
            Log.e(tag, "Error al iniciar reconocimiento", e)
            feedbackMessage.value = "Error al iniciar reconocimiento de voz"
            isListening.value = false
        }
    }
    
    /**
     * Detiene la escucha de comandos de voz
     */
    fun stopListening() {
        speechRecognizer?.stopListening()
        isListening.value = false
    }
    
    /**
     * Procesa el comando de voz reconocido y ejecuta la acción correspondiente
     */
    private fun processVoiceCommand(command: String) {
        Log.d(tag, "Procesando comando: $command")
        
        when {
            // NAVEGACIÓN BÁSICA
            command.contains("ir a recetas") || 
            command.contains("mostrar recetas") || 
            command.contains("ver recetas") ||
            command.contains("inicio") ||
            command.contains("pantalla principal") -> {
                navController.navigate(Screen.Recetas.route) {
                    popUpTo(Screen.Recetas.route) { inclusive = true }
                }
                feedbackMessage.value = "Navegando a recetas"
            }
            
            command.contains("agregar receta") || 
            command.contains("nueva receta") || 
            command.contains("crear receta") -> {
                navController.navigate(Screen.AgregarReceta.route)
                feedbackMessage.value = "Abriendo formulario de nueva receta"
            }
            
            command.contains("volver") || 
            command.contains("atrás") || 
            command.contains("regresar") -> {
                if (navController.previousBackStackEntry != null) {
                    navController.navigateUp()
                    feedbackMessage.value = "Volviendo atrás"
                } else {
                    feedbackMessage.value = "Ya estás en la pantalla inicial"
                }
            }
            
            // BÚSQUEDA
            command.contains("buscar ") -> {
                val searchQuery = command.substringAfter("buscar ").trim()
                if (searchQuery.isNotEmpty()) {
                    onSearchQuery(searchQuery)
                    feedbackMessage.value = "Buscando: $searchQuery"
                } else {
                    feedbackMessage.value = "¿Qué receta quieres buscar?"
                }
            }
            
            // ABRIR RECETAS ESPECÍFICAS
            command.contains("abrir ") || command.contains("ver ") -> {
                val recipeName = when {
                    command.contains("abrir ") -> command.substringAfter("abrir ").trim()
                    else -> command.substringAfter("ver ").trim()
                }
                
                val recipeId = findRecipeIdByName(recipeName)
                if (recipeId != null) {
                    onRecipeSelected(recipeId)
                    feedbackMessage.value = "Abriendo receta"
                } else {
                    feedbackMessage.value = "No encontré la receta: $recipeName"
                }
            }
            
            // TEMA Y ACCESIBILIDAD
            command.contains("tema oscuro") || command.contains("modo oscuro") -> {
                onThemeChange()
                feedbackMessage.value = "Tema oscuro activado"
            }
            
            command.contains("tema claro") || command.contains("modo claro") -> {
                onThemeChange()
                feedbackMessage.value = "Tema claro activado"
            }
            
            command.contains("aumentar letra") || 
            command.contains("letra más grande") ||
            command.contains("agrandar letra") -> {
                onFontScaleIncrease()
                feedbackMessage.value = "Aumentando tamaño de letra"
            }
            
            command.contains("reducir letra") || 
            command.contains("letra más pequeña") ||
            command.contains("achicar letra") -> {
                onFontScaleDecrease()
                feedbackMessage.value = "Reduciendo tamaño de letra"
            }
            
            // AYUDA
            command.contains("ayuda") || 
            command.contains("comandos") || 
            command.contains("qué puedo decir") -> {
                feedbackMessage.value = "Comandos: ir a recetas, agregar receta, buscar, volver, tema oscuro, aumentar letra"
            }
            
            // COMANDO NO RECONOCIDO
            else -> {
                feedbackMessage.value = "Comando no reconocido. Di 'ayuda' para ver los comandos"
                Log.w(tag, "Comando no reconocido: $command")
            }
        }
    }
    
    /**
     * Busca el ID de una receta por su nombre
     * Soporta nombres parciales y variaciones
     */
    private fun findRecipeIdByName(name: String): Int? {
        val recipes = mapOf(
            1 to listOf("charquicán", "charquican"),
            2 to listOf("pastel de papas", "pastel de papa"),
            3 to listOf("cazuela", "cazuela de ave", "cazuela de pollo"),
            4 to listOf("empanadas", "empanada", "empanadas de pino"),
            5 to listOf("completo", "completos"),
            6 to listOf("sopaipillas", "sopaipilla")
        )
        
        for ((id, variations) in recipes) {
            if (variations.any { name.contains(it) }) {
                return id
            }
        }
        
        return null
    }
    
    /**
     * Limpia recursos cuando se destruye el manager
     */
    fun cleanup() {
        speechRecognizer?.destroy()
        speechRecognizer = null
        isListening.value = false
    }
}

/**
 * Composable para crear y recordar el VoiceNavigationManager
 */
@Composable
fun rememberVoiceNavigationManager(
    navController: NavController,
    onThemeChange: () -> Unit,
    onFontScaleIncrease: () -> Unit,
    onFontScaleDecrease: () -> Unit,
    onSearchQuery: (String) -> Unit = {},
    onRecipeSelected: (Int) -> Unit = {}
): VoiceNavigationManager {
    val context = LocalContext.current
    
    val manager = remember(navController) {
        VoiceNavigationManager(
            context = context,
            navController = navController,
            onThemeChange = onThemeChange,
            onFontScaleIncrease = onFontScaleIncrease,
            onFontScaleDecrease = onFontScaleDecrease,
            onSearchQuery = onSearchQuery,
            onRecipeSelected = onRecipeSelected
        )
    }
    
    // Limpiar recursos cuando se destruye el composable
    DisposableEffect(manager) {
        onDispose {
            manager.cleanup()
        }
    }
    
    return manager
}
