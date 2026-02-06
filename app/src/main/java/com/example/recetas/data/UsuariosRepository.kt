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
    
    // Mapa para almacenar códigos de recuperación temporales
    // Key: email, Value: código de 6 dígitos
    private val codigosRecuperacion = mutableMapOf<String, String>()
    
    // Array mutable para almacenar usuarios registrados
    // Semana 4: Demuestra el uso de arrays dinámicos en Kotlin
    // Semana 5: Array con 5 usuarios predefinidos según requisitos de la actividad sumativa 2
    private val usuarios = mutableListOf<Usuario>(
        // Usuario 1 - Administrador
        Usuario(
            id = "1",
            nombre = "María González",
            email = "maria.gonzalez@recetas.cl",
            password = "Maria2024!"
        ),
        // Usuario 2 - Usuario regular
        Usuario(
            id = "2",
            nombre = "Carlos Muñoz",
            email = "carlos.munoz@recetas.cl",
            password = "Carlos123"
        ),
        // Usuario 3 - Usuario regular
        Usuario(
            id = "3",
            nombre = "Ana Pérez",
            email = "ana.perez@recetas.cl",
            password = "Ana456"
        ),
        // Usuario 4 - Usuario regular
        Usuario(
            id = "4",
            nombre = "Luis Fernández",
            email = "luis.fernandez@recetas.cl",
            password = "Luis789"
        ),
        // Usuario 5 - Usuario regular
        Usuario(
            id = "5",
            nombre = "Sofía Rojas",
            email = "sofia.rojas@recetas.cl",
            password = "Sofia012"
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
    
    // ==================== RECUPERACIÓN DE CONTRASEÑA ====================
    
    /**
     * Genera un código de recuperación de 6 dígitos y lo envía al email del usuario.
     * En una app real, esto enviaría un email. Aquí solo lo almacenamos.
     * 
     * @param email Correo electrónico del usuario
     * @return Código generado si el usuario existe, null si no existe
     */
    fun solicitarRecuperacionPassword(email: String): String? {
        val usuario = buscarPorEmail(email)
        
        if (usuario != null) {
            // Generar código aleatorio de 6 dígitos
            val codigo = (100000..999999).random().toString()
            
            // Almacenar código asociado al email
            codigosRecuperacion[email.lowercase()] = codigo
            
            // En una app real, aquí se enviaría el email
            // Por ahora solo retornamos el código para mostrarlo en pantalla
            return codigo
        }
        
        return null
    }
    
    /**
     * Verifica si el código ingresado es correcto para el email dado.
     * 
     * @param email Correo electrónico del usuario
     * @param codigo Código de 6 dígitos ingresado por el usuario
     * @return true si el código es correcto, false en caso contrario
     */
    fun verificarCodigoRecuperacion(email: String, codigo: String): Boolean {
        val codigoAlmacenado = codigosRecuperacion[email.lowercase()]
        return codigoAlmacenado == codigo
    }
    
    /**
     * Cambia la contraseña del usuario después de verificar el código.
     * 
     * @param email Correo electrónico del usuario
     * @param nuevaPassword Nueva contraseña
     * @return true si se cambió exitosamente, false en caso contrario
     */
    fun cambiarPassword(email: String, nuevaPassword: String): Boolean {
        val index = usuarios.indexOfFirst { it.email.equals(email, ignoreCase = true) }
        
        if (index != -1) {
            // Crear nuevo usuario con la nueva contraseña
            val usuarioActual = usuarios[index]
            val usuarioActualizado = usuarioActual.copy(password = nuevaPassword)
            
            // Reemplazar en la lista
            usuarios[index] = usuarioActualizado
            
            // Eliminar el código usado
            codigosRecuperacion.remove(email.lowercase())
            
            return true
        }
        
        return false
    }
    
    /**
     * Obtiene el código de recuperación para mostrar en modo debug.
     * SOLO PARA DESARROLLO - Remover en producción.
     */
    fun obtenerCodigoDebug(email: String): String? {
        return codigosRecuperacion[email.lowercase()]
    }
}
