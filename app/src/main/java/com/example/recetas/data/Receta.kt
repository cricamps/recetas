package com.example.recetas.data

/**
 * Modelo de datos para representar una receta de cocina chilena.
 * 
 * @property id Identificador único de la receta
 * @property nombre Nombre de la receta (ej: "Pastel de Papas")
 * @property origen País o región de origen (ej: "Chile")
 * @property tiempoPreparacion Tiempo estimado de preparación (ej: "90 min")
 * @property dificultad Nivel de dificultad: "Fácil", "Media", "Difícil"
 * @property descripcion Descripción breve de la receta
 * @property ingredientes Lista de ingredientes necesarios
 * @property preparacion Pasos de preparación de la receta
 * @property isFavorita Indica si la receta está marcada como favorita
 */
data class Receta(
    val id: String,
    val nombre: String,
    val origen: String,
    val tiempoPreparacion: String,
    val dificultad: String,
    val descripcion: String,
    val ingredientes: List<String> = emptyList(),
    val preparacion: List<String> = emptyList(),
    val isFavorita: Boolean = false
)
