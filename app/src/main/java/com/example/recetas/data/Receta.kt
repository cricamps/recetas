package com.example.recetas.data

/**
 * Clase que representa una receta de cocina chilena.
 * Demuestra herencia (extiende FoodItem) e implementación de interfaces múltiples.
 * 
 * Esta clase implementa Programación Orientada a Objetos:
 * - Herencia: extiende de FoodItem
 * - Interfaces: implementa Nutritional y Preparable
 * - Encapsulación: propiedades privadas y públicas
 * - Polimorfismo: sobrescribe métodos de la clase base
 * 
 * @property id Identificador único de la receta
 * @property nombre Nombre de la receta (ej: "Pastel de Papas")
 * @property origen País o región de origen (ej: "Chile")
 * @property tiempoPreparacion Tiempo estimado de preparación (ej: "90 min")
 * @property dificultad Nivel de dificultad: "Fácil", "Media", "Difícil"
 * @property descripcion Descripción breve de la receta
 * @property ingredientes Lista de ingredientes necesarios
 * @property preparacion Pasos de preparación de la receta
 * @property infoNutricional Información nutricional de la receta (opcional)
 * @property categoria Categoría de la receta (entrada, plato principal, postre, etc.)
 * @property porciones Número de porciones que rinde la receta
 */
class Receta(
    override val id: String,
    override val nombre: String,
    override val origen: String = "Chile",
    override val descripcion: String,
    private val tiempoPreparacion: String,
    private val dificultad: String,
    val ingredientes: List<String> = emptyList(),
    private val preparacion: List<String> = emptyList(),
    private val infoNutricional: NutritionalInfo? = null,
    val categoria: String = "Plato Principal",
    val porciones: Int = 4
) : FoodItem(
    id = id,
    nombre = nombre,
    origen = origen,
    descripcion = descripcion
), Nutritional, Preparable {
    
    // Constructor secundario simplificado (sin información nutricional)
    constructor(
        id: String,
        nombre: String,
        descripcion: String,
        tiempoPreparacion: String,
        dificultad: String,
        ingredientes: List<String>,
        preparacion: List<String>
    ) : this(
        id = id,
        nombre = nombre,
        origen = "Chile",
        descripcion = descripcion,
        tiempoPreparacion = tiempoPreparacion,
        dificultad = dificultad,
        ingredientes = ingredientes,
        preparacion = preparacion,
        infoNutricional = null
    )
    
    // Implementación de métodos abstractos de FoodItem
    override fun obtenerDescripcionDetallada(): String {
        return buildString {
            appendLine("=== $nombre ===")
            appendLine("Origen: $origen")
            appendLine("Categoría: $categoria")
            appendLine("Porciones: $porciones")
            appendLine()
            appendLine("Descripción:")
            appendLine(descripcion)
            appendLine()
            appendLine("Tiempo de preparación: $tiempoPreparacion")
            appendLine("Dificultad: $dificultad")
            
            if (infoNutricional != null) {
                appendLine()
                appendLine("Información Nutricional:")
                appendLine(infoNutricional.obtenerResumenNutricional())
            }
        }
    }
    
    override fun obtenerTipoComida(): String = "Receta Tradicional Chilena"
    
    // Sobrescritura del método de la clase base
    override fun obtenerInfoBasica(): String {
        return "$nombre - $descripcion (${tiempoPreparacion})"
    }
    
    // Implementación de interfaz Nutritional
    override fun obtenerInfoNutricional(): NutritionalInfo? = infoNutricional
    
    override fun obtenerCaloriasTotales(): Int {
        return infoNutricional?.calorias ?: 0
    }
    
    // Implementación de interfaz Preparable
    override fun obtenerTiempoPreparacion(): String = tiempoPreparacion
    
    override fun obtenerNivelDificultad(): String = dificultad
    
    override fun obtenerPasosPreparacion(): List<String> = preparacion
    
    /**
     * Obtiene los ingredientes de la receta.
     * @return Lista de ingredientes
     */
    fun obtenerIngredientes(): List<String> = ingredientes
    
    /**
     * Obtiene el número de ingredientes.
     * @return Cantidad de ingredientes
     */
    fun obtenerCantidadIngredientes(): Int = ingredientes.size
    
    /**
     * Obtiene el número de pasos de preparación.
     * @return Cantidad de pasos
     */
    fun obtenerCantidadPasos(): Int = preparacion.size
    
    /**
     * Verifica si la receta es apta para vegetarianos.
     * Busca palabras clave de carne en los ingredientes.
     * @return true si no contiene carne
     */
    fun esVegetariana(): Boolean {
        val palabrasCarne = listOf("carne", "pollo", "vacuno", "cerdo", "pescado", "chorizo")
        return ingredientes.none { ingrediente ->
            palabrasCarne.any { ingrediente.contains(it, ignoreCase = true) }
        }
    }
    
    /**
     * Indica si la receta es rápida de preparar (menos de 40 minutos).
     * @return true si se prepara en menos de 40 minutos
     */
    fun esRecetaRapida(): Boolean {
        val minutos = tiempoPreparacion.filter { it.isDigit() }.toIntOrNull() ?: 999
        return minutos < 40
    }
    
    /**
     * Genera un resumen completo de la receta.
     * @return String con toda la información
     */
    fun obtenerResumenCompleto(): String {
        return buildString {
            appendLine(obtenerDescripcionDetallada())
            appendLine()
            appendLine("Ingredientes (${obtenerCantidadIngredientes()}):")
            ingredientes.forEachIndexed { index, ing ->
                appendLine("  ${index + 1}. $ing")
            }
            appendLine()
            appendLine("Preparación (${obtenerCantidadPasos()} pasos):")
            preparacion.forEachIndexed { index, paso ->
                appendLine("  ${index + 1}. $paso")
            }
            
            if (infoNutricional != null) {
                appendLine()
                appendLine("Recomendaciones Nutricionales:")
                infoNutricional.obtenerRecomendaciones().forEach { rec ->
                    appendLine("  $rec")
                }
            }
        }
    }
    
    /**
     * Crea una copia de la receta con algunos valores modificados.
     * Útil para el patrón Builder o para modificaciones.
     */
    fun copiar(
        nuevoId: String = this.id,
        nuevoNombre: String = this.nombre,
        nuevoOrigen: String = this.origen,
        nuevaDescripcion: String = this.descripcion,
        nuevoTiempoPreparacion: String = this.tiempoPreparacion,
        nuevaDificultad: String = this.dificultad,
        nuevosIngredientes: List<String> = this.ingredientes,
        nuevaPreparacion: List<String> = this.preparacion,
        nuevaInfoNutricional: NutritionalInfo? = this.infoNutricional,
        nuevaCategoria: String = this.categoria,
        nuevasPorciones: Int = this.porciones
    ): Receta {
        return Receta(
            id = nuevoId,
            nombre = nuevoNombre,
            origen = nuevoOrigen,
            descripcion = nuevaDescripcion,
            tiempoPreparacion = nuevoTiempoPreparacion,
            dificultad = nuevaDificultad,
            ingredientes = nuevosIngredientes,
            preparacion = nuevaPreparacion,
            infoNutricional = nuevaInfoNutricional,
            categoria = nuevaCategoria,
            porciones = nuevasPorciones
        )
    }
    
    override fun toString(): String {
        return "Receta(nombre='$nombre', dificultad='$dificultad', tiempo='$tiempoPreparacion')"
    }
    
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Receta) return false
        return id == other.id
    }
    
    override fun hashCode(): Int {
        return id.hashCode()
    }
}
