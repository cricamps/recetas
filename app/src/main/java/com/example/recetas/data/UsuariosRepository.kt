package com.example.recetas.data

import java.util.UUID

/**
 * Repositorio que gestiona el registro y autenticación de usuarios.
 * Implementa un sistema simple de almacenamiento en memoria usando un Array.
 * 
 * En una aplicación real, esto debería conectarse a una base de datos
 * y las contraseñas deberían estar encriptadas.
 */
object UsuariosRepository {
    
    // Array mutable para almacenar usuarios registrados
    // Semana 4: Demuestra el uso de arrays dinámicos en Kotlin
    private val usuarios = mutableListOf<Usuario>(
        // Usuario de prueba predefinido
        Usuario(
            id = "1",
            nombre = "Administrador",
            email = "admin@recetas.cl",
            password = "123456"
        )
    )
    
    /**
     * Registra un nuevo usuario en el sistema.
     * 
     * @param nombre Nombre completo del usuario
     * @param email Correo electrónico del usuario
     * @param password Contraseña del usuario
     * @return Usuario creado si el registro fue exitoso, null si el email ya existe
     */
    fun registrarUsuario(nombre: String, email: String, password: String): Usuario? {
        // Verificar si el email ya está registrado
        if (usuarios.any { it.email.equals(email, ignoreCase = true) }) {
            return null // Email ya existe
        }
        
        // Crear nuevo usuario
        val nuevoUsuario = Usuario(
            id = UUID.randomUUID().toString(),
            nombre = nombre,
            email = email,
            password = password
        )
        
        // Agregar al array de usuarios
        usuarios.add(nuevoUsuario)
        
        return nuevoUsuario
    }
    
    /**
     * Autentica un usuario con email y contraseña.
     * 
     * @param email Correo electrónico del usuario
     * @param password Contraseña del usuario
     * @return true si las credenciales son correctas, false en caso contrario
     */
    fun autenticarUsuario(email: String, password: String): Boolean {
        return usuarios.any { usuario ->
            usuario.email.equals(email, ignoreCase = true) && usuario.password == password
        }
    }
    
    /**
     * Busca un usuario por su email.
     * 
     * @param email Correo electrónico del usuario
     * @return Usuario encontrado o null si no existe
     */
    fun buscarPorEmail(email: String): Usuario? {
        return usuarios.find { it.email.equals(email, ignoreCase = true) }
    }
    
    /**
     * Obtiene todos los usuarios registrados (para propósitos de debug).
     * 
     * @return Lista de todos los usuarios
     */
    fun obtenerTodosLosUsuarios(): List<Usuario> {
        return usuarios.toList()
    }
    
    /**
     * Obtiene el número total de usuarios registrados.
     * 
     * @return Cantidad de usuarios en el sistema
     */
    fun obtenerCantidadUsuarios(): Int {
        return usuarios.size
    }
    
    /**
     * Elimina todos los usuarios excepto el administrador (para testing).
     */
    fun limpiarUsuarios() {
        val admin = usuarios.find { it.email == "admin@recetas.cl" }
        usuarios.clear()
        admin?.let { usuarios.add(it) }
    }
}
