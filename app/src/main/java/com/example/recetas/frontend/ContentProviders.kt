package com.example.recetas.frontend

import androidx.compose.runtime.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * COMPONENTE 3: CONTENT PROVIDERS
 * 
 * Sistema moderno de provisión de datos usando CompositionLocal y StateFlow
 */

class RecetasContentProvider {
    private val _recetas = MutableStateFlow<List<RecetaData>>(emptyList())
    val recetas: StateFlow<List<RecetaData>> = _recetas
    
    fun agregarReceta(receta: RecetaData) {
        _recetas.value = _recetas.value + receta
    }
    
    fun actualizarReceta(id: String, receta: RecetaData) {
        _recetas.value = _recetas.value.map { 
            if (it.id == id) receta else it 
        }
    }
    
    fun eliminarReceta(id: String) {
        _recetas.value = _recetas.value.filter { it.id != id }
    }
    
    fun buscarReceta(id: String): RecetaData? {
        return _recetas.value.find { it.id == id }
    }
    
    fun buscarPorCategoria(categoria: String): List<RecetaData> {
        return _recetas.value.filter { it.categoria == categoria }
    }
}

class UsuariosContentProvider {
    private val _usuarioActual = MutableStateFlow<UsuarioData?>(null)
    val usuarioActual: StateFlow<UsuarioData?> = _usuarioActual
    
    fun iniciarSesion(usuario: UsuarioData) {
        _usuarioActual.value = usuario
    }
    
    fun cerrarSesion() {
        _usuarioActual.value = null
    }
    
    fun actualizarPerfil(usuario: UsuarioData) {
        _usuarioActual.value = usuario
    }
    
    fun estaAutenticado(): Boolean {
        return _usuarioActual.value != null
    }
}

class MinutasContentProvider {
    private val _minutaSemanal = MutableStateFlow<MinutaData?>(null)
    val minutaSemanal: StateFlow<MinutaData?> = _minutaSemanal
    
    fun establecerMinuta(minuta: MinutaData) {
        _minutaSemanal.value = minuta
    }
    
    fun actualizarDia(dia: String, recetas: List<RecetaData>) {
        _minutaSemanal.value?.let { minuta ->
            val diasActualizados = minuta.dias.toMutableMap()
            diasActualizados[dia] = recetas
            _minutaSemanal.value = minuta.copy(dias = diasActualizados)
        }
    }
}

class ConfiguracionContentProvider {
    private val _configuracion = MutableStateFlow<Map<String, Any>>(emptyMap())
    val configuracion: StateFlow<Map<String, Any>> = _configuracion
    
    fun obtener(clave: String): Any? {
        return _configuracion.value[clave]
    }
    
    fun establecer(clave: String, valor: Any) {
        _configuracion.value = _configuracion.value + (clave to valor)
    }
    
    fun eliminar(clave: String) {
        _configuracion.value = _configuracion.value - clave
    }
}

val LocalRecetasProvider = compositionLocalOf<RecetasContentProvider> { 
    error("RecetasContentProvider no proporcionado") 
}

val LocalUsuariosProvider = compositionLocalOf<UsuariosContentProvider> { 
    error("UsuariosContentProvider no proporcionado") 
}

val LocalMinutasProvider = compositionLocalOf<MinutasContentProvider> { 
    error("MinutasContentProvider no proporcionado") 
}

val LocalConfiguracionProvider = compositionLocalOf<ConfiguracionContentProvider> { 
    error("ConfiguracionContentProvider no proporcionado") 
}

@Composable
fun ContentProvidersWrapper(
    recetasProvider: RecetasContentProvider = remember { RecetasContentProvider() },
    usuariosProvider: UsuariosContentProvider = remember { UsuariosContentProvider() },
    minutasProvider: MinutasContentProvider = remember { MinutasContentProvider() },
    configuracionProvider: ConfiguracionContentProvider = remember { ConfiguracionContentProvider() },
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(
        LocalRecetasProvider provides recetasProvider,
        LocalUsuariosProvider provides usuariosProvider,
        LocalMinutasProvider provides minutasProvider,
        LocalConfiguracionProvider provides configuracionProvider
    ) {
        content()
    }
}

@Composable
fun useRecetasProvider(): RecetasContentProvider {
    return LocalRecetasProvider.current
}

@Composable
fun useUsuariosProvider(): UsuariosContentProvider {
    return LocalUsuariosProvider.current
}

@Composable
fun useMinutasProvider(): MinutasContentProvider {
    return LocalMinutasProvider.current
}

@Composable
fun useConfiguracionProvider(): ConfiguracionContentProvider {
    return LocalConfiguracionProvider.current
}

data class RecetaData(
    val id: String,
    val nombre: String,
    val categoria: String,
    val ingredientes: List<String>,
    val pasos: List<String>,
    val tiempoPreparacion: Int,
    val dificultad: String
)

data class UsuarioData(
    val id: String,
    val nombre: String,
    val email: String,
    val preferencias: Map<String, Any> = emptyMap()
)

data class MinutaData(
    val id: String,
    val semana: String,
    val dias: Map<String, List<RecetaData>>
)
