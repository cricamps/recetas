package com.example.recetas.data

/**
 * Clase que representa una minuta semanal de recetas.
 * Demuestra conceptos avanzados de POO: composición, colecciones y delegación.
 * 
 * Una minuta semanal contiene exactamente 5 recetas con sus recomendaciones nutricionales.
 * Esta clase gestiona la planificación semanal de comidas y proporciona análisis nutricional.
 * 
 * @property nombre Nombre de la minuta semanal
 * @property recetas Array de 5 recetas para la semana
 * @property semana Número de semana (opcional)
 */
class MinutaSemanal(
    val nombre: String,
    private val recetas: Array<Receta>,
    val semana: Int = 1
) {
    // Validación en el init block
    init {
        require(recetas.size == 5) {
            "Una minuta semanal debe contener exactamente 5 recetas (una para cada día laborable)"
        }
        
        recetas.forEach { receta ->
            require(receta.nombre.isNotBlank()) {
                "Todas las recetas deben tener un nombre válido"
            }
        }
    }
    
    // Propiedades calculadas (lazy)
    val totalRecetas: Int
        get() = recetas.size
    
    // Propiedad perezosa: solo se calcula cuando se necesita
    val totalCaloriasSemana: Int by lazy {
        recetas.sumOf { it.obtenerCaloriasTotales() }
    }
    
    val promedioCaloriasDiario: Double by lazy {
        totalCaloriasSemana.toDouble() / totalRecetas
    }
    
    /**
     * Obtiene una receta específica por índice.
     * @param indice Índice de la receta (0-4)
     * @return Receta en el índice especificado
     */
    fun obtenerReceta(indice: Int): Receta {
        require(indice in 0..4) { "El índice debe estar entre 0 y 4" }
        return recetas[indice]
    }
    
    /**
     * Obtiene todas las recetas como lista.
     * @return Lista inmutable de recetas
     */
    fun obtenerTodasLasRecetas(): List<Receta> = recetas.toList()
    
    /**
     * Obtiene las recetas ordenadas por dificultad.
     * @return Lista ordenada de recetas
     */
    fun obtenerRecetasPorDificultad(): List<Receta> {
        val ordenDificultad = mapOf("Fácil" to 1, "Media" to 2, "Difícil" to 3)
        return recetas.sortedBy { ordenDificultad[it.obtenerNivelDificultad()] ?: 4 }
    }
    
    /**
     * Obtiene las recetas vegetarianas de la minuta.
     * @return Lista de recetas vegetarianas
     */
    fun obtenerRecetasVegetarianas(): List<Receta> {
        return recetas.filter { it.esVegetariana() }
    }
    
    /**
     * Obtiene las recetas rápidas (menos de 40 minutos).
     * @return Lista de recetas rápidas
     */
    fun obtenerRecetasRapidas(): List<Receta> {
        return recetas.filter { it.esRecetaRapida() }
    }
    
    /**
     * Cuenta cuántas recetas tienen información nutricional.
     * @return Número de recetas con info nutricional
     */
    fun contarRecetasConNutricion(): Int {
        return recetas.count { it.tieneInfoNutricional() }
    }
    
    /**
     * Genera un resumen nutricional de toda la semana.
     * @return String con el análisis nutricional semanal
     */
    fun obtenerResumenSemanal(): String {
        return buildString {
            appendLine("═══════════════════════════════════════")
            appendLine("  MINUTA SEMANAL: $nombre")
            appendLine("  Semana #$semana")
            appendLine("═══════════════════════════════════════")
            appendLine()
            
            recetas.forEachIndexed { indice, receta ->
                val dia = when(indice) {
                    0 -> "LUNES"
                    1 -> "MARTES"
                    2 -> "MIÉRCOLES"
                    3 -> "JUEVES"
                    4 -> "VIERNES"
                    else -> "DÍA ${indice + 1}"
                }
                
                appendLine("$dia: ${receta.nombre}")
                appendLine("  ${receta.descripcion}")
                appendLine("  Tiempo: ${receta.obtenerTiempoPreparacion()} | Dificultad: ${receta.obtenerNivelDificultad()}")
                
                if (receta.tieneInfoNutricional()) {
                    appendLine("  Calorías: ${receta.obtenerCaloriasTotales()} kcal")
                    receta.obtenerInfoNutricional()?.obtenerRecomendaciones()?.firstOrNull()?.let {
                        appendLine("  $it")
                    }
                }
                appendLine()
            }
            
            appendLine("─────────────────────────────────────")
            appendLine("RESUMEN SEMANAL:")
            appendLine("  Total de calorías: $totalCaloriasSemana kcal")
            appendLine("  Promedio diario: ${String.format("%.0f", promedioCaloriasDiario)} kcal")
            appendLine("  Recetas vegetarianas: ${obtenerRecetasVegetarianas().size}")
            appendLine("  Recetas rápidas: ${obtenerRecetasRapidas().size}")
            appendLine("═══════════════════════════════════════")
        }
    }
    
    /**
     * Genera recomendaciones nutricionales para la semana.
     * @return Lista de recomendaciones
     */
    fun obtenerRecomendacionesSemanales(): List<String> {
        val recomendaciones = mutableListOf<String>()
        
        // Análisis de calorías promedio
        when {
            promedioCaloriasDiario < 400 -> {
                recomendaciones.add("⚠ Promedio calórico bajo - considera aumentar las porciones")
            }
            promedioCaloriasDiario > 800 -> {
                recomendaciones.add("⚠ Promedio calórico alto - considera recetas más ligeras")
            }
            else -> {
                recomendaciones.add("✓ Promedio calórico balanceado")
            }
        }
        
        // Análisis de variedad
        val cantidadVegetarianas = obtenerRecetasVegetarianas().size
        if (cantidadVegetarianas == 0) {
            recomendaciones.add("💡 Considera agregar recetas vegetarianas para mayor variedad")
        } else if (cantidadVegetarianas >= 2) {
            recomendaciones.add("✓ Buena variedad con opciones vegetarianas")
        }
        
        // Análisis de complejidad
        val cantidadFaciles = recetas.count { it.obtenerNivelDificultad() == "Fácil" }
        if (cantidadFaciles >= 3) {
            recomendaciones.add("✓ Mayoría de recetas fáciles - ideal para principiantes")
        }
        
        // Análisis de tiempo
        val cantidadRapidas = obtenerRecetasRapidas().size
        if (cantidadRapidas >= 3) {
            recomendaciones.add("✓ Varias recetas rápidas - ideal para días ocupados")
        }
        
        return recomendaciones
    }
    
    /**
     * Obtiene un mapa con el resumen nutricional por día.
     * @return Map con día y calorías
     */
    fun obtenerMapaCaloriasDiarias(): Map<String, Int> {
        val dias = listOf("Lunes", "Martes", "Miércoles", "Jueves", "Viernes")
        return recetas.mapIndexed { indice, receta ->
            dias[indice] to receta.obtenerCaloriasTotales()
        }.toMap()
    }
    
    /**
     * Busca recetas por nombre en la minuta.
     * @param consulta Término de búsqueda
     * @return Lista de recetas que coinciden
     */
    fun buscarRecetas(consulta: String): List<Receta> {
        return if (consulta.isBlank()) {
            recetas.toList()
        } else {
            recetas.filter { 
                it.nombre.contains(consulta, ignoreCase = true) ||
                it.descripcion.contains(consulta, ignoreCase = true)
            }
        }
    }
    
    /**
     * Verifica si la minuta tiene al menos una receta de cada dificultad.
     * @return true si hay variedad de dificultades
     */
    fun tieneVariedadDificultad(): Boolean {
        val dificultades = recetas.map { it.obtenerNivelDificultad() }.toSet()
        return dificultades.size >= 2
    }
    
    override fun toString(): String {
        return "MinutaSemanal(nombre='$nombre', semana=$semana, recetas=${recetas.size})"
    }
    
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is MinutaSemanal) return false
        return nombre == other.nombre && semana == other.semana
    }
    
    override fun hashCode(): Int {
        var result = nombre.hashCode()
        result = 31 * result + semana
        return result
    }
    
    companion object {
        /**
         * Crea una minuta semanal de ejemplo con recetas chilenas.
         * @param semana Número de semana
         * @return MinutaSemanal configurada
         */
        fun crearMinutaEjemplo(semana: Int = 1): MinutaSemanal {
            // Este método será implementado en MinutasRepository
            // para evitar duplicación de código
            throw NotImplementedError("Usar MinutasRepository.obtenerMinutaSemanal() en su lugar")
        }
    }
}
