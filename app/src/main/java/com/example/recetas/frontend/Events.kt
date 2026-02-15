package com.example.recetas.frontend

import android.util.Log
import androidx.compose.foundation.gestures.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.input.pointer.*
import com.example.recetas.utils.ejecutarAccion
import com.example.recetas.utils.ejecutarAccionSegura
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

/**
 * COMPONENTE 7: EVENTS
 * Sistema completo de manejo de eventos
 */

object EventBus {
    private val _events = MutableSharedFlow<AppEvent>()
    val events: SharedFlow<AppEvent> = _events
    
    suspend fun emit(event: AppEvent) {
        _events.emit(event)
        Log.d("EventBus", "Evento emitido: ${event.javaClass.simpleName}")
    }
}

sealed class AppEvent {
    data class UsuarioLogin(val email: String) : AppEvent()
    object UsuarioLogout : AppEvent()
    data class RecetaSeleccionada(val recetaId: String) : AppEvent()
    data class RecetaGuardada(val recetaId: String) : AppEvent()
    data class MinutaGenerada(val semana: String) : AppEvent()
    data class MostrarMensaje(val mensaje: String, val tipo: TipoMensaje) : AppEvent()
    data class NavegacionSolicitada(val destino: String) : AppEvent()
    data class CambioFontSize(val escala: Float) : AppEvent()
    object ActualizarUI : AppEvent()
}

enum class TipoMensaje {
    INFO, SUCCESS, WARNING, ERROR
}

fun Modifier.onClickEvent(
    eventName: String = "click",
    enabled: Boolean = true,
    onClick: () -> Unit
) = composed {
    this.clickable(
        enabled = enabled,
        interactionSource = remember { MutableInteractionSource() },
        indication = null
    ) {
        ejecutarAccion {
            Log.d("ClickEvent", "Click detectado: $eventName")
            onClick()
        }
    }
}

fun Modifier.onLongClickEvent(
    eventName: String = "long_click",
    onLongClick: () -> Unit,
    onClick: () -> Unit = {}
) = composed {
    this.pointerInput(Unit) {
        detectTapGestures(
            onTap = {
                ejecutarAccionSegura {
                    Log.d("ClickEvent", "Tap: $eventName")
                    onClick()
                }
            },
            onLongPress = {
                ejecutarAccionSegura {
                    Log.d("LongClickEvent", "Long press: $eventName")
                    onLongClick()
                }
            }
        )
    }
}

fun Modifier.onDoubleClickEvent(
    eventName: String = "double_click",
    onDoubleClick: () -> Unit,
    onClick: () -> Unit = {}
) = composed {
    this.pointerInput(Unit) {
        detectTapGestures(
            onDoubleTap = {
                ejecutarAccion {
                    Log.d("DoubleClickEvent", "Double tap: $eventName")
                    onDoubleClick()
                }
            },
            onTap = {
                ejecutarAccion {
                    onClick()
                }
            }
        )
    }
}

@Composable
fun EventListener(
    onEvent: (AppEvent) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    
    LaunchedEffect(Unit) {
        EventBus.events.collect { event ->
            try {
                Log.d("EventListener", "Procesando: ${event.javaClass.simpleName}")
                onEvent(event)
            } catch (e: Exception) {
                Log.e("EventListener", "Error procesando evento", e)
            }
        }
    }
}

@Composable
fun rememberEventEmitter(): (AppEvent) -> Unit {
    val coroutineScope = rememberCoroutineScope()
    
    return remember {
        { event: AppEvent ->
            coroutineScope.launch {
                try {
                    EventBus.emit(event)
                } catch (e: Exception) {
                    Log.e("EventEmitter", "Error emitiendo evento", e)
                }
            }
        }
    }
}

object EventAnalytics {
    private val eventos = mutableListOf<EventoRegistrado>()
    
    fun registrar(nombre: String, parametros: Map<String, Any> = emptyMap()) {
        val evento = EventoRegistrado(
            nombre = nombre,
            timestamp = System.currentTimeMillis(),
            parametros = parametros
        )
        eventos.add(evento)
        Log.d("Analytics", "Evento: $nombre - $parametros")
    }
    
    fun obtenerEventos(): List<EventoRegistrado> = eventos.toList()
    
    fun limpiar() {
        eventos.clear()
    }
}

data class EventoRegistrado(
    val nombre: String,
    val timestamp: Long,
    val parametros: Map<String, Any>
)
