package com.example.recetas.data

/**
 * Clase que representa el menú completo de un día en la minuta semanal.
 * Incluye el plato principal y las sugerencias de acompañamiento, ensalada y postre.
 */
data class DiaMenuSemanal(
    val platoPrincipal: Receta,
    val ensalada: Receta? = null,
    val acompanamiento: Receta? = null,  // Para platos que requieren acompañamiento
    val postre: Receta? = null
) {
    /**
     * Obtiene el nombre completo del menú del día.
     */
    fun obtenerNombreCompleto(): String {
        val partes = mutableListOf(platoPrincipal.nombre)
        if (acompanamiento != null) {
            partes.add("con ${acompanamiento.nombre}")
        }
        return partes.joinToString(" ")
    }
    
    /**
     * Obtiene todas las recetas del día como lista.
     */
    fun obtenerTodasLasRecetas(): List<Receta> {
        val recetas = mutableListOf(platoPrincipal)
        acompanamiento?.let { recetas.add(it) }
        ensalada?.let { recetas.add(it) }
        postre?.let { recetas.add(it) }
        return recetas
    }
    
    /**
     * Calcula las calorías totales del día.
     */
    fun obtenerCaloriasTotales(): Int {
        var total = platoPrincipal.obtenerCaloriasTotales()
        acompanamiento?.let { total += it.obtenerCaloriasTotales() }
        ensalada?.let { total += it.obtenerCaloriasTotales() }
        postre?.let { total += it.obtenerCaloriasTotales() }
        return total
    }
}
