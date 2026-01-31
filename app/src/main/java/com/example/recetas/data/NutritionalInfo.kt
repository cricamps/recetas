package com.example.recetas.data

/**
 * Clase que representa la información nutricional de una receta.
 * Implementa conceptos de POO: encapsulación, propiedades y validación.
 * 
 * @property calorias Calorías por porción
 * @property proteinas Proteínas en gramos
 * @property carbohidratos Carbohidratos en gramos
 * @property grasas Grasas en gramos
 * @property fibra Fibra en gramos
 * @property sodio Sodio en miligramos
 */
data class NutritionalInfo(
    val calorias: Int,
    val proteinas: Double,
    val carbohidratos: Double,
    val grasas: Double,
    val fibra: Double = 0.0,
    val sodio: Int = 0,
    val azucar: Double = 0.0  // Azúcares en gramos
) {
    // Constructor secundario con valores por defecto
    constructor(calorias: Int) : this(
        calorias = calorias,
        proteinas = 0.0,
        carbohidratos = 0.0,
        grasas = 0.0
    )
    
    // Propiedad calculada: indica si es bajo en calorías (menos de 300 cal)
    val esBajoEnCalorias: Boolean
        get() = calorias < 300
    
    // Propiedad calculada: indica si es alto en proteínas (más de 20g)
    val esAltoEnProteinas: Boolean
        get() = proteinas > 20.0
    
    // Propiedad calculada: indica si es bajo en sodio (menos de 140mg)
    val esBajoEnSodio: Boolean
        get() = sodio < 140
    
    /**
     * Calcula el total de calorías provenientes de macronutrientes.
     * Proteínas y carbohidratos: 4 cal/g, Grasas: 9 cal/g
     * @return Total de calorías calculadas
     */
    fun calcularCaloriasMacros(): Int {
        return ((proteinas * 4) + (carbohidratos * 4) + (grasas * 9)).toInt()
    }
    
    /**
     * Genera una descripción nutricional en texto.
     * @return String con resumen nutricional
     */
    fun obtenerResumenNutricional(): String {
        return buildString {
            append("Calorías: ${calorias} kcal\n")
            append("Proteínas: ${proteinas}g\n")
            append("Carbohidratos: ${carbohidratos}g\n")
            append("Grasas: ${grasas}g\n")
            if (fibra > 0) append("Fibra: ${fibra}g\n")
            if (azucar > 0) append("Azúcares: ${azucar}g\n")
            if (sodio > 0) append("Sodio: ${sodio}mg")
        }
    }
    
    /**
     * Genera recomendaciones nutricionales basadas en los valores.
     * @return Lista de recomendaciones
     */
    fun obtenerRecomendaciones(): List<String> {
        val recomendaciones = mutableListOf<String>()
        
        if (esBajoEnCalorias) {
            recomendaciones.add("✓ Bajo en calorías - ideal para control de peso")
        }
        
        if (esAltoEnProteinas) {
            recomendaciones.add("✓ Alto en proteínas - excelente para desarrollo muscular")
        }
        
        if (esBajoEnSodio) {
            recomendaciones.add("✓ Bajo en sodio - bueno para la presión arterial")
        }
        
        if (fibra >= 5.0) {
            recomendaciones.add("✓ Buena fuente de fibra - ayuda a la digestión")
        }
        
        if (grasas > 20.0) {
            recomendaciones.add("⚠ Alto en grasas - consumir con moderación")
        }
        
        if (sodio > 400) {
            recomendaciones.add("⚠ Alto en sodio - no recomendado para hipertensión")
        }
        
        if (recomendaciones.isEmpty()) {
            recomendaciones.add("Receta nutricionalmente balanceada")
        }
        
        return recomendaciones
    }
    
    companion object {
        /**
         * Valores nutricionales recomendados diarios (aproximados).
         */
        const val CALORIAS_DIARIAS = 2000
        const val PROTEINAS_DIARIAS = 50.0
        const val CARBOHIDRATOS_DIARIOS = 300.0
        const val GRASAS_DIARIAS = 70.0
        const val FIBRA_DIARIA = 25.0
        const val SODIO_DIARIO = 2300
        
        /**
         * Calcula el porcentaje del valor diario que representa.
         * @param nutriente Tipo de nutriente
         * @param cantidad Cantidad del nutriente
         * @return Porcentaje del valor diario
         */
        fun obtenerPorcentajeValorDiario(nutriente: String, cantidad: Double): Double {
            return when (nutriente.lowercase()) {
                "calorias", "calorías" -> (cantidad / CALORIAS_DIARIAS) * 100
                "proteinas", "proteínas" -> (cantidad / PROTEINAS_DIARIAS) * 100
                "carbohidratos" -> (cantidad / CARBOHIDRATOS_DIARIOS) * 100
                "grasas" -> (cantidad / GRASAS_DIARIAS) * 100
                "fibra" -> (cantidad / FIBRA_DIARIA) * 100
                "sodio" -> (cantidad / SODIO_DIARIO) * 100
                else -> 0.0
            }
        }
    }
}
