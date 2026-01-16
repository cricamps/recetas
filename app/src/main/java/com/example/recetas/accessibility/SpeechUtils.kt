package com.example.recetas.accessibility

import android.content.Context
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.util.Log
import java.util.*

/**
 * Gestor de Text-to-Speech (TTS) para accesibilidad del habla.
 * 
 * PROPÓSITO:
 * Permite que personas con discapacidad del habla puedan comunicarse
 * mediante texto que la aplicación convierte en voz.
 * 
 * USO:
 * ```kotlin
 * // Inicializar (una vez)
 * SpeechManager.initialize(context)
 * 
 * // Hablar texto
 * SpeechManager.speak("Hola, quiero preparar Charquicán")
 * 
 * // Detener
 * SpeechManager.stop()
 * 
 * // Limpiar recursos
 * SpeechManager.shutdown()
 * ```
 * 
 * CARACTERÍSTICAS:
 * - Español de Chile (es-CL)
 * - Velocidad normal
 * - Volumen normal
 * - Soporte para textos largos
 */
object SpeechManager {
    private var tts: TextToSpeech? = null
    private var isInitialized = false
    private const val TAG = "SpeechManager"
    
    /**
     * Inicializa el motor Text-to-Speech.
     * 
     * @param context Contexto de Android
     * @param onInitialized Callback cuando está listo
     */
    fun initialize(context: Context, onInitialized: () -> Unit = {}) {
        if (tts == null) {
            Log.d(TAG, "Inicializando Text-to-Speech...")
            
            tts = TextToSpeech(context) { status ->
                if (status == TextToSpeech.SUCCESS) {
                    // Configurar idioma español de Chile
                    val result = tts?.setLanguage(Locale("es", "CL"))
                    
                    if (result == TextToSpeech.LANG_MISSING_DATA ||
                        result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e(TAG, "Idioma español no soportado, usando español genérico")
                        tts?.setLanguage(Locale("es"))
                    }
                    
                    // Configurar velocidad y tono
                    tts?.setSpeechRate(1.0f)  // Velocidad normal
                    tts?.setPitch(1.0f)        // Tono normal
                    
                    isInitialized = true
                    Log.d(TAG, "Text-to-Speech inicializado correctamente")
                    onInitialized()
                } else {
                    Log.e(TAG, "Error al inicializar Text-to-Speech")
                }
            }
        } else if (isInitialized) {
            onInitialized()
        }
    }
    
    /**
     * Habla el texto proporcionado.
     * 
     * @param text Texto a hablar
     * @param queue Si es true, agrega a la cola; si es false, interrumpe
     */
    fun speak(text: String, queue: Boolean = false) {
        if (isInitialized && text.isNotBlank()) {
            val queueMode = if (queue) TextToSpeech.QUEUE_ADD else TextToSpeech.QUEUE_FLUSH
            tts?.speak(text, queueMode, null, "utteranceId")
            Log.d(TAG, "Hablando: $text")
        } else {
            Log.w(TAG, "TTS no inicializado o texto vacío")
        }
    }
    
    /**
     * Detiene la reproducción actual.
     */
    fun stop() {
        if (isInitialized) {
            tts?.stop()
            Log.d(TAG, "Detenido")
        }
    }
    
    /**
     * Verifica si está hablando actualmente.
     */
    fun isSpeaking(): Boolean {
        return tts?.isSpeaking ?: false
    }
    
    /**
     * Cambia la velocidad de habla.
     * 
     * @param rate Velocidad (0.5 = lento, 1.0 = normal, 2.0 = rápido)
     */
    fun setSpeechRate(rate: Float) {
        tts?.setSpeechRate(rate.coerceIn(0.1f, 3.0f))
    }
    
    /**
     * Libera los recursos del TTS.
     * Llamar cuando ya no se necesite.
     */
    fun shutdown() {
        if (tts != null) {
            tts?.stop()
            tts?.shutdown()
            tts = null
            isInitialized = false
            Log.d(TAG, "Recursos liberados")
        }
    }
}

/**
 * Función de utilidad para hablar texto de forma rápida.
 * 
 * Uso simple:
 * ```kotlin
 * hablarTexto(context, "Hola mundo")
 * ```
 * 
 * @param context Contexto de Android
 * @param texto Texto a hablar
 */
fun hablarTexto(context: Context, texto: String) {
    SpeechManager.initialize(context)
    SpeechManager.speak(texto)
}

/**
 * Función para hablar con callback de finalización.
 * 
 * @param context Contexto de Android
 * @param texto Texto a hablar
 * @param onFinish Callback cuando termina de hablar
 */
fun hablarTextoConCallback(
    context: Context, 
    texto: String,
    onFinish: () -> Unit
) {
    var ttsInstance: TextToSpeech? = null
    
    ttsInstance = TextToSpeech(context) { status ->
        if (status == TextToSpeech.SUCCESS) {
            ttsInstance?.setLanguage(Locale("es", "CL"))
            
            ttsInstance?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                override fun onStart(utteranceId: String?) {}
                
                override fun onDone(utteranceId: String?) {
                    onFinish()
                    ttsInstance?.shutdown()
                }
                
                override fun onError(utteranceId: String?) {
                    ttsInstance?.shutdown()
                }
            })
            
            ttsInstance?.speak(texto, TextToSpeech.QUEUE_FLUSH, null, "utteranceId")
        }
    }
}

/**
 * Extension function para String - hablar directamente.
 * 
 * Uso:
 * ```kotlin
 * "Hola mundo".hablar(context)
 * ```
 */
fun String.hablar(context: Context) {
    hablarTexto(context, this)
}
