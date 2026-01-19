package com.example.recetas.accessibility

/**
 * Niveles de contraste para mejorar la accesibilidad visual.
 * 
 * La mejora de contraste es esencial para personas con:
 * - Baja visión
 * - Daltonismo
 * - Sensibilidad a la luz
 * - Dificultad para distinguir colores
 * 
 * Niveles disponibles:
 * - NORMAL: Contraste estándar (WCAG AA)
 * - AUMENTADO: Contraste incrementado (WCAG AAA parcial)
 * - ALTO: Contraste alto (WCAG AAA completo)
 * - MUY_ALTO: Contraste máximo para baja visión severa
 * 
 * Cada nivel cumple y supera los estándares WCAG 2.1:
 * - AA: Ratio mínimo 4.5:1 para texto normal
 * - AAA: Ratio mínimo 7:1 para texto normal
 * - Muy Alto: Ratio superior a 15:1
 * 
 * @property label Etiqueta mostrada al usuario
 * @property multiplier Multiplicador de contraste (1.0 = normal)
 * @property description Descripción del nivel de contraste
 */
enum class ContrastMode(
    val label: String,
    val multiplier: Float,
    val description: String
) {
    /**
     * Contraste normal - cumple WCAG 2.1 nivel AA
     * Ratio aproximado: 4.5:1 a 7:1
     */
    NORMAL(
        label = "Normal",
        multiplier = 1.0f,
        description = "Contraste estándar"
    ),
    
    /**
     * Contraste aumentado - supera WCAG 2.1 nivel AA
     * Ratio aproximado: 7:1 a 10:1
     */
    AUMENTADO(
        label = "Aumentado",
        multiplier = 1.3f,
        description = "Mayor diferencia entre colores"
    ),
    
    /**
     * Contraste alto - cumple WCAG 2.1 nivel AAA
     * Ratio aproximado: 10:1 a 15:1
     */
    ALTO(
        label = "Alto",
        multiplier = 1.6f,
        description = "Contraste alto para baja visión"
    ),
    
    /**
     * Contraste muy alto - excede WCAG 2.1 nivel AAA
     * Ratio aproximado: >15:1
     */
    MUY_ALTO(
        label = "Muy Alto",
        multiplier = 2.0f,
        description = "Contraste máximo"
    );
    
    companion object {
        /**
         * Obtiene el siguiente nivel de contraste de forma cíclica
         * @param current Nivel actual
         * @return Siguiente nivel (vuelve a NORMAL después de MUY_ALTO)
         */
        fun getNext(current: ContrastMode): ContrastMode {
            return when (current) {
                NORMAL -> AUMENTADO
                AUMENTADO -> ALTO
                ALTO -> MUY_ALTO
                MUY_ALTO -> NORMAL
            }
        }
        
        /**
         * Convierte un String a ContrastMode
         * @param name Nombre del modo
         * @return ContrastMode correspondiente o NORMAL si no se encuentra
         */
        fun fromString(name: String): ContrastMode {
            return try {
                valueOf(name)
            } catch (e: IllegalArgumentException) {
                NORMAL
            }
        }
    }
}
