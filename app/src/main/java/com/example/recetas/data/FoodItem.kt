package com.example.recetas.data

/**
 * Clase base abstracta para representar cualquier ítem de comida.
 * Demuestra conceptos de POO: abstracción, herencia y polimorfismo.
 * 
 * Esta clase debe ser marcada como 'open' o 'abstract' para permitir herencia.
 * En Kotlin, las clases son 'final' por defecto.
 */
abstract class FoodItem(
    open val id: String,
    open val nombre: String,
    open val origen: String,
    open val descripcion: String
) {
    // Propiedad mutable protegida - solo accesible por subclases
    protected var _esFavorita: Boolean = false
    
    // Propiedad pública de solo lectura
    val esFavorita: Boolean
        get() = _esFavorita
    
    /**
     * Marca o desmarca el ítem como favorito.
     * @param favorita true para marcar como favorito, false para desmarcar
     */
    fun establecerFavorita(favorita: Boolean) {
        _esFavorita = favorita
    }
    
    /**
     * Método abstracto que debe ser implementado por las subclases.
     * Retorna una descripción detallada del ítem.
     */
    abstract fun obtenerDescripcionDetallada(): String
    
    /**
     * Método abstracto para obtener el tipo de comida.
     */
    abstract fun obtenerTipoComida(): String
    
    /**
     * Método que puede ser sobreescrito por las subclases.
     * Retorna información básica del ítem.
     */
    open fun obtenerInfoBasica(): String {
        return "Nombre: $nombre\nOrigen: $origen\n$descripcion"
    }
    
    /**
     * Método final que no puede ser sobreescrito.
     * Retorna si el ítem es de origen chileno.
     */
    fun esChilena(): Boolean {
        return origen.equals("Chile", ignoreCase = true)
    }
    
    companion object {
        // Constante compartida por todas las instancias
        const val ORIGEN_PREDETERMINADO = "Chile"
    }
}

/**
 * Interfaz para ítems que pueden tener información nutricional.
 * Demuestra el uso de interfaces en POO.
 */
interface Nutritional {
    /**
     * Obtiene la información nutricional del ítem.
     */
    fun obtenerInfoNutricional(): NutritionalInfo?
    
    /**
     * Indica si el ítem tiene información nutricional disponible.
     */
    fun tieneInfoNutricional(): Boolean = obtenerInfoNutricional() != null
    
    /**
     * Obtiene las calorías totales.
     */
    fun obtenerCaloriasTotales(): Int = obtenerInfoNutricional()?.calorias ?: 0
}

/**
 * Interfaz para ítems que pueden ser preparados.
 * Demuestra herencia múltiple de interfaces.
 */
interface Preparable {
    /**
     * Obtiene el tiempo estimado de preparación.
     */
    fun obtenerTiempoPreparacion(): String
    
    /**
     * Obtiene el nivel de dificultad.
     */
    fun obtenerNivelDificultad(): String
    
    /**
     * Obtiene los pasos de preparación.
     */
    fun obtenerPasosPreparacion(): List<String>
    
    /**
     * Indica si la receta es fácil de preparar.
     */
    fun esFacilDePreparar(): Boolean = obtenerNivelDificultad().equals("Fácil", ignoreCase = true)
}
