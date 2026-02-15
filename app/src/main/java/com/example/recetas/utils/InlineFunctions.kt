package com.example.recetas.utils

import android.util.Log

/**
 * Funciones inline y lambdas con etiqueta
 * Implementadas según recomendación del profesor para evidenciar conceptos avanzados de Kotlin
 */

/**
 * Función inline para ejecutar acciones con mejor rendimiento
 * Al ser inline, el código de la lambda se inserta directamente en el punto de llamada
 */
inline fun ejecutarAccion(accion: () -> Unit) {
    accion()
}

/**
 * Función inline para ejecutar acciones con manejo de errores
 */
inline fun ejecutarAccionSegura(crossinline accion: () -> Unit) {
    try {
        accion()
    } catch (e: Exception) {
        Log.e("InlineFunction", "Error ejecutando acción: ${e.message}")
    }
}

/**
 * Función inline para medir el tiempo de ejecución de una acción
 */
inline fun <T> medirTiempo(etiqueta: String, accion: () -> T): T {
    val inicio = System.currentTimeMillis()
    val resultado = accion()
    val tiempo = System.currentTimeMillis() - inicio
    Log.d("PerformanceTimer", "$etiqueta tomó ${tiempo}ms")
    return resultado
}

/**
 * Validar lista con lambda etiquetada
 * Usa return@etiqueta para saltar elementos en blanco sin salir de la función completa
 */
fun validarLista(lista: List<String>) {
    lista.forEach etiqueta@{
        if (it.isBlank()) return@etiqueta // Salta este elemento
        println(it)
    }
}

/**
 * Procesar lista de recetas con lambda etiquetada
 */
fun procesarRecetas(recetas: List<String>, accion: (String) -> Unit) {
    recetas.forEach procesamiento@{
        if (it.isEmpty()) return@procesamiento
        accion(it)
    }
}

/**
 * Filtrar y transformar con lambda etiquetada
 */
inline fun <T, R> List<T>.transformarConValidacion(
    crossinline validacion: (T) -> Boolean,
    crossinline transformacion: (T) -> R
): List<R> {
    val resultado = mutableListOf<R>()
    this.forEach transformar@{ item ->
        if (!validacion(item)) return@transformar
        resultado.add(transformacion(item))
    }
    return resultado
}

/**
 * Ejecutar múltiples acciones con control de flujo
 */
fun ejecutarMultiplesAcciones(acciones: List<() -> Unit>) {
    acciones.forEach accion@{
        try {
            it()
        } catch (e: Exception) {
            Log.e("InlineFunction", "Error en acción: ${e.message}")
            return@accion // Continúa con la siguiente acción
        }
    }
}
