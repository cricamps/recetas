package com.example.recetas.data

/**
 * Clase de datos que representa un usuario registrado en la aplicación.
 * Demuestra el uso de data classes en Kotlin para almacenar información de usuario.
 * 
 * @property id Identificador único del usuario
 * @property nombre Nombre completo del usuario
 * @property email Correo electrónico del usuario (usado como username)
 * @property password Contraseña del usuario (en producción debería estar encriptada)
 * @property fechaRegistro Timestamp de cuando se registró el usuario
 */
data class Usuario(
    val id: String,
    val nombre: String,
    val email: String,
    val password: String,
    val fechaRegistro: Long = System.currentTimeMillis()
)
